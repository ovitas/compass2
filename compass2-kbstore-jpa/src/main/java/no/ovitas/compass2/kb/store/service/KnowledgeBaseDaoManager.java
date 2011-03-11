/**
 * 
 */
package no.ovitas.compass2.kb.store.service;

import java.util.List;

import no.ovitas.compass2.kb.store.EntityModel;
import no.ovitas.compass2.kb.store.model.KnowledgeBaseEntity;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBase;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;

/**
 * @class KnowledgeBaseManager
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.08.04.
 * 
 */
public interface KnowledgeBaseDaoManager extends GenericManager<KnowledgeBaseEntity, Long> {

	/**
	 * List all knowledge base.
	 * @return
	 */
	List<KnowledgeBaseDescriptor> listActiveKnowledgeBases();
	
	
	/**
	 * Get specified <code>KnowledgeBaseEntity</code>.
	 * @param kbName
	 * @return
	 */
	public Long getKnowledgeBase(String kbName);

	Long updateKnowledgeBaseEntity(KnowledgeBase knowledgeBase,
			String kbName);

	String getDisplayName(Long kbEntityID);


	void importKnowledgeBase(EntityModel em);


	void deleteKnowledgeBase(KnowledgeBaseDescriptor knowledgeBaseDescriptor);


	void saveKnowledgeBaseEntity(EntityModel em);

}
