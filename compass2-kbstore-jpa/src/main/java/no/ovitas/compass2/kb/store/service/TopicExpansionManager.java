package no.ovitas.compass2.kb.store.service;

import java.util.Collection;
import java.util.List;

import no.ovitas.compass2.model.knowledgebase.RelationDirection;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.model.knowledgebase.Topic;
import no.ovitas.compass2.search.TopicResult;
import no.ovitas.compass2.search.TopicResultNode;

/**
 * Manager service that expands topics related to a specified base topic.
 * 
 * @author Csaba Daniel
 */
public interface TopicExpansionManager {

	/**
	 * Set the maximum number of hops to expand from the base topic.
	 * 
	 * @param maxHops
	 *            the maximum number of hops to expand
	 */
	public void setMaxHops(int maxHops);

	/**
	 * Set the minimum total weight to expand.
	 * 
	 * @param minWeight
	 *            the minimum total weight to expand
	 */
	public void setMinWeight(double minWeight);

	/**
	 * Set the maximum number of results to expand.
	 * 
	 * @param maxResults
	 *            the maximum number of results
	 */
	public void setMaxResults(int maxResults);

	/**
	 * Set the scope filter to apply on names of the topics appear in the
	 * result. Every name under a scope that is not allowed will be eliminated
	 * during the expansion.
	 * <p>
	 * To reset this filter, set it to <code>null</code>.
	 * </p>
	 * 
	 * @param allowedScopes
	 *            collection of scopes those are allowed
	 */
	public void setScopeFilter(Collection<Scope> allowedScopes);

	/**
	 * Set the relation direction filter to apply during expansion. Relations
	 * will be expanded only in the specified direction.
	 * <p>
	 * To reset this filter, set it to <code>null</code>.
	 * </p>
	 * 
	 * @param direction
	 */
	public void setDirectionFilter(RelationDirection direction);

	/**
	 * Expand the specified base topics into a list of topic results ordered by
	 * the weight of the best path from the base topic. The result contains the
	 * base topic results and all the related topic results within the specified
	 * criteria.
	 * 
	 * @param baseTopic
	 *            the base topic to start the search from
	 * @return a list of expanded topic results, including the base topic
	 *         results
	 */
	public List<TopicResult> expandList(Collection<Topic> baseTopics);

	/**
	 * Expand the specified base topics into a list of topics result nodes
	 * ordered by the weight of the best path from the base topic. The result
	 * contains the base topics result nodes and all the related topic result
	 * nodes within the specified criteria.
	 * 
	 * @param baseTopics
	 *            the base topic to start the search from
	 * @return a list of expanded topic nodes, including the base topic nodes
	 */
	public List<TopicResultNode> expandTree(Collection<Topic> baseTopics);
}
