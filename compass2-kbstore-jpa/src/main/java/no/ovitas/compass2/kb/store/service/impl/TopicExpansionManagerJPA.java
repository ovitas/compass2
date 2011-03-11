/**
 * 
 */
package no.ovitas.compass2.kb.store.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import no.ovitas.compass2.kb.store.dao.DirectRelationDao;
import no.ovitas.compass2.kb.store.model.DirectRelationEntity;
import no.ovitas.compass2.kb.store.model.TopicEntity;
import no.ovitas.compass2.kb.store.model.TopicResultNodeImpl;
import no.ovitas.compass2.kb.store.service.impl.TopicExpansionManagerImpl.TopicComparator;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;
import no.ovitas.compass2.model.knowledgebase.Topic;
import no.ovitas.compass2.search.TopicResult;
import no.ovitas.compass2.search.TopicResultNode;

/**
 * @author gyalai
 * 
 */
public class TopicExpansionManagerJPA extends TopicExpansionManagerImpl {

	private DirectRelationDao directRelationDao;

	@Override
	protected Collection<TopicResultNodeImpl> getRelatedTopics(
			TopicResultNodeImpl topicResult) {
		Topic topic = topicResult.getTopic();

		List<TopicResultNodeImpl> results = new ArrayList<TopicResultNodeImpl>();

		List<DirectRelationEntity> directRelations = getDirectRelations(topic);

		// If we are already at max hops, quit the process
		if (topicResult.getHops() == maxHops)
			return results;
		
		double weight = topicResult.getWeight();
		int hopCount = topicResult.getHops();

		hopCount++;

		Topic neighbor;
		double newWeight;
		for (DirectRelationEntity directRelationEntity : directRelations) {
			if (directRelationEntity.getStartTopic().getId() == topic.getId()) {
				if (RelationDirection.GEN.equals(direction))
					continue;

				neighbor = directRelationEntity.getEndTopic();
				newWeight = directRelationEntity.getRelationType().getWeight()
						* weight;
			} else {
				if (RelationDirection.SPEC.equals(direction))
					continue;

				neighbor = directRelationEntity.getStartTopic();
				newWeight = directRelationEntity.getRelationType()
						.getGeneralizationWeight()
						* weight;
			}

			Collection<String> neighborNames = filterTopicNames(neighbor);

			if (neighborNames.size() == 0)
				continue;

			if (newWeight < minWeight)
				continue;

			TopicResultNodeImpl neighborResult = new TopicResultNodeImpl(
					neighbor);

			neighborResult.setBaseIndex(topicResult.getBaseIndex());
			neighborResult.setBaseTopicId(topicResult.getBaseTopicId());
			neighborResult.setHops(hopCount);
			neighborResult.setNames(neighborNames);
			neighborResult.setRelationTypeName(directRelationEntity.getType()
					.getExternalId());
			neighborResult.setWeight(newWeight);

			results.add(neighborResult);
		}

		return results;
	}

	private List<DirectRelationEntity> getDirectRelations(Topic topic) {
		List<DirectRelationEntity> directRelations;

		if (direction == null) {
			directRelations = directRelationDao
					.getRelationsStart(topic.getId());
			directRelations.addAll(directRelationDao.getRelationsEnd(topic
					.getId()));
		} else if (direction == RelationDirection.SPEC) {
			directRelations = directRelationDao
					.getRelationsStart(topic.getId());
		} else {
			directRelations = directRelationDao.getRelationsEnd(topic.getId());
		}

		return directRelations;
	}

	public void setDirectRelationDao(DirectRelationDao directRelationDao) {
		this.directRelationDao = directRelationDao;
	}
}
