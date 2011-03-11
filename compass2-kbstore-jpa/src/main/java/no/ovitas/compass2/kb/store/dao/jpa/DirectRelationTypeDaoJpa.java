/**
 * 
 */
package no.ovitas.compass2.kb.store.dao.jpa;

import java.util.List;

import no.ovitas.compass2.kb.store.dao.DirectRelationTypeDao;
import no.ovitas.compass2.kb.store.model.DirectRelationTypeEntity;

/**
 * @class DirectRelationTypeDaoJpa
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.03.
 * 
 */
public class DirectRelationTypeDaoJpa extends
		GenericDaoJpa<DirectRelationTypeEntity, Long> implements
		DirectRelationTypeDao {

	public DirectRelationTypeDaoJpa() {
		super(DirectRelationTypeEntity.class);
	}

	public DirectRelationTypeEntity getDirectRelationTypeEntity(String externalId, long kbId) {

		return (DirectRelationTypeEntity) entityManager.createNamedQuery(
				"DirectRelationTypeEntity.getDirectRelationTypeEntity")
				.setParameter("id", externalId).setParameter("kb_id", kbId).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<DirectRelationTypeEntity> getAllFromKnowledgeBase(long kbId) {
		// TODO Auto-generated method stub
		return entityManager.createNamedQuery("DirectRelationTypeEntity.getAllFromKnowledgeBase").setParameter("kb_id", kbId).getResultList();
	}

	public void deleteAllInKnowledgeBase(long kbId) {
		entityManager.createNamedQuery("DirectRelationTypeEntity.deleteAllInKnowledgeBase").setParameter("kb_id", kbId).executeUpdate();		
	}
}
