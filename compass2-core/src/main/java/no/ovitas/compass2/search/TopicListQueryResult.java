/**
 * 
 */
package no.ovitas.compass2.search;

import java.util.Collection;

/**
 * @author gyalai
 *
 */
public interface TopicListQueryResult extends TopicQueryResult {
	
	/**
	 * Get knowledge base results. 
	 * 
	 * @return
	 */
	public Collection<KnowledgeBaseResult> getResult();
	
}
