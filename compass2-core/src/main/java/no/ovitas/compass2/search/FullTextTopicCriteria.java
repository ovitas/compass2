/**
 * 
 */
package no.ovitas.compass2.search;

import java.util.Map;

/**
 * @author gyalai
 *
 */
public interface FullTextTopicCriteria {

	/**
	 * Get topics names with weights.
	 * 
	 * @return
	 */
	public Map<String, Double> getTopicsWithWeight();
}
