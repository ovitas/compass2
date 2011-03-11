/**
 * 
 */
package no.ovitas.compass2.kbimport.factory;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import no.ovitas.compass2.config.ConfigConstants;
import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.kbimport.ImportManager;
import no.ovitas.compass2.kbimport.impl.ImportManagerImpl;
import no.ovitas.compass2.util.CompassUtil;

/**
 * @class ImportManagerFactory
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.23.
 * 
 */
public class ImportManagerFactory {

	private static ConfigurationManager configurationManager;
	private static ImportManagerFactory instance;

	private Log log = LogFactory.getLog(getClass());
	private ImportManager importManager;

	private ImportManagerFactory() {
		ApplicationContext ctx = CompassUtil.getApplicationContext();
		configurationManager = (ConfigurationManager) ctx
				.getBean(ConfigConstants.BEAN_CONFIGURATION_MANAGER);
	}

	public static ImportManagerFactory getInstance() {
		if (instance == null) {
			instance = new ImportManagerFactory();
		}

		return instance;
	}

	public ImportManager getImportManager() throws ConfigurationException {
		if (importManager == null) {

			log.info("Import Manager initialization started...");

			importManager = new ImportManagerImpl();

			importManager.setConfiguration(configurationManager);
			importManager.init(new Properties());

			log.info("Import manager initialization finished!");
		}

		return importManager;
	}

	public static void clear() {
		if (instance != null)
			instance.importManager = null;
	}

}
