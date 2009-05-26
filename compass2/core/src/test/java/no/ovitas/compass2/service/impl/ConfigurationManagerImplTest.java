/**
 * 
 */
package no.ovitas.compass2.service.impl;

import no.ovitas.compass2.service.ConfigurationManager;

/**
 * @author magyar
 *
 */
public class ConfigurationManagerImplTest extends BaseManagerTestCase {
	
	protected ConfigurationManager configurationManager;

	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}
	
	
	
	public void testConfigurationGetter(){
		String prop1 = configurationManager.getConfigParameter("no.ovitas.compass2.service.FTSImplementation");
		assertNotNull(prop1);
		String prop2 = configurationManager.getConfigParameter("error");
		assertNull(prop2);
		
	}

}
