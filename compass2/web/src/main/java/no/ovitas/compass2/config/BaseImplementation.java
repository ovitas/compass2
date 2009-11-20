package no.ovitas.compass2.config;

import org.apache.log4j.Logger;

public class BaseImplementation {
	
	// Attributes

	protected String className;
	protected ParamContainer params;
	
	// Getter / setter methods
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	public ParamContainer getParams() {
		return params;
	}

	public void setParams(ParamContainer params) {
		this.params = params;
	}
	
	// Constructors
	
	public BaseImplementation(){}
	
	// Methods
}
