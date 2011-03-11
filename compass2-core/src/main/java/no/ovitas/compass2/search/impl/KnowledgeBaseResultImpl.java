package no.ovitas.compass2.search.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;
import no.ovitas.compass2.search.KnowledgeBaseResult;
import no.ovitas.compass2.search.TermTopicWeightResultCollector;
import no.ovitas.compass2.search.TopicResult;
import no.ovitas.compass2.search.TopicWeightResultCollector;

public class KnowledgeBaseResultImpl implements KnowledgeBaseResult {

	private Map<String, Set<TopicResult>> result = new HashMap<String, Set<TopicResult>>(
			0);
	private KnowledgeBaseDescriptor knowledgeBaseDescriptor;
	private RelationDirection direction;

	@Override
	public KnowledgeBaseDescriptor getKnowledgeBase() {
		return knowledgeBaseDescriptor;
	}

	@Override
	public void setKnowledgeBase(KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		this.knowledgeBaseDescriptor = knowledgeBaseDescriptor;
	}

	public void addTerm(String term, Set<TopicResult> topicResult) {
		result.put(term, topicResult);
	}

	public Map<String, Set<TopicResult>> getResult() {
		return result;
	}

	public Set<TopicResult> getTermResult(String term) {
		return result.get(term);
	}
	
	@Override
	public RelationDirection getDirection() {
		return direction;
	}

	public void setDirection(RelationDirection direction) {
		this.direction = direction;
	};

	@Override
	public void collectTermTopicWeights(TermTopicWeightResultCollector collector) {

		TopicWeightResultCollector topicCollector;

		for (Entry<String, Set<TopicResult>> termResultPair : result.entrySet()) {

			if (!collector.checkFiltered(termResultPair.getKey(),
					knowledgeBaseDescriptor, direction)) {

				topicCollector = collector.createTopicWeightResultCollector(
						termResultPair.getKey(), knowledgeBaseDescriptor,
						direction);

				for (TopicResult topicResult : termResultPair.getValue()) {
					topicResult.collectTopicWeight(topicCollector);
				}
			}
		}
	}
}
