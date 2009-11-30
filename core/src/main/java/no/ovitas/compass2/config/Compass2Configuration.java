package no.ovitas.compass2.config;

import org.apache.log4j.Logger;

public class Compass2Configuration {
	
	// Attributes
	private Logger logger = Logger.getLogger(this.getClass());
	private FullTextSearch fullTextSearch;
	private LanguageTools languageTools;
	private KnowledgeBases knowledgeBases;
	private Result result;
	
	// Getter / setter methods

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public FullTextSearch getFullTextSearch() {
		return fullTextSearch;
	}

	public void setFullTextSearch(FullTextSearch fullTextSearch) {
		this.fullTextSearch = fullTextSearch;
	}

	public LanguageTools getLanguageTools() {
		return languageTools;
	}

	public void setLanguageTools(LanguageTools languageTools) {
		this.languageTools = languageTools;
	}

	public KnowledgeBases getKnowledgeBases() {
		return knowledgeBases;
	}

	public void setKnowledgeBases(KnowledgeBases knowledgeBases) {
		this.knowledgeBases = knowledgeBases;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
	
	// Constructors

	public Compass2Configuration(){
	}
	
	// Methods
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "Compass2Configuration\n";
		toDumpOut += ind + fullTextSearch.dumpOut(ind) + "\n";
		toDumpOut += ind + languageTools.dumpOut(ind) + "\n";
		toDumpOut += ind + knowledgeBases.dumpOut(ind) + "\n";
		toDumpOut += ind + result.dumpOut(ind) + "\n";
		
		return toDumpOut;
	}
}
