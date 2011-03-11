package no.ovitas.compass2.model.knowledgebase;

import java.util.Set;

import no.ovitas.compass2.model.Properties;

/**
 * A class implementing this interface represents a topic of a Compass2
 * knowledge base.
 * 
 * @author Csaba Daniel
 */
public interface Topic extends Properties {

	public long getId();
	
	/**
	 * Get the external id of the topic that identifies the topic in the
	 * external knowledge base definition.
	 * 
	 * @return the external topic id
	 */
	public String getExternalId();

	/**
	 * Get the display name of the topic.
	 * 
	 * @return the display name of the topic
	 */
	public String getDisplayName();

	/**
	 * Get the names of this topic.
	 * 
	 * @return the topic names
	 */
	public Set<TopicName> getNames();
	

	/**
	 * Get the relations of this topic.
	 * 
	 * @return the topic relations.
	 */
	public Set<Relation> getRelations();
	
}
