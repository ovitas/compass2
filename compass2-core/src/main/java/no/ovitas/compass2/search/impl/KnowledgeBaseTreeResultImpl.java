package no.ovitas.compass2.search.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;
import no.ovitas.compass2.search.KnowledgeBaseTreeResult;
import no.ovitas.compass2.search.TermTopicWeightResultCollector;
import no.ovitas.compass2.search.TopicResultNode;
import no.ovitas.compass2.search.TopicWeightResultCollector;

public class KnowledgeBaseTreeResultImpl implements KnowledgeBaseTreeResult {

	private Map<String, Set<TopicResultNode>> result = new HashMap<String, Set<TopicResultNode>>(
			0);

	private RelationDirection direction;

	private KnowledgeBaseDescriptor knowledgeBaseDescriptor;

	public KnowledgeBaseTreeResultImpl() {

	}

	@Override
	public KnowledgeBaseDescriptor getKnowledgeBase() {
		return knowledgeBaseDescriptor;
	}

	@Override
	public void setKnowledgeBase(KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		this.knowledgeBaseDescriptor = knowledgeBaseDescriptor;
	}

	public void addTerm(String term, Set<TopicResultNode> topicResult) {
		result.put(term, topicResult);
	}

	@Override
	public Collection<String> getTerms() {
		return Collections.unmodifiableCollection(result.keySet());
	}

	public Map<String, Set<TopicResultNode>> getResult() {
		return result;
	}

	public Set<TopicResultNode> getTermResult(String term) {
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

		for (Entry<String, Set<TopicResultNode>> termResultPair : result
				.entrySet()) {

			if (!collector.checkFiltered(termResultPair.getKey(),
					knowledgeBaseDescriptor, direction)) {

				topicCollector = collector
						.createTopicWeightResultCollector(termResultPair
								.getKey(), knowledgeBaseDescriptor, direction);

				for (TopicResultNode topicResult : termResultPair.getValue()) {
					topicResult.collectTopicWeight(topicCollector);
				}
			}

		}

	}
}
