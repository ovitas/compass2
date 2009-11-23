/**
 * 
 */
package no.ovitas.compass2.service.impl;

import java.util.Properties;

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
	private Log log = LogFactory.getLog(getClass());

	
	
	/* (non-Javadoc)
	 * @see no.ovitas.compass2.service.ConfigurationManager#getConfigParameter(java.lang.String)
	 */
	public String getConfigParameter(String parameterName) {
		// TODO Auto-generated method stub
		return null;
	}

}
