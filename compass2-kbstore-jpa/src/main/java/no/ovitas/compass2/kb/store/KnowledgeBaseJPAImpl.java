/**
 * 
 */
package no.ovitas.compass2.kb.store;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import no.ovitas.compass2.kb.KnowledgeBase;
import no.ovitas.compass2.kb.store.model.DirectRelationEntity;
import no.ovitas.compass2.kb.store.model.DirectRelationTypeEntity;
import no.ovitas.compass2.kb.store.model.KnowledgeBaseEntity;
import no.ovitas.compass2.kb.store.model.ScopeEntity;
import no.ovitas.compass2.kb.store.model.TopicEntity;
import no.ovitas.compass2.kb.store.model.TopicNameEntity;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseType;
import no.ovitas.compass2.model.knowledgebase.Relation;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.model.knowledgebase.Topic;
import no.ovitas.compass2.model.knowledgebase.TopicName;

import org.apache.commons.lang.StringUtils;

/**
 * @class EntityCreatorFatcory
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.09.15.
 * 
 */
class KnowledgeBaseJPAImpl implements KnowledgeBase, EntityModel {

	private static final String KEY_REMOVE = "REMOVE";

	private KnowledgeBaseEntity kbEntity;

	private KBManagerImpl knowledgeBaseManager;

	private Map<Long, TopicEntity> topicEntities = new HashMap<Long, TopicEntity>(
			0);

	private Map<Long, ScopeEntity> scopeEntities = new HashMap<Long, ScopeEntity>(
			0);

	private Map<Long, TopicNameEntity> topicNameEntities = new HashMap<Long, TopicNameEntity>(
			0);

	private Map<Long, DirectRelationEntity> directRelationEntities = new HashMap<Long, DirectRelationEntity>(
			0);

	private Map<Long, Set<DirectRelationEntity>> directRelationEntitiesByTopicImportId = new HashMap<Long, Set<DirectRelationEntity>>(
			0);

	private Set<DirectRelationEntity> directRelationEntitiesSet = new HashSet<DirectRelationEntity>(
			0);

	private Map<Long, DirectRelationTypeEntity> directRelationTypeEntities = new HashMap<Long, DirectRelationTypeEntity>(
			0);

	private Map<String, TopicEntity> topicEntitiesWithExternalID = new HashMap<String, TopicEntity>(
			0);

	private Map<String, ScopeEntity> scopeEntitiesWithExternalID = new HashMap<String, ScopeEntity>(
			0);

	private Map<String, DirectRelationTypeEntity> directRelationTypeEntitiesWithExternalID = new HashMap<String, DirectRelationTypeEntity>(
			0);

	private long importId = 0;

	private Collection<DirectRelationTypeEntity> storedRelationTypes;

	private boolean existingKnowledgeBase = false;

	private boolean closedModel = false;

	private boolean error = false;

	private KnowledgeBaseType knowledgeBaseType = KnowledgeBaseType.SPECGEN;

	private boolean suggestionUpload;

	private KnowledgeBaseJPAImpl(KBManagerImpl knowledgeBaseManager) {
		this.knowledgeBaseManager = knowledgeBaseManager;

	}

	KnowledgeBaseJPAImpl(KBManagerImpl knowledgeBaseManager,
			KnowledgeBaseType knowledgeBaseType) {
		this.knowledgeBaseManager = knowledgeBaseManager;
		this.knowledgeBaseType = knowledgeBaseType;
	}

	public Topic createTopic(String externalId) {
		if (closedModel)
			return null;
		if (kbEntity != null) {
			TopicEntity topicEntity = new TopicEntity();
			topicEntity.setKnowledgeBase(kbEntity);
			topicEntity.setImportId(++importId);
			topicEntity.setExternalId(externalId);
			topicEntities.put(topicEntity.getImportId(), topicEntity);
			topicEntitiesWithExternalID.put(externalId, topicEntity);
			return topicEntity;
		}
		return null;
	}

