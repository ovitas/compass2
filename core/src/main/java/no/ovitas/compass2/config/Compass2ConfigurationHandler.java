package no.ovitas.compass2.config;

import java.io.File;

import no.ovitas.compass2.exception.ConfigurationException;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class Compass2ConfigurationHandler {
	
	// Attributes
	
	private static Logger logger = Logger.getLogger(Compass2ConfigurationHandler.class);
	protected Compass2Configuration config = null;
	private static Compass2ConfigurationHandler instance = null;

	
	public static Compass2ConfigurationHandler getInstance(){
		if(instance==null){
			instance = new Compass2ConfigurationHandler();
		}
		return instance;
	}
	// Getter / setter methods
	
	public Compass2Configuration getConfig() {
		return config;
	}

	
	// Constructor
	public Compass2ConfigurationHandler() {
	}

	// Methods
	
	/**
	 * Actual content loading.
	 */
	public void loadConfig(String configPath) throws ConfigurationException{
	
		Digester digester = new Digester();
		setupDigester(digester);
		digester.setValidating(true);
		digester.setStackAction(new ConfigDigesterStackAction());
		
		File cfile = new File( configPath);
		if(cfile!=null && cfile.canRead() && cfile.isFile()){
			try {
				config = (Compass2Configuration)digester.parse(cfile);
			} catch (Exception e) {
				logger.fatal("Exception occured while loading and processing the configuration: " + e.getMessage());
				throw new ConfigurationException("Exception occured while loading and processing the configuration: " + e.getMessage(),e);
			}
		}
	}
	
	/**
	 * Setup digester
	 */
	private void setupDigester(Digester digester){
	
		// Compass2Configuration
		digester.addObjectCreate(ConfigConstants.TAG_ROOT, Compass2Configuration.class);

		// FullTextSearch
		digester.addObjectCreate (ConfigConstants.TAG_FULL_TEXT_SEARCH, FullTextSearch.class);
		digester.addSetNext		 (ConfigConstants.TAG_FULL_TEXT_SEARCH, "setFullTextSearch");
		digester.addSetProperties(ConfigConstants.TAG_FULL_TEXT_SEARCH, ConfigConstants.ATTR_PREFIX_MATCH, "prefixMatch");
		digester.addSetProperties(ConfigConstants.TAG_FULL_TEXT_SEARCH, ConfigConstants.ATTR_FUZZY_MATCH, "fuzzyMatch");
		
		
		// FullTextSearchImplementation
		digester.addObjectCreate (ConfigConstants.TAG_FULL_TEXT_SEARCH_IMPLEMENTATION, FullTextSearchImplementation.class);
		digester.addSetNext		 (ConfigConstants.TAG_FULL_TEXT_SEARCH_IMPLEMENTATION, "setFullTextSearchImplementation");
		digester.addSetProperties(ConfigConstants.TAG_FULL_TEXT_SEARCH_IMPLEMENTATION, ConfigConstants.ATTR_CLASS, "className");
		
		// FullTextSearchImplementation Params
		digester.addObjectCreate (ConfigConstants.TAG_PARAMS_FULL_TEXT_SEARCH_IMPLEMENTATION, ParamContainer.class);
		digester.addSetNext		 (ConfigConstants.TAG_PARAMS_FULL_TEXT_SEARCH_IMPLEMENTATION, "setParams");

		// FullTextSearchImplementation Param
		digester.addObjectCreate (ConfigConstants.TAG_PARAM_FULL_TEXT_SEARCH_IMPLEMENTATION, Param.class);
		digester.addSetNext      (ConfigConstants.TAG_PARAM_FULL_TEXT_SEARCH_IMPLEMENTATION, "addParam");
		digester.addSetProperties(ConfigConstants.TAG_PARAM_FULL_TEXT_SEARCH_IMPLEMENTATION, ConfigConstants.ATTR_NAME, "name");
		digester.addSetProperties(ConfigConstants.TAG_PARAM_FULL_TEXT_SEARCH_IMPLEMENTATION, ConfigConstants.ATTR_VALUE, "value");
		
		// ContentIndexerImplementation
		digester.addObjectCreate (ConfigConstants.TAG_CONTENT_INDEXER_IMPLEMENTATION, ContentIndexerImplementation.class);
		digester.addSetNext		 (ConfigConstants.TAG_CONTENT_INDEXER_IMPLEMENTATION, "setContentIndexerImplementation");
		digester.addSetProperties(ConfigConstants.TAG_CONTENT_INDEXER_IMPLEMENTATION, ConfigConstants.ATTR_CLASS, "className");
		
		// LanguageTools
		digester.addObjectCreate (ConfigConstants.TAG_LANGUAGE_TOOLS, LanguageTools.class);
		digester.addSetNext(ConfigConstants.TAG_LANGUAGE_TOOLS, "setLanguageTools");
		
		// LanguageToolsImplementation
		digester.addObjectCreate (ConfigConstants.TAG_LANGUAGE_TOOLS_IMPLEMENTATION, LanguageToolsImplementation.class);
		digester.addSetNext		 (ConfigConstants.TAG_LANGUAGE_TOOLS_IMPLEMENTATION, "setLanguageToolsImplementation");
		digester.addSetProperties(ConfigConstants.TAG_LANGUAGE_TOOLS_IMPLEMENTATION, ConfigConstants.ATTR_CLASS, "className");
		
		// LanguageToolsImplementation Params
		digester.addObjectCreate (ConfigConstants.TAG_PARAMS_LANGUAGE_TOOLS_IMPLEMENTATION, ParamContainer.class);
		digester.addSetNext		 (ConfigConstants.TAG_PARAMS_LANGUAGE_TOOLS_IMPLEMENTATION, "setParams");

		// LanguageToolsImplementation Param
		digester.addObjectCreate (ConfigConstants.TAG_PARAM_LANGUAGE_TOOLS_IMPLEMENTATION, Param.class);
		digester.addSetNext      (ConfigConstants.TAG_PARAM_LANGUAGE_TOOLS_IMPLEMENTATION, "addParam");
		digester.addSetProperties(ConfigConstants.TAG_PARAM_LANGUAGE_TOOLS_IMPLEMENTATION, ConfigConstants.ATTR_NAME, "name");
		digester.addSetProperties(ConfigConstants.TAG_PARAM_LANGUAGE_TOOLS_IMPLEMENTATION, ConfigConstants.ATTR_VALUE, "value");
		
		// KnowledgeBases
		digester.addObjectCreate (ConfigConstants.TAG_KNOWLEDGE_BASES, KnowledgeBases.class);
		digester.addSetNext		 (ConfigConstants.TAG_KNOWLEDGE_BASES, "setKnowledgeBases");
		
		// KnowledgeBase
		digester.addObjectCreate (ConfigConstants.TAG_KNOWLEDGE_BASE, KnowledgeBase.class);
		digester.addSetNext		 (ConfigConstants.TAG_KNOWLEDGE_BASE, "addElement");
		digester.addSetProperties(ConfigConstants.TAG_KNOWLEDGE_BASE, ConfigConstants.ATTR_NAME, "name");
		
		// KnowledgeBaseImplementation
		digester.addObjectCreate (ConfigConstants.TAG_KNOWLEDGE_BASE_IMPLEMENTATION, KnowledgeBaseImplementation.class);
		digester.addSetNext		 (ConfigConstants.TAG_KNOWLEDGE_BASE_IMPLEMENTATION, "setKnowledgeBaseImplementation");
		digester.addSetProperties(ConfigConstants.TAG_KNOWLEDGE_BASE_IMPLEMENTATION, ConfigConstants.ATTR_CLASS, "className");

		// KnowledgeBaseImplementation Params
		digester.addObjectCreate (ConfigConstants.TAG_PARAMS_KNOWLEDGE_BASE_IMPLEMENTATION, ParamContainer.class);
		digester.addSetNext		 (ConfigConstants.TAG_PARAMS_KNOWLEDGE_BASE_IMPLEMENTATION, "setParams");
		
		// KnowledgeBaseImplementation Param
		digester.addObjectCreate (ConfigConstants.TAG_PARAM_KNOWLEDGE_BASE_IMPLEMENTATION, Param.class);
		digester.addSetNext      (ConfigConstants.TAG_PARAM_KNOWLEDGE_BASE_IMPLEMENTATION, "addParam");
		digester.addSetProperties(ConfigConstants.TAG_PARAM_KNOWLEDGE_BASE_IMPLEMENTATION, ConfigConstants.ATTR_NAME, "name");
		digester.addSetProperties(ConfigConstants.TAG_PARAM_KNOWLEDGE_BASE_IMPLEMENTATION, ConfigConstants.ATTR_VALUE, "value");
		
		// Expansion
		digester.addObjectCreate (ConfigConstants.TAG_EXPANSION, Expansion.class);
		digester.addSetNext		 (ConfigConstants.TAG_EXPANSION, "setExpansion");
		digester.addSetProperties(ConfigConstants.TAG_EXPANSION, ConfigConstants.ATTR_USE_RANDOM_WEIGHT, "useRandomWeight");
		digester.addSetProperties(ConfigConstants.TAG_EXPANSION, ConfigConstants.ATTR_EXPANSION_THRESHOLD, "expansionThreshold");
		digester.addSetProperties(ConfigConstants.TAG_EXPANSION, ConfigConstants.ATTR_MAX_NUM_OF_TOPIC_TO_EXPAND, "maxNumOfTopicToExpand");
		digester.addSetProperties(ConfigConstants.TAG_EXPANSION, ConfigConstants.ATTR_HOP_COUNT, "hopCount");
		
		// AssociationTypes
		digester.addObjectCreate (ConfigConstants.TAG_ASSOCIATION_TYPES, AssociationTypes.class);	
		digester.addSetNext		 (ConfigConstants.TAG_ASSOCIATION_TYPES, "setAssociationTypes");
		
		// AssociationType
		digester.addObjectCreate (ConfigConstants.TAG_ASSOCIATION_TYPE, AssociationType.class);
		digester.addSetNext      (ConfigConstants.TAG_ASSOCIATION_TYPE, "addElement");
		digester.addSetProperties(ConfigConstants.TAG_ASSOCIATION_TYPE, ConfigConstants.ATTR_ID, "id");
		digester.addSetProperties(ConfigConstants.TAG_ASSOCIATION_TYPE, ConfigConstants.ATTR_NAME, "name");
		digester.addSetProperties(ConfigConstants.TAG_ASSOCIATION_TYPE, ConfigConstants.ATTR_WEIGHT_AHEAD, "weightAhead");
		digester.addSetProperties(ConfigConstants.TAG_ASSOCIATION_TYPE, ConfigConstants.ATTR_WEIGHT_ABACK, "weightAback");
		
		// Result
		digester.addObjectCreate (ConfigConstants.TAG_RESULT, Result.class);
		digester.addSetNext		 (ConfigConstants.TAG_RESULT, "setResult");
		digester.addSetProperties(ConfigConstants.TAG_RESULT, ConfigConstants.ATTR_RESULT_THRESHOLD, "resultThreshold");
		digester.addSetProperties(ConfigConstants.TAG_RESULT, ConfigConstants.ATTR_MAX_NUM_OF_HITS, "maxNumberOfHits");
		
	}

}
