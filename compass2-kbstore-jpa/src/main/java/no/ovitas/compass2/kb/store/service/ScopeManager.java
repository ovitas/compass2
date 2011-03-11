/**
 * 
 */
package no.ovitas.compass2.kb.store.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import no.ovitas.compass2.kb.store.model.KnowledgeBaseEntity;
import no.ovitas.compass2.kb.store.model.ScopeEntity;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.Scope;

/**
 * @class ScopeManager
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.30.
 * 
 */
public interface ScopeManager extends GenericManager<ScopeEntity, Long> {

	/**
	 * Add scopes to knowledge base. Only add that scope which does not exist
	 * yet.
	 * 
	 * @param scopes
	 */
	Map<Scope, Long> addScopes(Set<Scope> scopes, KnowledgeBaseDescriptor knowledgeBaseDescriptor);

	Collection<Scope> getScopesInKnowledgeBase(
			KnowledgeBaseDescriptor KnowledgeBaseDescriptor);

}
