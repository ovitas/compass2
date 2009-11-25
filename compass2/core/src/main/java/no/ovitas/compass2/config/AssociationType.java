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

	public double getWeightAhead() {
		return weightAhead;
	}

	public void setWeightAhead(double weightAhead) {
		this.weightAhead = weightAhead;
	}

	public void setWeightAback(double weightAback) {
		this.weightAback = weightAback;
	}

	public void setWeightAhead(String weightAhead) {
		double doubleValue = 0.0;
		if(weightAhead != null) {
			try {
				doubleValue = Double.parseDouble(weightAhead);
			} catch (NumberFormatException nfe) {
				logger.error("Invalid weight ahead: " + weightAhead + ", " + nfe.getMessage());
			}
		}
		this.weightAhead = doubleValue;
	}

	public double getWeightAback() {
		return weightAback;
	}

	public void setWeightAback(String weightAback) {
		double doubleValue = 0.0;
		if(weightAback != null) {
			try {
				doubleValue = Double.parseDouble(weightAback);
			} catch (NumberFormatException nfe) {
				logger.error("Invalid weight aback: " + weightAback + ", " + nfe.getMessage());
			}
		}
		this.weightAback = doubleValue;
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
