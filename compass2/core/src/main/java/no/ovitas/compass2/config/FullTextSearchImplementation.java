package no.ovitas.compass2.config;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class FullTextSearchImplementation  {

	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());
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
	
	public FullTextSearchImplementation(){
		super();
	}	
	
	// Methods
}
