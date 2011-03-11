/**
 * 
 */
package no.ovitas.compass2.search;

import java.util.Map;
import java.util.Set;

import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;

/**
 * @author gyalai
 *
 */
public interface KnowledgeBaseResult {

	/**
	 * Get knowledge base descriptor.
	 * 
	 * @return
	 */
	public KnowledgeBaseDescriptor getKnowledgeBase();
	
	/**
	 * Set knowledge base descriptor.
	 * 
	 * @param knowledgeBaseDescriptor
	 */
	public void setKnowledgeBase(KnowledgeBaseDescriptor knowledgeBaseDescriptor);
	
	/**
	 * Add term with founded results.
	 * 
	 * @param term
	 * @param topicResult
	 */
	public void addTerm(String term, Set<TopicResult> topicResult);
	
	/**
	 * Get results.
	 * 
	 * @return
	 */
	public Map<String, Set<TopicResult>> getResult();
	
	/**
	 * Get results which is founded with term.
	 * 
	 * @param term
	 * @return
	 */
	public Set<TopicResult> getTermResult(String term);

	/**
	 * Collect term with weights. 
	 * 
	 * @param collector
	 */
	public void collectTermTopicWeights(TermTopicWeightResultCollector collector);

	/**
	 * Get relation direction.
	 * 
	 * @return
	 */
	public RelationDirection getDirection();
	
}
