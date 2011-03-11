/**
 * 
 */
package no.ovitas.compass2.kb.store;

import java.util.Collection;

import no.ovitas.compass2.kb.store.model.DirectRelationEntity;
import no.ovitas.compass2.kb.store.model.DirectRelationTypeEntity;
import no.ovitas.compass2.kb.store.model.KnowledgeBaseEntity;
import no.ovitas.compass2.kb.store.model.ScopeEntity;
import no.ovitas.compass2.kb.store.model.TopicEntity;
import no.ovitas.compass2.kb.store.model.TopicNameEntity;

/**
 * @class EntityModel
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.09.16.
 * 
 */
public interface EntityModel {
	
	public Collection<TopicEntity> getTopicEnities();
	
	public Collection<TopicNameEntity> getTopicNameEnities();
	
	public Collection<ScopeEntity> getScopeEnities();
	
	public Collection<DirectRelationTypeEntity> getDirectRelationTypeEnities();
	
	public Collection<DirectRelationEntity> getDirecRelationEnities();
	
	public KnowledgeBaseEntity getKnowledgeBaseEntity();
	
	public boolean isExistingKnowledgeBase();

	public Collection<DirectRelationEntity> getRelationsByStartTopicImportID(long topicID);
}
