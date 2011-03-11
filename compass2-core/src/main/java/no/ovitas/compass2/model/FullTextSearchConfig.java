/**
 * 
 */
package no.ovitas.compass2.model;

/**
 * @class SearchConfig
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.02.
 * 
 */
public class FullTextSearchConfig {

	private boolean fuzzySearch;

	private int maxNumberOfHits;

	private double resultThreshold;

	public FullTextSearchConfig(){
		
	}

	public void setFuzzySearch(boolean fuzzySearch) {
		this.fuzzySearch = fuzzySearch;
	}
	

	public boolean isFuzzySearch() {
		return fuzzySearch;
	}

	public void setMaxNumberOfHits(int maxNumberOfHits) {
		this.maxNumberOfHits = maxNumberOfHits;
	}

	public int getMaxNumberOfHits() {
		return maxNumberOfHits;
	}
		
	public void setResultThreshold(double resultThreshold) {
		this.resultThreshold = resultThreshold;
	}
	
	public double getResultThreshold() {
		return resultThreshold;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "SearchConfig: fuzzy search: " + true + ", max number of hits: "
				+ getMaxNumberOfHits() + ", result treshold: " + getResultThreshold();
	}

}
