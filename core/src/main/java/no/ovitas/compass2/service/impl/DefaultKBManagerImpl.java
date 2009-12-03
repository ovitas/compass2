package no.ovitas.compass2.service.impl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import no.ovitas.compass2.config.AssociationType;
import no.ovitas.compass2.config.Expansion;
import no.ovitas.compass2.config.KnowledgeBaseImplementation;
import no.ovitas.compass2.dao.KBBuilderDao;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.model.KnowledgeBaseHolder;
import no.ovitas.compass2.model.Relation;
import no.ovitas.compass2.model.RelationType;
import no.ovitas.compass2.model.Topic;
import no.ovitas.compass2.model.TopicTreeNode;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.KnowledgeBaseManager;
import no.ovitas.compass2.util.CompassUtil;
import no.ovitas.compass2.util.TopicTreeNodeComparator;
import no.ovitas.compass2.util.TopicUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author magyar
 * @version 1.0
 */
public class DefaultKBManagerImpl implements KnowledgeBaseManager {

	protected KBBuilderDao builderDao;
	protected KnowledgeBaseHolder knowledgeBase;
	protected double expansionThreshold;
	protected KnowledgeBaseImplementation kbImpl;
	protected Expansion expansion;

	protected int maxTopicNumberToExpand;
	

	public DefaultKBManagerImpl(){
		this("kbBuilderDao");
	}

	protected DefaultKBManagerImpl(String daoName) {
		ApplicationContext context = CompassUtil.getApplicationContext();
		builderDao = (KBBuilderDao)context.getBean(daoName);
	}

	protected ConfigurationManager configManager;
    private Log log = LogFactory.getLog(getClass());
	
	public void setConfiguration(ConfigurationManager manager){
		configManager = manager;
	}
	
	public void finalize() throws Throwable {

	}

	/**
	 * 
	 * @param fuzzyMatch
	 * @param prefixMatching
	 * @param hopCount
	 * @param thresholdWeight
	 * @param search
	 */
	public List<Set<TopicTreeNode>> getExpansion(boolean fuzzyMatch, boolean prefixMatching, 
			int hopCount, double thresholdWeight, List<String> search, Integer maxTopicNumberToExpand){
		
		int aMaxTopicNumberToExpand = this.maxTopicNumberToExpand;
		if (maxTopicNumberToExpand != null) aMaxTopicNumberToExpand = maxTopicNumberToExpand.intValue();
		
		List<Set<TopicTreeNode>> ret = new ArrayList<Set<TopicTreeNode>>();
		int limit = aMaxTopicNumberToExpand;
		for (String s : search){
			Set<Topic> topicSet = new HashSet<Topic>();
			
			String toSearch = s;
			if (prefixMatching){
				if(s.endsWith("%")){
					toSearch = s.replaceAll("%", "");
				}
				List<Topic> tp = this.knowledgeBase.findTopicByPrefixMatchCaseInSensitive(toSearch);
				if (tp != null){
					for (Topic topic : tp){
						topicSet.add(topic);
					}
				}
			} else {
				List<Topic> tp = this.knowledgeBase.findTopicCaseInSensitive(toSearch);
				if(tp!=null && tp.size()>0){
					for(Topic topic : tp){
						topicSet.add(topic);
					}
				}
			}
			
			//Set<TopicTreeNode> topicTreeNodeSet = new HashSet<TopicTreeNode>();
			Set<TopicTreeNode> topicTreeNodeSet = new TreeSet<TopicTreeNode>(new TopicTreeNodeComparator(s, hopCount));
			for (Topic topic : topicSet) {
				TopicTreeNode node1 = thresholdWeight < 0 ? null : 
					TopicUtil.expandTopicsForMaxWeight(topic, thresholdWeight, aMaxTopicNumberToExpand);
				TopicTreeNode node2 = hopCount < 0 ? null : 
					TopicUtil.expandTopicsForMinHopCount(topic, hopCount, aMaxTopicNumberToExpand);
				if (node1 == null) {
					node1 = node2;
				} else if (node2 != null) {
					node1.intersect(node2);
				}
				if (node1 != null) {
					topicTreeNodeSet.add(node1);
				}
			}
			
			// Create new tree node set, add nodes to it if has space for them
			Set<TopicTreeNode> subTopicTreeNodeSet = new HashSet<TopicTreeNode>();
			
			// Iterate over all of the topic tree nodes from dijkstra
			for(TopicTreeNode node : topicTreeNodeSet){
				
				// Count how many node has the node tree
				int nodeCount = countNodes(node, 0, false);
				
				// If tree node has fewer nodes than limit then add it
				if (nodeCount <= limit) {
					subTopicTreeNodeSet.add(node);
					limit -= nodeCount;
					
				// If tree node has more nodes than limit then truncate it
				} else {
					if (limit != 0) {
						log.debug("Add subtree. Rootnode is: " + node.getName());
						log.debug("This tree has " + nodeCount + " element, but limit is " + limit);
						log.debug("Add " + limit + " treenode, and remove the last " + (nodeCount-limit) + " nodes");
						
						// Set flag of the first limit node 
						nodeCount = countNodes(node, limit, true);
												
						// Add truncated node1
						if (nodeCount <= limit) {
							
							// Remove not flagged nodes in node
							node.truncate();
							subTopicTreeNodeSet.add(node);
							limit -= nodeCount;
						}
						
						// After all limit should be null
						log.debug("After truncate the limit should be 0: " + limit);
					}
					break;
				}
			}
			
			ret.add(subTopicTreeNodeSet);
		}
		
		return ret;
	}

