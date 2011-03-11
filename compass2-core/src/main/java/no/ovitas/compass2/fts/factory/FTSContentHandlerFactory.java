package no.ovitas.compass2.fts.factory;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.config.settings.ContentHandler;
import no.ovitas.compass2.config.settings.FullTextSearchPlugin;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.fts.FTSContentHandler;
import no.ovitas.compass2.util.CompassUtil;
import no.ovitas.compass2.util.loaders.ImplementationLoader;

/**
 * @class FTSContentHandlerFactory
 * @project compass2-fts-lucene
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version 1.0
 * @date 2010.07.28.
 * 
 */
public class FTSContentHandlerFactory {

	private static FTSContentHandlerFactory instance = null;
	protected ConfigurationManager configurationManager;
	private FTSContentHandler manager;
	private Log log = LogFactory.getLog(getClass());

	protected FTSContentHandlerFactory() {

		ApplicationContext context = CompassUtil.getApplicationContext();
		configurationManager = (ConfigurationManager) context
				.getBean("configurationManager");
	}

	public static FTSContentHandlerFactory getInstance() {
		if (instance == null) {
			instance = new FTSContentHandlerFactory();
		}
		return instance;
	}

	/**
	 * This method returns with a configured FTSContentHandler,
	 * 
	 * @return FTSContentHandler
	 * @throws ConfigurationException
	 */
	public FTSContentHandler getHandlerImplementation(String type)
			throws ConfigurationException {
		if (manager == null) {
			FullTextSearchPlugin fts = configurationManager
					.getFullTextSearchPlugin();

			ContentHandler contentHandler = fts.getContentHandlerPlugin()
					.getSpecifiedContentHandler(type);
			if (contentHandler != null) {
				log.info("Started Initializing ContentHandler...");

				manager = ImplementationLoader.loadInstance(
						contentHandler.getImplementation(),
						FTSContentHandler.class);

				if (manager != null) {
					manager.setConfiguration(configurationManager);
					manager.init(contentHandler.getParams().getParams());
					manager.setFields(contentHandler.getFields().getFields());
					log.info("Finished Initalizing ContentHandler!");
				} else {
					log.debug("ContentHandler not available!");
					throw new ConfigurationException(
							"ContentHandler not availeable");
				}
			} else {
				log.debug("ContentHandler with specified type doesn't available!");
				throw new ConfigurationException(
						"ContentHandler not availeable");
			}

		}
		return manager;
	}

	public Set<String> listAllContentHandlerType()
			throws ConfigurationException {
		Set<String> result = new HashSet<String>();
		try {

			Set<ContentHandler> contentHanders = configurationManager
					.getFullTextSearchPlugin().getContentHandlerPlugin()
					.getContentHanders();

			for (ContentHandler contentHandler : contentHanders) {
				result.add(contentHandler.getType());
			}
		} catch (NullPointerException e) {
			throw new ConfigurationException("ContentHandler not available!", e);
		}

		return result;
	}

	public static void clear() {
		if (instance != null)
			instance.manager = null;
	}

}
