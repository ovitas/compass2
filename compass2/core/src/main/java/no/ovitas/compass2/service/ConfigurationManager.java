package no.ovitas.compass2.service;

import no.ovitas.compass2.config.FullTextSearch;
import no.ovitas.compass2.config.KnowledgeBase;
import no.ovitas.compass2.config.LanguageToolsImplementation;
import no.ovitas.compass2.config.Result;
import no.ovitas.compass2.exception.ConfigurationException;

/**
 * @author magyar
 * @version 1.0
 */
public interface ConfigurationManager {

	/**
	 * 
	 * @param parameterName
	 */
	public String getConfigParameter(String parameterName);
	
	public abstract FullTextSearch getFullTextSearch();

	public abstract KnowledgeBase getKnowledgeBase(String name);

	public abstract LanguageToolsImplementation getLanguageToolsImplementation();

	public abstract Result getResult();

	public abstract void initConfig() throws ConfigurationException;
	

}