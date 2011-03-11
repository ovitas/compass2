package no.ovitas.compass2.kb.store.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import no.ovitas.compass2.kb.store.model.TopicResultNodeImpl;
import no.ovitas.compass2.kb.store.service.TopicExpansionManager;
import no.ovitas.compass2.model.knowledgebase.Relation;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.model.knowledgebase.Topic;
import no.ovitas.compass2.model.knowledgebase.TopicName;
import no.ovitas.compass2.search.TopicResult;
import no.ovitas.compass2.search.TopicResultNode;

/**
 * Generic implementation of the <code>TopicExpansionManager</code> interface
 * that handles the configuration of the manager.
 * 
 * @author Csaba Daniel
 */
public class TopicExpansionManagerImpl implements TopicExpansionManager {

	class TopicComparator implements Comparator<TopicResultNodeImpl> {

		public int compare(TopicResultNodeImpl o1, TopicResultNodeImpl o2) {
			boolean complete1 = o1.isComplete();
			boolean complete2 = o2.isComplete();
			double weight1 = o1.getWeight();
			double weight2 = o2.getWeight();
			int hopCount1 = o1.getHops();
			int hopCount2 = o2.getHops();
			int baseIndex1 = o1.getBaseIndex();
			int baseIndex2 = o2.getBaseIndex();
			long topicImportId1 = o1.getImportId();
			long topicImportId2 = o2.getImportId();
			long topicId1 = o1.getId();
			long topicId2 = o2.getId();

			if (!complete1 & complete2)
				return -1;
			if (!complete2 & complete1)
				return 1;

			if (weight1 > weight2)
				return -1;
			if (weight2 > weight1)
				return 1;

			if (hopCount1 < hopCount2)
				return -1;
			if (hopCount2 < hopCount1)
				return 1;

			if (baseIndex1 < baseIndex2)
				return -1;
			if (baseIndex2 < baseIndex1)
				return 1;

			if (topicImportId1 < topicImportId2)
				return -1;
			if (topicImportId2 < topicImportId1)
				return 1;

			if (topicId1 < topicId2)
				return -1;
			if (topicId2 < topicId1)
				return 1;
			
			return 0;
		}
	}

	private class TopicResultComparator implements Comparator<TopicResult> {

		public int compare(TopicResult o1, TopicResult o2) {
			double weight1 = o1.getWeight();
			double weight2 = o2.getWeight();

			if (weight1 > weight2)
				return -1;
			if (weight2 > weight1)
				return 1;

			return 0;
		}
	}

	protected int maxHops;
	protected int maxResults;
	protected double minWeight;
	protected Collection<Scope> allowedScopes = new ArrayList<Scope>();
	protected RelationDirection direction;

