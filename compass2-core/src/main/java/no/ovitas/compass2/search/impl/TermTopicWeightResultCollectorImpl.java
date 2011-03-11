/**
 * 
 */
package no.ovitas.compass2.search.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;
import no.ovitas.compass2.search.FullTextTopicCriteria;
import no.ovitas.compass2.search.FullTextTopicQuery;
import no.ovitas.compass2.search.TermFilter;
import no.ovitas.compass2.search.TermTopicWeightResultCollector;
import no.ovitas.compass2.search.TopicFilter;
import no.ovitas.compass2.search.TopicTreeFilter;
import no.ovitas.compass2.search.TopicWeightResultCollector;

/**
 * @author gyalai
 * 
 */
public class TermTopicWeightResultCollectorImpl implements
		TermTopicWeightResultCollector, FullTextTopicQuery {

	Map<String, TopicWeightResultCollectorImpl> termCollectors = new HashMap<String, TopicWeightResultCollectorImpl>(
			0);
	private Collection<TopicFilter> unSelectedTopics;
	private Collection<TermFilter> unSelectedTerms;
	private Collection<TopicTreeFilter> unSelectedTopicTrees;
	private Map<String, String> termWithStemPair;

	@Override
	public TopicWeightResultCollector createTopicWeightResultCollector(
			String term, KnowledgeBaseDescriptor knowledgeBaseDescriptor,
			RelationDirection direction) {

		TopicWeightResultCollectorImpl termCollector = termCollectors.get(term);

		if (termCollector == null) {
			termCollector = new TopicWeightResultCollectorImpl();
			termCollector.addFilters(unSelectedTopics);

			termCollectors.put(term, termCollector);
		}

		Collection<TopicTreeFilter> treeFilters = new ArrayList<TopicTreeFilter>();

		if (unSelectedTopicTrees != null) {
			for (TopicTreeFilter topicTreeFilter : unSelectedTopicTrees) {
				if (topicTreeFilter.getKnowledgeBaseDescriptor().getId() == knowledgeBaseDescriptor
						.getId()) {
					if ((direction != null && direction.equals(topicTreeFilter
							.getRelationDirection()))
							|| direction == null
							&& topicTreeFilter.getRelationDirection() == null)
						treeFilters.add(topicTreeFilter);
				}
			}

			termCollector.addTreeFilter(treeFilters);
		}

		return termCollector;
	}

	@Override
	public FullTextTopicCriteria getFullTextTopicCriteria(String term) {
		
		if (termWithStemPair == null) {
			return null;
		}
		
		String stemmedTerm = termWithStemPair.get(term);
		
		if (stemmedTerm != null) {
			return termCollectors.get(stemmedTerm);
		}
		
		throw new RuntimeException("No stemmed terms for term : " + term);
	}

	@Override
	public void addTopicFilters(Collection<TopicFilter> unSelectedTopics) {
		this.unSelectedTopics = unSelectedTopics;
	}

	@Override
	public void addTreeTopicFilters(
			Collection<TopicTreeFilter> unSelectedTopicTrees) {
		this.unSelectedTopicTrees = unSelectedTopicTrees;
	}

	@Override
	public void addTermFilters(Collection<TermFilter> unSelectedTerms) {
		this.unSelectedTerms = unSelectedTerms;
	}
	
	@Override
	public void addTermWithStemPair(Map<String, String> termWithStemPair) {
		this.termWithStemPair = termWithStemPair;				
	}

	@Override
	public boolean checkFiltered(String term,
			KnowledgeBaseDescriptor knowledgeBaseDescriptor,
			RelationDirection direction) {

		if (unSelectedTerms != null) {
			for (TermFilter filter : unSelectedTerms) {
				if (filter.checkTerm(term, knowledgeBaseDescriptor, direction))
					return true;
			}
		}

		return false;
	}

}
