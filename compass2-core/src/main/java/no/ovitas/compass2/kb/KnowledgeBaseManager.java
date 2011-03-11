package no.ovitas.compass2.kb;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import no.ovitas.compass2.Manager;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseType;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.RelationTypeSetting;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.search.TopicListQueryResult;
import no.ovitas.compass2.search.TopicQuery;
import no.ovitas.compass2.search.TopicTreeQueryResult;

/**
 * @author magyar
 * @version 1.0
 */
public interface KnowledgeBaseManager extends Manager {

	/**
	 * Get the {@link RelationType} from specified knowledge base.
	 * 
	 * @param kb
	 * @return relation types
	 */
	public Collection<RelationType> getRelationTypes(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor);

	/**
	 * Get the {@link Scope} from specified knowledge base.
	 * 
	 * @param knowledgeBaseDescriptor
	 * @return scopes
	 */
	public Collection<Scope> getScopes(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor);

	/**
	 * Get all active stored knowledge bases. The knowledge base is not active
	 * while the store process is not finished.
	 * 
	 * @return knowledge bases
	 */
	public Collection<KnowledgeBaseDescriptor> listActiveKnowledgeBases();

	/**
	 * Delete specified knowledge base.
	 * 
	 * @param knowledgeBaseDescriptor
	 *            specified knowledge base
	 */
	public void deleteKnowledgeBase(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor);

	/**
	 * Save a new knowledge base or update exist. The update means all element
	 * will be removed except {@link RelationType}. Those relation types which
	 * not represented in the new knowledge base instance will be deactivated.
	 * 
	 * @param knowledgeBase
	 *            the knowledge base instance
	 */
	public void storeKnowledgeBase(KnowledgeBase knowledgeBase);

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
	 * Modify the {@link RelationType}'s weights.
	 * 
	 * @param relationTypeSetting
	 *            the relation type's weights
	 * @param knowledgeBaseDescriptor
	 *            specified knowledge base
	 */
	public void updateRelationTypeWeight(
			RelationTypeSetting relationTypeSetting,
			KnowledgeBaseDescriptor knowledgeBaseDescriptor);

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
	 * Search in knowledge bases and the results are contained in tree format.
	 * 
	 * @param topicQuery
	 *            the specified query
	 * @return the query result
	 */
	public TopicTreeQueryResult getExpansionTree(TopicQuery query);

	/**
	 * Search in knowledge bases and the results are contained in list format.
	 * 
	 * @param topicQuery
	 *            the specified query
	 * @return the query result
	 */
	public TopicListQueryResult getExpansionList(TopicQuery query);

	/**
	 * Get files from the specified import directory. This setting come from
	 * configuration XML.
	 * 
	 * @return the files from directory
	 */
	public Collection<File> getFilesFromImportDirectory();

	/**
	 * Set topicNameIndexer. This is important object because we search topics
	 * with this object and the result topics will be extended.
	 * 
	 * @param topicNameIndexer
	 *            the topic name indexer instance
	 */
	public void setTopicNameIndexer(TopicNameIndexer topicNameIndexer);

}