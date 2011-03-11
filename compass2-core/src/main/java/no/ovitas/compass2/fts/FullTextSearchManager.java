package no.ovitas.compass2.fts;

import java.util.Set;

import no.ovitas.compass2.Manager;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.model.DocumentDetails;
import no.ovitas.compass2.search.FullTextQuery;
import no.ovitas.compass2.search.FullTextQueryResult;
import no.ovitas.compass2.search.FullTextTopicQuery;

/**
 * @author magyar
 * @version 1.0
 */
public interface FullTextSearchManager extends Manager {

	/**
	 * List all available content handler type.
	 * 
	 * @return content handler types.
	 * @throws ConfigurationException
	 */
	public Set<String> listAllContentHandlerType()
			throws ConfigurationException;

	/**
	 * Index documents under specified directory. The directory can be a single
	 * file then only it will be indexed.
	 * 
	 * @param reindex
	 *            if true update the stored document if it is existed
	 * @param depth
	 *            specified the max directory depth
	 * @param dir
	 *            specified the directory, it can be a single file
	 * @param contentHandlerType
	 *            specified the content handler type
	 */
	public void indexDocument(boolean reindex, int depth, String dir,
			String contentHandlerType);

	/**
	 * Index documents like {@link #indexDocument(boolean, int, String, String)}
	 * , but the parameters are in a configuration file.
	 * 
	 * @param configPath
	 *            the configuration file path
	 * @param contentHandlerType
	 *            specified the content handler type
	 */
	public void indexDocuments(String configurationFilePath,
			String contentHandlerType);

	/**
	 * Get {@link DocumentDetails} for document which specified with id
	 * parameter.
	 * 
	 * @param id
	 *            the specified document id
	 * @return the document details.
	 */
	public DocumentDetails getDocument(String id);

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