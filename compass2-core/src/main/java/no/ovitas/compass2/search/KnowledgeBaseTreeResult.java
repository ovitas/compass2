/**
 * 
 */
package no.ovitas.compass2.search;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;

/**
 * @author gyalai
 *
 */
public interface KnowledgeBaseTreeResult {
	
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
	 * Get relation direction.
	 * 
	 * @return
	 */
	public RelationDirection getDirection();
	
	/**
	 * Add term with founded results.
	 * 
	 * @param term
	 * @param topicResult
	 */
	public void addTerm(String term, Set<TopicResultNode> topicResult);
	
	/**
	 * Get results.
	 * 
	 * @return
	 */
	public Map<String, Set<TopicResultNode>> getResult();
	
	/**
	 * Get results which is founded with term.
	 * 
	 * @param term
	 * @return
	 */
	public Set<TopicResultNode> getTermResult(String term);
	
	/**
	 * Get searched terms.
	 * 
	 * @return
	 */
	public Collection<String> getTerms();

	/**
	 * Collect term with weights. 
	 * 
	 * @param collector
	 */
	public void collectTermTopicWeights(TermTopicWeightResultCollector collector);

}
