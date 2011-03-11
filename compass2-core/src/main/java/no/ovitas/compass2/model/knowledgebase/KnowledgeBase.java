package no.ovitas.compass2.model.knowledgebase;

import java.util.Set;


/**
 * A class implementing this interface represents a Compass2 knowledge base.
 * 
 * @author Csaba Daniel
 */
public interface KnowledgeBase {

	/**
	 * Get the topics of this knowledge base.
	 * 
	 * @return the list of topics of this knowledge base
	 */
	public Set<Topic> getTopics();

	/**
	 * Get the relations of this knowledge base.
	 * 
	 * @return the list of relations of this knowledge base
	 */
	public Set<Relation> getRelations();

	/**
	 * Get the set of the relation types occur in this knowledge base.
	 * 
	 * @return the set of relation types of this knowledge base
	 */
	public Set<RelationType> getRelationTypes();

	/**
	 * Get the set of the scopes occur in this knowledge base.
	 * 
	 * @return the set of scopes of this knowledge base
	 */
	public Set<Scope> getScopes();
	
	
	public KnowledgeBaseDescriptor getKnowledgeBaseDescriptor();
	
	public void setKnowledgeBaseDescriptor(KnowledgeBaseDescriptor knowledgeBaseDescriptor);

	/**
	 * Filters out every topic names and relations those scope is not the
	 * specified scope list, then the unused scopes are also being removed.
	 * 
	 * @param allowedScopes
	 *            the <code>Set</code> of scopes which we want to keep
	 */
	public void filterScopes(Set<Scope> allowedScopes);

	public void setRelationTypes(Set<RelationType> types);
}
