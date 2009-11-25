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
	protected String id;
	protected String name;

	// Getter / setter methods

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

	// Constructors

	public BaseConfigItem() {}

	// Methods
	
	public void dumpOut(){
		String toDumpOut = ".[name].="+name+"\n";
		toDumpOut +=".[id].="+id+"\n";
		logger.debug(toDumpOut); 
	}
	
	public String dumpOut(String indent){
		String ind = indent + " ";
		return  ind + "id: " +id + ", name: " + name + "\n"; 
	}

}
