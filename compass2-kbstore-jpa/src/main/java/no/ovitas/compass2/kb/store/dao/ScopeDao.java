/**
 * 
 */
package no.ovitas.compass2.kb.store.dao;

import java.util.List;

import no.ovitas.compass2.kb.store.model.ScopeEntity;

/**
 * @class ScopeDao
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.08.30.
 * 
 */
public interface ScopeDao extends GenericDao<ScopeEntity, Long> {

	/**
	 * Get all scope entities from specified knowledge base. 
	 * @param kbName
	 * @return
	 */
	List<ScopeEntity> getAllFromKnowledgeBase(Long kbId);

	void deleteAllInKnowledgeBase(Long kbId);

}
