/**
 * 
 */
package no.ovitas.compass2.search;

/**
 * @author gyalai
 * 
 */
public interface QueryResult {

	/**
	 * Get full text query result.
	 * 
	 * @return
	 */
	public FullTextQueryResult getFullTextQueryResult();

	/**
	 * Get topic query result. If the {@link TopicQuery} was tree search then
	 * return null.
	 * 
	 * @return
	 */
	public TopicListQueryResult getTopicListQueryResult();

	/**
	 * Get topic query result. If the {@link TopicQuery} didn't be tree search then
	 * return null.
	 * 
	 * @return
	 */
	public TopicTreeQueryResult getTopicTreeQueryResult();

}
