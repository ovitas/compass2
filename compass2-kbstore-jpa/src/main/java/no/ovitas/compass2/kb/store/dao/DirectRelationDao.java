/**
 * 
 */
package no.ovitas.compass2.kb.store.dao;

import java.util.List;

import no.ovitas.compass2.kb.store.model.DirectRelationEntity;

/**
 * @class DirectRelationDao
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.03.
 * 
 */
public interface DirectRelationDao extends
		GenericDao<DirectRelationEntity, Long> {

	/**
	 * Get all direct relation in the knowledge base.
	 * 
	 * @param name
	 * @return
	 */
	List<DirectRelationEntity> getAllFromKnowledgeBase(Long kbId);

	/**
	 * Get direct relation between start topic and end topic.
	 * 
	 * @param startTopicID
	 * @param endTopicID
	 * @param knowledgeBaseName
	 * @return
	 */
	DirectRelationEntity getRelation(String startTopicID, String endTopicID,
			Long kbId);

	public void deleteAllInKnowledgeBase(Long kbId);
	
	public List<DirectRelationEntity> getRelationsStart(long topicId);
	
	public List<DirectRelationEntity> getRelationsEnd(long topicId);

}
