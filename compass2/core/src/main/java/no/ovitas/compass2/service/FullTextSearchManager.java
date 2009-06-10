package no.ovitas.compass2.service;

import java.util.List;
import java.util.Set;

import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.model.DocumentDetails;
import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.model.Topic;

/**
 * @author magyar
 * @version 1.0
 */
public interface FullTextSearchManager {

	public void setConfiguration(ConfigurationManager manager);	
	/**
	 * 
	 * @param reindex
	 * @param depth
	 * @param uri
	 * @throws ConfigurationException 
	 */
	public void addDocument(boolean reindex, int depth, String dir) throws ConfigurationException;

	/**
	 * 
	 * @param xpathExpression
	 * @param fieldName
	 */
	public void addDocumentField(String xpathExpression, String fieldName);

	/**
	 * 
	 * @param docId
	 * @param uri
	 */
	public void deleteDocument(String docId, String uri);

	/**
	 * 
	 * @param searchTopics
	 */
	public List<Hit> doSearch(List<Set<String>> searchTopics, int pageNum);

	/**
	 * 
	 * @param search
	 */
	public List<Hit> doSearch(String search, int pageNum);
	
    /**
     * Returns a concrete document.
     * @param id
     * @return DocumentDetails
     */
	public DocumentDetails getDocument(String id);	

}