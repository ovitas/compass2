package no.ovitas.compass2.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.config.LanguageToolsImplementation;
import no.ovitas.compass2.exception.ConfigParameterMissingException;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.LanguageToolsManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.spell.LevensteinDistance;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @author magyar
 * @version 1.0
 * @created 24-márc.-2009 9:35:40
 */
public class LuceneLTManagerImpl implements LanguageToolsManager {

	protected ConfigurationManager configManager;
    private Log log = LogFactory.getLog(getClass());
	private LanguageToolsImplementation ltImpl;
    
	@Override
	public void setLanguageToolsImpl(LanguageToolsImplementation ltImpl) {
		this.ltImpl = ltImpl;
		
	}
	
	public void setConfiguration(ConfigurationManager manager){
		configManager = manager;
	}
	public LuceneLTManagerImpl(){

	}

	public void initSpellchecker() throws ConfigParameterMissingException, ConfigurationException, IOException {
		String spellCheckDir = getLTImplementationParamValue(Constants.LUCENE_SPELLCHECKER_DIRECTORY);
		String indexDir = getLTImplementationParamValue(Constants.LUCENE_SPELLCHECKER_INDEX_DIRECTORY);
		String indexField = getLTImplementationParamValue(Constants.LUCENE_SPELLCHECKER_FIELD);
		if(spellCheckDir==null){
			new ConfigParameterMissingException("Paremeter: "+Constants.LUCENE_SPELLCHECKER_DIRECTORY+" is missing from config!");
		}
		if(indexDir==null){
			new ConfigParameterMissingException("Paremeter: "+Constants.LUCENE_SPELLCHECKER_INDEX_DIRECTORY+" is missing from config!");
		}
		if(indexField==null){
			new ConfigParameterMissingException("Paremeter: "+Constants.LUCENE_SPELLCHECKER_FIELD+" is missing from config!");
		}
		java.io.File scd = new java.io.File(spellCheckDir);
		if(!scd.isDirectory()){
			new ConfigurationException(spellCheckDir +" is not a directory!");
		}
		Directory dir = FSDirectory.getDirectory(scd, null);
		SpellChecker spell = new SpellChecker(dir);
		IndexReader r = IndexReader.open(indexDir); //#2
		try {
		 spell.indexDictionary(new LuceneDictionary(r, indexField)); //#3
		} finally {
		r.close();
		}
		dir.close();
	}

	
	/**
	 * 
	 * @param userSearch
	 */
	public String getSpellingSuggestion(String userSearch)throws ConfigParameterMissingException, ConfigurationException, IOException {
		String spellCheckDir = getLTImplementationParamValue(Constants.LUCENE_SPELLCHECKER_DIRECTORY);
		if(spellCheckDir==null){
			new ConfigParameterMissingException("Paremeter: "+Constants.LUCENE_SPELLCHECKER_DIRECTORY+" is missing from config!");
		}
		java.io.File scd = new java.io.File(spellCheckDir);
		if(!scd.isDirectory()){
			new ConfigurationException(spellCheckDir +" is not a directory!");
		}
		Directory dir = FSDirectory.getDirectory(scd, null);
		SpellChecker spell = new SpellChecker(dir);
		spell.setStringDistance(new LevensteinDistance());
		String[] suggestions = spell.suggestSimilar(userSearch,1);
		if(suggestions!=null && suggestions.length>0){
			return suggestions[0];
		}
		
		return null;
	}

	/**
	 * 
	 * @param userSearch
	 */
	public List<String> getSpellingSuggestions(String userSearch)throws ConfigParameterMissingException, ConfigurationException, IOException {
		String spellCheckDir = getLTImplementationParamValue(Constants.LUCENE_SPELLCHECKER_DIRECTORY);
		if(spellCheckDir==null){
			new ConfigParameterMissingException("Paremeter: "+Constants.LUCENE_SPELLCHECKER_DIRECTORY+" is missing from config!");
		}
		java.io.File scd = new java.io.File(spellCheckDir);
		if(!scd.isDirectory()){
			new ConfigurationException(spellCheckDir +" is not a directory!");
		}
		Directory dir = FSDirectory.getDirectory(scd, null);
		SpellChecker spell = new SpellChecker(dir);
		spell.setStringDistance(new LevensteinDistance());
		String suggestionNum = getLTImplementationParamValue(Constants.LUCENE_SPELLCHECK_MAX_SUGGESTION_NUM);
		int sn = 5;
		if(suggestionNum!=null){
			try{
			 sn = Integer.parseInt(suggestionNum);	
			}catch(Exception ex){
				
			}
		}
		String[] suggestions = spell.suggestSimilar(userSearch,sn);
		if(suggestions!=null && suggestions.length>0){
			List<String> s = new ArrayList<String>();
			for(String suggestion : suggestions){
				s.add(suggestion);
			}
			return s;
		}
		return null;
	}

	/**
	 * 
	 * @param word
	 */
	public String getStem(String word){
		String language = getLTImplementationParamValue(Constants.SNOWBALL_LANGUAGE);
		Analyzer a = new SnowballAnalyzer(language);
		try {
		    QueryParser qp = new QueryParser("", a);
		    Query stemmed = qp.parse(word); // Throws ParseException
			return stemmed.toString();
		} catch (ParseException pe) {
			log.error(pe.getMessage());
			return null;
		}
	}

	/**
	 * 
	 * @param word
	 */
	public List<String> getStems(List<String> tokens){
		List<String> stems = new ArrayList<String>();
		String language = getLTImplementationParamValue(Constants.SNOWBALL_LANGUAGE);
		Analyzer a = new SnowballAnalyzer(language);
		try {
			for(String token: tokens)
			{
			    QueryParser qp = new QueryParser("", a);
			    Query stemmed = qp.parse(token); // Throws ParseException
			    stems.add(stemmed.toString());
			}
			return stems;
		} catch (ParseException pe) {
			log.error(pe.getMessage());
			return null;
		}		
	}
	
	/**
	 * Get parameter value of specified paramName in LanguageToolImplementation
	 * @param paramName
	 * @return the value
	 */
	private String getLTImplementationParamValue(String paramName) {
		return configManager.getLanguageToolsImplementation().getParams().getParam(paramName).getValue();
	}

}