	public void setMaxHops(int maxHops) {
		this.maxHops = maxHops;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public void setMinWeight(double minWeight) {
		this.minWeight = minWeight;
	}

	public void setScopeFilter(Collection<Scope> allowedScopes) {
		this.allowedScopes = allowedScopes;
	}

	public void setDirectionFilter(RelationDirection direction) {
		this.direction = direction;
	}

	public List<TopicResult> expandList(Collection<Topic> baseTopics) {
		// Prepare a list for the result
		TopicResultComparator comparator = new TopicResultComparator();
		List<TopicResult> results = new ArrayList<TopicResult>();

		// Retieve the result tree
		List<TopicResultNodeImpl> baseTopicResults = expandTreeImpl(baseTopics);

		// Iterate over base results and expand them into the result
		for (TopicResultNodeImpl baseTopicResult : baseTopicResults) {
			expand(baseTopicResult, results);
		}

		// Sort the results
		Collections.sort(results, comparator);

		return results;
	}

	private List<TopicResult> expand(TopicResultNodeImpl node,
			List<TopicResult> set) {
		for (TopicResultNodeImpl child : node.getChildrenImpl()) {
			expand(child, set);
		}

		node.setChildrenImpl(new ArrayList<TopicResultNodeImpl>(0));
		set.add(node);

		return set;
	}

	@SuppressWarnings("unchecked")
	public List<TopicResultNode> expandTree(Collection<Topic> baseTopics) {
		return (List) expandTreeImpl(baseTopics);
	}

	private List<TopicResultNodeImpl> expandTreeImpl(
			Collection<Topic> baseTopics) {
		// Prepare a list for the results
		List<TopicResultNodeImpl> results = new ArrayList<TopicResultNodeImpl>();

		// Prepare a sorted set for storing active topics
		TopicComparator comparator = new TopicComparator();
		SortedSet<TopicResultNodeImpl> activeTopics = new TreeSet<TopicResultNodeImpl>(
				comparator);

		// Prepare a map for indexing topics by id
		Map<Long, TopicResultNodeImpl> activeMap = new HashMap<Long, TopicResultNodeImpl>();

		// If no base topics were provided, return the empty result
		if (baseTopics.size() == 0)
			return results;

		// Add base topics to the list
		int i = 0;
		for (Topic baseTopic : baseTopics) {
			// Filter topic names
			Collection<String> names = filterTopicNames(baseTopic);

			if (names.size() > 0) {
				// Prepare result topic and add to the active topics
				TopicResultNodeImpl baseTopicImpl = new TopicResultNodeImpl(
						baseTopic);

				baseTopicImpl.setBaseIndex(i);
				baseTopicImpl.setBaseTopicId(baseTopic.getId());
				baseTopicImpl.setNames(names);
				baseTopicImpl.setWeight(1);

				activeTopics.add(baseTopicImpl);
				activeMap.put(baseTopic.getId(), baseTopicImpl);
				i++;
			}
		}

		// If we are already over max results, return the fitting subset
		if (activeTopics.size() >= maxResults) {
			Iterator<TopicResultNodeImpl> activeTopicIter = activeTopics
					.iterator();

			for (i = 0; i < maxResults; i++) {
				TopicResultNodeImpl topicResult = activeTopicIter.next();
				results.add(topicResult);
			}

			return results;
		}

		int resultSize = 0;

		// Do the expansion
		while (true) {
			// Check exist conditions
			if (results.size() == maxResults || activeTopics.isEmpty())
				break;

			// Pick the topmost element (not complete, max weight, min hops, min
			// index)
			TopicResultNodeImpl currentTopic = activeTopics.first();

			if (currentTopic.isComplete())
				break;

			// Retrieve the related topics
			Collection<TopicResultNodeImpl> relatedTopics = getRelatedTopics(currentTopic);

			// Iterate over the related topics
			for (TopicResultNodeImpl relatedTopic : relatedTopics) {
				// Check if we already have this related topic in the
				// process
				long relatedImportId = relatedTopic.getId();
				if (activeMap.containsKey(relatedImportId)) {
					// Retrieve the stored topic
					TopicResultNodeImpl storedTopic = activeMap
							.get(relatedImportId);

					// Skip if we found a complete topic
					if (storedTopic.isComplete())
						continue;

					// Compare the stored and related topic
					if (relatedTopic.getWeight() > storedTopic.getWeight()) {
						// Remove stored topic
						TopicResultNodeImpl parentTopic = storedTopic
								.getParent();
						parentTopic.removeChild(storedTopic);

						// Remove stored topic from the active collections
						activeTopics.remove(storedTopic);
						activeMap.remove(relatedImportId);

						// Add the related topic to the current topic
						currentTopic.addChild(relatedTopic);

						// Add the related topic to the active collections
						activeTopics.add(relatedTopic);
						activeMap.put(relatedImportId, relatedTopic);
					}
				} else {
					// Add the related topic to the current topic
					currentTopic.addChild(relatedTopic);

					// Add the related topic to the active collections
					activeTopics.add(relatedTopic);
					activeMap.put(relatedImportId, relatedTopic);
				}
			}

			// Finalize the topic and increase result size
			activeTopics.remove(currentTopic);
			// activeMap.remove(currentTopic.getId());
			currentTopic.setComplete(true);
			resultSize++;

			// If this is a base topic, add it to the result
			if (currentTopic.getId() == currentTopic.getBaseTopicId()) {
				results.add(currentTopic);
			}

			// Get out of processing on reaching max results
			if (resultSize == maxResults)
				break;
		}

		// Iterate over the non complete topics and remove them from the tree
		Iterator<TopicResultNodeImpl> activeTopicIter = activeTopics.iterator();
		while (activeTopicIter.hasNext()) {
			TopicResultNodeImpl activeTopic = activeTopicIter.next();

			if (activeTopic.isComplete())
				break;

			TopicResultNodeImpl activeParent = activeTopic.getParent();
			activeParent.removeChild(activeTopic);

			activeTopicIter.remove();
		}

		// Return the result
		return results;
	}

	/**
	 * Get the topics those are in direct connection with the specified topics.
	 * The specified <code>direction</code> filter should be considered during
	 * relation expansion.
	 * 
	 * @param topic
	 *            the topic to get directly related topics for
	 * @return the collection of directly related topics
	 */
	protected Collection<TopicResultNodeImpl> getRelatedTopics(
			TopicResultNodeImpl topicResult) {
		// Prepare a collection for the related topics
		Collection<TopicResultNodeImpl> results = new ArrayList<TopicResultNodeImpl>();

		// If we are already at max hops, quit the process
		if (topicResult.getHops() == maxHops)
			return results;

		// Get the underlying topic
		Topic topic = topicResult.getTopic();

		// Iterate on the the relations of the underlying topic
		for (Relation relation : topic.getRelations()) {
			// Retrieve other topic and relation data
			Topic otherTopic = null;
			double otherWeight = 0;
			RelationDirection otherDirection = null;

			// Retrieve other topic and relation data
			if (relation.getSource().equals(topic)) {
				if (RelationDirection.GEN.equals(direction))
					continue;

				otherTopic = relation.getTarget();
				otherWeight = relation.getType().getWeight();

				if (topicResult.isRoot()
						|| RelationDirection.SPEC.equals(topicResult
								.getDirection()))
					otherDirection = RelationDirection.SPEC;
			} else {
				if (RelationDirection.SPEC.equals(direction))
					continue;

				otherTopic = relation.getSource();
				otherWeight = relation.getType().getGeneralizationWeight();

				if (topicResult.isRoot()
						|| RelationDirection.GEN.equals(topicResult
								.getDirection()))
					otherDirection = RelationDirection.GEN;
			}
			String relationTypeName = relation.getType().getExternalId();
			long relationId = relation.getId();

			// Skip under minimum weight
			if (topicResult.getWeight() * otherWeight < minWeight)
				continue;

			// Filter topic names
			Collection<String> otherTopicNames = filterTopicNames(otherTopic);

			// Skip on no name
			if (otherTopicNames.size() == 0)
				continue;

			// Prepare the topic result and add to the results
			TopicResultNodeImpl otherResult = new TopicResultNodeImpl(
					otherTopic);

			otherResult.setBaseTopicId(topicResult.getBaseTopicId());
			otherResult.setDirection(otherDirection);
			otherResult.setHops(topicResult.getHops() + 1);
			otherResult.setNames(otherTopicNames);
			otherResult.setRelationId(relationId);
			otherResult.setRelationTypeName(relationTypeName);
			otherResult.setWeight(topicResult.getWeight() * otherWeight);

			results.add(otherResult);
		}

		return results;
	}

	/**
	 * Return the topic names of the specified topic filtered by the scopes
	 * filter set with {@link TopicExpansionManager#setScopeFilter(Collection)}.
	 * 
	 * @param topic
	 *            the topic to filter the names of
	 */
	protected Collection<String> filterTopicNames(Topic topic) {
		Collection<String> results = new ArrayList<String>();

		Collection<TopicName> topicNameColl = topic.getNames();
		Iterator<TopicName> topicNameIter = topicNameColl.iterator();

		while (topicNameIter.hasNext()) {
			TopicName topicName = topicNameIter.next();

			Scope scope = topicName.getScope();
			if ((allowedScopes.size() == 0) || (allowedScopes.contains(scope)))
				results.add(topicName.getName());
		}

		return results;
	}
}
