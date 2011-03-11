/**
 * 
 */
package no.ovitas.compass2.kb.store.dao;

import java.util.List;
import java.util.Set;

import no.ovitas.compass2.kb.store.model.TopicNameEntity;

/**
 * @class TopicNameDao
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.08.03.
 * 
 */
public interface TopicNameDao extends GenericDao<TopicNameEntity, Long> {

	/**
	 * Get same topics.
	 * @param name
	 * @param scopeFilters 
	 * @param kbName
	 * @return
	 */
	public List<TopicNameEntity> sameTopicName(String name, Long kbId, Set<Long> scopeFilters);

	/**
	 * Get equal topics.
	 * @param name
	 * @param kbName
	 * @return
	 */
	public List<TopicNameEntity> equalTopicName(String name, Long kbId);

	
	
	public void deleteAllInKnowledgeBase(Long kbId);

}
