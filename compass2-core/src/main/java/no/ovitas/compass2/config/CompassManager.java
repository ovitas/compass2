package no.ovitas.compass2.config;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

import no.ovitas.compass2.Manager;
import no.ovitas.compass2.config.settings.SearchField;
import no.ovitas.compass2.config.settings.SearchOptions;
import no.ovitas.compass2.exception.ConfigParameterMissingException;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.exception.ImportException;
import no.ovitas.compass2.kb.KnowledgeBase;
import no.ovitas.compass2.model.DocumentDetails;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseType;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.RelationTypeSetting;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.search.FullTextQuery;
import no.ovitas.compass2.search.FullTextQueryResult;
import no.ovitas.compass2.search.FullTextTopicQuery;
import no.ovitas.compass2.search.QueryResult;
import no.ovitas.compass2.search.TopicListQueryResult;
import no.ovitas.compass2.search.TopicQuery;
import no.ovitas.compass2.search.TopicTreeQueryResult;

/**
 * The Interface CompassManager.
 */
public interface CompassManager extends Manager {

	/**
	 * Create a {@link TopicQuery}, with this <b>query</b> can search in
	 * Knowledge bases.
	 * 
	 * The <b>query</b> parameters are set up with defaults values.
	 * 
	 * @return the created <b>query</b>.
	 */
	public TopicQuery createTopicQuery();

	/**
	 * Create a {@link FullTextQuery}, with this <b>query</b> can search in
	 * FullText indexes.
	 * 
	 * The <b>query</b> parameters are set up with defaults values.
	 * 
	 * @return the created <b>query</b>.
	 */
	public FullTextQuery createFullTextQuery();

	/**
	 * Search in knowledge bases and the results are contained in tree format.
	 * 
	 * @param topicQuery
	 *            the specified query
	 * @return the query result
	 */
	TopicTreeQueryResult searchTree(TopicQuery topicQuery);

	/**
	 * Search in knowledge bases and the results are contained in list format.
	 * 
	 * @param topicQuery
	 *            the specified query
	 * @return the query result
	 */
	TopicListQueryResult search(TopicQuery topicQuery);

	/**
	 * Search in full text index.
	 * 
	 * @param fullTextQuery
	 *            the specified query
	 * @return the query result
	 */
	FullTextQueryResult search(FullTextQuery fullTextQuery);

	/**
	 * Search in full text index with topic extension.
	 * 
	 * @param fullTextQuery
	 *            the specified query
	 * @param fullTextTopicQuery
	 *            the specified topic extension. It can be null.
	 * @return the query result
	 */
	FullTextQueryResult search(FullTextQuery fullTextQuery,
			FullTextTopicQuery fullTextTopicQuery);

	/**
	 * Search in knowledge bases and full text index. If topic query search has
	 * result it will be converted to {@link FullTextTopicQuery} and then use
	 * {@link #search(FullTextQuery, FullTextTopicQuery)} method for full text
	 * search.
	 * 
	 * @param topicQuery
	 *            the specified topic query
	 * @param fullTextQuery
	 * @return
	 */
	QueryResult search(TopicQuery topicQuery, FullTextQuery fullTextQuery);

	/**
	 * Get spelling suggestion for the specified term.
	 * 
	 * @param term
	 *            the specified term
	 * 
	 * @return the correct term or the input term if it's correct or doesn't
	 *         have suggestion
	 */
	public String getSpellingSuggestion(String term);

	/**
	 * Get spelling suggestions for the specified term.
	 * 
	 * @param term
	 *            the specified term
	 * 
	 * @return the correct terms or the input term if it's correct or doesn't
	 *         have suggestion
	 */
	public List<String> getSpellingSuggestions(String term);

	/**
	 * Get suggestions for term.
	 * 
	 * @param term
	 *            the specified term
	 * @param maxSuggestionNumber
	 *            the max size of the result collection
	 * @return the suggestions
	 * @throws ConfigurationException
	 */
	Collection<String> getSuggestions(String term, int maxSuggestionNumber);

	/**
	 * Index knowledge base for suggestion. If knowledge base is uploaded then
	 * it is available for search with {@link #getSuggestions(String, int)}
	 * method.
	 * 
	 * @param knowledgeBase
	 *            the knowledge bases will be indexed
	 */
	public void indexKnowledgeBaseForSuggestion(KnowledgeBase knowledgeBase);

	/**
	 * Get stem of the term.
	 * 
	 * @param term
	 * @return the stemmed word or if the word can not stem then the word.
	 */
	public String getStem(String term);

	/**
	 * Get {@link DocumentDetails} for document which specified with id
	 * parameter.
	 * 
	 * @param id
	 *            the specified document id
	 * @return the document details.
	 * @throws ConfigurationException
	 */
	public DocumentDetails getDocument(String id) throws ConfigurationException;

