/**
 * 
 */
package no.ovitas.compass2.search;

import java.util.Collection;
import java.util.Map;

import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;

/**
 * @author gyalai
 * 
 */
public interface TermTopicWeightResultCollector {

	/**
	 * Create {@link TopicWeightResultCollector} for a specified term. If it
	 * existed then return that instance. Set knowledge base descriptor and
	 * direction.
	 * 
	 * @param term
	 *            specified term
	 * @param knowledgeBaseDescriptor
	 *            actual knowledge base descriptor
	 * @param direction
	 *            actual direction
	 * @return
	 */
	public TopicWeightResultCollector createTopicWeightResultCollector(
			String term, KnowledgeBaseDescriptor knowledgeBaseDescriptor,
			RelationDirection direction);

	/**
	 * Add topic filter to the collector.
	 * 
	 * @param unSelectedTopics
	 *            the topic filters
	 */
	void addTopicFilters(Collection<TopicFilter> unSelectedTopics);

	/**
	 * Add term filter to the collector.
	 * 
	 * @param unSelectedTerms
	 *            the term filters
	 */
	void addTermFilters(Collection<TermFilter> unSelectedTerms);

	/**
	 * Check term is filtered in this knowledge base and direction.
	 * 
	 * @param term
	 *            specified term
	 * @param knowledgeBaseDescriptor
	 *            specified knowledge base
	 * @param direction
	 *            specified direction
	 * @return true if it is filtered else return false
	 */
	public boolean checkFiltered(String term,
			KnowledgeBaseDescriptor knowledgeBaseDescriptor,
			RelationDirection direction);

	/**
	 * Add tree topic filters to collection.
	 * 
	 * @param unSelectedTopics
	 *            the tree filters
	 */
	void addTreeTopicFilters(Collection<TopicTreeFilter> unSelectedTopics);

	/**
	 * Add term stem pair to collection.
	 * 
	 * @param termWithStemPair
	 */
	public void addTermWithStemPair(Map<String, String> termWithStemPair);
}
