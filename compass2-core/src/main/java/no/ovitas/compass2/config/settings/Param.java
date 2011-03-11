package no.ovitas.compass2.config.settings;


/**
 * @author csanyi
 * 
 */
public class Param implements Setting {

	// Attributes

	private String name;
	
	private String value;

	// Getter / setter methods

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	

	/**
	 * This is a getter method for name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * This is a setter method for name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}



	// Methods
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		return ind + "Param: name: " + name + " value: " + value + "\n";
	}
}
