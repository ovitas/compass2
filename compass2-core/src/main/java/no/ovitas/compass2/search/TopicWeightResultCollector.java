/**
 * 
 */
package no.ovitas.compass2.search;

import java.util.Collection;

/**
 * @author gyalai
 *
 */
public interface TopicWeightResultCollector {

	/**
	 * Collect topicResult weight.
	 * 
	 * @param topicResult
	 * @return
	 */
	boolean collect(TopicResult topicResult);
	
	/**
	 * Add topic filters.
	 * 
	 * @param unSelectedTopics
	 */
	void addFilters(Collection<TopicFilter> unSelectedTopics);

}
