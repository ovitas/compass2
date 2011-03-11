/**
 * 
 */
package no.ovitas.compass2.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author gyalai
 *
 */
public class MatchTopicResult {
	
	private final Map<String, Set<Long>> results;
	private int size = 0;
	
	public MatchTopicResult() {
		this.results = new HashMap<String, Set<Long>>(0);	
	}

	public Map<String, Set<Long>> getResults() {
		return results;
	}
	
	public Collection<String> getTerms() {
		return results.keySet();
	}
	
	public Set<Long> getTopicIDs(String term) {
		return results.get(term);
	}
	
	public void addTopicIDToTerm(String term, long topicId) {
		Set<Long> topicIds = results.get(term);
		if (topicIds == null) {
			topicIds = new HashSet<Long>();
			results.put(term, topicIds);
			topicIds.add(topicId);
			size++;
		} else {
			if (!topicIds.contains(topicId)) {
				topicIds.add(topicId);
				size++;
			}
		}
		
	}

	public int getSize() {
		return size;
	}	

}
