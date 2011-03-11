package no.ovitas.compass2.config;
/**
 * @author csanyi
 * 
 */
public class ConfigConstants {
	
	// TAG CONSTANTS OF COMPASS2-CONFIG XML FILE

	// ROOT TAG
	public static final String TAG_ROOT = "compass2-config";
	
	// FULLTEXT SEARCH TAG
	public static final String TAG_FULL_TEXT_SEARCH_PLUGIN = TAG_ROOT + "/" + "full-text-search-plugin";
	public static final String TAG_FULL_TEXT_SEARCH = TAG_FULL_TEXT_SEARCH_PLUGIN + "/" + "full-text-search";
	public static final String TAG_INDEXER = TAG_FULL_TEXT_SEARCH_PLUGIN + "/" + "indexer";
	public static final String TAG_CONTENT_HANDLER_PLUGIN = TAG_FULL_TEXT_SEARCH_PLUGIN + "/" + "content-handler-plugin";
	public static final String TAG_CONTENT_HANDLER =  TAG_CONTENT_HANDLER_PLUGIN + "/" + "content-handler";
	public static final String TAG_CONTENT_HANDLER_DOCUMENT_FIELDS = TAG_CONTENT_HANDLER + "/" + "document-fields";
	public static final String TAG_QUERY_BUILDER = TAG_FULL_TEXT_SEARCH_PLUGIN + "/" + "query-builder";
	
	// LANGUAGE TOOLS TAG
	public static final String TAG_LANGUAGE_TOOLS = TAG_ROOT + "/" + "language-tool-plugin";
	public static final String TAG_LANGUAGE_TOOLS_IMPLEMENTATION = TAG_LANGUAGE_TOOLS + "/" + "language-tool";
	
	// KNOWLEDGE BASES TAG
	public static final String TAG_KNOWLEDGE_BASES = TAG_ROOT + "/" + "knowledge-base-plugin";
	public static final String TAG_KNOWLEDGE_BASE = TAG_KNOWLEDGE_BASES + "/" + "knowledge-base";
	public static final String TAG_TOPIC_NAME_INDEXER = TAG_KNOWLEDGE_BASE + "/" + "topic-name-indexer";
	public static final String TAG_SEARCH_SETTINGS = TAG_KNOWLEDGE_BASE + "/" + "search-settings";
	public static final String TAG_KNOWLEDGE_BASE_SETTINGS = TAG_SEARCH_SETTINGS + "/" + "knowledge-base-setting";
	public static final String TAG_OPTIONS = TAG_KNOWLEDGE_BASE_SETTINGS + "/" + "options";
	public static final String TAG_SCOPE = TAG_KNOWLEDGE_BASE_SETTINGS + "/" + "scope";

	// SEARCH BASES TAG
	public static final String TAG_SEARCH_FIELDS = TAG_ROOT + "/" + "search-fields";
	public static final String TAG_SEARCH_FIELD_STRING = TAG_SEARCH_FIELDS + "/" + "search-field-string";
	public static final String TAG_SEARCH_FIELD_DATEINTERVAL = TAG_SEARCH_FIELDS + "/" + "search-field-dateinterval";
	public static final String TAG_SEARCH_FIELD_NUMERIC = TAG_SEARCH_FIELDS + "/" + "search-field-numeric";
	
	// RESULT TAG
	public static final String TAG_SEARCH_OPTIONS = TAG_ROOT + "/" + "search-options";

	// IMPORT TAGS
	public static final String TAG_IMPORT_PLUGIN = TAG_ROOT + "/" + "import-plugin";
	public static final String TAG_IMPORT = TAG_IMPORT_PLUGIN + "/"
			+ "import";

	public static final String TAG_SUGGESTION_PLUGIN = TAG_ROOT + "/" + "suggestion-provider-plugin";
	public static final String TAG_SUGGESTION = TAG_SUGGESTION_PLUGIN + "/" + "suggestion-provider";

	public static final String TAG_REFERRAL_PLUGIN = TAG_ROOT + "/" + "referral-plugin";
	public static final String TAG_REFERRAL = TAG_REFERRAL_PLUGIN + "/" + "referral";

	// ATTRIBUTE CONSTANTS OF COMPASS2-CONFIG XML FILE
	
	public static final String ATTR_PREFIX_MATCH = "prefix-match";
	public static final String ATTR_FUZZY_MATCH = "fuzzy-match";

	public static final String ATTR_USE_RANDOM_WEIGHT = "use-random-weight";
	public static final String ATTR_EXPANSION_THRESHOLD = "expansion-threshold";
	public static final String ATTR_MAX_NUM_OF_TOPIC_TO_EXPAND = "max-nr-of-topic-to-expand";
	public static final String ATTR_ID = "id";
	public static final String ATTR_NAME = "name";
	public static final String ATTR_WEIGHT_AHEAD = "weight-ahead";
	public static final String ATTR_WEIGHT_ABACK = "weight-aback";
	public static final String ATTR_RESULT_THRESHOLD = "result-threshold";
	public static final String ATTR_MAX_NUM_OF_HITS = "max-number-of-hits";
	public static final String ATTR_HOP_COUNT = "hop-count";
	public static final String ATTR_VALUE = "value";
	public static final String ATTR_INDEXED = "indexed";
	public static final String ATTR_STORED = "stored";
	public static final String ATTR_INDEX_FIELD = "index-field";
	public static final String ATTR_SEARCH_FIELD = "search-field";
	public static final String ATTR_SEARCH_TYPE = "type";
	public static final String ATTR_FIELD_TYPE = "type";
	public static final String ATTR_DEFAULT = "default";
	public static final String ATTR_SPRING_MODE = "spring-mode";
	public static final String ATTR_BEAN = "bean";
	public static final String ATTR_FILE = "file";
	public static final String ATTR_WEIGHT = "weight";
	public static final String ATTR_MATCH = "match";
	public static final String ATTR_FIT = "fit";
	public static final String ATTR_IMPORT_DIR = "import-dir";
	public static final String ATTR_TYPE = "type";
	public static final String ATTR_TREE_SEARCH = "tree-search";
	public static final String ATTR_TREE = "tree";

	// BEAN CONSTANTS
	public static final String BEAN_CONFIGURATION_MANAGER = "configurationManager";
	
	public static final String TAG_IMPLEMENTATION_SPRING = "*/implementation-spring";
	public static final String TAG_IMPLEMENTATION_SPRING_CONTEXT_FILES = TAG_IMPLEMENTATION_SPRING + "/" + "context-files";
	public static final String TAG_IMPLEMENTATION_SPRING_CONTEXT_FILE = TAG_IMPLEMENTATION_SPRING_CONTEXT_FILES + "/" + "context-file";

	public static final String TAG_IMPLEMENTATION_REFLECTION = "*/implementation-reflection";

	public static final String TAG_PARAMS = "*/params";
	public static final String TAG_PARAM = TAG_PARAMS + "/" + "param";
	
	public static final String ATTR_BEAN_NAME = "beanName";
	public static final String ATTR_CLASS_NAME = "className";

	public static final String TAG_FIELD = "*/document-fields/field";


















}
