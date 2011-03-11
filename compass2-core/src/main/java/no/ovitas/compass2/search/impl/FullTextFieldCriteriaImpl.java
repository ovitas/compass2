/**
 * 
 */
package no.ovitas.compass2.search.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Collections;

import no.ovitas.compass2.model.ConnectingType;
import no.ovitas.compass2.model.FittingType;
import no.ovitas.compass2.model.MatchingType;
import no.ovitas.compass2.search.FullTextFieldCriteria;

/**
 * @author gyalai
 *
 */
public class FullTextFieldCriteriaImpl implements FullTextFieldCriteria {

	private Collection<String> terms;
	private final String fieldName;
	private ConnectingType connectionType;
	private FittingType fittingType;
	private MatchingType matchingType;
	private double boost;
	private boolean fuzzy;
	
	public FullTextFieldCriteriaImpl(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public void addTerms(Collection<String> terms) {
		if (this.terms == null) {
			this.terms = new HashSet<String>();
		}
		this.terms.addAll(terms);
	}

	@Override
	public void addTerm(String term) {
		if (terms == null) {
			terms = new HashSet<String>();
		}
		
		terms.add(term);
	}
	
	@Override
	public void setTerms(Collection<String> terms) {
		this.terms = terms;
	}
	
	@Override
	public Collection<String> getTerms() {
		return Collections.unmodifiableCollection(terms);
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}


	@Override
	public ConnectingType getConnectionType() {
		return connectionType;
	}

	@Override
	public void setConnectionType(ConnectingType connectionType) {
		this.connectionType = connectionType;
	}

	@Override
	public FittingType getFittingType() {
		return fittingType;
	}

	@Override
	public void setFittingType(FittingType fittingType) {
		this.fittingType = fittingType;
	}

	@Override
	public MatchingType getMatchingType() {
		return matchingType;
	}

	@Override
	public void setMatchingType(MatchingType matchingType) {
		this.matchingType = matchingType;
	}

	@Override
	public double getBoost() {
		return boost;
	}

	@Override
	public void setBoost(double boost) {
		this.boost = boost;
	}
	
	@Override
	public boolean isFuzzySearch() {
		return fuzzy;
	}
	
	@Override
	public void setFuzzySearch(boolean fuzzy) {
		this.fuzzy = fuzzy;
	}

}
