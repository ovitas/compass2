/**
 * 
 */
package no.ovitas.compass2.search;

import java.util.Collection;

import no.ovitas.compass2.model.ConnectingType;
import no.ovitas.compass2.model.FittingType;
import no.ovitas.compass2.model.FullTextFieldType;
import no.ovitas.compass2.model.MatchingType;

/**
 * @author gyalai
 * 
 */
public interface FullTextFieldCriteria {

	/**
	 * Add search terms to full text search criteria.
	 * 
	 * @param terms
	 *            the searched terms
	 */
	public void addTerms(Collection<String> terms);

	/**
	 * Add search term to full text search criteria.
	 * 
	 * @param term
	 *            the searched term
	 */
	public void addTerm(String term);

	/**
	 * Set searching terms.
	 * 
	 * @param terms
	 */
	public void setTerms(Collection<String> terms);

	/**
	 * Get search terms.
	 * 
	 * @return with terms in unmodifiable collection
	 */
	public Collection<String> getTerms();

	/**
	 * Get field name where the criteria will be search.
	 * 
	 * @return
	 */
	public String getFieldName();

	/**
	 * Get connection type between fields.
	 * 
	 * @return
	 */
	public ConnectingType getConnectionType();

	/**
	 * Set connection type between fields.
	 * 
	 * @param connectionType
	 */
	public void setConnectionType(ConnectingType connectionType);

	/**
	 * Get fitting type.
	 * 
	 * @return
	 */
	public FittingType getFittingType();

	/**
	 * Set fitting type.
	 * 
	 * @param fittingType
	 */
	public void setFittingType(FittingType fittingType);

	/**
	 * Get matching type between terms.
	 * 
	 * @return
	 */
	public MatchingType getMatchingType();

	/**
	 * Set matching type between terms.
	 * 
	 * @param matchingType
	 */
	public void setMatchingType(MatchingType matchingType);

	/**
	 * Get is fuzzy search.
	 * 
	 * @return
	 */
	public boolean isFuzzySearch();

	/**
	 * Set is fuzzy search.
	 * 
	 * @param fuzzy
	 */
	public void setFuzzySearch(boolean fuzzy);

	/**
	 * Get field boost.
	 * 
	 * @return
	 */
	public double getBoost();

	/**
	 * Set field boost.
	 * 
	 * @param boost
	 */
	public void setBoost(double boost);

}
