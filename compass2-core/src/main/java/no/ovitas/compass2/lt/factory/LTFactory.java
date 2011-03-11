package no.ovitas.compass2.lt.factory;

import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.config.settings.LanguageTool;
import no.ovitas.compass2.config.settings.LanguageToolPlugin;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.lt.LanguageToolsManager;
import no.ovitas.compass2.util.CompassUtil;
import no.ovitas.compass2.util.loaders.ImplementationLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author magyar
 * @version 1.0
 */
public class LTFactory {

	private static LTFactory instance = null;
	protected ConfigurationManager configurationManager;
	protected LanguageToolsManager manager = null;;
	private Log log = LogFactory.getLog(getClass());

	public LTFactory() {

		ApplicationContext context = CompassUtil.getApplicationContext();
		configurationManager = (ConfigurationManager) context
				.getBean("configurationManager");
	}

	public LanguageToolsManager getLTImplementation()
			throws ConfigurationException {
		if (manager == null) {

			log.info("Started Initializing LanguageToolManager...");

			LanguageToolPlugin languageToolPlugin = configurationManager
					.getLanguageToolPlugin();
			if (languageToolPlugin != null) {

				LanguageTool languageTool = languageToolPlugin
						.getLanguageTool();
				manager = ImplementationLoader.loadInstance(
						languageTool.getImplementation(),
						LanguageToolsManager.class);

				if (manager != null) {
					manager.setConfiguration(configurationManager);
					manager.init(languageTool.getParams().getParams());
					log.info("Finished Initializing LanguageToolManager!");
				} else {
					log.info("LanguageToolManager not available!");
				}
			} else {
				log.info("LanguageToolManager not available!");
			}

		}
		return manager;
	}

	public static LTFactory getInstance() {
		if (instance == null) {
			instance = new LTFactory();
		}
		return instance;
	}

	public static void clear() {
		if (instance != null)
			instance.manager = null;
	}

}