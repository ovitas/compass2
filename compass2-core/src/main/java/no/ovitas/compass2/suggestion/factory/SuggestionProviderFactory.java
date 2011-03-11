/**
 * 
 */
package no.ovitas.compass2.suggestion.factory;

import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.config.settings.SuggestionProvider;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.suggestion.SuggestionProviderManager;
import no.ovitas.compass2.util.CompassUtil;
import no.ovitas.compass2.util.loaders.ImplementationLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

/**
 * @class SuggestionProviderFactory
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.09.21.
 * 
 */
public class SuggestionProviderFactory {

	private static SuggestionProviderFactory instance = null;
	private ConfigurationManager configurationManager;
	private SuggestionProviderManager manager = null;
	private Log log = LogFactory.getLog(getClass());

	private SuggestionProviderFactory() {
		ApplicationContext context = CompassUtil.getApplicationContext();
		configurationManager = (ConfigurationManager) context
				.getBean("configurationManager");
	}

	public static SuggestionProviderFactory getInstance() {
		if (instance == null) {
			instance = new SuggestionProviderFactory();
		}

		return instance;
	}

	public SuggestionProviderManager getSuggestionProviderManager()
			throws ConfigurationException {
		if (manager == null) {
			if (configurationManager.getSuggestionProviderPlugin() == null) {
				log.info("SuggestionProviderManager not available!");
				return null;
			}

			log.info("Started Initializing SuggestionProviderManager...");

			SuggestionProvider suggestionProvider = configurationManager
					.getSuggestionProviderPlugin().getSuggestionProvider();

			if (suggestionProvider != null) {

				manager = ImplementationLoader.loadInstance(
						suggestionProvider.getImplementation(),
						SuggestionProviderManager.class);

				if (manager != null) {
					manager.setConfiguration(configurationManager);
					manager.init(suggestionProvider.getParams().getParams());
					log.info("Finished Initalizing SuggestionProviderManager!");
				} else {
					log.info("SuggestionProviderManager not available!");
				}

			} else {
				log.info("SuggestionProviderManager not available!");
			}

		}

		return manager;
	}

	public static void clear() {
		if (instance != null)
			instance.manager = null;
	}

}
