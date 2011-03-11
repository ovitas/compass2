/**
 * 
 */
package no.ovitas.compass2.search.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import no.ovitas.compass2.config.settings.SearchFieldString;
import no.ovitas.compass2.model.ConnectingType;
import no.ovitas.compass2.model.FittingType;
import no.ovitas.compass2.model.MatchingType;
import no.ovitas.compass2.search.FullTextFieldCriteria;
import no.ovitas.compass2.search.FullTextQuery;

/**
 * @author gyalai
 * 
 */
public class FullTextQueryImpl implements FullTextQuery {

	private Collection<FullTextFieldCriteria> criterias;
	private boolean fuzzySearch;
	private int maxNumberOfHits;
	private double resultThreshold;
	private Map<String, SearchFieldString> defaultFieldCriteriaConfig;

	public FullTextQueryImpl() {
		criterias = new LinkedList<FullTextFieldCriteria>();
	}

	public void setDefaultFieldCriteriaConfig(
			Map<String, SearchFieldString> defaultFieldCriteriaConfig) {
		this.defaultFieldCriteriaConfig = defaultFieldCriteriaConfig;
	}

	@Override
	public FullTextFieldCriteria createCriteria(String fieldName) {
		FullTextFieldCriteria criteria = new FullTextFieldCriteriaImpl(
				fieldName);

		SearchFieldString searchFieldString = defaultFieldCriteriaConfig
				.get(fieldName);
		if (searchFieldString != null) {
			criteria.setBoost(searchFieldString.getWeight());
			criteria.setFittingType(searchFieldString.getFitType());
			criteria.setMatchingType(searchFieldString.getMatchType());
		} else {
			criteria.setBoost(1);
			criteria.setFittingType(FittingType.PREFIX);
			criteria.setMatchingType(MatchingType.MATCH_ANY);
		}
		
		criteria.setConnectionType(ConnectingType.OR);
		
		criterias.add(criteria);

		return criteria;
	}
	
	@Override
	public Collection<FullTextFieldCriteria> getCriterias() {
		return Collections.unmodifiableCollection(criterias);
	}

	@Override
	public boolean isFuzzySearch() {
		return fuzzySearch;
	}

	@Override
	public void setFuzzySearch(boolean fuzzySearch) {
		this.fuzzySearch = fuzzySearch;
	}

	@Override
	public int getMaxNumberOfHits() {
		return maxNumberOfHits;
	}

	@Override
	public void setMaxNumberOfHits(int maxNumberOfHits) {
		this.maxNumberOfHits = maxNumberOfHits;
	}

	@Override
	public double getResultThreshold() {
		return resultThreshold;
	}

	@Override
	public void setResultThreshold(double resultThreshold) {
		this.resultThreshold = resultThreshold;
	}

}
