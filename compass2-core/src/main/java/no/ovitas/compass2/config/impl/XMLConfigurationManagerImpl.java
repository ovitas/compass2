/**
 * 
 */
package no.ovitas.compass2.config.impl;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.config.Compass2ConfigurationHandler;
import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.config.settings.Compass2Configuration;
import no.ovitas.compass2.config.settings.FullTextSearchPlugin;
import no.ovitas.compass2.config.settings.ImportPlugin;
import no.ovitas.compass2.config.settings.KnowledgeBasePlugin;
import no.ovitas.compass2.config.settings.LanguageToolPlugin;
import no.ovitas.compass2.config.settings.ReferralPlugin;
import no.ovitas.compass2.config.settings.SearchFields;
import no.ovitas.compass2.config.settings.SearchOptions;
import no.ovitas.compass2.config.settings.SuggestionProviderPlugin;
import no.ovitas.compass2.exception.ConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author magyar0
 *
 */
public class XMLConfigurationManagerImpl implements ConfigurationManager {

	private Log log = LogFactory.getLog(getClass());
	protected Compass2ConfigurationHandler  handler = null;
	protected Compass2Configuration configuration = null;
	protected String configPath;
	protected String defaultKBImplementation;

	public XMLConfigurationManagerImpl(String configPath) throws ConfigurationException{
		super();
		this.configPath = configPath;
		
		// Check if config.path is define in system property
		String configProperty = System.getProperty(Constants.CONFIG_PATH);
		if (configProperty != null && !configProperty.isEmpty()){
			this.configPath = System.getProperty(Constants.CONFIG_PATH);
			if(log.isDebugEnabled()){
				log.debug("Config path from system property is: "+this.configPath);
			}
		} else {
			if(log.isDebugEnabled()){
				log.debug("Config path from pom is: "+this.configPath);
			}
		}
		
		if(log.isDebugEnabled()){
			log.debug("Calling initConfig()");
		}
		initConfig();
		if(log.isDebugEnabled()){
			log.debug("initConfig() called"); 
		}
		
	}
     
	public FullTextSearchPlugin getFullTextSearchPlugin(){
		return configuration.getFullTextSearchPlugin();
	}
	
	public SuggestionProviderPlugin getSuggestionProviderPlugin() {
		return configuration.getSuggestionProviderPlugin();
	}

    	
	/* (non-Javadoc)
	 * @see no.ovitas.compass2.service.ConfigurationManager#getConfigParameter(java.lang.String)
	 */
	public String getConfigParameter(String parameterName){
		throw new RuntimeException("deprecated for this kind of configuration!");
		
	}

	public void initConfig() throws ConfigurationException{
		log.info("Started Processing the config file...");
		
		if(configPath==null || configPath.equals("")){
			throw new ConfigurationException("No configuration file set!");
		}
		handler = Compass2ConfigurationHandler.getInstance();
		handler.loadConfig(configPath);
		configuration = handler.getConfig();
		
		if(log.isDebugEnabled()){
			log.debug(configuration.dumpOut(""));
		}
		
		log.info("Finished Processing the config file!");
	}
	
	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	public void setDefaultKBImplementationName(String name){
		defaultKBImplementation = name;
	}

	@Override
	public KnowledgeBasePlugin getKnowledgeBasePlugin() {
		return configuration.getKnowledgeBasePlugin();
	}

	@Override
	public LanguageToolPlugin getLanguageToolPlugin() {
		return configuration.getLanguageToolLanguageToolPlugin();
	}

	@Override
	public SearchOptions getSearchOptions() {
		return configuration.getSearchOptions();
	}

	@Override
	public SearchFields getSearchFields() {
		return configuration.getSearchFields();
	}

	@Override
	public ImportPlugin getImportPlugin() {
		return configuration.getImportPlugin();
	}

	@Override
	public ReferralPlugin getReferralPlugin() {
		return configuration.getReferralPlugin();
	}

}
