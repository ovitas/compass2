package no.ovitas.compass2.model.knowledgebase;

import no.ovitas.compass2.model.Properties;

/**
 * A class implementing this interface represents a relation of a Compass2
 * knowledge base.
 * 
 * @author Csaba Daniel
 */
public interface Relation extends Properties {

	public long getId();
	
	/**
	 * Get the source topic of this relation.
	 * 
	 * @return the source topic
	 */
	public Topic getSource();

	/**
	 * Get the target topic of this relation.
	 * 
	 * @return the target topic
	 */
	public Topic getTarget();

	/**
	 * Get the type of this relation.
	 * 
	 * @return the name of the type
	 */
	public RelationType getType();
		

}
