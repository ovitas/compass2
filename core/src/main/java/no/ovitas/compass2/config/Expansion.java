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
	private double expansionThreshold;
	private int maxNumOfTopicToExpand;
	private int hopCount;
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

	public void setUseRandomWeight(boolean useRandomWeight) {
		this.useRandomWeight = useRandomWeight;
	}

	public void setExpansionThreshold(double expansionThreshold) {
		this.expansionThreshold = expansionThreshold;
	}

	public void setMaxNumOfTopicToExpand(int maxNumOfTopicToExpand) {
		this.maxNumOfTopicToExpand = maxNumOfTopicToExpand;
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
	
	public int getHopCount() {
		return hopCount;
	}

	public void setHopCount(int hopCount) {
		this.hopCount = hopCount;
	}
	
	public void setHopCount(String hopCount) {
		try{
			this.hopCount = Integer.parseInt(hopCount);
		}catch(NumberFormatException nfe){
			logger.error("Wrong hopCount value: "+hopCount);
		}
	}
	
	// Constructors

	public Expansion() {}

	// Methods
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "Expansion: useRandomWeight: " + useRandomWeight + ", expansionThreshold: " + expansionThreshold + ", maxNumOfTopicToExpand: " + maxNumOfTopicToExpand + ", hopCount: " + hopCount + "\n";
		toDumpOut += ind + associationTypes.dumpOut(ind);
		return toDumpOut;
	}
}
