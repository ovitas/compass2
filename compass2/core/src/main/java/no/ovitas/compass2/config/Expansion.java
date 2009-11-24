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
	private boolean useRandomWeight;
	private boolean prefixMatch;
	private double expansionThreshold;
	private int maxNumOfTopicToExpand;
	private AssociationTypes associationTypes;

	// Getter / setter methods

	public boolean getUseRandomWeight() {
		return useRandomWeight;
	}

	public void setUseRandomWeight(String useRandomWeight) {
		if(useRandomWeight != null && useRandomWeight.equals("yes")) {
			this.useRandomWeight = true;
		} else {
			this.useRandomWeight = false;
		}
	}

	public boolean getPrefixMatch() {
		return prefixMatch;
	}

	public void setPrefixMatch(String prefixMatch) {
		if(prefixMatch != null && prefixMatch.equals("yes")) {
			this.prefixMatch = true;
		} else {
			this.prefixMatch = false;
		}
	}

	public double getExpansionThreshold() {
		return expansionThreshold;
	}

	public void setExpansionThreshold(String exansionThreshold) {
		try{
			this.expansionThreshold = Double.parseDouble(exansionThreshold);
		}catch(NumberFormatException nfe){
			logger.error("Wrong expansionThreshold value: "+exansionThreshold);
		}
	}

	public int getMaxNumOfTopicToExpand() {
		return maxNumOfTopicToExpand;
	}

	public void setMaxNumOfTopicToExpand(String maxNumOfTopicToExpand) {
		try{
			this.maxNumOfTopicToExpand = Integer.parseInt(maxNumOfTopicToExpand);
		}catch(NumberFormatException nfe){
			logger.error("Wrong maxNumOfTopicToExpand value: "+maxNumOfTopicToExpand);
		}
	}
	
	public AssociationTypes getAssociationTypes() {
		return associationTypes;
	}

	public void setAssociationTypes(AssociationTypes associationTypes) {
		this.associationTypes = associationTypes;
	}
	
	// Constructors

	public Expansion() {
		associationTypes = new AssociationTypes();
	}

	// Methods
}
