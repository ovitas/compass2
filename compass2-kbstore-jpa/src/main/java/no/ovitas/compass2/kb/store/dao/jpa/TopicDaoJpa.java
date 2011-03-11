/**
 * 
 */
package no.ovitas.compass2.kb.store.dao.jpa;

import java.util.List;

import no.ovitas.compass2.kb.store.dao.TopicDao;
import no.ovitas.compass2.kb.store.model.TopicEntity;

/**
 * @class TopicDaoJpa
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.03.
 * 
 */
public class TopicDaoJpa extends GenericDaoJpa<TopicEntity, Long> implements
		TopicDao {

	public TopicDaoJpa() {
		super(TopicEntity.class);
	}

	public List<TopicEntity> equalTopic(String name) {
		return entityManager.createNamedQuery("TopicEntity.equalTopic")
		.setParameter("tn_name", name).getResultList();
	}

	public List<TopicEntity> sameTopic(String name) {
		// TODO Auto-generated method stub
		return entityManager.createNamedQuery("TopicEntity.sameTopic")
		.setParameter("tn_name", name).getResultList();
	}

	public List<TopicEntity> getAllFromKnowledgeBase(Long kbId) {
		// TODO Auto-generated method stub
		return entityManager.createNamedQuery("TopicEntity.getAllWithKnowledgeBase").setParameter("kb_id", kbId).getResultList();
	}

	public void deleteAllInKnowledgeBase(Long kbId) {
		entityManager.createNamedQuery("TopicEntity.deleteAllInKnowledgeBase").setParameter("kb_id", kbId).executeUpdate();
		
	}

}
