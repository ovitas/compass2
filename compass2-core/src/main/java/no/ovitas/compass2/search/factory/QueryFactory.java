/**
 * 
 */
package no.ovitas.compass2.search.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.config.settings.SearchField;
import no.ovitas.compass2.config.settings.SearchFieldString;
import no.ovitas.compass2.config.settings.SearchOptions;
import no.ovitas.compass2.search.FullTextQuery;
import no.ovitas.compass2.search.TopicQuery;
import no.ovitas.compass2.search.impl.FullTextQueryImpl;
import no.ovitas.compass2.search.impl.TopicQueryImpl;
import no.ovitas.compass2.util.CompassUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author gyalai
 *
 */
public class QueryFactory {
	
	private static QueryFactory instance;
	private ConfigurationManager configurationManager;
	private SearchOptions searchOptions;
	private Map<String, SearchFieldString> defaultFieldCriteriaConfig;
	private Log log = LogFactory.getLog(getClass());
	
	private QueryFactory() {
		ApplicationContext context = CompassUtil.getApplicationContext();
		configurationManager = (ConfigurationManager) context
				.getBean("configurationManager");
		
		searchOptions = configurationManager.getSearchOptions();
		
		readDefaultSearchFields();
	}
	
	private void readDefaultSearchFields() {
		if (defaultFieldCriteriaConfig == null) {
			List<SearchField> fields2 = configurationManager.getSearchFields()
					.getFields();

			defaultFieldCriteriaConfig = new TreeMap<String, SearchFieldString>();

			for (SearchField searchField : fields2) {
				if (searchField.isDefaultIndex()) {
					if (searchField instanceof SearchFieldString) {
						defaultFieldCriteriaConfig.put(searchField.getIndexField(), (SearchFieldString) searchField);
					} else {
						log .error("Configuration setting error!"
								+ "Default search field must be in search-field-string tag!");
					}

				}
			}
		}		
	}

	public static QueryFactory getInstance() {
		
		if (instance == null) {
			instance = new QueryFactory();
		}
		
		return instance;
	}
	
	public FullTextQuery createFullTextQuery() {
		FullTextQueryImpl query = new FullTextQueryImpl();
		
		query.setFuzzySearch(searchOptions.isFuzzyMatch());
		query.setMaxNumberOfHits(searchOptions.getMaxNumberOfHits());
		query.setResultThreshold(searchOptions.getResultThreshold());
		query.setDefaultFieldCriteriaConfig(defaultFieldCriteriaConfig);
		
		return query;
	}
	
	public TopicQuery createTopicQuery() {
		TopicQueryImpl query = new TopicQueryImpl();
		
		query.setHopCount(searchOptions.getHopCount());
		query.setMaxTopicNumberToExpand(searchOptions.getMaxTopicNumToExpand());
		query.setPrefixMatch(searchOptions.isPrefixMatch());
		query.setThresholdWeight(searchOptions.getExpansionThreshold());
		query.setTreeSearch(false);
		query.setKnowledgeBase(configurationManager.getKnowledgeBasePlugin().getDefaultKnowledgeBase());
		query.setSearchOptions(configurationManager.getSearchOptions());
		
		return query;
	}

}
