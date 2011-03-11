/**
 * 
 */
package no.ovitas.compass2.search.impl;

import java.util.Collection;
import java.util.HashSet;

import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.search.TopicCriteria;

/**
 * @author gyalai
 *
 */
public class TopicCriteriaImpl implements TopicCriteria {


	private Collection<Scope> scopes;
	private RelationDirection relationDirection;
	private KnowledgeBaseDescriptor knowledgeBaseDescriptor;

	@Override
	public KnowledgeBaseDescriptor getKnowledgeBase() {
		return knowledgeBaseDescriptor;
	}
	
	public void setKnowledgeBase(KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		this.knowledgeBaseDescriptor = knowledgeBaseDescriptor;	
	}

	@Override
	public Collection<Scope> getScopes() {
		return scopes;
	}

	@Override
	public void setScopes(Collection<Scope> scopes) {
		this.scopes = scopes;
	}

	@Override
	public RelationDirection getRelationDirection() {
		return relationDirection;
	}

	@Override
	public void setRelationDirection(RelationDirection relationDirection) {
		this.relationDirection = relationDirection;
	}

}
