/**
 * 
 */
package no.ovitas.compass2.search;

import java.util.Collection;

/**
 * @author gyalai
 *
 */
public interface TopicTreeQueryResult extends TopicQueryResult {
	
	/**
	 * GEt knowledge base results.
	 * 
	 * @return
	 */
	public Collection<KnowledgeBaseTreeResult> getResult();
	
}
