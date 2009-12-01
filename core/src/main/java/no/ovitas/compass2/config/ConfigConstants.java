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
	public static final String TAG_FULL_TEXT_SEARCH = TAG_ROOT + "/" + "full-text-search";
	public static final String TAG_FULL_TEXT_SEARCH_IMPLEMENTATION = TAG_FULL_TEXT_SEARCH + "/" + "full-text-search-implementation";
	public static final String TAG_PARAMS_FULL_TEXT_SEARCH_IMPLEMENTATION = TAG_FULL_TEXT_SEARCH_IMPLEMENTATION + "/" + "params";
	public static final String TAG_PARAM_FULL_TEXT_SEARCH_IMPLEMENTATION = TAG_PARAMS_FULL_TEXT_SEARCH_IMPLEMENTATION + "/" + "param";
	public static final String TAG_CONTENT_INDEXER_IMPLEMENTATION =  TAG_FULL_TEXT_SEARCH + "/" + "content-indexer-implementation";
	
	// LANGUAGE TOOLS TAG
	public static final String TAG_LANGUAGE_TOOLS = TAG_ROOT + "/" + "language-tools";
	public static final String TAG_LANGUAGE_TOOLS_IMPLEMENTATION = TAG_LANGUAGE_TOOLS + "/" + "language-tools-implementation";
	public static final String TAG_PARAMS_LANGUAGE_TOOLS_IMPLEMENTATION = TAG_LANGUAGE_TOOLS_IMPLEMENTATION + "/" + "params";
	public static final String TAG_PARAM_LANGUAGE_TOOLS_IMPLEMENTATION = TAG_PARAMS_LANGUAGE_TOOLS_IMPLEMENTATION + "/" + "param";
	
	// KNOWLEDGE BASES TAG
	public static final String TAG_KNOWLEDGE_BASES = TAG_ROOT + "/" + "knowledge-bases";
	public static final String TAG_KNOWLEDGE_BASE = TAG_KNOWLEDGE_BASES + "/" + "knowledge-base";
	public static final String TAG_KNOWLEDGE_BASE_IMPLEMENTATION = TAG_KNOWLEDGE_BASE + "/" + "knowledge-base-implementation";
	public static final String TAG_PARAMS_KNOWLEDGE_BASE_IMPLEMENTATION = TAG_KNOWLEDGE_BASE_IMPLEMENTATION + "/" + "params";
	public static final String TAG_PARAM_KNOWLEDGE_BASE_IMPLEMENTATION = TAG_PARAMS_KNOWLEDGE_BASE_IMPLEMENTATION + "/" + "param";
	public static final String TAG_EXPANSION = TAG_KNOWLEDGE_BASE + "/" + "expansion";
	public static final String TAG_ASSOCIATION_TYPES = TAG_EXPANSION + "/" + "association-types";
	public static final String TAG_ASSOCIATION_TYPE = TAG_ASSOCIATION_TYPES + "/" + "association-type";
	
	// RESULT TAG
	public static final String TAG_RESULT = TAG_ROOT + "/" + "result";

	
	// ATTRIBUTE CONSTANTS OF COMPASS2-CONFIG XML FILE
	
	public static final String ATTR_PREFIX_MATCH = "prefix-match";
	public static final String ATTR_FUZZY_MATCH = "fuzzy-match";
	public static final String ATTR_CLASS = "class";
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
	
	
	
}
