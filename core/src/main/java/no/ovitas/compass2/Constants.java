package no.ovitas.compass2;

import no.ovitas.compass2.model.Topic;



/**
 * Constant values used throughout the application.
 * 
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class Constants {
    //~ Static fields/initializers =============================================

    /**
     * The name of the ResourceBundle used in this application
     */
    public static final String BUNDLE_KEY = "ApplicationResources";

    /**
     * File separator from System properties
     */
    public static final String FILE_SEP = System.getProperty("file.separator");

    /**
     * User home from System properties
     */
    public static final String USER_HOME = System.getProperty("user.home") + FILE_SEP;

    /**
     * The name of the configuration hashmap stored in application scope.
     */
    public static final String CONFIG = "appConfig";

    /**
     * Session scope attribute that holds the locale set by the user. By setting this key
     * to the same one that Struts uses, we get synchronization in Struts w/o having
     * to do extra work or have two session-level variables.
     */
    public static final String PREFERRED_LOCALE_KEY = "org.apache.struts2.action.LOCALE";

    /**
     * The request scope attribute under which an editable user form is stored
     */
    public static final String USER_KEY = "userForm";

    /**
     * The request scope attribute that holds the user list
     */
    public static final String USER_LIST = "userList";

    /**
     * The request scope attribute for indicating a newly-registered user
     */
    public static final String REGISTERED = "registered";

    /**
     * The name of the Administrator role, as specified in web.xml
     */
    public static final String ADMIN_ROLE = "ROLE_ADMIN";

    /**
     * The name of the User role, as specified in web.xml
     */
    public static final String USER_ROLE = "ROLE_USER";

    /**
     * The name of the user's role list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String USER_ROLES = "userRoles";

    /**
     * The name of the available roles list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String AVAILABLE_ROLES = "availableRoles";

    /**
     * The name of the CSS Theme setting.
     */
    public static final String CSS_THEME = "csstheme";
    
    /**
     * The configuration file
     */
    public static final String CONFIG_FILE = "config.properties";
    public static final String LUCENE_SPELLCHECK_DIR="lucene.spellchecker.dir";
    public static final String LUCENE_SPELLCHECK_INDEX_DIR="lucene.spellchecker.index.dir";
    public static final String LUCENE_FTS_INDEX_DIR="lucene.fts.index.dir";
    public static final String LUCENE_SPELLCHECK_FIELD="lucene.spellchecker.field";
    public static final String LUCENE_SPELLCHECK_MAX_SUGGESTION_NUM="lucene.spellchecker.max.suggestion.num";
    public static final String SNWOBALL_LANGUAGE="English";
    public static final String LUCENE_FTS_MAX_HITS_PER_QUERY= "lucene.fts.max.hits.per.query";
    public static final String TITLE_INDEX="title";
    public static final String FILE_TYPE_INDEX="fileType";
    public static final String CONTENT_INDEX="content";
    public static final String LAST_MODIFIED_INDEX="lastModified";
    public static final String URI_INDEX="URI";
    public static final String ID_INDEX="ID";
    public static final String KNOWLEDGE_BASE_FILE="knowledge.base.file";
    public static final String KNOWLEDGE_BASE_LOAD_ON_STARTUP="knowladge.base.load.on.startup";
    public static final String MAX_TOPIC_NUMBER_TO_EXPAND="max.topic.number.to.expand";
    public static final String USE_RANDOM_WEIGHT="use.random.weight";
    public static final String DOCUMENT_REPOSITORY_PROPERTY = "document.repository";
    public static final String LUCENE_CONTEXT_INDEXER="no.ovitas.compass2.service.fts.ContentIndexerImplementation";
    public static final String SUBHEADING_INDEX = "subHeading";
    public static final String DATA_INDEX = "pageContent";
    
    
    /**
     * The compass2-config.xml configuration file
     */    
    public static final String INDEXDIRECTORY_PATH						= "indexdirectory-path";
    public static final String MAX_HITS_PER_QUERY						= "max-hits-per-query";
    public static final String DOCUMENT_REPOSITORY						= "document-repository";
    public static final String SNOWBALL_LANGUAGE						= "snowball-language";
    public static final String LUCENE_SPELLCHECKER_DIRECTORY			= "lucene-spellchecker-directory";
    public static final String LUCENE_SPELLCHECKER_INDEX_DIRECTORY		= "lucene-spellchecker-index-directory";
    public static final String LUCENE_SPELLCHECKER_FIELD				= "lucene-spellchecker-field";
    public static final String LUCENE_SPELLCHECKER_MAX_SUGGESTION_NUM	= "lucene-spellchecker-max-suggestion-num";
    public static final String FILE_PATH								= "file-path";
    public static final String LOAD_ON_STARTUP							= "load-on-startup";

	public static final double BOOST_PREFIX_VALUE = 0.5;
	public static final double BOOST_SAME_VALUE = 1;
	
	/**
	 * System property names
	 */
	public static final String CONFIG_PATH								= "config.path";
    
    
    
}
