package no.ovitas.compass2.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.ovitas.compass2.exception.ConfigParameterMissingException;
import no.ovitas.compass2.exception.ConfigurationException;
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
	

	public ResultObject search(String search, int hopCount,
			double thresholdWeight, boolean prefixMatch, boolean fuzzyMatch) {
		
		List<Hit> hits = null;
		if(search!=null && !search.isEmpty()){
		 	if(search.trim().startsWith("lucene:")){
		 		return this.directLuceneSearch(search, 1);
		 	}else{
		 		return indirectSearch(search, 1, hopCount, thresholdWeight, prefixMatch, fuzzyMatch, 1);
		 	}
		}
		return null;
	}


	public ResultObject search(String search, int hopCount,
			double thresholdWeight, boolean prefixMatch, boolean fuzzyMatch, int pageNum) {
		
		List<Hit> hits = null;
		if(search!=null && !search.isEmpty()){
		 	if(search.trim().startsWith("lucene:")){
		 		return this.directLuceneSearch(search, pageNum);
		 	}else{
		 		return indirectSearch(search, pageNum, hopCount, thresholdWeight, prefixMatch, fuzzyMatch, pageNum);
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
 		return new ResultObject(null,hits);

	}
	
	private void collectNames(TopicTreeNode node, Set<String> res) {
		res.add(node.getName());
		if (node.getAlternativeNames() != null) {
			res.addAll(node.getAlternativeNames());
		}
		for (TopicTreeNode topicTreeNode : node.getChildren()) {
			collectNames(topicTreeNode, res);
		}
	}
	
	private List<Hit> ftSearch(List<Set<TopicTreeNode>> topicSetList, List<String> words, int pageNum) {
		List<Set<String>> stringSetList = new ArrayList<Set<String>>();
		Iterator<String> wordIterator = words.iterator();
		for (Set<TopicTreeNode> topicTreeNodeSet : topicSetList) {
			Set<String> stringSet = new HashSet<String>();
			if (wordIterator.hasNext()) 
				stringSet.add(wordIterator.next());
			for (TopicTreeNode topicTreeNode : topicTreeNodeSet) {				
				collectNames(topicTreeNode, stringSet);
			}
			stringSetList.add(stringSet);
		}
		return ftsManager.doSearch(stringSetList, pageNum);		
	}
	
	protected ResultObject indirectSearch(String search, int i, int hopCount,
			double thresholdWeight, boolean prefixMatching, boolean fuzzyMatch, int pageNum) {
		List<Hit> retList = null;
		List<String> words = this.parseSearchString(search);
		List<Set<TopicTreeNode>> topics = null;
		if (this.useStemmingEnabled){
			words = this.ltManager.getStems(words);
		}
		if (words != null && !words.isEmpty()){
			topics = this.kbManager.getExpansion(fuzzyMatch, prefixMatching, hopCount, thresholdWeight, words);
			retList = ftSearch(topics, words, pageNum);
			
			if (retList == null || retList.size() < hitThreshold) {
				try {
					List<String> spellingSuggestedWords = new ArrayList<String>(words.size());
					for (String word : words) {
						String spelligSuggestedWord = ltManager.getSpellingSuggestion(word);
						if (spelligSuggestedWord != null) 
							spellingSuggestedWords.add(spelligSuggestedWord);
					}
					
					topics = kbManager.getExpansion(fuzzyMatch, prefixMatching, hopCount, thresholdWeight, spellingSuggestedWords);
					retList = ftSearch(topics, spellingSuggestedWords, pageNum);
				} catch (ConfigParameterMissingException ex) {
					log.fatal("FATAL Exception occured: "+ex.getMessage(),ex);
				} catch (ConfigurationException ex) {
					log.fatal("FATAL Exception occured: "+ex.getMessage(),ex);
				} catch (IOException ex) {
					log.fatal("FATAL Exception occured: "+ex.getMessage(),ex);
				}
			}
		}
		
		return new ResultObject(topics, retList);
	}


	public ResultObject search(String search, int hopCount,
			double thresholdWeight, boolean prefixMatch, boolean fuzzyMatch,
			Integer maxTopicNumberToExpand) {
		// TODO Auto-generated method stub
		return null;
	}


	public ResultObject search(String search, int hopCount,
			double thresholdWeight, boolean prefixMatch, boolean fuzzyMatch,
			int pageNum, Integer maxTopicNumberToExpand) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