	public Scope createScope(String externalId) {
		if (closedModel)
			return null;
		if (kbEntity != null) {
			ScopeEntity scopeEntity = new ScopeEntity();
			scopeEntity.setKnowledgeBase(kbEntity);
			scopeEntity.setImportId(++importId);
			scopeEntity.setExternalId(externalId);
			scopeEntities.put(scopeEntity.getImportId(), scopeEntity);
			scopeEntitiesWithExternalID.put(externalId, scopeEntity);
			return scopeEntity;
		}
		return null;
	}

	public TopicName createTopicName() {
		if (closedModel)
			return null;
		if (kbEntity != null) {
			TopicNameEntity topicNameEntity = new TopicNameEntity();
			topicNameEntity.setImportId(++importId);
			topicNameEntities.put(topicNameEntity.getImportId(),
					topicNameEntity);
			return topicNameEntity;
		}
		return null;
	}

	public Relation createRelation() {
		if (closedModel)
			return null;
		if (kbEntity != null) {
			DirectRelationEntity directRelationEntity = new DirectRelationEntity();
			directRelationEntity.setImportId(++importId);
			this.directRelationEntities.put(directRelationEntity.getImportId(),
					directRelationEntity);
			return directRelationEntity;
		}
		return null;
	}

	public RelationType createRelationType(String externalId) {
		if (closedModel)
			return null;
		if (kbEntity != null) {
			DirectRelationTypeEntity directRelationTypeEntity = null;
			if (storedRelationTypes != null) {
				for (DirectRelationTypeEntity stored : storedRelationTypes) {
					if (stored.getExternalId().equals(externalId)) {
						directRelationTypeEntity = stored;
						directRelationTypeEntity.setId(0);
						break;
					}
				}

				if (directRelationTypeEntity == null) {
					directRelationTypeEntity = new DirectRelationTypeEntity();
					directRelationTypeEntity.setWeight(0.5);
					directRelationTypeEntity.setGeneralizationWeight(0.5);
				}

			} else {
				directRelationTypeEntity = new DirectRelationTypeEntity();
				directRelationTypeEntity.setWeight(0.5);
				directRelationTypeEntity.setGeneralizationWeight(0.5);
			}
			directRelationTypeEntity.setKnowledgeBaseEntity(kbEntity);
			directRelationTypeEntity.setImportId(++importId);
			directRelationTypeEntity.setExternalId(externalId);
			directRelationTypeEntity.setActive(false);
			directRelationTypeEntity.setOccurrence(0);
			directRelationTypeEntities.put(
					directRelationTypeEntity.getImportId(),
					directRelationTypeEntity);
			directRelationTypeEntitiesWithExternalID.put(externalId,
					directRelationTypeEntity);
			return directRelationTypeEntity;
		}
		return null;
	}

	static KnowledgeBaseJPAImpl newInstance(KBManagerImpl knowledgeBaseManager) {

		return new KnowledgeBaseJPAImpl(knowledgeBaseManager);
	}

	public KnowledgeBaseDescriptor createDefaultKnowledgeBase(String displayName) {
		if (kbEntity == null) {
			kbEntity = new KnowledgeBaseEntity();
			kbEntity.setDisplayName(displayName);
			kbEntity.setDescription("");
			kbEntity.setType(knowledgeBaseType);
		}
		return kbEntity;
	}

	public KnowledgeBaseDescriptor createDefaultKnowledgeBase(
			String displayName, String descriptor) {
		if (kbEntity == null) {
			kbEntity = new KnowledgeBaseEntity();
			kbEntity.setDisplayName(displayName);
			kbEntity.setDescription(descriptor);
			kbEntity.setType(knowledgeBaseType);
		}
		return kbEntity;
	}

