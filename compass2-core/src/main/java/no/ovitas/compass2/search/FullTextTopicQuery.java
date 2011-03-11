/**
 * 
 */
package no.ovitas.compass2.search;

/**
 * @author gyalai
 *
 */
public interface FullTextTopicQuery {

	/**
	 * Get topic criteria which is founded with the specified term.
	 * 
	 * @param term
	 * @return
	 */
	public FullTextTopicCriteria getFullTextTopicCriteria(String term);
}
