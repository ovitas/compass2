package no.ovitas.compass2.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.exception.ConfigParameterMissingException;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.model.DocumentDetails;
import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.model.ResultObject;
import no.ovitas.compass2.model.Topic;
import no.ovitas.compass2.model.TopicTreeNode;
import no.ovitas.compass2.service.CompassManager;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.FullTextSearchManager;
import no.ovitas.compass2.service.KnowledgeBaseManager;
import no.ovitas.compass2.service.LanguageToolsManager;
import no.ovitas.compass2.service.factory.FTSFactory;
import no.ovitas.compass2.service.factory.KBFactory;
import no.ovitas.compass2.service.factory.LTFactory;
import no.ovitas.compass2.util.XPair;

/**
 * The Class CompassManagerImpl.
 */
/**
 * @author magyar
 *
 */
public class CompassManagerImpl implements CompassManager {

	
	
	protected ConfigurationManager configurationManager;
	protected FullTextSearchManager ftsManager;
	protected LanguageToolsManager ltManager;
	protected KnowledgeBaseManager kbManager;
	protected boolean useStemmingEnabled;
	protected boolean prefixMatchingEnabled;
	protected int hitThreshold;
	private Log log = LogFactory.getLog(getClass());
	private double resultThreshold;
	private int maxExpansionNode;
	


	
	public ResultObject search(String search, int hopCount,
			double thresholdWeight, boolean prefixMatch, boolean fuzzyMatch, 
			Integer maxTopicNumberToExpand, double resultThreshold, int	maxNumberOfHits) {
		ftsManager.setFuzzySearch(fuzzyMatch);
		ftsManager.setMaxNumberOfHits(maxNumberOfHits);
		ftsManager.setResultThreshold(resultThreshold);
		kbManager.setExpansionThreshold(thresholdWeight);
		kbManager.setMaxTopicNumberToExpand(maxTopicNumberToExpand);
		List<Hit> hits = null;
		if(search!=null && !search.isEmpty()){
		 	if(search.trim().startsWith("lucene:")){
		 		return this.directLuceneSearch(search, 1);
		 	}else{
		 		return indirectSearch(search, 1, hopCount, thresholdWeight, prefixMatch, fuzzyMatch, 1, maxTopicNumberToExpand);
		 	}
		}
		return null;
	}




	public CompassManagerImpl() {
	}
	
	public void init() throws ConfigurationException{
		
		ftsManager = FTSFactory.getInstance().getFTSImplementation();
		if(ftsManager == null){
			throw new ConfigurationException("FTSManager is null");
		}
		ltManager = LTFactory.getInstance().getLTImplementation();
		if(ltManager == null){
			throw new ConfigurationException("LTManager is null");
			
		}
		kbManager = KBFactory.getInstance().getKBImplementation();
		if(kbManager == null){
			throw new ConfigurationException("KBManager is null");
		}
		
	}
	
	
	public void setConfigurationManager(ConfigurationManager configurationManager)throws ConfigurationException {
		this.configurationManager = configurationManager;
	}
	
	
	/**
	 * 
	 * @param userSearch
	 */
	public String getSpellingSuggestion(String userSearch) throws ConfigParameterMissingException, ConfigurationException, IOException{
      return ltManager.getSpellingSuggestion(userSearch);
	}

	/**
	 * 
	 * @param userSearch
	 */
	public List<String> getSpellingSuggestions(String userSearch) throws ConfigParameterMissingException, ConfigurationException, IOException{
		return ltManager.getSpellingSuggestions(userSearch);
	}

	public boolean isUseStemmingEnabled() {
		return useStemmingEnabled;
	}

	public void setUseStemmingEnabled(boolean useStemmingEnabled) {
		this.useStemmingEnabled = useStemmingEnabled;
	}

	public boolean isPrefixMatchingEnabled() {
		return prefixMatchingEnabled;
	}

	public void setPrefixMatchingEnabled(boolean prefixMatchingEnabled) {
		this.prefixMatchingEnabled = prefixMatchingEnabled;
	}

	public int getHitThreshold() {
		return hitThreshold;
	}

	public void setHitThreshold(int hitThreshold) {
		this.hitThreshold = hitThreshold;
	}


