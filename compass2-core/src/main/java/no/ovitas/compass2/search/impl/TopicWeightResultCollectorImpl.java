/**
 * 
 */
package no.ovitas.compass2.search.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import no.ovitas.compass2.search.FullTextTopicCriteria;
import no.ovitas.compass2.search.TopicFilter;
import no.ovitas.compass2.search.TopicResult;
import no.ovitas.compass2.search.TopicTreeFilter;
import no.ovitas.compass2.search.TopicWeightResultCollector;

/**
 * @author gyalai
 * 
 */
public class TopicWeightResultCollectorImpl implements
		TopicWeightResultCollector, FullTextTopicCriteria {

	Map<String, Double> values = new HashMap<String, Double>();
	private Collection<TopicFilter> unSelectedTopics;
	private Collection<TopicTreeFilter> treeFilters;
	private Map<String, String> termWithStemPair;

	@Override
	public boolean collect(TopicResult topicResult) {

		if (treeFilters != null) {
			for (TopicFilter filter : treeFilters) {
				if (filter.check(topicResult))
					return false;
			}
		}

		if (unSelectedTopics != null) {
			for (TopicFilter filter : unSelectedTopics) {
				if (filter.check(topicResult))
					return true;
			}
		}		
		
		Collection<String> names = topicResult.getNames();

		double topicResultValue = topicResult.getWeight();

		Double value;
		for (String name : names) {
			value = values.get(name);

			if (value == null || topicResultValue > value) {
				values.put(name, topicResultValue);
			}
		}
		
		return true;
	}
	
	@Override
	public Map<String, Double> getTopicsWithWeight() {
		return Collections.unmodifiableMap(values);
	}

	@Override
	public void addFilters(Collection<TopicFilter> unSelectedTopics) {
				this.unSelectedTopics = unSelectedTopics;				
	}

	public void addTreeFilter(Collection<TopicTreeFilter> treeFilters) {
		this.treeFilters = treeFilters;		
	}

}
