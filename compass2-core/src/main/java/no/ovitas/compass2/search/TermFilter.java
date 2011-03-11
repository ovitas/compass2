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
public interface TermFilter {
	
	/**
	 * Check term is filtered in the specified knowledge base and direction.
	 * 
	 * @param term
	 * @param knowledgeBaseDescriptor
	 * @param direction
	 * @return
	 */
	public boolean checkTerm(String term, KnowledgeBaseDescriptor knowledgeBaseDescriptor, RelationDirection direction);
	
}
