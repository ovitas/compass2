/**
 * 
 */
package no.ovitas.compass2.search.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import no.ovitas.compass2.search.TermTopicWeightResultCollector;
import no.ovitas.compass2.search.KnowledgeBaseResult;
import no.ovitas.compass2.search.TopicListQueryResult;

/**
 * @author gyalai
 *
 */
public class TopicListQueryResultImpl extends TopicQueryResultImpl implements TopicListQueryResult{
	
	private Collection<KnowledgeBaseResult> result = new LinkedList<KnowledgeBaseResult>();
	
	public TopicListQueryResultImpl(Map<String, String> termWithStemPair) {
		super(termWithStemPair);
	}
	
	public void addKnowledgeBaseResult(KnowledgeBaseResult termResult) {
		result.add(termResult);
	}
	
	public Collection<KnowledgeBaseResult> getResult() {
		return result;
	}

	@Override
	public void collectTermTopicWeights(TermTopicWeightResultCollector collector) {
		
		setupCollector(collector);
		
		for (KnowledgeBaseResult kbResult : result) {
			kbResult.collectTermTopicWeights(collector);
		}		
	}
	
}
