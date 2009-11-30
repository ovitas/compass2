package no.ovitas.compass2.config;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class FullTextSearch {

	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());
	private boolean prefixMatch;
	private boolean fuzzyMatch;
	private FullTextSearchImplementation fullTextSearchImplementation;
	private ContentIndexerImplementation contentIndexerImplementation;

	// Getter / setter methods

	public boolean prefixMatch() {
		return prefixMatch;
	}

	public void setFuzzyMatch(boolean fuzzyMatch) {
		this.fuzzyMatch = fuzzyMatch;
	}

	public void setPrefixMatch(boolean prefixMatch) {
		this.prefixMatch = prefixMatch;
	}

	public void setPrefixMatch(String prefixMatch) {
		if(prefixMatch != null && prefixMatch.equals("yes")) {
			this.prefixMatch = true;
		} else {
			this.prefixMatch = false;
		}
	}
	
	public boolean getPrefixMatch() {
		return prefixMatch;
	}

	public boolean getFuzzyMatch() {
		return fuzzyMatch;
	}

	public void setFuzzyMatch(String fuzzyMatch) {
		if(fuzzyMatch != null && fuzzyMatch.equals("yes")) {
			this.fuzzyMatch = true;
		} else {
			this.fuzzyMatch = false;
		}
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

	public FullTextSearch() {
		
	}

	// Methods
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "FullTextSearch: prefixMatch: " + prefixMatch + ", fuzzyMatch: " + fuzzyMatch + "\n";
		toDumpOut += ind + fullTextSearchImplementation.dumpOut(ind) + "\n";
		toDumpOut += ind + contentIndexerImplementation.dumpOut(ind) + "\n";
		
		return toDumpOut;
	}
}
