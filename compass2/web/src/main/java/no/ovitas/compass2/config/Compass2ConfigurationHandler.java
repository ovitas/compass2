package no.ovitas.compass2.config;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class Compass2ConfigurationHandler {
	
	// Attributes
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	// Getter / setter methods
	
	// Constructors

	public Compass2ConfigurationHandler() {}

	// Methods
	
	/**
	 * Setup digester
	 */
	protected void setupDigester(Digester digester){
		
		String prefix = ConfigConstants.COPMASS2_CONFIG;
		
		// Compass2Configuration
		digester.addObjectCreate(prefix, Compass2Configuration.class);
		
		// FullTextSearch
		digester.addObjectCreate(prefix + "/" + ConfigConstants.FULL_TEXT_SEARCH, FullTextSearch.class);
		
		// FullTextSearchImplementation
		digester.addObjectCreate(prefix + "/" + ConfigConstants.FULL_TEXT_SEARCH + "/" + ConfigConstants.FULL_TEXT_SEARCH_IMPLEMENTATION, FullTextSearchImplementation.class);
		
		// ContentIndexerImplementation
		digester.addObjectCreate(prefix + "/" + ConfigConstants.FULL_TEXT_SEARCH + "/" + ConfigConstants.CONTENT_INDEXER_IMPLEMENTATION, ContentIndexerImplementation.class);
		
		// LanguageTools
		digester.addObjectCreate(prefix + "/" + ConfigConstants.LANGUAGE_TOOLS, LanguageTools.class);
		
		// LanguageToolsImplementation
		digester.addObjectCreate(prefix + "/" + ConfigConstants.LANGUAGE_TOOLS + "/" + ConfigConstants.LANGUAGE_TOOLS_IMPLEMENTATION, LanguageToolsImplementation.class);
		
		// KnowledgeBases
		digester.addObjectCreate(prefix + "/" + ConfigConstants.KNOWLEDGE_BASES, KnowledgeBases.class);
		
		// KnowledgeBase
		digester.addObjectCreate(prefix + "/" + ConfigConstants.KNOWLEDGE_BASE, KnowledgeBase.class);
		
		// KnowledgeBaseImplementation
		digester.addObjectCreate(prefix + "/" + ConfigConstants.KNOWLEDGE_BASE + "/" + ConfigConstants.KNOWLEDGE_BASE_IMPLEMENTATION, KnowledgeBaseImplementation.class);
		
		// Expansion
		digester.addObjectCreate(prefix + "/" + ConfigConstants.KNOWLEDGE_BASE + "/" + ConfigConstants.EXPANSION, Expansion.class);
		
		// AssociationTypes
		digester.addObjectCreate(prefix + "/" + ConfigConstants.KNOWLEDGE_BASE + "/" + ConfigConstants.EXPANSION  + "/" + ConfigConstants.ASSOCIATION_TYPES, AssociationTypes.class);
		
		// AssociationType
		digester.addObjectCreate(prefix + "/" + ConfigConstants.KNOWLEDGE_BASE + "/" + ConfigConstants.EXPANSION  + "/" + ConfigConstants.ASSOCIATION_TYPES  + "/" + ConfigConstants.ASSOCIATION_TYPE, AssociationType.class);
		
		// Result
		digester.addObjectCreate(prefix + "/" + ConfigConstants.RESULT, Result.class);		
	}

}
