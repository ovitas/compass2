package no.ovitas.compass2.model.knowledgebase;

import no.ovitas.compass2.model.Properties;

/**
 * A class implementing this interface represents a relation type of a Compass2
 * knowledge base.
 * 
 * @author Csaba Daniel
 */
public interface RelationType extends Properties {

	/**
	 * Get the external id of the relation type that identifies the relation
	 * type in the external knowledge base definition.
	 * 
	 * @return the external relation type id
	 */
	public String getExternalId();
	
	/**
	 * Set the external id of the relation type that identifies the relation
	 * type in the external knowledge base definition.
	 * 
	 * @param externalId
	 */
	public void setExternalId(String externalId);

	/**
	 * Get the display name of the relation type.
	 * 
	 * @return the display name of the relation type
	 */
	public String getDisplayName();

	/**
	 * Set the display name of the relation type.
	 * 
	 * @param displayName
	 */
	public void setDisplayName(String displayName);
	
	/**
	 * Add display name to the current relation type. If it has got a display name then
	 * concatenate them.
	 * 
	 * @param displayName
	 */
	public void addDisplayName(String displayName);
	
	/**
	 * Get the weight of the relations of this type.
	 * 
	 * @return the relation weight of this type
	 */
	public double getWeight();
	
	/**
	 * Set the weight of the relations of this type.
	 * 
	 * @param weight
	 */
	public void setWeight(double weight);

	/**
	 * Get the generalization weight (weight in the reverse direction) of the
	 * relations of this type.
	 * 
	 * @return the relation generalization weight of this type
	 */
	public double getGeneralizationWeight();
	
	/**
	 * Set the generalization weight (weight in the reverse direction) of the
	 * relations of this type.
	 * 
	 * @param generalizationWeight
	 */
	public void setGeneralizationWeight(double generalizationWeight);

	/**
	 * Get the number of occurrence of the relations of this type within the
	 * containing knwoledge base.
	 * 
	 * @return the number of occurrence of this type relations
	 */
	public int getOccurrence();

	public void makeUnique();
	
}
