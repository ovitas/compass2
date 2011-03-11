package no.ovitas.compass2.search;

import java.util.Collection;

/**
 * The class that implements this interface represents a result of a topic
 * expansion result.
 * 
 * @author Csaba Daniel
 */
public interface TopicResult {

	/**
	 * Get the id of the topic.
	 * 
	 * @return topic id
	 */
	public long getId();

	/**
	 * Get the id of the base topic the current topic was expanded from.
	 * 
	 * @return base topic id
	 */
	public long getBaseTopicId();

	/**
	 * Get the names of the topic
	 * 
	 * @return topic names
	 */
	public Collection<String> getNames();

	/**
	 * Get the weight assigned to this topic via the relations from one of the
	 * base topics.
	 * 
	 * @return topic weight
	 */
	public double getWeight();
	
	/**
	 * Add topic result value to TopicWeight result.
	 * 
	 * @param topicWeightResult result collector
	 */
	public void collectTopicWeight(TopicWeightResultCollector topicWeightResult);
}
