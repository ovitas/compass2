package no.ovitas.compass2.service;

import java.io.IOException;
import java.util.List;

import no.ovitas.compass2.exception.ConfigParameterMissingException;
import no.ovitas.compass2.exception.ConfigurationException;

/**
 * @author magyar
 * @version 1.0
 */
public interface LanguageToolsManager {

	
	public void initSpellchecker() throws ConfigParameterMissingException, ConfigurationException, IOException;	
	
	public void setConfiguration(ConfigurationManager manager);
	/**
	 * 
	 * @param userSearch
	 */
	public String getSpellingSuggestion(String userSearch) throws ConfigParameterMissingException, ConfigurationException, IOException;

	/**
	 * 
	 * @param userSearch
	 */
	public List<String> getSpellingSuggestions(String userSearch) throws ConfigParameterMissingException, ConfigurationException, IOException ;

	/**
	 * 
	 * @param word
	 */
	public String getStem(String word);

	/**
	 * 
	 * @param word
	 */
	public List<String> getStems(List<String> tokens);
	
}