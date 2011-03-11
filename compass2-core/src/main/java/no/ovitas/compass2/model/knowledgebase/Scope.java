package no.ovitas.compass2.model.knowledgebase;

import no.ovitas.compass2.model.Properties;

/**
 * A class implementing this interface represents a scope in which a topic name
 * exist in a Compass2 knowledge base.
 * 
 * @author Csaba Daniel
 */
public interface Scope extends Properties {

	
	public long getId();
	
	/**
	 * Get the external id of the scope that identifies the scope in the
	 * external knowledge base definition.
	 * 
	 * @return the external scope id
	 */
	public String getExternalId();

	/**
	 * Get the display name of the scope.
	 * 
	 * @return the display name of the scope
	 */
	public String getDisplayName();

	/**
	 * Set the display name of the scope.
	 * 
	 * @param displayName
	 */
	public void setDisplayName(String displayName);

	/**
	 * Add display name to the current scope. If it has got a display name then
	 * concatenate them.
	 * 
	 * @param displayName
	 */
	public void addDisplayName(String displayName);

}
