package no.ovitas.compass2.model.knowledgebase;

/**
 * This class represents a relation type settings, that can be used for
 * configuring a relation type within a knowledge base.
 * 
 * @author Csaba Daniel
 */
public class RelationTypeSetting {

	private Long importId = null;
	private String externalId = null;
	private double weight = 0;
	private double generalizationWeight = 0;

	/**
	 * Construct a new setting with default values.
	 */
	public RelationTypeSetting() {
	}

	/**
	 * Create a new setting with the specified parameters.
	 * 
	 * @param importlId
	 *            the import id of the relation type to configure
	 * @param externallId
	 *            the external id of the relation type to configure
	 * @param weight
	 *            the weight to configure for the referred relation type
	 * @param generalizationWeight
	 *            the generalization weight to configure for the referred
	 *            relation type
	 */
	public RelationTypeSetting(Long importlId, String externalId, double weight,
			double generalizationWeight) {
		this.importId = importlId;
		this.externalId = externalId;
		this.weight = weight;
		this.generalizationWeight = generalizationWeight;
	}

	/**
	 * Get the import id of the relation type to be configured.
	 * 
	 * @return the import relation type id
	 */
	public Long getImportId() {
		return importId;
	}

	/**
	 * Set the external id of the relation type to be configured.
	 * 
	 * @param importId
	 *            the import relation type id to set
	 */
	public void setImportId(Long importId) {
		this.importId = importId;
	}
		

	/**
	 * Get the external id of the relation type to be configured.
	 * 
	 * @@return the external relation type id
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * This is a setter method for externalId.
	 * 
	 * @param externalId 
	 * 				the external relation type id to set
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * Get the weight of the relations to be configured for this type.
	 * 
	 * @return the relation weight of this type
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Set the weight of the relations to be configured for this type.
	 * 
	 * @param weight
	 *            the relation weight to set for this type
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * Get the generalization weight (weight in the reverse direction) of the
	 * relations to be configured for this type.
	 * 
	 * @return the relation generalization weight of this type
	 */
	public double getGeneralizationWeight() {
		return generalizationWeight;
	}

	/**
	 * Set the generalization weight (weight in the reverse direction) of the
	 * relations to be configured for this type.
	 * 
	 * @param generalizationWeight
	 *            the generalization weight to set for this type
	 */
	public void setGeneralizationWeight(double generalizationWeight) {
		this.generalizationWeight = generalizationWeight;
	}
}
