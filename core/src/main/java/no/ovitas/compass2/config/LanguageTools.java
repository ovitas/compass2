package no.ovitas.compass2.config;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class LanguageTools {

	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());
	private LanguageToolsImplementation languageToolsImplementation;

	// Getter / setter methods

	public LanguageToolsImplementation getLanguageToolsImplementation() {
		return languageToolsImplementation;
	}

	public void setLanguageToolsImplementation(
			LanguageToolsImplementation languageToolsImplementation) {
		this.languageToolsImplementation = languageToolsImplementation;
	}

	// Constructors

	public LanguageTools() {
		
	}

	// Methods
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "LangageTools\n";
		toDumpOut += ind + languageToolsImplementation.dumpOut(ind) + "\n";
		
		return toDumpOut;
	}
}
