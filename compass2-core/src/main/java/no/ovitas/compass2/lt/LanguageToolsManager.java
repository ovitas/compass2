package no.ovitas.compass2.lt;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import no.ovitas.compass2.Manager;
import no.ovitas.compass2.exception.ConfigParameterMissingException;
import no.ovitas.compass2.exception.ConfigurationException;

/**
 * @author magyar
 * @version 1.0
 */
public interface LanguageToolsManager extends Manager{

	/**
	 * Get the highest ranked spelling suggestion for the word entered.
	 * 
	 * @param term the word
	 */
	public String getSpellingSuggestion(String term);

	/**
	 * Get all spelling suggestions for the word entered.
	 * 
	 * @param term the word
	 */
	public List<String> getSpellingSuggestions(String term);

	/**
	 * Stem the word passed in and returns the stemmed word
	 * 
	 * @param word
	 */
	public String getStem(String word);

	/**
	 * Stems the words passed in and returns the stemmed words
	 * 
	 * @param words
	 */
	public Collection<String> getStems(Collection<String> words);
	
}