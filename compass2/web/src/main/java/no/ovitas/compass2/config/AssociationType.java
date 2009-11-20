package no.ovitas.compass2.config;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class AssociationType {
	
	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());
	private String id;
	private String name;
	private String weightAhead;
	private String weightAback;

	// Getter / setter methods

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public AssociationType() {}

	// Methods
}
