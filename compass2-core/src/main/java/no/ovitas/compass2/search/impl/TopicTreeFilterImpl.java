/**
 * 
 */
package no.ovitas.compass2.search.impl;

import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;
import no.ovitas.compass2.search.TopicTreeFilter;

/**
 * @author gyalai
 *
 */
public class TopicTreeFilterImpl extends TopicFilterImpl implements
		TopicTreeFilter {

	private KnowledgeBaseDescriptor knowledgeBaseDescriptor;
	private RelationDirection direction;

	public TopicTreeFilterImpl(long topicId) {
		super(topicId);
	}

	public TopicTreeFilterImpl(long topicId,
			KnowledgeBaseDescriptor knowledgeBaseDescriptor,
			RelationDirection direction) {
		super(topicId);
		this.knowledgeBaseDescriptor = knowledgeBaseDescriptor;
		this.direction = direction;
	}



	@Override
	public KnowledgeBaseDescriptor getKnowledgeBaseDescriptor() {
		return knowledgeBaseDescriptor;
	}
	
	public void setKnowledgeBaseDescriptor(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		this.knowledgeBaseDescriptor = knowledgeBaseDescriptor;
	}

	@Override
	public RelationDirection getRelationDirection() {
		return direction;
	}
	
	public void setDirection(RelationDirection direction) {
		this.direction = direction;
	}

}
