package no.ovitas.compass2.model.knowledgebase;


public class KnowledgeBaseConfig {
	private boolean prefixMatch;
	private double thresholdWeight;
	private int maxTopicNumberToExpand;
	private int hopCount;
	private boolean generalization;
	private boolean treeSearch;

	public KnowledgeBaseConfig() {
	}

	public boolean isPrefixMatch() {
		return prefixMatch;
	}

	public void setPrefixMatch(boolean prefixMatch) {
		this.prefixMatch = prefixMatch;
	}

	public double getThresholdWeight() {
		return thresholdWeight;
	}

	public void setThresholdWeight(double thresholdWeight) {
		this.thresholdWeight = thresholdWeight;
	}

	public int getMaxTopicNumberToExpand() {
		return maxTopicNumberToExpand;
	}

	public void setMaxTopicNumberToExpand(int maxTopicNumberToExpand) {
		this.maxTopicNumberToExpand = maxTopicNumberToExpand;
	}

	public int getHopCount() {
		return hopCount;
	}

	public void setHopCount(int hopCount) {
		this.hopCount = hopCount;
	}

	public boolean isGeneralization() {
		return generalization;
	}

	public void setGeneralization(boolean generalization) {
		this.generalization = generalization;
	}

	public boolean isTreeSearch() {
		return treeSearch;
	}

	public void setTreeSearch(boolean treeSearch) {
		this.treeSearch = treeSearch;
	}
}