/**
 * 
 */
package no.ovitas.compass2.kb.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.config.settings.KnowledgeBaseSettingPlugin;
import no.ovitas.compass2.config.settings.TopicNameIndexerSetting;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.kb.TopicNameIndexer;
import no.ovitas.compass2.util.CompassUtil;
import no.ovitas.compass2.util.loaders.ImplementationLoader;

/**
 * @author gyalai
 * 
 */
public class TopicNameIndexerFactory {

	private static TopicNameIndexerFactory instance = null;
	protected ConfigurationManager configurationManager;
	protected TopicNameIndexer manager = null;
	private Log log = LogFactory.getLog(getClass());

	private TopicNameIndexerFactory() {
		ApplicationContext context = CompassUtil.getApplicationContext();
		configurationManager = (ConfigurationManager) context
				.getBean("configurationManager");
	}

	public TopicNameIndexer getTopicNameIndexerImplementation()
			throws ConfigurationException {
		if (manager == null) {

			log.info("Started Initializing TopicNameIndexer...");

			if (configurationManager.getKnowledgeBasePlugin() == null) {
				log.info("TopicNameIndexer not available!");
				return null;
			}

			KnowledgeBaseSettingPlugin defaultKnowledgeBase = configurationManager
					.getKnowledgeBasePlugin().getDefaultKnowledgeBase();

			log.info("Load default knowledge base manager: "
					+ defaultKnowledgeBase.getId());

			TopicNameIndexerSetting topicNameIndexerSetting = defaultKnowledgeBase
					.getTopicNameIndexerSetting();

			if (topicNameIndexerSetting == null) {
				log.info("TopicNameIndexer not available!");
				return null;
			}

			manager = ImplementationLoader.loadInstance(
					topicNameIndexerSetting.getImplementation(),
					TopicNameIndexer.class);

			if (manager != null) {
				manager.setConfiguration(configurationManager);
				manager.init(topicNameIndexerSetting.getParams().getParams());
				log.info("Finished Initalizing TopicNameIndexer!");
			} else {
				log.info("TopicNameIndexer not available!");
			}

		}

		return manager;
	}

	public static TopicNameIndexerFactory getInstance() {
		if (instance == null) {
			instance = new TopicNameIndexerFactory();
		}
		return instance;
	}

	public static void clear() {
		if (instance != null)
			instance.manager = null;
	}
}
