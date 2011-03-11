/**
 * 
 */
package no.ovitas.compass2.fts;

import no.ovitas.compass2.Manager;
import no.ovitas.compass2.search.FullTextFieldCriteria;
import no.ovitas.compass2.search.FullTextQuery;
import no.ovitas.compass2.search.FullTextTopicQuery;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;

/**
 * @class QueryBuilder
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.09.08.
 * 
 */
public interface QueryBuilder extends Manager {

	/**
	 * Get created query.
	 * 
	 * @return created query.
	 */
	public Query getCreatedQuery();

	/**
	 * Start create query. This is the first step.
	 */
	public void startCreateQuery();

	/**
	 * Add {@link FullTextFieldCriteria} to the processing query. If the
	 * {@link FullTextTopicQuery} contains either of the criteria terms then add
	 * its values to the query.
	 * 
	 * @param fullTextFieldCriteria
	 *            the fullTextCriteria
	 * @param fullTextTopicQuery
	 *            it can be null.
	 * @param fuzzy
	 *            if true then the these criteria processed as fuzzy query
	 * @param indexReader 
	 * 				an indexReader to index repository
	 */
	public void addCriteria(FullTextFieldCriteria fullTextFieldCriteria,
			FullTextTopicQuery fullTextTopicQuery, boolean fuzzy, IndexReader indexReader);
}
