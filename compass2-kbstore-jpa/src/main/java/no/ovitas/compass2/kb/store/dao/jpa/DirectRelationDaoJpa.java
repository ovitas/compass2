/**
 * 
 */
package no.ovitas.compass2.kb.store.dao.jpa;

import java.util.List;

import no.ovitas.compass2.kb.store.dao.DirectRelationDao;
import no.ovitas.compass2.kb.store.model.DirectRelationEntity;

/**
 * @class DirectRelationDaoJpa
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.08.03.
 * 
 */
public class DirectRelationDaoJpa extends GenericDaoJpa<DirectRelationEntity, Long> implements
		DirectRelationDao {

	public DirectRelationDaoJpa() {
		super(DirectRelationEntity.class);
	}

	@SuppressWarnings("unchecked")
	public List<DirectRelationEntity> getAllFromKnowledgeBase(Long kbId) {
		return entityManager.createNamedQuery("DirectRelationEntity.getAllWithKnowledgeBase").setParameter("kb_id", kbId).getResultList();
	}

	public DirectRelationEntity getRelation(String startTopicID,
			String endTopicID, Long kbId) {
		return (DirectRelationEntity) entityManager.createNamedQuery("DirectRelationEntity.getRelation").setParameter("start_id", startTopicID).setParameter("end_id", endTopicID).setParameter("kb_id", kbId).getSingleResult();
	}

	public void deleteAllInKnowledgeBase(Long kbId) {
		entityManager.createNamedQuery("DirectRelationEntity.deleteAllInKnowledgeBase").setParameter("kb_id", kbId).executeUpdate();		
	}
	
	@SuppressWarnings("unchecked")
	public List<DirectRelationEntity> getRelationsStart(long topicId) {
		List<DirectRelationEntity> resultList = entityManager.createNamedQuery("DirectRelationEntity.getRelationsStart").setParameter("start_id", topicId).getResultList();
		
		for (DirectRelationEntity directRelationEntity : resultList) {
			directRelationEntity.getEndTopic().getNamesEntity();
		}
		
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<DirectRelationEntity> getRelationsEnd(long topicId) {
		List<DirectRelationEntity> resultList = entityManager.createNamedQuery("DirectRelationEntity.getRelationsEnd").setParameter("end_id", topicId).getResultList();
		
		for (DirectRelationEntity directRelationEntity : resultList) {
			directRelationEntity.getStartTopic().getNamesEntity();
		}
		
		return resultList;
	}
	
}
