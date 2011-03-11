package no.ovitas.compass2.search;

public interface TopicFilter {
	
	/**
	 * Check topic result is filtered.
	 * 
	 * @param topicResult
	 * @return
	 */
	boolean check(TopicResult topicResult);
}
