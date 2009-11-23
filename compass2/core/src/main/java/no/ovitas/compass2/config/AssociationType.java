package no.ovitas.compass2.config;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class AssociationType extends BaseConfigItem {
	
	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());
	private String weightAhead;
	private String weightAback;

	// Getter / setter methods

	public String getWeightAhead() {
		return weightAhead;
	}

	public void setWeightAhead(String weightAhead) {
		this.weightAhead = weightAhead;
	}

	public String getWeightAback() {
		return weightAback;
	}

	public void setWeightAback(String weightAback) {
		this.weightAback = weightAback;
	}

	// Constructors

	public AssociationType() {
		super();
	}

	// Methods
}