	protected List<String> parseSearchString(String searchString){
		ArrayList<String> retList = new ArrayList<String>();
		String[] dividedWords = searchString.split(" ");
		for(String s : dividedWords){
			if(!s.trim().isEmpty()){
				retList.add(s);
			}
		}
		return retList;
	}
	
	protected ResultObject directLuceneSearch(String search, int pageNum){
 		String tmpSearch = search.replaceFirst("lucene:", "");
 		List<Hit> hits = ftsManager.doSearch(tmpSearch, pageNum);
 		ResultObject ro = new ResultObject(null,hits);
 		ro.setAllHitNumber(ftsManager.getAllHitNumber());
 		return ro;

	}
	
	private void collectNames(TopicTreeNode node, List<XPair<String, Double>> res) {
		res.add(new XPair<String, Double>(node.getName(), node.getBoost()));
		if (node.getAlternativeNames() != null) {
			// TODO if alternatenames exists
			//res.addAll(node.getAlternativeNames());
		}
		for (TopicTreeNode topicTreeNode : node.getChildren()) {
			collectNames(topicTreeNode, res);
		}
	}
	
	private List<Hit> ftSearch(List<Set<TopicTreeNode>> topicSetList, List<String> words, int pageNum) {
		List<List<XPair<String, Double>>> stringSetList = new ArrayList<List<XPair<String, Double>>>();
		List<XPair<String, Double>> firstSet = new ArrayList<XPair<String, Double>>();
		
		for(String word : words) {
			firstSet.add(new XPair<String, Double>(word, Constants.BOOST_SAME_VALUE));
		}
		stringSetList.add(firstSet);
		
		for (Set<TopicTreeNode> topicTreeNodeSet : topicSetList) {
			List<XPair<String, Double>> stringSet = new ArrayList<XPair<String, Double>>();
			
			for (TopicTreeNode topicTreeNode : topicTreeNodeSet) {
				collectNames(topicTreeNode, stringSet);
			}
			
			stringSetList.add(stringSet);
		}
		
		return ftsManager.doSearch(stringSetList, pageNum);		
	}
	
	protected ResultObject indirectSearch(String search, int i, int hopCount,
			double thresholdWeight, boolean prefixMatching, boolean fuzzyMatch, 
			int pageNum, Integer maxTopicNumberToExpand) {
		List<Hit> retList = null;
		List<String> words = this.parseSearchString(search);
		List<Set<TopicTreeNode>> topics = null;
		if (this.useStemmingEnabled){
			words = this.ltManager.getStems(words);
		}
		if (words != null && !words.isEmpty()){
			topics = this.kbManager.getExpansion(fuzzyMatch, prefixMatching, 
					hopCount, thresholdWeight, words, maxTopicNumberToExpand);
			retList = ftSearch(topics, words, pageNum);
			
			/*if (retList == null || retList.size() < hitThreshold) {
				try {
					List<String> spellingSuggestedWords = new ArrayList<String>(words.size());
					for (String word : words) {
						String spelligSuggestedWord = ltManager.getSpellingSuggestion(word);
						if (spelligSuggestedWord != null) 
							spellingSuggestedWords.add(spelligSuggestedWord);
					}
					
					topics = kbManager.getExpansion(fuzzyMatch, prefixMatching, 
							hopCount, thresholdWeight, spellingSuggestedWords, maxTopicNumberToExpand);
					retList = ftSearch(topics, spellingSuggestedWords, pageNum);
				} catch (ConfigParameterMissingException ex) {
					log.fatal("FATAL Exception occured: "+ex.getMessage(),ex);
				} catch (ConfigurationException ex) {
					log.fatal("FATAL Exception occured: "+ex.getMessage(),ex);
				} catch (IOException ex) {
					log.fatal("FATAL Exception occured: "+ex.getMessage(),ex);
				}
			}*/
		}
 		ResultObject ro = new ResultObject(topics, retList);
 		ro.setAllHitNumber(ftsManager.getAllHitNumber());
		
		return ro;
	}


	public DocumentDetails getDocument(String id) {
		return this.ftsManager.getDocument(id);
	}


	public double getResultThreshold() {
		return resultThreshold;
	}


	public void setResultThreshold(double resultThreshold) {
		this.resultThreshold = resultThreshold;
	}


	public int getMaxExpansionNode() {
		return maxExpansionNode;
	}


	public void setMaxExpansionNode(int maxExpansionNode) {
		this.maxExpansionNode = maxExpansionNode;
	}
}
