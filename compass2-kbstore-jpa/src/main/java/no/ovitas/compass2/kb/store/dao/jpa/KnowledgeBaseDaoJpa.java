/**
 * 
 */
package no.ovitas.compass2.kb.store.dao.jpa;

import no.ovitas.compass2.kb.store.dao.KnowledgeBaseDao;
import no.ovitas.compass2.kb.store.model.KnowledgeBaseEntity;

/**
 * @class KnowledgeBaseDaoJpa
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.08.03.
 * 
 */
public class KnowledgeBaseDaoJpa extends GenericDaoJpa<KnowledgeBaseEntity, Long> implements
		KnowledgeBaseDao {
	
	public KnowledgeBaseDaoJpa() {
		super(KnowledgeBaseEntity.class);
	}

	public KnowledgeBaseEntity getKnowledgeBase(String kbName) {
		// TODO Auto-generated method stub
		return (KnowledgeBaseEntity) entityManager.createNamedQuery("KnowledgeBaseEntity.getKnowledgeBase").setParameter("kb_name", kbName).getSingleResult();
	}

	public String getDisplayName(Long kbID) {
		// TODO Auto-generated method stub
		return get(kbID).getDisplayName();
	}

}
