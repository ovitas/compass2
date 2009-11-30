/**
 * 
 */
package no.ovitas.compass2.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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
		return round(weight, 3);
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
		return round(generalizationWeight, 3);
	}
	
	public void setGeneralizationWeight(double generalizationWeight) {
		this.generalizationWeight = generalizationWeight;
	}
	
	/**
	 * Round double
	 * @param number
	 * @param precision
	 * @return
	 */
	private double round(double number, int precision)
	{
	   double decimalShift = Math.pow(10,precision);
	   double intPart = Math.floor(number);
	   double fracPart = Math.floor((number - intPart) * decimalShift) / decimalShift;
	 
	   return intPart + fracPart;
	}

}
