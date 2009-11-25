package no.ovitas.compass2.config;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class Result {

	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());
	protected double resultThreshold;
	protected int maxNumberOfHits;
	
	// Getter / setter methods

	public double getResultThreshold() {
		return resultThreshold;
	}

	public void setResultThreshold(String value) {
		try{
		this.resultThreshold = Double.parseDouble(value);
		}catch(Exception ex){
			logger.error("The resultThreshold value is not a number: "+value);
		}
	}

	// Constructors

	public Result() {}

	public int getMaxNumberOfHits() {
		return maxNumberOfHits;
	}

	public void setMaxNumberOfHits(String maxNumberOfHits) {
		try{
		this.maxNumberOfHits = Integer.parseInt(maxNumberOfHits);
		}catch(Exception ex){
			logger.error("The maxNumberOfHits value is not a number: "+maxNumberOfHits);
		}
	}

	// Methods
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		return ind + "Result: resultThreshold: " + resultThreshold + ", maxNumberOfHits: " + maxNumberOfHits + "\n";
	}
}
