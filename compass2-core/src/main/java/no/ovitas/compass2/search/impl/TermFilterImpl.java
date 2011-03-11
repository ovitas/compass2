/**
 * 
 */
package no.ovitas.compass2.search.impl;

import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;
import no.ovitas.compass2.search.TermFilter;

/**
 * @author gyalai
 * 
 */
public class TermFilterImpl implements TermFilter {

	private final KnowledgeBaseDescriptor knowledgeBaseDescriptor;

	private final RelationDirection relationDirection;

	private final String term;

	public TermFilterImpl(KnowledgeBaseDescriptor knowledgeBaseDescriptor,
			RelationDirection relationDirection, String term) {
		this.knowledgeBaseDescriptor = knowledgeBaseDescriptor;
		this.relationDirection = relationDirection;
		this.term = term;
	}

	@Override
	public boolean checkTerm(String term,
			KnowledgeBaseDescriptor knowledgeBaseDescriptor,
			RelationDirection direction) {

		if (knowledgeBaseDescriptor.getId() == this.knowledgeBaseDescriptor
				.getId() && term.equals(this.term)) {
			if ((direction != null && direction.equals(relationDirection))
					|| direction == null && relationDirection == null)
				return true;
		}

		return false;
	}

}
