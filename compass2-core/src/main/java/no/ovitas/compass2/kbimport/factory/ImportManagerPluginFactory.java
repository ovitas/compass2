package no.ovitas.compass2.kbimport.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ovitas.compass2.config.ConfigConstants;
import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.config.settings.Import;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.kbimport.ImportManagerPlugin;
import no.ovitas.compass2.util.CompassUtil;
import no.ovitas.compass2.util.loaders.ImplementationLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

/**
 * Factory implementation for the <code>ImportManager</code> plugins.
 * 
 * @author Csaba Daniel
 */
public class ImportManagerPluginFactory {

	private static ConfigurationManager cfg;
	private static ImportManagerPluginFactory instance = new ImportManagerPluginFactory();
	private Map<String, ImportManagerPlugin> impls = new HashMap<String, ImportManagerPlugin>();
	private Log log = LogFactory.getLog(getClass());

	private ImportManagerPluginFactory() {
		ApplicationContext ctx = CompassUtil.getApplicationContext();
		cfg = (ConfigurationManager) ctx
				.getBean(ConfigConstants.BEAN_CONFIGURATION_MANAGER);
		
	}

	/**
	 * Get the singleton instance of the <code>ImportManagerFactory</code>.
	 * 
	 * @return the singleton instance of the class
	 */
	public static ImportManagerPluginFactory getInstance() {
		return instance;
	}

	/**
	 * Obtain an <code>ImportManager</code> implementation associated with the
	 * specified knowledge base definition type.
	 * 
	 * @param type
	 *            the type name of the knowledge base definition
	 * @return the <code>ImportManager</code> implementation specified in the
	 *         configuration that is able to import form the specified knowledge
	 *         base definition type
	 * @throws ConfigurationException
	 *             in case if an error occurs during the configuration of the
	 *             import manager
	 */
	public ImportManagerPlugin getImportManager(String type)
			throws ConfigurationException {

		ImportManagerPlugin manager = impls.get(type);

		
		
		if (manager == null) {
			log.info("Started Initializing ImportManagerPlugin " + type + "...");
			if (cfg.getImportPlugin() != null) {
				
				List<Import> imports = cfg.getImportPlugin().getImports();
				
				for (Import importPlugin : imports) {
					
					if (type.equals(importPlugin.getType())) {
						manager = ImplementationLoader.loadInstance(importPlugin.getImplementation(), ImportManagerPlugin.class);
						
						if (manager != null) {
							manager.setConfiguration(cfg);
							manager.init(importPlugin.getParams().getParams());
							impls.put(type, manager);
							log.info("Finished Initializing ImportManagerPlugin " + type + "!");
							break;
						}
					}
					
				}
				
				if (manager == null) {
					log.info("ImportManagerPlugin " + type + " not available!");
				}
				
			} else {
				log.info("ImportManagerPlugin " + type + " not available!");
			}
		}
			

		return manager;
	}




}
