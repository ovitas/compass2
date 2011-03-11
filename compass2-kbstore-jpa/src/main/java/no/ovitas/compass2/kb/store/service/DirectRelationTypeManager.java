/**
 * 
 */
package no.ovitas.compass2.kb.store.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.ovitas.compass2.kb.store.model.DirectRelationTypeEntity;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.RelationTypeSetting;

/**
 * @class DirectRelationTypeManager
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.04.
 * 
 */
public interface DirectRelationTypeManager extends
		GenericManager<DirectRelationTypeEntity, Long> {

	/**
	 * Get all <code>DirectRelationTypeEntity</code> from knowledge base. All
	 * entity are deactivated and occurrence is nulled.
	 * 
	 * @param kbName
	 * @return
	 */
	public Map<String, Long> getNulledDirectRelationTypes(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor);
	
	/**
	 * Get all <code>DirectRelationTypeEntity</code> from knowledge base.
	 * @param knowledgeBaseDescriptor
	 * @return
	 */
	public Collection<DirectRelationTypeEntity> getDirectRelationTypesFromKnowledgeBase(KnowledgeBaseDescriptor knowledgeBaseDescriptor);

	public void updateRelationsWeight(RelationTypeSetting relationType,
			KnowledgeBaseDescriptor defaultKnowledgeBase);

}
