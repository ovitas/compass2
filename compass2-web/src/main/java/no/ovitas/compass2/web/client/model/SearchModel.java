/**
 * Created on 2010.07.16.
 *
 * Copyright (C) 2010 Thot-Soft 2002 Ltd. All Rights Reserved.
 *
 * This software is the proprietary information of ThotSoft 2002 Ltd.
 * Use is subject to license terms.
 *
 * $Id:$
 */
package no.ovitas.compass2.web.client.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import no.ovitas.compass2.model.ConnectingType;
import no.ovitas.compass2.model.FittingNumberType;
import no.ovitas.compass2.model.FittingType;
import no.ovitas.compass2.model.MatchingType;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author nyari
 *
 */
public class SearchModel extends BaseModel implements IsSerializable {
	
	@SuppressWarnings("unused")
	private ConnectingType connectingType;
	@SuppressWarnings("unused")
	private MatchingType matchingType;
	@SuppressWarnings("unused")
	private FittingType fittingType;
	@SuppressWarnings("unused")
	private FittingNumberType fittingNumberType;

	private static final long serialVersionUID = 1L;
	
	public void setSearch(String search) {
		set(Compass2Constans.SEARCH, search);
	}
	
	public String getSearch() {
		return get(Compass2Constans.SEARCH);
	}
	
	public void setHopCount(Integer hopCount) {
		set(Compass2Constans.HOP_COUNT, hopCount);
	}
	
	public Integer getHopCount() {
		return get(Compass2Constans.HOP_COUNT);
	}
	
	public void setMaxTopicNumberToExpand(Integer maxTopicNumberToExpand) {
		set(Compass2Constans.MAX_TOPIC_NUMBER_TO_EXPAND, maxTopicNumberToExpand);
	}
	
	public Integer getMaxTopicNumberToExpand() {
		return get(Compass2Constans.MAX_TOPIC_NUMBER_TO_EXPAND);
	}
	
	public void setExpansionThreshold(Double expansionThreshold) {
		set(Compass2Constans.EXPANSION_THRESHOLD, expansionThreshold);
	}
	
	public Double getExpansionThreshold() {
		return get(Compass2Constans.EXPANSION_THRESHOLD);
	}
	
	public void setResultThreshold(Double resultThreshold) {
		set(Compass2Constans.RESULT_THRESHOLD, resultThreshold);
	}
	
	public Double getResultThreshold() {
		return get(Compass2Constans.RESULT_THRESHOLD);
	}
	
	public void setMaxNumberOfHits(Integer maxNumberOfHits) {
		set(Compass2Constans.MAX_NUMBER_OF_HITS, maxNumberOfHits);
	}
	
	public Integer getMaxNumberOfHits() {
		return get(Compass2Constans.MAX_NUMBER_OF_HITS);
	}
	
	public void setTopicPrefixMatch(Boolean topicPrefixMatch) {
		set(Compass2Constans.TOPIC_PREFIX_MATCH, topicPrefixMatch);
	}
	
	public Boolean getTopicPrefixMatch() {
		return get(Compass2Constans.TOPIC_PREFIX_MATCH);
	}
	
	public void setFuzzyMatch(Boolean fuzzyMatch) {
		set(Compass2Constans.FUZZY_MATCH, fuzzyMatch);
	}
	
	public Boolean getFuzzyMatch() {
		return get(Compass2Constans.FUZZY_MATCH);
	}
	
	public void setTreeSearch(Boolean treeSearch) {
		set(Compass2Constans.TREE_SEARCH, treeSearch);
	}
	
	public Boolean isTreeSearch() {
		return get(Compass2Constans.TREE_SEARCH);
	}
	
	public void setAdvancedSearch(Boolean advancedSearch) {
		set(Compass2Constans.ADVANCED_SEARCH, advancedSearch);
	}
	
	public Boolean isAdvancedSearch() {
		return get(Compass2Constans.ADVANCED_SEARCH);
	}
	
	
	public Map<KnowledgeBaseTreeModel, Set<ScopeTreeModel>> getKnowledgeBaseList() {
		return get(Compass2Constans.KB_LIST);
	}

	public void setKnowledgeBaseList(Map<KnowledgeBaseTreeModel, Set<ScopeTreeModel>> knowledgeBase) {
		set(Compass2Constans.KB_LIST, knowledgeBase);		
	}

}