	public KnowledgeBaseDescriptor createDefaultKnowledgeBase(
			String displayName, String description, KnowledgeBaseType type) {
		if (kbEntity == null) {
			kbEntity = new KnowledgeBaseEntity();
			kbEntity.setDisplayName(displayName);
			kbEntity.setDescription(description);
			kbEntity.setType(type);
		}

		return kbEntity;
	}

	public KnowledgeBaseDescriptor loadDefaultKnowledgeBase(Long id) {
		if (kbEntity == null) {
			kbEntity = knowledgeBaseManager.getKnowledgeBaseEntity(id);
			if (kbEntity == null) {
				error = true;
				return null;
			}
			storedRelationTypes = knowledgeBaseManager
					.getDirectRelationTypes(kbEntity);
			existingKnowledgeBase = true;
		}
		return kbEntity;
	}

	public KnowledgeBaseDescriptor getDefaultKnowledgeBase() {
		return kbEntity;
	}

	public void removeTopic(long importId) {
		if (closedModel)
			return;
		TopicEntity topicEntity = topicEntities.remove(importId);
		topicEntitiesWithExternalID.remove(topicEntity.getExternalId());
	}

	public void removeScope(long importId) {
		if (closedModel)
			return;
		ScopeEntity scopeEntity = scopeEntities.remove(importId);
		scopeEntitiesWithExternalID.remove(scopeEntity.getExternalId());
	}

	public void removeTopicName(long importId) {
		if (closedModel)
			return;
		topicNameEntities.remove(importId);
	}

	public void removeRelation(long importId) {
		if (closedModel)
			return;
		DirectRelationEntity remove = directRelationEntities.remove(importId);
		TopicEntity startTopic = remove.getStartTopic();
		if (startTopic != null) {
			Set<DirectRelationEntity> directRelationList = directRelationEntitiesByTopicImportId
					.get(startTopic.getImportId());
			if (directRelationList != null) {
				directRelationList.remove(remove);
			}
		}
		DirectRelationTypeEntity relationType = remove.getRelationType();
		if (relationType != null) {
			relationType.decreaseOccurrence();
			if (relationType.getOccurrence() == 0) {
				relationType.setActive(false);
			}
		}
	}

	public void removeRelationType(long importId) {
		if (closedModel)
			return;
		DirectRelationTypeEntity directRelationTypeEntity = directRelationTypeEntities
				.remove(importId);
		directRelationTypeEntitiesWithExternalID
				.remove(directRelationTypeEntity.getExternalId());

	}

	public Collection<TopicEntity> getTopicEnities() {
		return topicEntities.values();
	}

	public Collection<TopicNameEntity> getTopicNameEnities() {
		return topicNameEntities.values();
	}

	public Collection<ScopeEntity> getScopeEnities() {
		return scopeEntities.values();
	}

	public Collection<DirectRelationTypeEntity> getDirectRelationTypeEnities() {
		return directRelationTypeEntities.values();
	}

	public Collection<DirectRelationEntity> getDirecRelationEnities() {
		return directRelationEntities.values();
	}

	public KnowledgeBaseEntity getKnowledgeBaseEntity() {
		return kbEntity;
	}

	public void addTopicsToRelation(Topic startTopic, Topic endTopic,
			Relation relation) {
		if (closedModel)
			return;
		long relationId = relation.getImportId();
		long startTopicID = startTopic.getImportId();
		long endTopicID = endTopic.getImportId();

		addTopicsToRelation(startTopicID, endTopicID, relationId);
	}

