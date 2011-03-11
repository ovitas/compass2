package no.ovitas.compass2.model.knowledgebase;

/**
 * Knowledge base type that refers to the transitive path calculation method
 * being used in connection with the knowledge base.
 * 
 * @author Csaba Daniel
 */
public enum KnowledgeBaseType {

	/**
	 * Knowledge base type where the specialization-generalization method is
	 * being used for calculating the transitive paths between non-adjacent
	 * topics.
	 */
	SPECGEN,

	/**
	 * Knowledge base type where the two-way method is being used for
	 * calculating the transitive paths between non-adjacent topics.
	 */
	TWOWAY;
}
