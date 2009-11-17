package no.ovitas.compass2.webapp.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collection;

import javax.swing.tree.TreeNode;

import org.apache.commons.digester.SetRootRule;
import org.springframework.beans.factory.ListableBeanFactory;

import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.model.KnowledgeBaseHolder;
import no.ovitas.compass2.model.RelationType;
import no.ovitas.compass2.model.ResultObject;
import no.ovitas.compass2.model.Topic;
import no.ovitas.compass2.model.Relation;
import no.ovitas.compass2.model.TopicTreeNode;
import no.ovitas.compass2.service.CompassManager;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.factory.CompassManagerFactory;
import no.ovitas.compass2.service.factory.KBFactory;
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
	private Integer hopCount;
	private double expansionThreshold;
	private double resultThreshold;

	private boolean prefixMatch;
	private boolean fuzzyMatch;
	private String treeJson;
	private int jsonNodeId = 0;
	private Integer maxTopicNumberToExpand;

	private List<Hit> result;
	private List<Hit> filteredResult;

	private int resultSize;
	private int filteredResultSize;
	
	private ArrayList<RelationType> relationTypes;
	
	protected CompassManager compassManager;
	protected ConfigurationManager configurationManager;
	
	private KnowledgeBaseHolder kbHolder;
	
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

	public double getExpansionThreshold() {
		return expansionThreshold;
	}

	public void setExpansionThreshold(double expansionThreshold) {
		this.expansionThreshold = expansionThreshold;
	}
	
	public double getResultThreshold() {
		return resultThreshold;
	}

	public void setResultThreshold(double resultThreshold) {
		this.resultThreshold = resultThreshold;
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
	
	public List<Hit> getFilteredResult() {
		return filteredResult;
	}

	public void setFilteredResult(List<Hit> filteredResult) {
		this.filteredResult = filteredResult;
	}
	
	public int getResultSize() {
		return this.result.size();
	}

	public int getFilteredResultSize() {
		return this.filteredResult.size();
	}
	
	public ArrayList<RelationType> getRelationTypes() {
		return relationTypes;
	}

	public void setRelationTypes(ArrayList<RelationType> relationTypes) {
		this.relationTypes = relationTypes;
	}

	public KnowledgeBaseHolder getKbHolder() {
		return kbHolder;
	}

	public void setKbHolder(KnowledgeBaseHolder kbHolder) {
		this.kbHolder = kbHolder;
	}
	
	//Getters & Setters END

	
	/*
	 * ACTIONS
	 */
	
	public void prepare() throws Exception {
		log.info("searchAction.prepare()");
		compassManager = CompassManagerFactory.getInstance().getCompassManager();
		relationTypes = new ArrayList<RelationType>();
		
		// Get knowledge base holder
		kbHolder = KBFactory.getInstance().getKBImplementation().getKbModel();
		Map<String, RelationType> relationTypes = kbHolder.getRelationTypes();
		
		// Fill relationTypes with relation types
		for(String key : relationTypes.keySet()){
			RelationType relType = relationTypes.get(key);
			this.relationTypes.add(relType);
		}
		
		// TODO set maxNumberOfHits value from config xml
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
		

		int hc = hopCount!=null ? hopCount.intValue() : 0;
		ResultObject resultObj = compassManager.search(
			search, 
			hc, 
			expansionThreshold, 
			prefixMatch, 
			fuzzyMatch,
			this.maxTopicNumberToExpand
		);
		
		List<Hit> hits = resultObj.getHits();
		List<Set<TopicTreeNode>> expansions = resultObj.getExpansions();
		
		//log.info("Search string: "+search);
		/*		if(expansions != null && expansions.size()>0){
			//setResult(hits);
			log.info("There are "+expansions.size()+" Expansions");
			for(Set<TopicTreeNode> exp: expansions){
				log.info(exp);
				if(exp != null && exp.size()>0){
					for(Topic topic: exp){
						//log.info("Topic: "+topic.getName());
						//log.info(topic);
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
		}*/
		
		if(hits != null && hits.size() > 0){
			String docRoot = configurationManager.getConfigParameter(no.ovitas.compass2.Constants.DOCUMENT_REPOSITORY_PROPERTY);
			List<Hit> filteredHits = new ArrayList<Hit>();
			for(Hit h: hits){
				String uri = h.getURI();
				if(uri.indexOf(docRoot)>-1){
					uri = uri.replace(docRoot, "http://");
					//uri= uri.replaceAll("\", "/");
					h.setURI(uri);
				}
				// Add hit from hit list if it's score greater than resultThreshold
				double score = h.getScore();
				if(score > resultThreshold)
					filteredHits.add(h);
			}
			setResult(hits);
			setFilteredResult(filteredHits);
		}
		setTreeJson(createJson(expansions));
		
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
	
	private List<TopicTreeNode> getRootNode(Set<TopicTreeNode> topictreeNodes){
		log.info("topictreeNodes(getRootNode): "+topictreeNodes.size());
		List<TopicTreeNode> retList = new ArrayList<TopicTreeNode>();
		for (TopicTreeNode treeNode: topictreeNodes) {
			if(treeNode.getParent() == null){
				retList.add(treeNode);
			  }else{
			    log.info("Node: "+treeNode.getName()+" Parent: "+treeNode.getParent().getName());
			  }
		}
		return retList;
	}
	
	private String createTreeFromSetTopicTreeNodes(Set<TopicTreeNode> topictreeNodes){
		List<TopicTreeNode> rootNodes = getRootNode(topictreeNodes);
		StringBuffer sb = new StringBuffer();
		boolean isFirst = true;
		if(rootNodes.size()>0){
			for(TopicTreeNode rootNode  : rootNodes){
			if(isFirst){
				isFirst = false;
			}else{
				sb.append(",");
			}
		     sb.append(treeNode2JsonSubTree(rootNode));
		     
			}
		}
		
		return sb.toString();
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

	/**
	 * @return the hopCount
	 */
	public Integer getHopCount() {
		return hopCount;
	}

	/**
	 * @param hopCount the hopCount to set
	 */
	public void setHopCount(Integer hopCount) {
		this.hopCount = hopCount;
	}

}
