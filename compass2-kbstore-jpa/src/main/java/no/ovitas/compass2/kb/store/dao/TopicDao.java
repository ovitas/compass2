/**
 * 
 */
package no.ovitas.compass2.kb.store.dao;

import java.util.List;

import no.ovitas.compass2.kb.store.model.TopicEntity;

/**
 * @class TopicDao
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.08.03.
 * 
 */
public interface TopicDao extends GenericDao<TopicEntity, Long> {

	/**
	 * Get all topics in the knowledge base.
	 * @param kbId
	 * @return
	 */
	public List<TopicEntity> getAllFromKnowledgeBase(Long kbId);
	
	public void deleteAllInKnowledgeBase(Long kbId);
	
}
