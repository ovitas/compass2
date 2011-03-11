package no.ovitas.compass2.model.knowledgebase;

/**
 * Enumeration of the directions of the direct relations.
 * 
 * @author Csaba Daniel
 */
public enum RelationDirection {

	/**
	 * Specialization, the direction should be considered as from start topic to
	 * end topic.
	 */
	SPEC,

	/**
	 * Generalization, the direction should be considered as from end topic to
	 * star topic.
	 */
	GEN;
}
