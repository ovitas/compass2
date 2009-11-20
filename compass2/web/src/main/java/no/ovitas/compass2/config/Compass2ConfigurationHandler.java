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

		String prefix = ConfigConstants.TAG_COPMASS2_CONFIG;
		
		// Compass2Configuration
		digester.addObjectCreate(prefix, Compass2Configuration.class);

		// FullTextSearch
		digester.addObjectCreate(prefix + "/" + ConfigConstants.TAG_FULL_TEXT_SEARCH, FullTextSearch.class);
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_FULL_TEXT_SEARCH, ConfigConstants.ATTR_PREFIX_MATCH, "prefixMatch");
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_FULL_TEXT_SEARCH, ConfigConstants.ATTR_FUZZY_MATCH, "fuzzyMatch");
		
		// FullTextSearchImplementation
		digester.addObjectCreate(prefix + "/" + ConfigConstants.TAG_FULL_TEXT_SEARCH + "/" + ConfigConstants.TAG_FULL_TEXT_SEARCH_IMPLEMENTATION, FullTextSearchImplementation.class);
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_FULL_TEXT_SEARCH + "/" + ConfigConstants.TAG_FULL_TEXT_SEARCH_IMPLEMENTATION, ConfigConstants.ATTR_CLASS, "className");
		digester.addSetNext(prefix + "/" + ConfigConstants.TAG_FULL_TEXT_SEARCH + "/" + ConfigConstants.TAG_FULL_TEXT_SEARCH_IMPLEMENTATION + "/" + ConfigConstants.TAG_PARAMS + "/" + ConfigConstants.TAG_PARAM, "addParam");
		
		// ContentIndexerImplementation
		digester.addObjectCreate(prefix + "/" + ConfigConstants.TAG_FULL_TEXT_SEARCH + "/" + ConfigConstants.TAG_CONTENT_INDEXER_IMPLEMENTATION, ContentIndexerImplementation.class);
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_FULL_TEXT_SEARCH + "/" + ConfigConstants.TAG_CONTENT_INDEXER_IMPLEMENTATION, ConfigConstants.ATTR_CLASS, "className");
		
		// LanguageTools
		digester.addObjectCreate(prefix + "/" + ConfigConstants.TAG_LANGUAGE_TOOLS, LanguageTools.class);
		
		// LanguageToolsImplementation
		digester.addObjectCreate(prefix + "/" + ConfigConstants.TAG_LANGUAGE_TOOLS + "/" + ConfigConstants.TAG_LANGUAGE_TOOLS_IMPLEMENTATION, LanguageToolsImplementation.class);
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_LANGUAGE_TOOLS + "/" + ConfigConstants.TAG_LANGUAGE_TOOLS_IMPLEMENTATION, ConfigConstants.ATTR_CLASS, "className");
		digester.addSetNext(prefix + "/" + ConfigConstants.TAG_LANGUAGE_TOOLS + "/" + ConfigConstants.TAG_LANGUAGE_TOOLS_IMPLEMENTATION + "/" + ConfigConstants.TAG_PARAMS + "/" + ConfigConstants.TAG_PARAM, "addParam");
		
		// KnowledgeBases
		digester.addObjectCreate(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASES, KnowledgeBases.class);
		
		// KnowledgeBase
		digester.addObjectCreate(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE, KnowledgeBase.class);
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE, ConfigConstants.ATTR_NAME, "name");
		
		// KnowledgeBaseImplementation
		digester.addObjectCreate(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE_IMPLEMENTATION, KnowledgeBaseImplementation.class);
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE_IMPLEMENTATION, ConfigConstants.ATTR_CLASS, "className");
		digester.addSetNext(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE_IMPLEMENTATION + "/" + ConfigConstants.TAG_PARAMS + "/" + ConfigConstants.TAG_PARAM, "addParam");
		
		// Expansion
		digester.addObjectCreate(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE + "/" + ConfigConstants.TAG_EXPANSION, Expansion.class);
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE + "/" + ConfigConstants.TAG_EXPANSION, ConfigConstants.ATTR_USE_RANDOM_WEIGHT, "useRandomWeight");
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE + "/" + ConfigConstants.TAG_EXPANSION, ConfigConstants.ATTR_PREFIX_MATCH, "prefixMatch");
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE + "/" + ConfigConstants.TAG_EXPANSION, ConfigConstants.ATTR_EXPANSION_THRESHOLD, "expansionThreshold");
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE + "/" + ConfigConstants.TAG_EXPANSION, ConfigConstants.ATTR_MAX_NUM_OF_TOPIC_TO_EXPAND, "maxNumOfTopicToExpand");
		
		// AssociationTypes
		digester.addObjectCreate(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE + "/" + ConfigConstants.TAG_EXPANSION  + "/" + ConfigConstants.TAG_ASSOCIATION_TYPES, AssociationTypes.class);
		digester.addSetNext(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE + "/" + ConfigConstants.TAG_EXPANSION  + "/" + ConfigConstants.TAG_ASSOCIATION_TYPES + "/" + ConfigConstants.TAG_ASSOCIATION_TYPE, "addAssociationType");
		
		// AssociationType
		digester.addObjectCreate(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE + "/" + ConfigConstants.TAG_EXPANSION  + "/" + ConfigConstants.TAG_ASSOCIATION_TYPES  + "/" + ConfigConstants.TAG_ASSOCIATION_TYPE, AssociationType.class);
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE + "/" + ConfigConstants.TAG_EXPANSION  + "/" + ConfigConstants.TAG_ASSOCIATION_TYPES  + "/" + ConfigConstants.TAG_ASSOCIATION_TYPE, ConfigConstants.ATTR_ID, "id");
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE + "/" + ConfigConstants.TAG_EXPANSION  + "/" + ConfigConstants.TAG_ASSOCIATION_TYPES  + "/" + ConfigConstants.TAG_ASSOCIATION_TYPE, ConfigConstants.ATTR_NAME, "name");
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE + "/" + ConfigConstants.TAG_EXPANSION  + "/" + ConfigConstants.TAG_ASSOCIATION_TYPES  + "/" + ConfigConstants.TAG_ASSOCIATION_TYPE, ConfigConstants.ATTR_WEIGHT_AHEAD, "weightAhead");
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_KNOWLEDGE_BASE + "/" + ConfigConstants.TAG_EXPANSION  + "/" + ConfigConstants.TAG_ASSOCIATION_TYPES  + "/" + ConfigConstants.TAG_ASSOCIATION_TYPE, ConfigConstants.ATTR_WEIGHT_ABACK, "weigthAback");
		
		// Result
		digester.addObjectCreate(prefix + "/" + ConfigConstants.TAG_RESULT, Result.class);
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_RESULT, ConfigConstants.ATTR_RESULT_THRESHOLD, "resultThreshold");
		
		// Params
		digester.addObjectCreate(prefix + "/" + ConfigConstants.TAG_PARAMS, ParamContainer.class);

		// Param
		digester.addObjectCreate(prefix + "/" + ConfigConstants.TAG_PARAM, Param.class);
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_PARAM, ConfigConstants.ATTR_NAME, "name");
		digester.addSetProperties(prefix + "/" + ConfigConstants.TAG_PARAM, ConfigConstants.ATTR_VALUE, "value");
	}

}
