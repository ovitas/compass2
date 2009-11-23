package no.ovitas.compass2.config;

/**
 * @author csanyi
 * 
 */
public class Param extends BaseConfigItem {

	// Attributes

	protected String value;

	// Getter / setter methods

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	// Constructors

	public Param() {
		super();
	}

	// Methods
}
