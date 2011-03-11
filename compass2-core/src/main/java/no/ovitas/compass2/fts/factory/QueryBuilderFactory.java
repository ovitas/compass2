/**
 * 
 */
package no.ovitas.compass2.fts.factory;

import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.config.settings.QBuilder;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.fts.QueryBuilder;
import no.ovitas.compass2.util.CompassUtil;
import no.ovitas.compass2.util.loaders.ImplementationLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

/**
 * @class QueryBuilderFactory
 * @project compass2-fts-lucene
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.09.08.
 * 
 */
public class QueryBuilderFactory {

	private static QueryBuilderFactory instance;
	private ConfigurationManager configurationManager;
	private Log log = LogFactory.getLog(getClass());

	public QueryBuilderFactory() {
		ApplicationContext context = CompassUtil.getApplicationContext();
		configurationManager = (ConfigurationManager) context
				.getBean("configurationManager");
	}

	public static QueryBuilderFactory getInstance() {
		if (instance == null) {
			instance = new QueryBuilderFactory();
		}
		return instance;
	}

	public QueryBuilder getQueryBuilder() throws ConfigurationException {

		QueryBuilder queryBuilder = null;
		QBuilder qBuilder = configurationManager.getFullTextSearchPlugin()
				.getQueryBuilder();

		if (qBuilder != null) {
			queryBuilder = ImplementationLoader.loadInstance(qBuilder.getImplementation(), QueryBuilder.class);
			
			if (queryBuilder != null) {
				queryBuilder.setConfiguration(configurationManager);
				queryBuilder.init(qBuilder.getParams().getParams());				
			} else {
				log.debug("QueryBuilder not available!");
				throw new ConfigurationException("QueryBuilder not availeable");			
			}			
		} else {
			log.debug("QueryBuilder not available!");
			throw new ConfigurationException("QueryBuilder not availeable");
		}

		return queryBuilder;
	}

	public static void clear() {
			
	}
}
