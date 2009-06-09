package no.ovitas.compass2.webapp.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Collection;

import javax.swing.tree.TreeNode;

import org.apache.commons.digester.SetRootRule;
import org.springframework.beans.factory.ListableBeanFactory;

import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.model.ResultObject;
import no.ovitas.compass2.model.Topic;
import no.ovitas.compass2.model.Relation;
import no.ovitas.compass2.model.TopicTreeNode;
import no.ovitas.compass2.service.CompassManager;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.factory.CompassManagerFactory;
import no.ovitas.compass2.service.impl.ConfigurationManagerImpl;
import no.ovitas.compass2.Constants;
import no.ovitas.compass2.service.impl.ConfigurationManagerImpl;
import com.opensymphony.xwork2.Preparable;

public class SearchAction extends BaseAction implements Preparable {

	private static final long serialVersionUID = 2394809088238874832L;

	/*
	 * FIELDS
	 */

	
	private String search;
	private int hopCount;
	private double thresholdWeight; 
	private boolean prefixMatch;
	private boolean fuzzyMatch;
	private String treeJson;
	private int jsonNodeId = 0;
	private Integer maxTopicNumberToExpand;

	private List<Hit> result;
	protected CompassManager compassManager;
	protected ConfigurationManager configurationManager;

	//FIELDS END
	
	/*
	 * Getters & Setters 
	 */	

	public String getTreeJson() {
		return treeJson;
	}

	public CompassManager getCompassManager() {
		return compassManager;
	}

	public void setCompassManager(CompassManager compassManager) {
		this.compassManager = compassManager;
	}

	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	public void setTreeJson(String treeJson) {
		this.treeJson = treeJson;
	}

	public int getHopCount() {
		return hopCount;
	}

	public void setHopCount(int hopCount) {
		this.hopCount = hopCount;
	}

	public double getThresholdWeight() {
		return thresholdWeight;
	}

	public void setThresholdWeight(double thresholdWeight) {
		this.thresholdWeight = thresholdWeight;
	}

	public boolean isPrefixMatch() {
		return prefixMatch;
	}

	public void setPrefixMatch(boolean prefixMatch) {
		this.prefixMatch = prefixMatch;
	}

	public boolean isFuzzyMatch() {
		return fuzzyMatch;
	}

