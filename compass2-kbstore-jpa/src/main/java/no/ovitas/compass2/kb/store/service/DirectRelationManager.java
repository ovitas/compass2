/**
 * 
 */
package no.ovitas.compass2.kb.store.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import no.ovitas.compass2.kb.store.model.DirectRelationEntity;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.Relation;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.Topic;


/**
 * @class DirectRelationManager
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.04.
 * 
 */
public interface DirectRelationManager extends
		GenericManager<DirectRelationEntity, Long> {

	/**
	 * 
	 * @param startTopicName
	 * @param endTopicName
	 * @param knowledgeBaseName
	 * @return
	 */
	DirectRelationEntity getRelation(String startTopicID, String endTopicID,
			KnowledgeBaseDescriptor knowledgeBaseDescriptor);

	/**
	 * Get all direct relation in the name knowledge base.
	 * 
	 * @param name
	 * @return
	 */
	List<Long> getAllInKnowledgeBase(KnowledgeBaseDescriptor knowledgeBaseDescriptor);

}
