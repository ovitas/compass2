/**
 * 
 */
package no.ovitas.compass2.kb;

import java.util.Collection;

import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseType;
import no.ovitas.compass2.model.knowledgebase.Relation;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.model.knowledgebase.Topic;
import no.ovitas.compass2.model.knowledgebase.TopicName;

/**
 * @class EntityCreatorFactory
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.09.15.
 * 
 */
public interface KnowledgeBase {

	/**
	 * Check Knowledge base model is closed.
	 * 
	 * @return
	 */
	public boolean isClosedModel();

	/**
	 * Create Topic type object.
	 * 
	 * @return
	 */
	public Topic createTopic(String externalId);

	/**
	 * Create Scope type object.
	 * 
	 * @return
	 */
	public Scope createScope(String externalId);

	/**
	 * Create TopicName type object.
	 * 
	 * @return
	 */
	public TopicName createTopicName();

	/**
	 * Create Relation type object.
	 * 
	 * @return
	 */
	public Relation createRelation();

	/**
	 * Create RelationType type object.
	 * 
	 * @return
	 */
	public RelationType createRelationType(String externalId);

	/**
	 * Create a new default knowledge base with the specified parameters.
	 * Knowledge base <code>description</code> and <code>type</code> are being
	 * set to <code>null</code>.
	 * 
	 * @param displayName
	 *            display name of the knowledge base
	 * @return the descriptor of the new knowledge base
	 */
	public KnowledgeBaseDescriptor createDefaultKnowledgeBase(String displayName);

	/**
	 * Create a new default knowledge base with the specified parameters.
	 * Knowledge base <code>type</code> is being set to <code>null</code>.
	 * 
	 * @param displayName
	 *            display name of the knowledge base
	 * @param description
	 *            description of the knowledge base
	 * @return the descriptor of the new knowledge base
	 */
	public KnowledgeBaseDescriptor createDefaultKnowledgeBase(
			String displayName, String description);

	/**
	 * Create a new default knowledge base with the specified parameters.
	 * 
	 * @param displayName
	 *            display name of the knowledge base
	 * @param description
	 *            description of the knowledge base
	 * @param type
	 *            type of the knowledge base
	 * @return the descriptor of the new knowledge base
	 */
	public KnowledgeBaseDescriptor createDefaultKnowledgeBase(
			String displayName, String description, KnowledgeBaseType type);

	void removeTopic(long importId);

	void removeScope(long importId);

	void removeTopicName(long importId);

	void removeRelation(long importId);

	void removeRelationType(long importId);

	void addTopicsToRelation(Topic topic, Topic target, Relation relation);

	void addTopicsToRelation(long startTopicID, long endTopicID, long relationId);

	void addRelationType(Relation relation, RelationType relationType);

	void addRelationType(long relationID, long relationTypeID);

	void addTopicName(Topic topic, TopicName topicName);

	void addTopicName(long topicID, long topicNameID);

	void addScope(TopicName topicName, Scope scope);

	void addScope(long topicNameID, long scopeID);

	Collection<Topic> getTopics();

	Collection<Scope> getScopes();

	Collection<RelationType> getRelationTypes();

	Collection<Relation> getRelations();

	Topic findTopicByExternalId(String externalId);

	Scope findScopeByExternalId(String externalId);

	RelationType findRelationTypeByExternalId(String externalId);

	Topic findTopic(long topicId);

	Scope findScope(long scopeId);

	RelationType findRelationType(long relationTypeId);

	Relation findRelation(long relationId);

	TopicName findTopicName(long topicNameId);

	void finishImport();

	/**
	 * Filter out every topic names and relations those scope is not the
	 * specified scope list and mark them for removal.
	 * 
	 * @param allowedScopes
	 *            the collection of allowed scope id-s
	 */
	public void filterScopes(Collection<Long> allowedScopes);

	/**
	 * Enforce integrity validation over the model and clean up every elements
	 * those fail to pass the validation (e.g. topics without topic names, topic
	 * names with disallowed scopes, relations with missing topic references
	 * etc.).
	 */
	public void cleanUp();

	public KnowledgeBaseDescriptor getKnowledgeBaseDescriptor();

	public KnowledgeBaseDescriptor loadDefaultKnowledgeBase(Long knowledgeBaseId);

	/**
	 * This method return true if this knowledge base will be had to upload to
	 * the suggestion provider.
	 * 
	 * @return
	 */
	public boolean isSuggestionUpload();
	
	/**
	 * Set knowledge base
	 * @param upload
	 */
	public void setSuggestionUpload(boolean upload);
	
}
