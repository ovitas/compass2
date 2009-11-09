/**
 * 
 */
package no.ovitas.compass2.model;

import java.io.Serializable;

/**
 * @author magyar
 *
 */
public class RelationType implements Serializable {

	private String id;
	private String relationName;
	private double weight;
	private double generalizationWeight;
	
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public double getGeneralizationWeight() {
		return generalizationWeight;
	}
	
	public void setGeneralizationWeight(double generalizationWeight) {
		this.generalizationWeight = generalizationWeight;
	}
}
