package no.ovitas.compass2.kb.factory;

import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.config.settings.KnowledgeBaseSettingPlugin;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.kb.KnowledgeBaseManager;
import no.ovitas.compass2.kb.TopicNameIndexer;
import no.ovitas.compass2.util.CompassUtil;
import no.ovitas.compass2.util.loaders.ImplementationLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author magyar
 * @version 1.0
 */
public class KBFactory {

	private static KBFactory instance = null;
	protected ConfigurationManager configurationManager;
	protected String kbFile;
	protected boolean loadOnStartup;
	private Log log = LogFactory.getLog(getClass());

	protected KnowledgeBaseManager manager = null;;

	public KBFactory() {
		ApplicationContext context = CompassUtil.getApplicationContext();
		configurationManager = (ConfigurationManager) context
				.getBean("configurationManager");

	}

	public KnowledgeBaseManager getKBImplementation()
			throws ConfigurationException {
		if (manager == null) {

			log.info("Started Initializing KnowledgeBaseManager...");

			if (configurationManager.getKnowledgeBasePlugin() == null) {
				log.info("KnowledgeBaseManager not available!");
				return null;
			}
			KnowledgeBaseSettingPlugin defaultKnowledgeBase = configurationManager
					.getKnowledgeBasePlugin().getDefaultKnowledgeBase();

			log.info("Load default knowledge base manager: "
					+ defaultKnowledgeBase.getId());

			manager = ImplementationLoader.loadInstance(
					defaultKnowledgeBase.getImplementation(),
					KnowledgeBaseManager.class);

			if (manager != null) {
				manager.setConfiguration(configurationManager);
				manager.init(defaultKnowledgeBase.getParams().getParams());
				TopicNameIndexer topicNameIndexerImplementation = TopicNameIndexerFactory
						.getInstance().getTopicNameIndexerImplementation();
				manager.setTopicNameIndexer(topicNameIndexerImplementation);
				log.info("Finished Initalizing KnowledgeBaseManager!");
			} else {
				log.info("KnowledgeBaseManager not available!");
			}
		}

		return manager;

	}

	public static KBFactory getInstance() {
		if (instance == null) {
			instance = new KBFactory();
		}
		return instance;
	}

	public static void clear() {
		if (instance != null)
			instance.manager = null;
		TopicNameIndexerFactory.clear();
	}

}