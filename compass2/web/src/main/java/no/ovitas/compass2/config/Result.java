package no.ovitas.compass2.config;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class Result {

	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());
	protected String resultThreshold;

	// Getter / setter methods

	public String getResultThreshold() {
		return resultThreshold;
	}

	public void setResultThreshold(String value) {
		this.resultThreshold = value;
	}

	// Constructors

	public Result() {}

	// Methods
}
