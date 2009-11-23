package no.ovitas.compass2.config;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class AssociationTypes {

	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());
	private ArrayList<AssociationType> associationTypes;

	// Getter / setter methods

	public ArrayList<AssociationType> getKnowledgeBases() {
		return associationTypes;
	}

	public void setKnowledgeBases(ArrayList<AssociationType> knowledgeBases) {
		this.associationTypes = associationTypes;
	}

	// Constructors

	public AssociationTypes() {
		associationTypes = new ArrayList<AssociationType>();
	}

	// Methods
	
	public void addAssociationType(AssociationType associationType) {
		this.associationTypes.add(associationType);
	}
}
