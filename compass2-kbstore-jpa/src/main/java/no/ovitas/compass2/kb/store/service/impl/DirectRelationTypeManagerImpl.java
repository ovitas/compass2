/**
 * 
 */
package no.ovitas.compass2.kb.store.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ovitas.compass2.kb.store.dao.DirectRelationTypeDao;
import no.ovitas.compass2.kb.store.model.DirectRelationTypeEntity;
import no.ovitas.compass2.kb.store.service.DirectRelationTypeManager;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;
import no.ovitas.compass2.model.knowledgebase.RelationTypeSetting;

/**
 * @class DirectRelationTypeManagerImpl
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.05.
 * 
 */
public class DirectRelationTypeManagerImpl extends
		GenericManagerImpl<DirectRelationTypeEntity, Long> implements
		DirectRelationTypeManager {

	private DirectRelationTypeDao directRelationTypeDao;

	public DirectRelationTypeManagerImpl(
			DirectRelationTypeDao directRelationTypeDao) {
		super(directRelationTypeDao);
		this.directRelationTypeDao = directRelationTypeDao;
	}

	public void updateRelationsWeight(RelationTypeSetting relationType,
			KnowledgeBaseDescriptor kb) {

		double newWeight = relationType.getWeight();
		double newGeneralizationWeight = relationType.getGeneralizationWeight();

		DirectRelationTypeEntity directRelationType = directRelationTypeDao
				.getDirectRelationTypeEntity(relationType.getExternalId(),
						kb.getId());

		directRelationType.setWeight(newWeight);
		directRelationType.setGeneralizationWeight(newGeneralizationWeight);

		directRelationTypeDao.save(directRelationType);
	}

	public Map<String, Long> getNulledDirectRelationTypes(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor) {

		List<DirectRelationTypeEntity> directEntities = directRelationTypeDao
				.getAllFromKnowledgeBase(knowledgeBaseDescriptor.getId());
		Map<String, Long> result = new HashMap<String, Long>();

		for (DirectRelationTypeEntity directRelationTypeEntity : directEntities) {
			directRelationTypeEntity.setActive(false);
			directRelationTypeEntity.setOccurrence(0);
			result.put(directRelationTypeEntity.getExternalId(),
					directRelationTypeEntity.getId());
		}

		return result;
	}

	public Collection<DirectRelationTypeEntity> getDirectRelationTypesFromKnowledgeBase(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		return directRelationTypeDao
				.getAllFromKnowledgeBase(knowledgeBaseDescriptor.getId());
	}

}
