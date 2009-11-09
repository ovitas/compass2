/**
 * 
 */
package no.ovitas.compass2.model;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * @author magyar
 *
 */
public class RelationType implements Serializable {

	private String id;
	private String relationName;
	private double weight;
	private double generalizationWeight;
	private DecimalFormat decimalFormat;

	public RelationType() {
		decimalFormat.setMaximumFractionDigits(3);
	}
	
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	public double getWeight() {
		return Double.parseDouble(decimalFormat.format(weight));
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
		return Double.parseDouble(decimalFormat.format(generalizationWeight));
	}
	
	public void setGeneralizationWeight(double generalizationWeight) {
		this.generalizationWeight = generalizationWeight;
	}
}
