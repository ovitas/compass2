/**
 * 
 */
package no.ovitas.compass2.fts;

import java.io.IOException;

import no.ovitas.compass2.Manager;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.model.Content;
import no.ovitas.compass2.model.DocumentDetails;
import no.ovitas.compass2.search.FullTextQuery;
import no.ovitas.compass2.search.FullTextQueryResult;
import no.ovitas.compass2.search.FullTextTopicQuery;

import org.apache.lucene.index.CorruptIndexException;

/**
 * @class FTSIndexer
 * @project compass2-fts-lucene
 * @author Milan Gyalai (gyalai.mail@thot-soft.com)
 * @version 1.0
 * @date 2010.07.28.
 * 
 */
public interface FTSIndexer extends Manager {

	/**
	 * Index the content.
	 * 
	 * @param content
	 *            the content
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	public void index(Content content);

	/**
	 * Close the indexer.
	 */
	public void closeIndexer();

	/**
	 * Open indexer. If parameter is true then delete index repository and
	 * create a new else all content will be added to the existed repository.
	 * 
	 * @param reWrite
	 * @throws ConfigurationException
	 */
	void openIndexer(boolean reWrite);

	/**
	 * Get the document from the index.
	 * 
	 * @param id
	 *            the document id
	 * @return the document details
	 */
	public DocumentDetails getDocument(String id);

	/**
	 * Update the indexed content with the new one. Two content is equals if
	 * theirs URI fields are same.
	 * 
	 * @param content
	 *            the new content.
	 * @throws ConfigurationException
	 */
	public void updateIndex(Content content);

	/**
	 * Delete document from index repository.
	 * 
	 * @param uri
	 *            the deleted documents URI field value. The URI field is the
	 *            unique field.
	 */
	public void deleteDocument(String uri);
	
	/**
	 * Search in full text index with topic extension.
	 * 
	 * @param fullTextQuery
	 *            the specified query
	 * @param fullTextTopicQuery
	 *            the specified topic extension. It can be null.
	 * @return the query result
	 * @throws ConfigurationException
	 */
	public FullTextQueryResult doSearch(FullTextQuery fullTextQuery,
			FullTextTopicQuery fullTextTopicQuery);

}
