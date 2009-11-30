/**
 * 
 */
package no.ovitas.compass2.service.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.service.CompassManager;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.impl.CompassManagerImpl;
import no.ovitas.compass2.util.CompassUtil;

/**
 * @author magyar
 *
 */
public class CompassManagerFactory {

	private CompassManager manager = null;
	protected ConfigurationManager configurationManager;
	private Log log = LogFactory.getLog(getClass());
	
	
	private static CompassManagerFactory instance = null;
	
	
	protected CompassManagerFactory(){
		ApplicationContext context = CompassUtil.getApplicationContext();
		configurationManager = (ConfigurationManager)context.getBean("configurationManager");
		
	}
	
	public static CompassManagerFactory getInstance(){
		if(instance==null){
			instance = new CompassManagerFactory(); 
		}
		return instance;
	}
	
	public CompassManager getCompassManager(){
		if(manager==null){
			manager = new CompassManagerImpl();
			try {
				manager.setConfigurationManager(configurationManager);
				manager.init();
			} catch (ConfigurationException e) {
				log.fatal("Error occured while initializing CompassManager: "+e.getMessage(),e);
				manager = null;
			}
		}
		return manager;
	}
}