	public void setFuzzyMatch(boolean fuzzyMatch) {
		this.fuzzyMatch = fuzzyMatch;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public List<Hit> getResult() {
		return result;
	}

	public void setResult(List<Hit> result) {
		this.result = result;
	}

	//Getters & Setters END

	
	/*
	 * ACTIONS
	 */
	
	public void prepare() throws Exception {
		log.info("searchAction.prepare()");
		compassManager = CompassManagerFactory.getInstance().getCompassManager();
	}
	
	public String execute() {
		log.info("searchAction.execute()");
		return SUCCESS;
	}
	
	public String search() {
		log.info("showResults running...");
		log.info("lucene.spellchecker.dir: "+configurationManager.getConfigParameter("lucene.spellchecker.dir"));
		log.info("lucene.spellchecker.index.dir: "+configurationManager.getConfigParameter("lucene.spellchecker.index.dir"));
		log.info("lucene.fts.index.dir: "+configurationManager.getConfigParameter("lucene.fts.index.dir"));
		log.info("knowledge.base.file: "+configurationManager.getConfigParameter("knowledge.base.file"));
		//String search = getSearch(); 
		if(search==null ||  search.equals("")){
			return SUCCESS;
		}
		

		try{
			log.info("compassManager: "+compassManager.toString());
		}catch(Exception e){
			log.info("NO compassManager");
		}
		
		ResultObject resultObj = compassManager.search(
			search, 
			hopCount, 
			thresholdWeight, 
			prefixMatch, 
			fuzzyMatch,
			this.maxTopicNumberToExpand
		);
		
		List<Hit> hits = resultObj.getHits();
		List<Set<TopicTreeNode>> expansions = resultObj.getExpansions();
		
		log.info("Search string: "+search);
		if(expansions != null && expansions.size()>0){
			//setResult(hits);
			log.info("There are "+expansions.size()+" Expansions");
			for(Set<TopicTreeNode> exp: expansions){
				log.info(exp);
				if(exp != null && exp.size()>0){
					for(Topic topic: exp){
						log.info("Topic: "+topic.getName());
						log.info(topic);
						List<Relation> relations = topic.getRelations();
						if(relations != null && relations.size()>0){
							for(Relation relation: relations){
								log.info("Relation: "+relation.toString());
							}
						}
					}	
				}
			}
		}else{
			log.info("There are no Expansions...");
		}
		
		if(hits != null && hits.size() > 0){
			setResult(hits);
			log.info("There are Hits...");
			for(Hit h: result){
				log.info("Hit: "+h.getURI());
			}
		}else{
			log.info("There are no Hits...");
		}
		setTreeJson(createJson(expansions));
		log.info("Tree: "+getTreeJson());
		
		return "showResults";
	}

	
	
	//ACTIONS END	

	private String createJson(List<Set<TopicTreeNode>> expansions) {
		String json = "";
		
		List<String> children = new ArrayList<String>();
		
		children = new ArrayList<String>();
		if(expansions != null && expansions.size()>0){
			for(Set<TopicTreeNode> topictreeNodes: expansions){
				children.add(
					createTreeFromSetTopicTreeNodes(topictreeNodes)
				);
			}
		}
		log.info("children: "+children.size());
		String results = createJsonNode("Results", children);
		
		
		json = "["+results+"]";
		
		return json;
	}
	
	private String treeNode2JsonSubTree(TopicTreeNode treeNode){
		if(treeNode == null){
			return "";
		}
		int childrenCount = treeNode.getChildren().size();
		List<String> children = new ArrayList<String>();
		if(childrenCount > 0){
			Collection<TopicTreeNode> childrenCollection = treeNode.getChildren();
			for (TopicTreeNode child: childrenCollection) {
				String subTree = treeNode2JsonSubTree(child);
				children.add(subTree);
			}
			return createJsonNode(treeNode.getName(), children);
		}
		return createJsonNode(treeNode.getName(), null);
	}
	
	private TopicTreeNode getRootNode(Set<TopicTreeNode> topictreeNodes){
		log.info("topictreeNodes(getRootNode): "+topictreeNodes.size());
		for (TopicTreeNode treeNode: topictreeNodes) {
			if(treeNode.getParent() == null){
				return treeNode;
			}
			log.info("Node: "+treeNode.getName()+" Parent: "+treeNode.getParent().getName());
		}
		return null;
	}
	
	private String createTreeFromSetTopicTreeNodes(Set<TopicTreeNode> topictreeNodes){
		TopicTreeNode rootNode = getRootNode(topictreeNodes);
		return treeNode2JsonSubTree(rootNode);
	}
	
	private String createJsonNode(String text, List<String> children) {
		jsonNodeId++;
		String node = "";
		String childrenString = "";
		boolean leaf = (children==null || children.isEmpty());
		
		if(leaf){
			node = "{text:'"+text+"', id:"+jsonNodeId+", leaf:true}";
		}else{
			node = "{text:'"+text+"', id:"+jsonNodeId+", leaf:false, " +
					"children:[";
			for(String child: children){
				childrenString += child+", ";
			}
			childrenString = childrenString.substring(0, childrenString.length()-2);
			node += childrenString;					
			node += "]" +
					"}";
		}
		return node;
	}

	/**
	 * @return the maxTopicNumberToExpand
	 */
	public Integer getMaxTopicNumberToExpand() {
		return maxTopicNumberToExpand;
	}

	/**
	 * @param maxTopicNumberToExpand the maxTopicNumberToExpand to set
	 */
	public void setMaxTopicNumberToExpand(Integer maxTopicNumberToExpand) {
		this.maxTopicNumberToExpand = maxTopicNumberToExpand;
	}

}
