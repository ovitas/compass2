package no.ovitas.compass2.config;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class AssociationTypes extends BaseConfigContainer<AssociationType> {

	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());

	// Constructors

	public AssociationTypes() {
		super();
	}

	// Methods

	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "AssociationTypes\n";
		toDumpOut += super.dumpOut(ind);
		
		return toDumpOut;
	}
	
}
