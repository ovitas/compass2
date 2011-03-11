/**
 * 
 */
package no.ovitas.compass2.search;

import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;

/**
 * @author gyalai
 *
 */
public interface TopicTreeFilter extends TopicFilter {

	/**
	 * Get knowledge base description.
	 * 
	 * @return
	 */
	KnowledgeBaseDescriptor getKnowledgeBaseDescriptor();

	/**
	 * Get direction.
	 * 
	 * @return
	 */
	RelationDirection getRelationDirection();

}
