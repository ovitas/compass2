/**
 * 
 */
package no.ovitas.compass2.search;

import java.util.Collection;

import no.ovitas.compass2.model.FullTextFieldType;

/**
 * @author gyalai
 * 
 */
public interface FullTextQuery {

	/**
	 * Create {@link FullTextFieldCriteria} with field name the criteria
	 * settings will be set up with default.
	 * 
	 * @param fieldName
	 *            the specified field name
	 * @return
	 */
	public FullTextFieldCriteria createCriteria(String fieldName);

	/**
	 * Get all criterias.
	 * 
	 * @return criterias in unmodifiable collection.
	 */
	public Collection<FullTextFieldCriteria> getCriterias();

	/**
	 * Get is fuzzy search.
	 * 
	 * @return
	 */
	public boolean isFuzzySearch();

	/**
	 * Set is fuzzy search.
	 * 
	 * @param fuzzySearch
	 */
	public void setFuzzySearch(boolean fuzzySearch);

	/**
	 * Get max number of hits.
	 * 
	 * @return
	 */
	public int getMaxNumberOfHits();

	/**
	 * Set max number of hits.
	 * 
	 * @param maxNumberOfHits
	 */
	public void setMaxNumberOfHits(int maxNumberOfHits);

	/**
	 * Get result threshold.
	 * 
	 * @return
	 */
	public double getResultThreshold();

	/**
	 * Set result threshold.
	 * 
	 * @param resultThreshold
	 */
	public void setResultThreshold(double resultThreshold);

}
