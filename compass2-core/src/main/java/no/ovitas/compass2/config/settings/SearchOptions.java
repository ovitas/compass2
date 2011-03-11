package no.ovitas.compass2.config.settings;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class SearchOptions implements Setting {

	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());
	protected float resultThreshold;
	protected int maxNumberOfHits;
	private int hopCount;
	private boolean prefixMatch;
	private boolean treeSearch;
	private String tree;
	private boolean fuzzyMatch;
	private int maxTopicNumToExpand;
	private float expansionThreshold;
	
	
	// Getter / setter methods

	public float getResultThreshold() {
		return resultThreshold;
	}

	public void setResultThreshold(float resultThreshold) {
		this.resultThreshold = resultThreshold;
	}

	public void setMaxNumberOfHits(int maxNumberOfHits) {
		this.maxNumberOfHits = maxNumberOfHits;
	}

	public void setResultThreshold(String value) {
		try{
		this.resultThreshold = Float.parseFloat(value);
		}catch(Exception ex){
			logger.error("The resultThreshold value is not a number: "+value);
		}
	}

	// Constructors

	public SearchOptions() {}

	public int getMaxNumberOfHits() {
		return maxNumberOfHits;
	}

	public void setMaxNumberOfHits(String maxNumberOfHits) {
		try{
			this.maxNumberOfHits = Integer.parseInt(maxNumberOfHits);
		}catch(Exception ex){
			logger.error("The maxNumberOfHits value is not a number: "+maxNumberOfHits);
		}
	}

	// Methods
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		return ind + "SearchOptions: resultThreshold: " + resultThreshold + ", maxNumberOfHits: " + maxNumberOfHits + ", hopCount: " + hopCount + ", prefixMatch: " + prefixMatch + ", fuzzyMatch: " + fuzzyMatch + ", maxTopicNumtoExpand: " + maxTopicNumToExpand + ", expansionTreshold: " + expansionThreshold + ", treeSearch: " + treeSearch + " ,tree: " + tree + "\n";
	}

	public void setHopCount(int hopCount) {
		this.hopCount = hopCount;
	}

	public int getHopCount() {
		return hopCount;
	}

	public void setPrefixMatch(boolean prefixMatch) {
		this.prefixMatch = prefixMatch;
	}

	public boolean isPrefixMatch() {
		return prefixMatch;
	}

	public void setFuzzyMatch(boolean fuzzyMatch) {
		this.fuzzyMatch = fuzzyMatch;
	}

	public boolean isFuzzyMatch() {
		return fuzzyMatch;
	}

	public void setMaxTopicNumToExpand(int maxTopicNumToExpand) {
		this.maxTopicNumToExpand = maxTopicNumToExpand;
	}

	public int getMaxTopicNumToExpand() {
		return maxTopicNumToExpand;
	}

	/**
	 * This is a setter method for expansionThreshold.
	 * @param expansionThreshold the expansionThreshold to set
	 */
	public void setExpansionThreshold(float expansionThreshold) {
		this.expansionThreshold = expansionThreshold;
	}

	/**
	 * This is a getter method for expansionThreshold.
	 * @return the expansionThreshold
	 */
	public float getExpansionThreshold() {
		return expansionThreshold;
	}

	public boolean isTreeSearch() {
		return treeSearch;
	}

	public void setTreeSearch(boolean treeSearch) {
		this.treeSearch = treeSearch;
	}
	
	@Override
	public String toString() {
		return "Result: resultThreshold: " + resultThreshold + ", maxNumberOfHits: " + maxNumberOfHits + ", hopCount: " + hopCount + ", prefixMatch: " + prefixMatch + ", fuzzyMatch: " + fuzzyMatch + ", maxTopicNumtoExpand: " + maxTopicNumToExpand + ", expansionTreshold: " + expansionThreshold + ", generalization: " + treeSearch;
	}
	
	public String getTree() {
		return tree;
	}

	public Tree getTreeEnum() {
		if (tree != null)
			return Tree.valueOf(tree.toUpperCase());

		return null;
	}

	public void setTree(String tree) {
		this.tree = tree;
	}
}