	public void addTopicsToRelation(long startTopicID, long endTopicID,
			long relationId) {
		if (closedModel)
			return;
		TopicEntity startTopicEntity = topicEntities.get(startTopicID);
		TopicEntity endTopicEntity = topicEntities.get(endTopicID);
		DirectRelationEntity directRelationEntity = directRelationEntities
				.get(relationId);

		directRelationEntity.setStartTopic(startTopicEntity);
		directRelationEntity.setEndTopic(endTopicEntity);
		startTopicEntity.addRelation(directRelationEntity);
		endTopicEntity.addRelation(directRelationEntity);

		Set<DirectRelationEntity> directRelationList = directRelationEntitiesByTopicImportId
				.get(startTopicID);

		if (directRelationList != null) {
			directRelationList.add(directRelationEntity);
		} else {
			directRelationList = new HashSet<DirectRelationEntity>(1);
			directRelationList.add(directRelationEntity);
			directRelationEntitiesByTopicImportId.put(startTopicID,
					directRelationList);
		}
	}

	public void addRelationType(Relation relation, RelationType relationType) {
		if (closedModel)
			return;
		long relationID = relation.getImportId();
		long relationTypeID = relationType.getImportId();

		addRelationType(relationID, relationTypeID);
	}

	public void addRelationType(long relationID, long relationTypeID) {
		if (closedModel)
			return;
		DirectRelationEntity directRelationEntity = directRelationEntities
				.get(relationID);
		DirectRelationTypeEntity directRelationTypeEntity = directRelationTypeEntities
				.get(relationTypeID);

		directRelationEntity.setRelationType(directRelationTypeEntity);
		directRelationTypeEntity.increaseOccurence();

		if (!directRelationTypeEntity.isActive()) {
			directRelationTypeEntity.setActive(true);
		}
	}

	public void addTopicName(Topic topic, TopicName topicName) {
		if (closedModel)
			return;
		long topicID = topic.getImportId();
		long topicNameID = topicName.getImportId();

		addTopicName(topicID, topicNameID);
	}

	public void addTopicName(long topicID, long topicNameID) {
		if (closedModel)
			return;
		TopicEntity topicEntity = topicEntities.get(topicID);
		TopicNameEntity topicNameEntity = topicNameEntities.get(topicNameID);

		topicNameEntity.setTopic(topicEntity);
		topicEntity.addTopicName(topicNameEntity);
	}

	public void addScope(TopicName topicName, Scope scope) {
		if (closedModel)
			return;
		long topicNameID = topicName.getImportId();
		long scopeID = scope.getImportId();

		addScope(topicNameID, scopeID);
	}