	private int countNodes(TopicTreeNode node, int flagged, boolean setFlag) {
		int nodeCount = 0;
		if (!setFlag) {
			// Leaf
			if (node.getChildren().size() == 0) {
				return 1;
			} else{
				// Iterate over Children
				for(TopicTreeNode child: node.getChildren()){
					nodeCount += countNodes(child,flagged,  false);
				}
				// Parent node
				nodeCount += 1;
				return nodeCount;
			}
		} else {
			
			// Flag the root node if flagged not reached
			if (flagged > 0) {
				node.setFlag(true);
				flagged--;
				nodeCount = 1;
			} else {
				return nodeCount;
			}
			
			// Flag children
			for (TopicTreeNode child : node.getChildren()){
				nodeCount += countNodes(child, flagged, true);
				flagged-=nodeCount;
			}
			
			return nodeCount;
		}	
	}
	
	/**
	 * 
	 * @param kb
	 */
	public void importKB(String kb){
		knowledgeBase = null;
		knowledgeBase = this.builderDao.buildKB(kb);
		if(this.expansion!=null){
			if(expansion.getAssociationTypes()!=null && expansion.getAssociationTypes().getElements()!=null && expansion.getAssociationTypes().getElements().size() > 0){
				for(AssociationType at : expansion.getAssociationTypes().getElements()){
					RelationType rt = knowledgeBase.getRelationTypes().get(at.getId());
					if(rt!=null){
						if(at.getWeightAback()!=0.0){
							rt.setGeneralizationWeight(at.getWeightAback());
						}
						if(at.getWeightAhead()!=0.0){
							rt.setWeight(at.getWeightAhead());
						}
					}
					
				}
			}
	
		}
	}

	/**
	 * 
	 * @param kb
	 */
	public void updateKB(String kb){

	}
	
	protected Set<Topic> expand(Topic root, int hopCount, double thresholdWeight, double currentThreshold){
		return null;
	}
	
	
	protected List<Topic> getRelatedTopics(Topic act, double actWeight, double weight){
		List<Topic> retList = new ArrayList<Topic>();
		for(Relation rel : act.getRelations()){
			if(rel.getRelationType().getWeight() * actWeight >= weight){
				if(!rel.getSource().equals(act)){
					retList.add(rel.getSource());
				}else{
					retList.add(rel.getTarget());
				}
			}
		}
		return retList;
	}

	public KnowledgeBaseHolder getKbModel() {
		return this.knowledgeBase;
	}

	public double getExpansionThreshold() {
		return expansionThreshold;
	}

	public void setExpansionThreshold(double expansionThreshold) {
		this.expansionThreshold = expansionThreshold;
	}

	public int getMaxTopicNumberToExpand() {
		return maxTopicNumberToExpand;
	}

	public void setMaxTopicNumberToExpand(int maxTopicNumberToExpand) {
		this.maxTopicNumberToExpand = maxTopicNumberToExpand;
	}

	public void setKnowledgeBaseImpl(KnowledgeBaseImplementation kbImpl) {
		this.kbImpl = kbImpl;
	}

	public void init() throws ConfigurationException {
		String defaultkbName = configManager.getDefaultKBImplementationName();
		maxTopicNumberToExpand = 100;
		if(configManager.getKnowledgeBase(defaultkbName).getExpansion().getMaxNumOfTopicToExpand()>0){
			maxTopicNumberToExpand = configManager.getKnowledgeBase(defaultkbName).getExpansion().getMaxNumOfTopicToExpand();
		}
	}

	public void setExpansion(Expansion expansion) {
		this.expansion = expansion;
	}

	
	

}