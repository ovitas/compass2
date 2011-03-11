/**
 * 
 */
package no.ovitas.compass2.search;

import java.util.Collection;

import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;
import no.ovitas.compass2.model.knowledgebase.Scope;

/**
 * @author gyalai
 *
 */
public interface TopicCriteria {

	/**
	 * Get knowledge base description. 
	 * 
	 * @return
	 */
	public KnowledgeBaseDescriptor getKnowledgeBase();
	
	/**
	 * Get scopes.
	 * @return
	 */
	public Collection<Scope> getScopes();
	
	/**
	 * Set scopes.
	 * 
	 * @param scopes
	 */
	public void setScopes(Collection<Scope> scopes);
	
	/**
	 * Get direction.
	 * 
	 * @return
	 */
	public RelationDirection getRelationDirection();
	
	/**
	 * Set direction.
	 * 
	 * @param relationDirection
	 */
	public void setRelationDirection(RelationDirection relationDirection);
	
}
