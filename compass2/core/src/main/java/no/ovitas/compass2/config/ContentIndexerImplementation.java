package no.ovitas.compass2.config;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class ContentIndexerImplementation {

	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());
	protected String className;

	// Getter / setter methods
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}


	// Constructors

	public ContentIndexerImplementation() {
		super();
	}

	// Methods
	
	public String dumpOut(String indent) {
		String toDumpOut = "\n" + indent;
		toDumpOut += "ContentIndexerImplementation: className: " + className;
		
		return toDumpOut;
	}
}
