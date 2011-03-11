package no.ovitas.compass2.exception;

/**
 * Id definitions of the Compass2 business errors.
 * 
 * @author Csaba Daniel
 */
public interface CompassErrorID {

	// Import error ids

	/**
	 * Invalid resource URL.
	 */
	public static final long IMP_INVALID_RES_URL = 30;

	/**
	 * Import resource is missing or unavailable.
	 */
	public static final long IMP_RES_MISSING = 31;

	/**
	 * Import mapping resource is missing or unavailable.
	 */
	public static final long IMP_MAP_RES_MISSING = 32;

	/**
	 * I/O error occured during import.
	 */
	public static final long IMP_IO_ERROR = 33;

	/**
	 * Invalid knowledge base format.
	 */
	public static final long IMP_INVALID_KB_FORMAT = 34;

	/**
	 * Invalid knowledge base mapping format.
	 */
	public static final long IMP_INVALID_MAP_FORMAT = 35;
	
	// FullTextSearch error ids
	
	public static final long FTS_INDEX_ERROR = 10;
	
	public static final long FTS_INDEX_DELETE_ERROR = 11;
	
	public static final long FTS_SEARCH_ERROR = 12;
	
	public static final long FTS_GET_DOCUMENT_ERROR = 13;
	
	public static final long FTS_INVALID_CONTENT_URL_ERROR = 15;
	
	public static final long FTS_CONTENT_PARSE_ERROR = 16;
	
	public static final long FTS_QUERY_ERROR = 17;
	
	public static final long FTS_INVALID_INDEX_CONFIGURATION_FILE_ERROR = 18;
	
	
	// KBstore error ids
	
	public static final long KB_SQL_QUERY_ERROR = 20;
	
	public static final long KB_SAVE_ERROR = 21;
	
}
