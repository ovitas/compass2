package no.ovitas.compass2.service.impl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import no.ovitas.compass2.dao.KBBuilderDao;
import no.ovitas.compass2.model.KnowledgeBaseHolder;
import no.ovitas.compass2.model.Relation;
import no.ovitas.compass2.model.Topic;
import no.ovitas.compass2.model.TopicTreeNode;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.KnowledgeBaseManager;
import no.ovitas.compass2.util.CompassUtil;
import no.ovitas.compass2.util.TopicUtil;

/**
 * @author magyar
 * @version 1.0
 */
public class DefaultKBManagerImpl implements KnowledgeBaseManager {

	protected KBBuilderDao builderDao;
	protected KnowledgeBaseHolder knowledgeBase;
	
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
	public Set<TopicTreeNode> getExpansion(boolean fuzzyMatch, boolean prefixMatching, int hopCount, double thresholdWeight, String search){
		Set<Topic> topicSet = new HashSet<Topic>();
		String s = search;
		String toSearch = s;
		if (prefixMatching){
			if(s.endsWith("%")){
				toSearch = s.replaceAll("%", "");
			}
			List<Topic> tp = this.knowledgeBase.findTopicByPrefixMatch(toSearch);
			if(tp!=null && tp.size()>0){
				for(Topic topic : tp){
					topicSet.add(topic);
				}
			}
		} else {
			Topic topic = this.knowledgeBase.findTopic(toSearch);
			if (topic != null){
				topicSet.add(topic);
			}
		}
		
		Set<TopicTreeNode> topicTreeNodeSet = new HashSet<TopicTreeNode>();
		for (Topic topic : topicSet) {
			TopicTreeNode node1 = TopicUtil.expandTopicsForMaxWeight(topic, thresholdWeight);
			TopicTreeNode node2 = TopicUtil.expandTopicsForMinHopCount(topic, hopCount);
			node1.intersect(node2);
			topicTreeNodeSet.add(node1);
		}
		return topicTreeNodeSet;
	}

	/**
	 * 
	 * @param fuzzyMatch
	 * @param prefixMatching
	 * @param hopCount
	 * @param thresholdWeight
	 * @param search
	 */
	public List<Set<TopicTreeNode>> getExpansion(boolean fuzzyMatch, boolean prefixMatching, int hopCount, double thresholdWeight, List<String> search){
		List<Set<TopicTreeNode>> ret = new ArrayList<Set<TopicTreeNode>>();
		for (String s : search){
			Set<Topic> topicSet = new HashSet<Topic>();
			
			String toSearch = s;
			if (prefixMatching){
				if(s.endsWith("%")){
					toSearch = s.replaceAll("%", "");
				}
				List<Topic> tp = this.knowledgeBase.findTopicByPrefixMatch(toSearch);
				if (tp != null){
					for (Topic topic : tp){
						topicSet.add(topic);
					}
				}
			} else {
				Topic topic = this.knowledgeBase.findTopic(toSearch);
				if (topic!=null){
					topicSet.add(topic);
				}
			}
			
			Set<TopicTreeNode> topicTreeNodeSet = new HashSet<TopicTreeNode>();
			for (Topic topic : topicSet) {
				TopicTreeNode node1 = TopicUtil.expandTopicsForMaxWeight(topic, thresholdWeight);
				TopicTreeNode node2 = TopicUtil.expandTopicsForMinHopCount(topic, hopCount);
				node1.intersect(node2);
				topicTreeNodeSet.add(node1);
			}
			ret.add(topicTreeNodeSet);
		}
		
		return ret;
	}

	/**
	 * 
	 * @param kb
	 */
	public void importKB(String kb){
		knowledgeBase = this.builderDao.buildKB(kb);
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
	
	

}