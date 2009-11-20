package no.ovitas.compass2.config;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class Expansion {
	
	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());
	private String useRandomWeight;
	private String prefixMatch;
	private String exansionThreshold;
	private String maxNumOfTopicToExpand;
	private ArrayList<AssociationType> associationTypes;

	// Getter / setter methods

	public String getUseRandomWeight() {
		return useRandomWeight;
	}

	public void setUseRandomWeight(String useRandomWeight) {
		this.useRandomWeight = useRandomWeight;
	}

	public String getPrefixMatch() {
		return prefixMatch;
	}

	public void setPrefixMatch(String prefixMatch) {
		this.prefixMatch = prefixMatch;
	}

	public String getExansionThreshold() {
		return exansionThreshold;
	}

	public void setExansionThreshold(String exansionThreshold) {
		this.exansionThreshold = exansionThreshold;
	}

	public String getMaxNumOfTopicToExpand() {
		return maxNumOfTopicToExpand;
	}

	public void setMaxNumOfTopicToExpand(String maxNumOfTopicToExpand) {
		this.maxNumOfTopicToExpand = maxNumOfTopicToExpand;
	}

	public ArrayList<AssociationType> getAssociationTypes() {
		return associationTypes;
	}

	public void setAssociationTypes(ArrayList<AssociationType> associationTypes) {
		this.associationTypes = associationTypes;
	}

	// Constructors

	public Expansion() {}

	// Methods
}