	public void addScope(long topicNameID, long scopeID) {
		if (closedModel)
			return;
		TopicNameEntity topicNameEntity = topicNameEntities.get(topicNameID);
		ScopeEntity scopeEntity = scopeEntities.get(scopeID);

		topicNameEntity.setScopeEntity(scopeEntity);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<Topic> getTopics() {
		return (Collection) topicEntities.values();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<Scope> getScopes() {
		return (Collection) scopeEntities.values();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection<RelationType> getRelationTypes() {
		return (Collection) directRelationTypeEntities.values();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection<Relation> getRelations() {
		return (Collection) directRelationEntities.values();
	}

	public Topic findTopicByExternalId(String externalId) {
		assert externalId != null;
		assert externalId != "";

		return topicEntitiesWithExternalID.get(externalId);
	}

	public Scope findScopeByExternalId(String externalId) {
		assert externalId != null;
		assert externalId != "";

		return scopeEntitiesWithExternalID.get(externalId);
	}

	public RelationType findRelationTypeByExternalId(String externalId) {
		assert externalId != null;
		assert externalId != "";

		return directRelationTypeEntitiesWithExternalID.get(externalId);
	}

	public boolean isExistingKnowledgeBase() {
		return existingKnowledgeBase;
	}

	public boolean isClosedModel() {
		return closedModel;
	}

	public Topic findTopic(long topicId) {
		return topicEntities.get(topicId);
	}

	public Scope findScope(long scopeId) {
		return scopeEntities.get(scopeId);
	}

	public RelationType findRelationType(long relationTypeId) {
		return directRelationTypeEntities.get(relationTypeId);
	}

	public Relation findRelation(long relationId) {
		return directRelationEntities.get(relationId);
	}

	/**
	 * Return a <code>DirectRelationEntity</code> based on its import id.
	 * 
	 * @param importId
	 *            the import id of the direct relation
	 * @return the <code>DirectRelationEntity</code>
	 */
	public DirectRelationEntity findDirectRelationEntity(long importId) {
		return directRelationEntities.get(importId);
	}

	public TopicName findTopicName(long topicNameId) {
		return topicNameEntities.get(topicNameId);
	}

	public void finishImport() {
		closedModel = true;
		if (existingKnowledgeBase) {
			for (DirectRelationTypeEntity directRelationTypeEntity : storedRelationTypes) {
				directRelationTypeEntity.setActive(false);
				directRelationTypeEntity.setId(0);
				directRelationTypeEntity.setOccurrence(0);
				directRelationTypeEntity.setKnowledgeBaseEntity(kbEntity);
				directRelationTypeEntities.put(++importId,
						directRelationTypeEntity);
			}
		}
	}

	public boolean hasError() {
		return error;
	}

	public Collection<DirectRelationEntity> getRelationsByStartTopicImportID(
			long topicID) {
		Set<DirectRelationEntity> result = directRelationEntitiesByTopicImportId
				.get(topicID);

		if (result != null) {
			return result;
		}
		return new HashSet<DirectRelationEntity>();

	}

	public void filterScopes(Collection<Long> allowedScopes) {
		// Iterate over topic names
		Collection<TopicNameEntity> topicNameColl = topicNameEntities.values();
		Iterator<TopicNameEntity> topicNameIter = topicNameColl.iterator();
		while (topicNameIter.hasNext()) {
			// Gather scope id for topic name
			TopicNameEntity topicName = topicNameIter.next();
			ScopeEntity scope = topicName.getScopeEntity();
			Long scopeId = null;
			if (scope != null)
				scopeId = scope.getImportId();
			// Validate scope id for allowed scopes
			if (!allowedScopes.contains(scopeId)) {
				// Set remove property for the failed
				topicName.setProperty(KEY_REMOVE, true);
			}
		}
		// Remove the not allowed scopes from the knowledge base
		Collection<ScopeEntity> scopeColl = scopeEntities.values();
		Iterator<ScopeEntity> scopeIter = scopeColl.iterator();
		while (scopeIter.hasNext()) {
			Scope scope = scopeIter.next();
			Long scopeId = scope.getImportId();

			if (!allowedScopes.contains(scopeId)) {
				String scopeExternalId = scope.getExternalId();
				scopeEntitiesWithExternalID.remove(scopeExternalId);
				scopeIter.remove();
			}
		}
	}

	public void cleanUp() {
		cleanUpTopicNames();
		cleanUpTopics();
		cleanUpRelationTypes();
		cleanUpRelations();
	}

	private void cleanUpTopicNames() {
		// Iterate over topic names
		Collection<TopicNameEntity> topicNameColl = topicNameEntities.values();
		Iterator<TopicNameEntity> topicNameIter = topicNameColl.iterator();
		while (topicNameIter.hasNext()) {
			// Gather scope remove property if exists
			TopicNameEntity topicName = topicNameIter.next();
			Boolean remove = (Boolean) topicName.getProperty(KEY_REMOVE);
			// Check remove property
			if (remove != null && remove == true) {
				// Remove topic - topicName relation
				TopicEntity topic = topicName.getTopic();
				Collection<TopicNameEntity> topicTopicNames = topic
						.getNamesEntity();
				topicTopicNames.remove(topicName);
				// Remove topicName
				topicNameIter.remove();
			}
		}
	}

	private void cleanUpTopics() {
		// Iterate over topics
		Collection<TopicEntity> topicColl = topicEntities.values();
		Iterator<TopicEntity> topicIter = topicColl.iterator();
		while (topicIter.hasNext()) {
			// Check topic name count, remove topics with no names
			TopicEntity topic = topicIter.next();
			Set<TopicNameEntity> topicNames = topic.getNamesEntity();
			if (topicNames == null || topicNames.size() == 0) {
				// Mark relations of this topic for removal
				Collection<Relation> relationColl = topic.getRelations();
				Iterator<Relation> relationIter = relationColl.iterator();
				while (relationIter.hasNext()) {
					Relation relation = relationIter.next();
					relation.setProperty(KEY_REMOVE, true);
				}
				// Remove topic
				topicEntitiesWithExternalID.remove(topic.getExternalId());
				topicIter.remove();
			}
		}
	}

	private void cleanUpRelationTypes() {
		// Iterate over relation types
		Collection<DirectRelationTypeEntity> relationTypeColl = directRelationTypeEntities
				.values();
		Iterator<DirectRelationTypeEntity> relationTypeIter = relationTypeColl
				.iterator();
		while (relationTypeIter.hasNext()) {
			// Check relation type topic existence, remove types without topic
			DirectRelationTypeEntity relationType = relationTypeIter.next();
			String topicId = relationType.getExternalId();
			if (StringUtils.isEmpty(relationType.getDisplayName())) {
				directRelationTypeEntitiesWithExternalID.remove(topicId);
				relationTypeIter.remove();
			}
		}
	}

	private void cleanUpRelations() {
		// Iterate over relations
		Collection<DirectRelationEntity> relationColl = directRelationEntities
				.values();
		Iterator<DirectRelationEntity> relationIter = relationColl.iterator();
		while (relationIter.hasNext()) {
			// Check relation type, if not exists, mark for deletion
			DirectRelationEntity relation = relationIter.next();
			DirectRelationTypeEntity relationType = relation.getRelationType();
			if (relationType == null) {
				relation.setProperty(KEY_REMOVE, true);
			} else {
				if (!directRelationTypeEntities.containsKey(relationType
						.getImportId())) {
					relation.setProperty(KEY_REMOVE, true);
				}
			}

			// Check connected topics, if any of them is missing, mark for
			// removal
			Long sourceId = null;
			Long targetId = null;
			Topic source = relation.getSource();
			Topic target = relation.getTarget();
			if (source != null)
				sourceId = source.getImportId();
			if (target != null)
				targetId = target.getImportId();
			if (sourceId == null || targetId == null
					|| !topicEntities.containsKey(sourceId)
					|| !topicEntities.containsKey(targetId)) {
				relation.setProperty(KEY_REMOVE, true);
			}
			// Finally remove relation
			Boolean remove = (Boolean) relation.getProperty(KEY_REMOVE);
			if (remove != null && remove == true) {
				source = relation.getSource();
				target = relation.getTarget();
				if (source != null) {
					Set<Relation> topicRelations = source.getRelations();
					topicRelations.remove(relation);
				}
				if (target != null) {
					Set<Relation> topicRelations = target.getRelations();
					topicRelations.remove(relation);
				}
				relationIter.remove();

				Set<DirectRelationEntity> directRelationList = directRelationEntitiesByTopicImportId
						.get(sourceId);

				if (directRelationList != null) {
					directRelationList.remove(relation);
				}

			} else {
				if (directRelationEntitiesSet.contains(relation)) {
					relationIter.remove();
					Set<DirectRelationEntity> directRelationList = directRelationEntitiesByTopicImportId
							.get(sourceId);

					if (directRelationList != null) {
						directRelationList.remove(relation);
					}
				} else {
					directRelationEntitiesSet.add(relation);
				}
			}
		}
	}
	
	public KnowledgeBaseType getKnowledgeBaseType() {
		return knowledgeBaseType;
	}

	public KnowledgeBaseDescriptor getKnowledgeBaseDescriptor() {
		return kbEntity;
	}
	
	public boolean isSuggestionUpload() {
		return suggestionUpload;
	}
	
	public void setSuggestionUpload(boolean upload) {
		this.suggestionUpload = upload;
	}
}
