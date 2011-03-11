/**
 * 
 */
package no.ovitas.compass2.search.impl;

import no.ovitas.compass2.search.TopicFilter;
import no.ovitas.compass2.search.TopicResult;

/**
 * @author gyalai
 *
 */
public class TopicFilterImpl implements TopicFilter {

	private final long topicId;
	
	public TopicFilterImpl(long topicId) {
		this.topicId = topicId;	
	}
	
	@Override
	public boolean check(TopicResult topicResult) {
		if (topicResult.getId() == topicId)
			return true;
		
		return false;
	}

}
