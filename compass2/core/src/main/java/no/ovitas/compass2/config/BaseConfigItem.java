/**
 * 
 */
package no.ovitas.compass2.config;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class BaseConfigItem {

	// Attributes
	private Logger logger = Logger.getLogger(this.getClass());
	protected String name;

	// Getter / setter methods

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Constructors

	public BaseConfigItem() {}

	// Methods

}