	/**
	 * Get available search fields. Only can search in these fields in
	 * {@link FullTextQuery}.
	 * 
	 * @return the list of {@link SearchField}
	 */
	public List<SearchField> getAvailableSearchFields();

	/**
	 * Get default search configurations.
	 * 
	 * @return default search configurations
	 */
	public SearchOptions getDefaultSearchConfig();

	/**
	 * Get default search fields. This is the set of the
	 * {@link #getAvailableSearchFields()} result.
	 * 
	 * @return default search fields
	 */
	Collection<SearchField> getDefaultSearchFields();

	/**
	 * Get available uploads file type. Practicality the importable knowledge
	 * base types.
	 * 
	 * @return the file type names
	 */
	public Collection<String> getAvailableUploadTypes();

	/**
	 * Create new {@link KnowledgeBase} instance.
	 * 
	 * @param knowledgeBaseType
	 *            specified knowledge base type
	 * @return the created knowledge base.
	 */
	public KnowledgeBase newInstanceKnowledgeBase(
			KnowledgeBaseType knowledgeBaseType);

	/**
	 * Import knowledge base and start the knowledge base storing process.
	 * 
	 * @param file
	 *            the uploaded knowledge base file.
	 * @param type
	 *            the uploaded file type.
	 * @param knowledgeBase
	 *            the {@link KnowledgeBase} object which will contain the
	 *            imported knowledge base.
	 * @throws ImportException
	 * 
	 */
	public void importKnowledgeBase(String file, String type,
			KnowledgeBase knowledgeBase) throws ImportException;

	/**
	 * Get all available scopes from knowledge base which is under importing.
	 * 
	 * @return the available scopes.
	 * @throws ImportException
	 */
	public Collection<Scope> getImportedScopes() throws ImportException;

	/**
	 * Filter knowledge base is under importing. This is the second step of
	 * knowledge base storing process.
	 * 
	 * @param scopesImportID
	 *            the import id for that scopes which want to import. The other
	 *            scopes will be dropped
	 * @throws ImportException
	 */
	public void filterImportedKnowledgeBase(Collection<Long> scopesImportID)
			throws ImportException;

	/**
	 * Get relation types from knowledge base is under importing.
	 * 
	 * @return the relation types.
	 * @throws ImportException
	 */
	public Collection<RelationType> getImportedRelationTypes()
			throws ImportException;

	/**
	 * Store imported knowledge base. Before store it all relation types weight
	 * will be updated with specified values in the relationTypeSettings
	 * parameter.
	 * 
	 * @param relationTypeSettings
	 *            specified the weight of the relation types
	 * @return
	 */
	KnowledgeBase storeImportedKnowledgeBase(
			Collection<RelationTypeSetting> relationTypeSettings);

	/**
	 * List the descriptors of the available knowledge bases.
	 * 
	 * @return the collection of knowledge base descriptors
	 */
	public Collection<KnowledgeBaseDescriptor> listKnowledgeBases();

	/**
	 * Get the knowledge base descriptor of the knowledge base having the
	 * specified id.
	 * 
	 * @param id
	 *            the id of the knowledge base
	 * @return the corresponding knowledge base descriptor
	 */
	public KnowledgeBaseDescriptor getKnowledgeBase(long id);

	/**
	 * Get {@link Scope} from the specified knowledge base.
	 * 
	 * @param knowledgeBaseDescriptor
	 *            specified the knowledge base.
	 * @return the stored scopes.
	 */
	public Collection<Scope> getScopes(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor);

	/**
	 * Get relation types from the specified knowledge base.
	 * 
	 * @param knowledgeBaseDescriptor
	 *            the specified knowledge base.
	 * @return the relation types
	 */
	public Collection<RelationType> getRelationTypes(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor);

	/**
	 * Update relation types weights in the specified knowledge base.
	 * 
	 * @param relationType
	 *            this object contains the new weights
	 * @param knowledgeBaseDescriptor
	 *            the specified knowledge base
	 */
	public void updateRelationTypes(
			Collection<RelationTypeSetting> relationType,
			KnowledgeBaseDescriptor knowledgeBaseDescriptor);

	/**
	 * Get files from the specified import directory. This setting come from
	 * configuration XML.
	 * 
	 * @return the files from directory
	 */
	public Collection<File> getFilesFromKnowledgeBaseImportDirectory();

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
	public void indexDocuments(String configPath, String contentHandlerType);

	/**
	 * Read the configuration file again and refresh the managers.
	 * 
	 * @throws ConfigurationException
	 */
	public void reReadConfig() throws ConfigurationException;

}
