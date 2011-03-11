/**
 * 
 */
package no.ovitas.compass2.kb;

import java.io.IOException;
import java.util.Collection;

import no.ovitas.compass2.Manager;
import no.ovitas.compass2.model.MatchTopicResult;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;

/**
 * @author gyalai
 * 
 */
public interface TopicNameIndexer extends Manager {

	/**
	 * Save knowledge base.
	 * 
	 * @param knowledgeBase
	 *            the knowledge base instance
	 * @throws IOException
	 */
	public void saveKnowledgeBase(KnowledgeBase knowledgeBase)
			throws IOException;

	/**
	 * Remove specified knowledge base.
	 * 
	 * @param knowledgeBaseDescriptor
	 *            the specified knowledge base
	 * @throws IOException
	 */
	public void removeKnowledgeBase(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor) throws IOException;

	/**
	 * Get the related results for the searched terms.
	 * 
	 * @param terms
	 *            the searched terms
	 * @param knowledgeBaseID
	 *            id of the knowledge base where we want to search
	 * @param maxTopicNumberToExpand
	 *            maximum number of the topics we want to get
	 * @param perfixMatch
	 *            specified the search type if it true the search will be prefix
	 *            search else whole word search
	 * @return the best topics
	 * @throws IOException
	 */
	public MatchTopicResult getRelatedResults(Collection<String> terms,
			long knowledgeBaseID, int maxTopicNumberToExpand,
			boolean perfixMatch) throws IOException;

}
