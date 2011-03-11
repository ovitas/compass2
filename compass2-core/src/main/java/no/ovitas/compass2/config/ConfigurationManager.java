package no.ovitas.compass2.config;

import no.ovitas.compass2.config.settings.FullTextSearchPlugin;
import no.ovitas.compass2.config.settings.ImportPlugin;
import no.ovitas.compass2.config.settings.KnowledgeBasePlugin;
import no.ovitas.compass2.config.settings.LanguageToolPlugin;
import no.ovitas.compass2.config.settings.ReferralPlugin;
import no.ovitas.compass2.config.settings.SearchOptions;
import no.ovitas.compass2.config.settings.SearchFields;
import no.ovitas.compass2.config.settings.SuggestionProviderPlugin;
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
	
	public abstract FullTextSearchPlugin getFullTextSearchPlugin();

	public abstract KnowledgeBasePlugin getKnowledgeBasePlugin();

	public abstract LanguageToolPlugin getLanguageToolPlugin();
	
	public abstract ReferralPlugin getReferralPlugin();

	public abstract SearchOptions getSearchOptions();

	public abstract void initConfig() throws ConfigurationException;
	
	public abstract SearchFields getSearchFields();

	/**
	 * Get the import related configuration of the application.
	 * 
	 * @return the import configuration
	 */
	public ImportPlugin getImportPlugin();

	SuggestionProviderPlugin getSuggestionProviderPlugin();
}