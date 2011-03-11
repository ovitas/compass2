/**
 * 
 */
package no.ovitas.compass2.kb.store.dao;

import java.util.List;

import no.ovitas.compass2.kb.store.model.DirectRelationTypeEntity;

/**
 * @class DirectRelationTypeDao
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.08.03.
 * 
 */
public interface DirectRelationTypeDao extends GenericDao<DirectRelationTypeEntity, Long> {
	
	/**
	 * Get direct relation type entity with specified name and weight.
	 * @param externalId
	 * @param knowledgeBaseName
	 * @return
	 */
	public DirectRelationTypeEntity getDirectRelationTypeEntity(String externalId, long kbId);
	
	/**
	 * Get <code>DirectRelationTypeEntity</code> from specified knowledge base.
	 * @param kbId
	 * @return
	 */
	public List<DirectRelationTypeEntity> getAllFromKnowledgeBase(long kbId);

	
	public void deleteAllInKnowledgeBase(long kbId);

}
