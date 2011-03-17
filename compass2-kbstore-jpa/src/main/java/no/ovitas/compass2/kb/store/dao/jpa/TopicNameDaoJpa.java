/**
 * 
 */
package no.ovitas.compass2.kb.store.dao.jpa;

import java.util.List;
import java.util.Set;

import no.ovitas.compass2.kb.store.dao.TopicNameDao;
import no.ovitas.compass2.kb.store.model.TopicNameEntity;

/**
 * @class TopicNameDaoJpa
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.03.
 * 
 */
public class TopicNameDaoJpa extends GenericDaoJpa<TopicNameEntity, Long>
		implements TopicNameDao {

	public TopicNameDaoJpa() {
		super(TopicNameEntity.class);
	}

	public List<TopicNameEntity> equalTopicName(String name, Long kbId) {
		return entityManager.createNamedQuery("TopicNameEntity.equalTopicName")
				.setParameter("tn_name", name).setParameter("kb_id", kbId)
				.getResultList();
	}

	public List<TopicNameEntity> sameTopicName(String name, Long kbId,
			Set<Long> scopeFilters) {

		StringBuffer buffer = new StringBuffer(
				"SELECT distinct t FROM TopicNameEntity t WHERE");
		buffer.append(" t.name LIKE '%");
		buffer.append(name);
		buffer.append("%' and t.topic.knowledgeBase.id = ");
		buffer.append(kbId);

		if (!scopeFilters.isEmpty()) {
			buffer.append(" and (");
			for (Long sId : scopeFilters) {
				buffer.append(" t.scopeEntity.id = ");
				buffer.append(sId);
				buffer.append(" OR");
			}
			
			int length = buffer.length();
			buffer.replace(length - 3, length, "");
			
			buffer.append(" )");
		}

		log.info(buffer.toString());
		List<TopicNameEntity> resultList = entityManager
				.createQuery(buffer.toString()).getResultList();

		log.info("Size: " + resultList.size());
		return resultList;
	}

	public void deleteAllInKnowledgeBase(Long kbId) {
		int executeUpdate = entityManager
				.createNamedQuery("TopicNameEntity.deleteAllInKnowledgeBase")
				.setParameter("kb_id", kbId).executeUpdate();
	}

}
