/**
 * 
 */
package no.ovitas.compass2.search;

import java.util.Map;

/**
 * @author gyalai
 *
 */
public interface TopicQueryResult {

	/**
	 * Collect topic weights with collector.
	 * 
	 * @param collector
	 */
	public void collectTermTopicWeights(TermTopicWeightResultCollector collector);
	
	/**
	 * Get term with stem.
	 * 
	 * @return
	 */
	public Map<String, String> getTermWithStemPair();
	
}
