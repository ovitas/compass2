package no.ovitas.compass2.config;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class AssociationType extends BaseConfigItem {
	
	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());
	private double weightAhead;
	private double weightAback;

	// Getter / setter methods

	public void setWeightAhead(String weightAhead) {
		try{
			this.weightAhead = Double.parseDouble(weightAhead);
		}catch(NumberFormatException nfe){
			logger.error("Wrong weightAhead value: "+weightAhead);
		}
	}

	public double getWeightAhead() {
		return weightAhead;
	}

	public void setWeightAhead(double weightAhead) {
		this.weightAhead = weightAhead;
	}

	public double getWeightAback() {
		return weightAback;
	}

	public void setWeightAback(double weightAback) {
		this.weightAback = weightAback;
	}

	public void setWeightAback(String weightAback) {
		try{
			this.weightAback = Double.parseDouble(weightAback);
		}catch(NumberFormatException nfe){
			logger.error("Wrong weightAback value: "+weightAback);
		}
	}

	// Constructors

	public AssociationType() {
		super();
	}

	// Methods
	
	public String dumpOut(String indent) {
		return indent + " " + "AssociationType: id: " + id + ", name: " + name + ", weightAhead: " + weightAhead + ", weightAback: " + weightAback + "\n";
	}
}
