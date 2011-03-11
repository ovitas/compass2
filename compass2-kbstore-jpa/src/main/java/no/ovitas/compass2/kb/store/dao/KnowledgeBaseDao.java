/**
 * 
 */
package no.ovitas.compass2.kb.store.dao;

import no.ovitas.compass2.kb.store.model.KnowledgeBaseEntity;

/**
 * @class KnowledgeBaseDao
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.08.03.
 * 
 */
public interface KnowledgeBaseDao extends GenericDao<KnowledgeBaseEntity, Long> {
	
	/**
	 * Get specified <code>KnowledgeBaseEntity</code>.
	 * @param kbName
	 * @return
	 */
	public KnowledgeBaseEntity getKnowledgeBase(String kbName);

	public String getDisplayName(Long knowledgeBaseEntity);
	

}
