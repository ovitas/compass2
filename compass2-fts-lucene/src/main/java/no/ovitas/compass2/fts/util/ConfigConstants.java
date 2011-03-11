/**
 * 
 */
package no.ovitas.compass2.fts.util;

/**
 * @author gyalai
 *
 */
public class ConfigConstants {

	public static final String TAG_ROOT = "indexer-config";
	public static final String TAG_REPLACE_IN_URL = TAG_ROOT + "/" + "replace-in-url";
	public static final String TAG_REPLACE = TAG_REPLACE_IN_URL + "/" + "replace";

	public static final String TAG_ADD = TAG_ROOT + "/" + "add";
	public static final String TAG_UPDATE = TAG_ROOT + "/" + "update";
	public static final String TAG_DELETE = TAG_ROOT + "/" + "delete";
	
	public static final String TAG_DOCUMENT = "*/" + "document";
	
	public static final String ATTR_BASE_DIR = "base-dir";
	public static final String ATTR_VALUE = "value";
	public static final String ATTR_WITH = "with";
	public static final String ATTR_SCOPE = "scope";
	public static final String ATTR_URL = "url";
	public static final String ATTR_DEPTH = "depth";
	public static final String ATTR_RE_INDEX = "re-index";
}
