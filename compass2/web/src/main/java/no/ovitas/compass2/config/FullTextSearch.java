package no.ovitas.compass2.config;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class FullTextSearch {

	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());
	private String prefixMatch;
	private String fuzzyMatch;
	private FullTextSearchImplementation fullTextSearchImplementation;
	private ContentIndexerImplementation contentIndexerImplementation;

	// Getter / setter methods

	public String getPrefixMatch() {
		return prefixMatch;
	}

	public void setPrefixMatch(String prefixMatch) {
		this.prefixMatch = prefixMatch;
	}

	public String getFuzzyMatch() {
		return fuzzyMatch;
	}

	public void setFuzzyMatch(String fuzzyMatch) {
		this.fuzzyMatch = fuzzyMatch;
	}

	public FullTextSearchImplementation getFullTextSearchImplementation() {
		return fullTextSearchImplementation;
	}

	public void setFullTextSearchImplementation(
			FullTextSearchImplementation fullTextSearchImplementation) {
		this.fullTextSearchImplementation = fullTextSearchImplementation;
	}

	public ContentIndexerImplementation getContentIndexerImplementation() {
		return contentIndexerImplementation;
	}

	public void setContentIndexerImplementation(
			ContentIndexerImplementation contentIndexerImplementation) {
		this.contentIndexerImplementation = contentIndexerImplementation;
	}

	// Constructors

	public FullTextSearch() {}

	// Methods
}
