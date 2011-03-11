/**
 * 
 */
package no.ovitas.compass2.search;

import java.util.Collection;
import java.util.Map;

import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;

/**
 * @author gyalai
 * 
 */
public interface TopicQuery {

	/**
	 * Add terms to the query.
	 * 
	 * @param terms
	 *            searched terms
	 */
	public void addTerms(Collection<String> terms);

	/**
	 * Add term to the query.
	 * 
	 * @param term
	 *            the searched term
	 */
	public void addTerm(String term);

	/**
	 * Add term with a stemmed term. The query search with the stemmed term.
	 * 
	 * @param term
	 *            the term
	 * @param stem
	 *            the stemmed value of the term. It will be used for the search.
	 */
	public void addTermWithStem(String term, String stem);

	/**
	 * Get the term stem pairs. It contains those terms too which add with
	 * {@link #addTerm(String)} and {@link #addTerms(Collection)} in this way
	 * the pair key and value contains same string.
	 * 
	 * @return with the map
	 */
	public Map<String, String> getTermStemPairs();

	/**
	 * Get search terms. This will be used for search.
	 * 
	 * @return the searched terms
	 */
	public Collection<String> getSearchTerms();

	/**
	 * Create new {@link TopicCriteria} instance with specified knowledge base.
	 * 
	 * @param knowledgeBaseDescriptor
	 *            specified knowledge base
	 * 
	 * @return new TopicCriteria instance
	 */
	public TopicCriteria createTopicCriteria(KnowledgeBaseDescriptor descriptor);

	/**
	 * Get created {@link TopicCriteria} instances.
	 * 
	 * @return created instances
	 */
	public Collection<TopicCriteria> getTopicCriterias();

	/**
	 * Get is prefix match.
	 * 
	 * @return
	 */
	public boolean isPerfixMatch();

	/**
	 * Set prefix match.
	 * 
	 * @param match
	 */
	public void setPrefixMatch(boolean match);

	/**
	 * Get threshold weight.
	 * 
	 * @return
	 */
	public double getThresholdWeight();

	/**
	 * Set threshold weight.
	 * 
	 * @param thresholdWeight
	 */
	public void setThresholdWeight(double thresholdWeight);

	/**
	 * Get max number of topic to expand.
	 * 
	 * @return
	 */
	public int getMaxTopicNumberToExpand();

	/**
	 * Set max number of topic to expand.
	 * 
	 * @param maxTopicNumberToExpand
	 */
	public void setMaxTopicNumberToExpand(int maxTopicNumberToExpand);

	/**
	 * Get hop count.
	 * 
	 * @return
	 */
	public int getHopCount();

	/**
	 * Set hop count.
	 * 
	 * @param hopCount
	 */
	public void setHopCount(int hopCount);

	/**
	 * Get is tree search.
	 * 
	 * @return
	 */
	public boolean isTreeSearch();

	/**
	 * Set tree seach.
	 * 
	 * @param treeSearch
	 */
	public void setTreeSearch(boolean treeSearch);

}
