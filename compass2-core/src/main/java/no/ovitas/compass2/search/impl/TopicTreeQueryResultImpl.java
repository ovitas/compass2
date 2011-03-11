/**
 * 
 */
package no.ovitas.compass2.search.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import no.ovitas.compass2.search.TermTopicWeightResultCollector;
import no.ovitas.compass2.search.KnowledgeBaseTreeResult;
import no.ovitas.compass2.search.TopicTreeQueryResult;

/**
 * @author gyalai
 *
 */
public class TopicTreeQueryResultImpl extends TopicQueryResultImpl implements TopicTreeQueryResult {
		
		private Collection<KnowledgeBaseTreeResult> result = new LinkedList<KnowledgeBaseTreeResult>();

		public TopicTreeQueryResultImpl(Map<String, String> termWithStemPair) {
			super(termWithStemPair);
		}
		
		public void addTermResult(KnowledgeBaseTreeResult termResult) {
			result.add(termResult);
		}
		
		public Collection<KnowledgeBaseTreeResult> getResult() {
			return result;
		}

		@Override
		public void collectTermTopicWeights(
				TermTopicWeightResultCollector collector) {
			
			setupCollector(collector);
			
			for (KnowledgeBaseTreeResult kbResult : result) {
				kbResult.collectTermTopicWeights(collector);
			}	
			
		}
	
}
