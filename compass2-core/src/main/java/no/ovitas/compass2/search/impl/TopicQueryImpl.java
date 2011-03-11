/**
 * 
 */
package no.ovitas.compass2.search.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import no.ovitas.compass2.config.factory.CompassManagerFactory;
import no.ovitas.compass2.config.settings.KnowldegeBaseSetting;
import no.ovitas.compass2.config.settings.KnowledgeBaseSettingPlugin;
import no.ovitas.compass2.config.settings.ScopeSetting;
import no.ovitas.compass2.config.settings.SearchOptions;
import no.ovitas.compass2.config.settings.SearchSettings;
import no.ovitas.compass2.config.settings.Tree;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.search.TopicCriteria;
import no.ovitas.compass2.search.TopicQuery;

/**
 * @author gyalai
 * 
 */
public class TopicQueryImpl implements TopicQuery {

	private Collection<TopicCriteria> criterias;
	private Map<String, String> terms = new HashMap<String, String>(0);
	private boolean prefixMatch;
	private double thresholdWeight;
	private int maxTopicNumberToExpand;
	private int hopCount;
	private boolean treeSearch;

	private KnowledgeBaseSettingPlugin knowledgeBase;
	private SearchOptions searchOptions;

	public TopicQueryImpl() {
		criterias = new LinkedList<TopicCriteria>();
	}

	@Override
	public void addTerms(Collection<String> terms) {

		for (String term : terms) {
			this.terms.put(term, term);
		}

	}

	@Override
	public void addTerm(String term) {
		this.terms.put(term, term);
	}

	@Override
	public void addTermWithStem(String term, String stem) {
		this.terms.put(term, stem);
	}

	@Override
	public Map<String, String> getTermStemPairs() {
		return Collections.unmodifiableMap(terms);
	}

	@Override
	public Collection<String> getSearchTerms() {
		return Collections.unmodifiableCollection(terms.values());
	}

	@Override
	public TopicCriteria createTopicCriteria(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		if (knowledgeBaseDescriptor == null) {
			throw new NullPointerException(
					"KnowledgeBaseDescriptor cann't be null!");
		}

		TopicCriteriaImpl criteria = new TopicCriteriaImpl();
		criteria.setKnowledgeBase(knowledgeBaseDescriptor);

		if (searchOptions != null) {
			setCriteriaDirection(criteria, searchOptions.getTreeEnum());
		}
		
		Collection<Scope> scopes = CompassManagerFactory.getInstance().getCompassManager().getScopes(knowledgeBaseDescriptor);
		
		criteria.setScopes(scopes);

		criterias.add(criteria);

		setKnowledgeBaseSpecificSettings(knowledgeBaseDescriptor, criteria, scopes);

		return criteria;
	}

	private void setKnowledgeBaseSpecificSettings(KnowledgeBaseDescriptor knowledgeBaseDescriptor,
			TopicCriteriaImpl criteria, Collection<Scope> scopes) {
		if (knowledgeBase != null) {

			SearchSettings searchSettings = knowledgeBase.getSearchSettings();

			if (searchSettings != null) {

				for (KnowldegeBaseSetting knowldegeBaseSetting : searchSettings
						.getKnowledgeBaseSettings()) {

					if (knowldegeBaseSetting.getName() != null
							&& knowldegeBaseSetting
									.equals(knowledgeBaseDescriptor
											.getDisplayName())) {
						setCriteriaDirection(criteria, knowldegeBaseSetting.getOptions().getTreeEnum());
						
						Collection<ScopeSetting> kbScope = knowldegeBaseSetting.getScopes();
						
						if (!kbScope.isEmpty()) {
							List<Scope> innerScope = new ArrayList<Scope>(0);
							
							for (ScopeSetting scopeSetting : kbScope) {
								
								for (Scope scope : scopes) {
									if (scope.getDisplayName().equals(scopeSetting.getName())) {
										innerScope.add(scope);
										break;
									}
								}
							}
							
							if (!innerScope.isEmpty()) {
								criteria.setScopes(innerScope);
							}
							
						}
						
					}

				}

			}
		}
	}

	private void setCriteriaDirection(TopicCriteriaImpl criteria, Tree tree) {
		switch (tree) {
		case AHEAD:
			criteria.setRelationDirection(RelationDirection.SPEC);
			break;
		case BACK:
			criteria.setRelationDirection(RelationDirection.GEN);
			break;
		case TWO_WAY:
			criteria.setRelationDirection(null);
			break;
		default:
			assert false;
		}
	}

	@Override
	public Collection<TopicCriteria> getTopicCriterias() {
		return Collections.unmodifiableCollection(criterias);
	}

	@Override
	public boolean isPerfixMatch() {
		return prefixMatch;
	}

	@Override
	public void setPrefixMatch(boolean match) {
		this.prefixMatch = match;
	}

	@Override
	public double getThresholdWeight() {
		return thresholdWeight;
	}

	@Override
	public void setThresholdWeight(double thresholdWeight) {
		this.thresholdWeight = thresholdWeight;
	}

	@Override
	public int getMaxTopicNumberToExpand() {
		return maxTopicNumberToExpand;
	}

	@Override
	public void setMaxTopicNumberToExpand(int maxTopicNumberToExpand) {
		this.maxTopicNumberToExpand = maxTopicNumberToExpand;
	}

	@Override
	public int getHopCount() {
		return hopCount;
	}

	@Override
	public void setHopCount(int hopCount) {
		this.hopCount = hopCount;
	}

	@Override
	public boolean isTreeSearch() {
		return treeSearch;
	}

	@Override
	public void setTreeSearch(boolean treeSearch) {
		this.treeSearch = treeSearch;
	}

	public void setKnowledgeBase(KnowledgeBaseSettingPlugin knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
	}

	public void setSearchOptions(SearchOptions searchOptions) {
		this.searchOptions = searchOptions;
	}

}
