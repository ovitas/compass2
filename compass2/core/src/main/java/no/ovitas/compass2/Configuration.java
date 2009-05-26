package no.ovitas.compass2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class Configuration.
 */
public class Configuration {

	/** The property file. */
	private Properties properties;
    
    /** Logging into file. */
    private Log log = LogFactory.getLog(getClass());
    
    // Default Compass settings
    
    /** networkedplanet.tmcore.dbconnect */
    public String NP_CONNECTION;
    
    /** compass.topicmap */
    public String C_TOPICMAP;
    
    /** compass.max.hits.per.query */
    public String C_MAX_HITS_PER_QUERY;
    
    /** compass.treshold */
    public String C_TRESHOLD;
    
    /** compass.weight.psi */
    public String C_WEIGHT_PSI;
    
    /** compass.max.hits */
    public String C_MAX_HITS;
    
    // Default Lucene settings
    
    /** lucene.index.directory */
    public String L_INDEX_DIRECTORY;
    
    /** lucene.index.default.text.field */
    public String L_INDEX_DEFAULT_TEXT_FIELD;
    
    /** lucene.default.uri.field */
    public String L_DEFAULT_URI_FIELD;
    
	/**
	 * Load properties.
	 */
	public Configuration() {
		properties = new Properties() ;
		URL url =  ClassLoader.getSystemResource(Constants.CONFIG_FILE);
		
		try {
			
			properties.load(new FileInputStream(new File(url.getFile())));
			NP_CONNECTION = properties.getProperty("networkedplanet.tmcore.dbconnect");
			C_TOPICMAP = properties.getProperty("compass.topicmap");
			L_INDEX_DIRECTORY = properties.getProperty("lucene.index.directory");
			C_MAX_HITS_PER_QUERY = properties.getProperty("compass.max.hits.per.query");
			C_TRESHOLD = properties.getProperty("compass.treshold");
			C_WEIGHT_PSI = properties.getProperty("compass.weight.psi");
			L_INDEX_DEFAULT_TEXT_FIELD = properties.getProperty("lucene.index.default.text.field");
			L_DEFAULT_URI_FIELD = properties.getProperty("lucene.default.uri.field");
			C_MAX_HITS = properties.getProperty("compass.max.hits");
			
		} catch (FileNotFoundException fnfe) {
			log.error("Property file not found: " + fnfe.getMessage());
		} catch (IOException ioe) {
			log.error("Exception: " + ioe.getMessage());
		}
	}
	
	/**
	 * Get the specific Lucene directory.
	 * 
	 * @param index the index
	 * 
	 * @return specific Lucene directory
	 */
	public String getLuceneDirectory(String index) {
		return properties.getProperty("lucene."+index+".directory");
	}
	
	/**
	 * Gets the lucene search field.
	 * 
	 * @param indexShort the index short
	 * 
	 * @return the lucene search field
	 */
	public String getLuceneSearchField(String indexShort) {
		return properties.getProperty("lucene." + indexShort + ".search.field");
	}
	
	/**
	 * Gets the lucene uri field.
	 * 
	 * @param indexShort the index short
	 * 
	 * @return the lucene uri field
	 */
	public String getLuceneUriField(String indexShort) {
		return properties.getProperty("lucene." + indexShort + ".uri.field");
	}
}
