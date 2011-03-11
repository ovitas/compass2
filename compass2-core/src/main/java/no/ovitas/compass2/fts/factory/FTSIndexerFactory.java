/**
 * 
 */
package no.ovitas.compass2.fts.factory;

import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.config.settings.Indexer;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.fts.FTSIndexer;
import no.ovitas.compass2.util.CompassUtil;
import no.ovitas.compass2.util.loaders.ImplementationLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

/**
 * @class FTSIndexerFactory
 * @project compass2-fts-lucene
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version 1.0
 * @date 2010.07.28.
 * 
 */
public class FTSIndexerFactory {

	private static FTSIndexerFactory instance;

	protected ConfigurationManager configurationManager;
	private Log log = LogFactory.getLog(getClass());

	protected FTSIndexerFactory() {
		ApplicationContext context = CompassUtil.getApplicationContext();
		configurationManager = (ConfigurationManager) context
				.getBean("configurationManager");
	}

	public static FTSIndexerFactory getInstance() {
		if (instance == null) {
			instance = new FTSIndexerFactory();
		}
		return instance;
	}

	/**
	 * This method returns with a configured FTSIndexer,
	 * 
	 * @return FTSIndexer
	 * @throws ConfigurationException
	 */
	public FTSIndexer getIndexerImplementation() throws ConfigurationException {

		Indexer indexer = configurationManager.getFullTextSearchPlugin()
				.getIndexer();
		FTSIndexer manager;

		if (indexer != null) {


			manager = ImplementationLoader.loadInstance(
					indexer.getImplementation(), FTSIndexer.class);

			if (manager != null) {
				manager.setConfiguration(configurationManager);
				manager.init(indexer.getParams().getParams());
			} else {
				log.debug("Indexer not available!");
				throw new ConfigurationException("Indexer not availeable");
			}
		} else {
			log.debug("Indexer not available!");
			throw new ConfigurationException("Indexer not availeable");
		}

		return manager;
	}

	public static void clear() {
				
	}

}
