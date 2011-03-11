package no.ovitas.compass2.config;

import java.io.File;

import no.ovitas.compass2.config.settings.Compass2Configuration;
import no.ovitas.compass2.config.settings.ContentHandler;
import no.ovitas.compass2.config.settings.ContentHandlerPlugin;
import no.ovitas.compass2.config.settings.ContextFile;
import no.ovitas.compass2.config.settings.ContextFileContainer;
import no.ovitas.compass2.config.settings.DocumentFieldContainer;
import no.ovitas.compass2.config.settings.Field;
import no.ovitas.compass2.config.settings.FullTextSearch;
import no.ovitas.compass2.config.settings.FullTextSearchPlugin;
import no.ovitas.compass2.config.settings.ImplementationReflection;
import no.ovitas.compass2.config.settings.ImplementationSpring;
import no.ovitas.compass2.config.settings.Import;
import no.ovitas.compass2.config.settings.ImportPlugin;
import no.ovitas.compass2.config.settings.Indexer;
import no.ovitas.compass2.config.settings.KnowldegeBaseSetting;
import no.ovitas.compass2.config.settings.KnowledgeBaseSettingPlugin;
import no.ovitas.compass2.config.settings.KnowledgeBasePlugin;
import no.ovitas.compass2.config.settings.LanguageTool;
import no.ovitas.compass2.config.settings.LanguageToolPlugin;
import no.ovitas.compass2.config.settings.Options;
import no.ovitas.compass2.config.settings.Param;
import no.ovitas.compass2.config.settings.ParamContainer;
import no.ovitas.compass2.config.settings.QBuilder;
import no.ovitas.compass2.config.settings.Referral;
import no.ovitas.compass2.config.settings.ReferralPlugin;
import no.ovitas.compass2.config.settings.ScopeSetting;
import no.ovitas.compass2.config.settings.SearchField;
import no.ovitas.compass2.config.settings.SearchFieldString;
import no.ovitas.compass2.config.settings.SearchFields;
import no.ovitas.compass2.config.settings.SearchOptions;
import no.ovitas.compass2.config.settings.SearchSettings;
import no.ovitas.compass2.config.settings.SuggestionProvider;
import no.ovitas.compass2.config.settings.SuggestionProviderPlugin;
import no.ovitas.compass2.config.settings.TopicNameIndexerSetting;
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
		digester.addObjectCreate (ConfigConstants.TAG_ROOT, Compass2Configuration.class);

		// Implementation Spring
		digester.addObjectCreate (ConfigConstants.TAG_IMPLEMENTATION_SPRING, ImplementationSpring.class);
		digester.addSetNext      (ConfigConstants.TAG_IMPLEMENTATION_SPRING, "setImplementation");
		digester.addSetProperties(ConfigConstants.TAG_IMPLEMENTATION_SPRING, ConfigConstants.ATTR_BEAN_NAME, "beanName");
		
		// Implementation Spring ContextFiles
		digester.addObjectCreate (ConfigConstants.TAG_IMPLEMENTATION_SPRING_CONTEXT_FILES, ContextFileContainer.class);
		digester.addSetNext		 (ConfigConstants.TAG_IMPLEMENTATION_SPRING_CONTEXT_FILES, "setContextFiles");

		// Implementation Spring ContextFile
		digester.addObjectCreate (ConfigConstants.TAG_IMPLEMENTATION_SPRING_CONTEXT_FILE, ContextFile.class);
		digester.addSetNext      (ConfigConstants.TAG_IMPLEMENTATION_SPRING_CONTEXT_FILE, "addFile");
		digester.addSetProperties(ConfigConstants.TAG_IMPLEMENTATION_SPRING_CONTEXT_FILE, ConfigConstants.ATTR_FILE, "file");
		
		// Implementation Reflection
		digester.addObjectCreate (ConfigConstants.TAG_IMPLEMENTATION_REFLECTION, ImplementationReflection.class);
		digester.addSetNext      (ConfigConstants.TAG_IMPLEMENTATION_REFLECTION, "setImplementation");
		digester.addSetProperties(ConfigConstants.TAG_IMPLEMENTATION_REFLECTION, ConfigConstants.ATTR_CLASS_NAME, "className");
		
		// Params
		digester.addObjectCreate (ConfigConstants.TAG_PARAMS, ParamContainer.class);
		digester.addSetNext		 (ConfigConstants.TAG_PARAMS, "setParams");
		
		// Param
		digester.addObjectCreate (ConfigConstants.TAG_PARAM, Param.class);
		digester.addSetNext      (ConfigConstants.TAG_PARAM, "addParam");
		digester.addSetProperties(ConfigConstants.TAG_PARAM, ConfigConstants.ATTR_NAME, "name");
		digester.addSetProperties(ConfigConstants.TAG_PARAM, ConfigConstants.ATTR_VALUE, "value");
		
		// Field
		digester.addObjectCreate (ConfigConstants.TAG_FIELD, Field.class);
		digester.addSetNext      (ConfigConstants.TAG_FIELD, "addField");
		digester.addSetProperties(ConfigConstants.TAG_FIELD, ConfigConstants.ATTR_NAME, "name");
		digester.addSetProperties(ConfigConstants.TAG_FIELD, ConfigConstants.ATTR_INDEXED, "indexed");
		digester.addSetProperties(ConfigConstants.TAG_FIELD, ConfigConstants.ATTR_STORED, "stored");
		digester.addSetProperties(ConfigConstants.TAG_FIELD, ConfigConstants.ATTR_TYPE, "type");
		
		// FullTextSearchPlugin
		digester.addObjectCreate (ConfigConstants.TAG_FULL_TEXT_SEARCH_PLUGIN, FullTextSearchPlugin.class);
		digester.addSetNext		 (ConfigConstants.TAG_FULL_TEXT_SEARCH_PLUGIN, "setFullTextSearchPlugin");

		// FullTextSearch
		digester.addObjectCreate (ConfigConstants.TAG_FULL_TEXT_SEARCH, FullTextSearch.class);
		digester.addSetNext		 (ConfigConstants.TAG_FULL_TEXT_SEARCH, "setFullTextSearch");
		
		
		// Indexer
		digester.addObjectCreate (ConfigConstants.TAG_INDEXER, Indexer.class);
		digester.addSetNext      (ConfigConstants.TAG_INDEXER, "setIndexer");
			
		// ContentHandlerPlugin
		digester.addObjectCreate (ConfigConstants.TAG_CONTENT_HANDLER_PLUGIN, ContentHandlerPlugin.class);
		digester.addSetNext		 (ConfigConstants.TAG_CONTENT_HANDLER_PLUGIN, "setContentHandlerPlugin");
	
		//ContentHandler
		digester.addObjectCreate (ConfigConstants.TAG_CONTENT_HANDLER, ContentHandler.class);
		digester.addSetNext		 (ConfigConstants.TAG_CONTENT_HANDLER, "addContentHandler");
		digester.addSetProperties(ConfigConstants.TAG_CONTENT_HANDLER, ConfigConstants.ATTR_TYPE, "type");
		
		// ContentHandler Document-files
		digester.addObjectCreate (ConfigConstants.TAG_CONTENT_HANDLER_DOCUMENT_FIELDS, DocumentFieldContainer.class);
		digester.addSetNext		 (ConfigConstants.TAG_CONTENT_HANDLER_DOCUMENT_FIELDS, "setFields");
		
		// QueryBuilder
		digester.addObjectCreate (ConfigConstants.TAG_QUERY_BUILDER, QBuilder.class);
		digester.addSetNext      (ConfigConstants.TAG_QUERY_BUILDER, "setQueryBuilder");
		
		// LanguageToolsPlugin
		digester.addObjectCreate (ConfigConstants.TAG_LANGUAGE_TOOLS, LanguageToolPlugin.class);
		digester.addSetNext      (ConfigConstants.TAG_LANGUAGE_TOOLS, "setLanguageToolLanguageToolPlugin");
		
		// LanguageToolsImplementation
		digester.addObjectCreate (ConfigConstants.TAG_LANGUAGE_TOOLS_IMPLEMENTATION, LanguageTool.class);
		digester.addSetNext		 (ConfigConstants.TAG_LANGUAGE_TOOLS_IMPLEMENTATION, "setLanguageTool");
		
		
		// KnowledgeBases
		digester.addObjectCreate (ConfigConstants.TAG_KNOWLEDGE_BASES, KnowledgeBasePlugin.class);
		digester.addSetNext		 (ConfigConstants.TAG_KNOWLEDGE_BASES, "setKnowledgeBasePlugin");
		digester.addSetProperties(ConfigConstants.TAG_KNOWLEDGE_BASES, ConfigConstants.ATTR_DEFAULT, "defaultKnowledgeBaseID");
		
		// KnowledgeBase
		digester.addObjectCreate (ConfigConstants.TAG_KNOWLEDGE_BASE, KnowledgeBaseSettingPlugin.class);
		digester.addSetNext		 (ConfigConstants.TAG_KNOWLEDGE_BASE, "addKnowledgeBase");
		digester.addSetProperties(ConfigConstants.TAG_KNOWLEDGE_BASE, ConfigConstants.ATTR_ID, "id");

		// TopicNameIndexer
		digester.addObjectCreate (ConfigConstants.TAG_TOPIC_NAME_INDEXER, TopicNameIndexerSetting.class);
		digester.addSetNext		 (ConfigConstants.TAG_TOPIC_NAME_INDEXER, "setTopicNameIndexerSetting");
		
		// SearchSetting
		digester.addObjectCreate (ConfigConstants.TAG_SEARCH_SETTINGS, SearchSettings.class);
		digester.addSetNext		 (ConfigConstants.TAG_SEARCH_SETTINGS, "setSearchSettings");
		
		// KnowldgeBaseSetting
		digester.addObjectCreate (ConfigConstants.TAG_KNOWLEDGE_BASE_SETTINGS, KnowldegeBaseSetting.class);
		digester.addSetNext		 (ConfigConstants.TAG_KNOWLEDGE_BASE_SETTINGS, "addKnowlegdeBaseSetting");	
		digester.addSetProperties(ConfigConstants.TAG_KNOWLEDGE_BASE_SETTINGS, ConfigConstants.ATTR_NAME, "name");
		
		// Options
		digester.addObjectCreate (ConfigConstants.TAG_OPTIONS, Options.class);
		digester.addSetNext		 (ConfigConstants.TAG_OPTIONS, "setOptions");	
		digester.addSetProperties(ConfigConstants.TAG_OPTIONS, ConfigConstants.ATTR_TREE, "tree");
		
		// Scope
		digester.addObjectCreate (ConfigConstants.TAG_SCOPE, ScopeSetting.class);
		digester.addSetNext		 (ConfigConstants.TAG_SCOPE, "addScope");	
		digester.addSetProperties(ConfigConstants.TAG_SCOPE, ConfigConstants.ATTR_NAME, "name");
		
		// Search-Fields
		digester.addObjectCreate (ConfigConstants.TAG_SEARCH_FIELDS, SearchFields.class);	
		digester.addSetNext		 (ConfigConstants.TAG_SEARCH_FIELDS, "setSearchFields");
		
		// Search Field
		digester.addObjectCreate (ConfigConstants.TAG_SEARCH_FIELD_STRING, SearchFieldString.class);
		digester.addSetNext      (ConfigConstants.TAG_SEARCH_FIELD_STRING, "addField");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_FIELD_STRING, ConfigConstants.ATTR_INDEX_FIELD, "indexField");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_FIELD_STRING, ConfigConstants.ATTR_SEARCH_FIELD, "searchField");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_FIELD_STRING, ConfigConstants.ATTR_SEARCH_TYPE, "type");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_FIELD_STRING, ConfigConstants.ATTR_DEFAULT, "defaultIndex");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_FIELD_STRING, ConfigConstants.ATTR_WEIGHT, "weight");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_FIELD_STRING, ConfigConstants.ATTR_MATCH, "match");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_FIELD_STRING, ConfigConstants.ATTR_FIT, "fit");
		
		digester.addObjectCreate (ConfigConstants.TAG_SEARCH_FIELD_DATEINTERVAL, SearchField.class);
		digester.addSetNext      (ConfigConstants.TAG_SEARCH_FIELD_DATEINTERVAL, "addField");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_FIELD_DATEINTERVAL, ConfigConstants.ATTR_INDEX_FIELD, "indexField");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_FIELD_DATEINTERVAL, ConfigConstants.ATTR_SEARCH_FIELD, "searchField");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_FIELD_DATEINTERVAL, ConfigConstants.ATTR_SEARCH_TYPE, "type");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_FIELD_DATEINTERVAL, ConfigConstants.ATTR_DEFAULT, "defaultIndex");
		
		digester.addObjectCreate (ConfigConstants.TAG_SEARCH_FIELD_NUMERIC, SearchField.class);
		digester.addSetNext      (ConfigConstants.TAG_SEARCH_FIELD_NUMERIC, "addField");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_FIELD_NUMERIC, ConfigConstants.ATTR_INDEX_FIELD, "indexField");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_FIELD_NUMERIC, ConfigConstants.ATTR_SEARCH_FIELD, "searchField");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_FIELD_NUMERIC, ConfigConstants.ATTR_SEARCH_TYPE, "type");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_FIELD_NUMERIC, ConfigConstants.ATTR_DEFAULT, "defaultIndex");
		
		// SearchOptions
		digester.addObjectCreate (ConfigConstants.TAG_SEARCH_OPTIONS, SearchOptions.class);
		digester.addSetNext		 (ConfigConstants.TAG_SEARCH_OPTIONS, "setSearchOptions");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_OPTIONS, ConfigConstants.ATTR_RESULT_THRESHOLD, "resultThreshold");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_OPTIONS, ConfigConstants.ATTR_MAX_NUM_OF_HITS, "maxNumberOfHits");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_OPTIONS, ConfigConstants.ATTR_HOP_COUNT, "hopCount");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_OPTIONS, ConfigConstants.ATTR_PREFIX_MATCH, "prefixMatch");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_OPTIONS, ConfigConstants.ATTR_FUZZY_MATCH, "fuzzyMatch");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_OPTIONS, ConfigConstants.ATTR_TREE_SEARCH, "treeSearch");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_OPTIONS, ConfigConstants.ATTR_TREE, "tree");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_OPTIONS, ConfigConstants.ATTR_MAX_NUM_OF_TOPIC_TO_EXPAND, "maxTopicNumToExpand");
		digester.addSetProperties(ConfigConstants.TAG_SEARCH_OPTIONS, ConfigConstants.ATTR_EXPANSION_THRESHOLD, "expansionThreshold");
		
		// Import
		digester.addObjectCreate (ConfigConstants.TAG_IMPORT_PLUGIN, ImportPlugin.class);
		digester.addSetNext      (ConfigConstants.TAG_IMPORT_PLUGIN, "setImportPlugin");
		digester.addSetProperties(ConfigConstants.TAG_IMPORT_PLUGIN, ConfigConstants.ATTR_IMPORT_DIR, "importDir");
		
		digester.addObjectCreate (ConfigConstants.TAG_IMPORT, Import.class);
		digester.addSetNext      (ConfigConstants.TAG_IMPORT, "addImport");
		digester.addSetProperties(ConfigConstants.TAG_IMPORT, ConfigConstants.ATTR_TYPE, "type");
		
		// Suggestion
		digester.addObjectCreate (ConfigConstants.TAG_SUGGESTION_PLUGIN, SuggestionProviderPlugin.class);
		digester.addSetNext      (ConfigConstants.TAG_SUGGESTION_PLUGIN, "setSuggestionProviderPlugin");
		
		digester.addObjectCreate (ConfigConstants.TAG_SUGGESTION, SuggestionProvider.class);
		digester.addSetNext      (ConfigConstants.TAG_SUGGESTION, "setSuggestionProvider");
		
		//Referral Plugin
		digester.addObjectCreate (ConfigConstants.TAG_REFERRAL_PLUGIN, ReferralPlugin.class);
		digester.addSetNext      (ConfigConstants.TAG_REFERRAL_PLUGIN, "setReferralPlugin");
		
		//Referral
		digester.addObjectCreate (ConfigConstants.TAG_REFERRAL, Referral.class);
		digester.addSetNext      (ConfigConstants.TAG_REFERRAL, "setReferral");
		
	}

}
