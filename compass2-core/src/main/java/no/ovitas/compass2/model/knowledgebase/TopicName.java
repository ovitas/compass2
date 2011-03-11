package no.ovitas.compass2.model.knowledgebase;

import no.ovitas.compass2.model.Properties;

/**
 * A class implementing this interface represents a topic name of a Compass2
 * knowledge base.
 * 
 * @author Csaba Daniel
 */
public interface TopicName extends Properties {

	/**
	 * Get the string representation of the topic name.
	 * 
	 * @return the topic name string
	 */
	public String getName();

	/**
	 * Set the string representation of the topic name.
	 * 
	 * @param name
	 *            the topic name to set
	 */
	public void setName(String name);

	/**
	 * Get the scope in which the topic name is interpreted.
	 * 
	 * @return the interpreter scope
	 */
	public Scope getScope();

	public long getId();

}
