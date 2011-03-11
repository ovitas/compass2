/**
 * 
 */
package no.ovitas.compass2.kb.store.dao.jpa;

import java.util.List;

import no.ovitas.compass2.kb.store.dao.ScopeDao;
import no.ovitas.compass2.kb.store.model.ScopeEntity;

/**
 * @class ScopeDaoJpa
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.08.30.
 * 
 */
public class ScopeDaoJpa extends GenericDaoJpa<ScopeEntity, Long> implements ScopeDao {

	public ScopeDaoJpa() {
		super(ScopeEntity.class);
	}

	public List<ScopeEntity> getAllFromKnowledgeBase(Long kbId) {
		// TODO Auto-generated method stub
		return entityManager.createNamedQuery("ScopeEntity.getAllFromKnowledgeBase").setParameter("kb_id", kbId).getResultList();
	}

	public void deleteAllInKnowledgeBase(Long kbId) {
		entityManager.createNamedQuery("ScopeEntity.deleteAllInKnowledgeBase").setParameter("kb_id", kbId).executeUpdate();		
	}

}
