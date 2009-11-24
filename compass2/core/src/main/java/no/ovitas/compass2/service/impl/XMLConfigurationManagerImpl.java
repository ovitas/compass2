/**
 * 
 */
package no.ovitas.compass2.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.ovitas.compass2.config.Compass2Configuration;
import no.ovitas.compass2.config.Compass2ConfigurationHandler;
import no.ovitas.compass2.config.Expansion;
import no.ovitas.compass2.config.FullTextSearch;
import no.ovitas.compass2.config.KnowledgeBase;
import no.ovitas.compass2.config.LanguageToolsImplementation;
import no.ovitas.compass2.config.Result;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.service.ConfigurationManager;

/**
 * @author magyar0
 *
 */
public class XMLConfigurationManagerImpl implements ConfigurationManager {

	private Log log = LogFactory.getLog(getClass());
	protected Compass2ConfigurationHandler  handler = null;
	protected Compass2Configuration configuration = null;
	protected String configPath;

	public XMLConfigurationManagerImpl(String configPath) throws ConfigurationException{
		super();
		this.configPath = configPath;
		if(log.isDebugEnabled()){
			log.debug("Config path is: "+configPath);
			log.debug("Calling initConfig()");
		}
		initConfig();
		if(log.isDebugEnabled()){
			log.debug("initConfig() called"); 
		}
		
	}
     
	public FullTextSearch getFullTextSearch(){
		return configuration.getFullTextSearch();
	}

	public KnowledgeBase getKnowledgeBase(String name){
		return configuration.getKnowledgeBases().getKnowladgeBase(name);
	}
	
	public LanguageToolsImplementation getLanguageToolsImplementation(){
		return configuration.getLanguageTools().getLanguageToolsImplementation();
	}
	
    public Result getResult(){
    	return configuration.getResult();
    }
    	
	/* (non-Javadoc)
	 * @see no.ovitas.compass2.service.ConfigurationManager#getConfigParameter(java.lang.String)
	 */
	public String getConfigParameter(String parameterName){
		throw new RuntimeException("deprecated for this kind of configuration!");
		
	}

	public void initConfig() throws ConfigurationException{
		if(configPath==null || configPath.equals("")){
			throw new ConfigurationException("No configuration file set!");
		}
		handler = Compass2ConfigurationHandler.getInstance();
		handler.loadConfig(configPath);
		configuration = handler.getConfig();
	}
	
	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

}
