package no.ovitas.compass2.fts.factory;

import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.config.settings.FullTextSearch;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.fts.FullTextSearchManager;
import no.ovitas.compass2.util.CompassUtil;
import no.ovitas.compass2.util.loaders.ImplementationLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author magyar
 * @version 1.0
 */
public class FTSFactory {

	private static FTSFactory instance = null;
	protected ConfigurationManager configurationManager;
	protected FullTextSearchManager manager = null;
	private Log log = LogFactory.getLog(getClass());

	private FTSFactory() {
		ApplicationContext context = CompassUtil.getApplicationContext();
		configurationManager = (ConfigurationManager) context
				.getBean("configurationManager");
	}

	public FullTextSearchManager getFTSImplementation()
			throws ConfigurationException {
		if (manager == null) {

			if (configurationManager.getFullTextSearchPlugin() == null) {
				log.info("FullTextSearchManager not available!");
				return null;
			}

			log.info("Started Initializing FullTextSearchManager...");
			FullTextSearch fullTextSearch = configurationManager
					.getFullTextSearchPlugin().getFullTextSearch();

			if (fullTextSearch != null) {

				manager = ImplementationLoader.loadInstance(
						fullTextSearch.getImplementation(),
						FullTextSearchManager.class);

				if (manager != null) {
					manager.setConfiguration(configurationManager);
					manager.init(fullTextSearch.getParams().getParams());
					log.info("Finished Initalizing FullTextSearchManager!");
				} else {
					log.info("FullTextSearchManager not available!");
				}
			} else {
				log.info("FullTextSearchManager not available!");
			}
		}

		return manager;
	}

	public static FTSFactory getInstance() {
		if (instance == null) {
			instance = new FTSFactory();
		}
		return instance;
	}

	public static void clear() {
		if (instance != null)
			instance.manager = null;	
		FTSContentHandlerFactory.clear();
		FTSIndexerFactory.clear();
		QueryBuilderFactory.clear();
	}

}