/**
 * 
 */
package no.ovitas.compass2.service.impl;

import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.ovitas.compass2.config.Compass2Configuration;
import no.ovitas.compass2.config.Compass2ConfigurationHandler;
import no.ovitas.compass2.service.ConfigurationManager;

/**
 * @author magyar
 *
 */
public class XMLConfigurationManagerImpl implements ConfigurationManager {

	protected Compass2ConfigurationHandler  handler = null;
	protected Compass2Configuration configuration = null;
	private ResourceBundle rb = null;
	private Log log = LogFactory.getLog(getClass());

	// Constructor, initialize handler
	
	public XMLConfigurationManagerImpl() {
		
		// Get configuration file location
		rb = ResourceBundle.getBundle("compass2");
		String configPath = rb.getString("config.file.location");
		
		handler = new Compass2ConfigurationHandler();
		handler.initialize(configPath);
		configuration = handler.getConfig();
	}
	
	/* (non-Javadoc)
	 * @see no.ovitas.compass2.service.ConfigurationManager#getConfigParameter(java.lang.String)
	 */
	public String getConfigParameter(String parameterName) {
		// TODO Auto-generated method stub
		return null;
	}

}
