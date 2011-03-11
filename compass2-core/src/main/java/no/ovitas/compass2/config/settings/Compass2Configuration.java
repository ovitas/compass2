package no.ovitas.compass2.config.settings;

import org.apache.log4j.Logger;

public class Compass2Configuration {

	// Attributes
	private Logger logger = Logger.getLogger(this.getClass());
	private FullTextSearchPlugin fullTextSearchPlugin;
	private LanguageToolPlugin languageToolLanguageToolPlugin;
	private KnowledgeBasePlugin knowledgeBasePlugin;
	private SearchFields searchFields;
	private SearchOptions searchOptions;
	private ImportPlugin importPlugin;
	private SuggestionProviderPlugin suggestionProviderPlugin;
	private ReferralPlugin referralPlugin;

	// Getter / setter methods

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	// Constructors

	public Compass2Configuration() {
	}

	// Methods

	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "Compass2Configuration\n";
		if (fullTextSearchPlugin != null) {
			toDumpOut += ind + fullTextSearchPlugin.dumpOut(ind) + "\n";
		}
		if (languageToolLanguageToolPlugin != null) {
			toDumpOut += ind + languageToolLanguageToolPlugin.dumpOut(ind)
					+ "\n";
		}
		toDumpOut += ind + knowledgeBasePlugin.dumpOut(ind) + "\n";
		toDumpOut += ind + searchFields.dumpOut(ind) + "\n";
		toDumpOut += ind + searchOptions.dumpOut(ind) + "\n";
		if (importPlugin != null) {

			toDumpOut += ind + importPlugin.dumpOut(ind) + "\n";

		}
		
		if (suggestionProviderPlugin != null) {
			toDumpOut += ind + suggestionProviderPlugin.dumpOut(ind) + "\n";
		}
		
		if (referralPlugin != null) {
			toDumpOut += ind + referralPlugin.dumpOut(ind) + "\n";
		}

		return toDumpOut;
	}


	/**
	 * This is a getter method for fullTextSearchPlugin.
	 * 
	 * @return the fullTextSearchPlugin
	 */
	public FullTextSearchPlugin getFullTextSearchPlugin() {
		return fullTextSearchPlugin;
	}

	/**
	 * This is a setter method for fullTextSearchPlugin.
	 * 
	 * @param fullTextSearchPlugin
	 *            the fullTextSearchPlugin to set
	 */
	public void setFullTextSearchPlugin(
			FullTextSearchPlugin fullTextSearchPlugin) {
		this.fullTextSearchPlugin = fullTextSearchPlugin;
	}

	/**
	 * This is a getter method for languageToolLanguageToolPlugin.
	 * 
	 * @return the languageToolLanguageToolPlugin
	 */
	public LanguageToolPlugin getLanguageToolLanguageToolPlugin() {
		return languageToolLanguageToolPlugin;
	}

	/**
	 * This is a setter method for languageToolLanguageToolPlugin.
	 * 
	 * @param languageToolLanguageToolPlugin
	 *            the languageToolLanguageToolPlugin to set
	 */
	public void setLanguageToolLanguageToolPlugin(
			LanguageToolPlugin languageToolLanguageToolPlugin) {
		this.languageToolLanguageToolPlugin = languageToolLanguageToolPlugin;
	}

	/**
	 * This is a getter method for knowledgeBasePlugin.
	 * 
	 * @return the knowledgeBasePlugin
	 */
	public KnowledgeBasePlugin getKnowledgeBasePlugin() {
		return knowledgeBasePlugin;
	}

	/**
	 * This is a setter method for knowledgeBasePlugin.
	 * 
	 * @param knowledgeBasePlugin
	 *            the knowledgeBasePlugin to set
	 */
	public void setKnowledgeBasePlugin(KnowledgeBasePlugin knowledgeBasePlugin) {
		this.knowledgeBasePlugin = knowledgeBasePlugin;
	}

	/**
	 * This is a getter method for searchFields.
	 * 
	 * @return the searchFields
	 */
	public SearchFields getSearchFields() {
		return searchFields;
	}

	/**
	 * This is a setter method for searchFields.
	 * 
	 * @param searchFields
	 *            the searchFields to set
	 */
	public void setSearchFields(SearchFields searchFields) {
		this.searchFields = searchFields;
	}

	/**
	 * This is a getter method for searchOptions.
	 * 
	 * @return the searchOptions
	 */
	public SearchOptions getSearchOptions() {
		return searchOptions;
	}

	/**
	 * This is a setter method for searchOptions.
	 * 
	 * @param searchOptions
	 *            the searchOptions to set
	 */
	public void setSearchOptions(SearchOptions searchOptions) {
		this.searchOptions = searchOptions;
	}

	/**
	 * This is a getter method for importPlugin.
	 * 
	 * @return the importPlugin
	 */
	public ImportPlugin getImportPlugin() {
		return importPlugin;
	}

	/**
	 * This is a setter method for importPlugins.
	 * 
	 * @param importPlugin
	 *            the importPlugin to set
	 */
	public void setImportPlugin(ImportPlugin importPlugin) {
		this.importPlugin = importPlugin;
	}

	public SuggestionProviderPlugin getSuggestionProviderPlugin() {
		return suggestionProviderPlugin;
	}

	/**
	 * This is a setter method for suggestionProviderPlugin.
	 * @param suggestionProviderPlugin the suggestionProviderPlugin to set
	 */
	public void setSuggestionProviderPlugin(
			SuggestionProviderPlugin suggestionProviderPlugin) {
		this.suggestionProviderPlugin = suggestionProviderPlugin;
	}

	public ReferralPlugin getReferralPlugin() {
		return referralPlugin;
	}

	public void setReferralPlugin(ReferralPlugin referralPlugin) {
		this.referralPlugin = referralPlugin;
	}

}
