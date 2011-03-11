/**
 * 
 */
package no.ovitas.compass2.config;


import junit.framework.TestCase;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.util.CompassUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;


/**
 * @author gyalai
 *
 */
public class ConfigurationManagerTest extends TestCase{

	private ConfigurationManager configurationManager;
	

	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ApplicationContext context = CompassUtil.getApplicationContext();
		configurationManager = (ConfigurationManager)context.getBean("configurationManager");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	
	
	@Test
	public void testInitConfig() throws ConfigurationException {
	
			configurationManager.initConfig();
		
	}

}
