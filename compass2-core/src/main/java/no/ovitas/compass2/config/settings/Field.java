/**
 * 
 */
package no.ovitas.compass2.config.settings;

import no.ovitas.compass2.model.FieldType;

/**
 * @author gyalai
 *
 */
public class Field implements Setting{

	private String name;
	private String indexed;
	private String stored;
	private String type;
	
	
	
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getIndexed() {
		return indexed;
	}



	public void setIndexed(String indexed) {
		this.indexed = indexed;
	}



	public String getStored() {
		return stored;
	}



	public void setStored(String stored) {
		this.stored = stored;
	}



	public String dumpOut(String indent) {
		return indent + " " + "Field: name: " + name + ", indexed: " + indexed + ", stored: " + stored + ", type: " + type+ "\n";
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getType() {
		return type;
	}
	
	public FieldType getFieldType() {
		String type2 = type.toUpperCase();
		
		return FieldType.valueOf(type2);
	}
}
