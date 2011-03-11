package no.ovitas.compass2.web.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchConfigModel extends BaseModel implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Boolean getFuzzySearchValue() {
		return get(Compass2Constans.FUZZY_MATCH);
	}
	
	public void setFuzzySearchValue(Boolean fuzzy) {
		set(Compass2Constans.FUZZY_MATCH, fuzzy);
	}
	
	public Integer getMaxNumberOfHits() {
		return get(Compass2Constans.MAX_NUMBER_OF_HITS);
	}
	
	public void setMaxNumberOfHits(Integer max) {
		set(Compass2Constans.MAX_NUMBER_OF_HITS, max);
	}
	
	public Double getResultTreshold() {
		return get(Compass2Constans.RESULT_THRESHOLD);
	}
	
	public void setResultTreshold(Double thres) {
		set(Compass2Constans.RESULT_THRESHOLD, thres);
	}
	
	public Boolean getPrefixMatch() {
		return get(Compass2Constans.TOPIC_PREFIX_MATCH);
	}
	
	public void setPrefixMatch(Boolean match) {
		set(Compass2Constans.TOPIC_PREFIX_MATCH, match);
	}
	
	public Double getTresholdWeight() {
		return get(Compass2Constans.EXPANSION_THRESHOLD);
	}
	
	public void setTresholdWeight(Double thres) {
		set(Compass2Constans.EXPANSION_THRESHOLD, thres);
	}

	public Integer getMaxNumberOfTopicToExpand() {
		return get(Compass2Constans.MAX_TOPIC_NUMBER_TO_EXPAND);
	}
	
	public void setMaxNumberOfTopicToExpand(Integer max) {
		set(Compass2Constans.MAX_TOPIC_NUMBER_TO_EXPAND, max);
	}
	
	public Integer getHopCount() {
		return get(Compass2Constans.HOP_COUNT);
	}
	
	public void setHopCount(Integer hop) {
		set(Compass2Constans.HOP_COUNT, hop);
	}
	
	public Boolean getTreeSearch() {
		return get(Compass2Constans.TREE_SEARCH);
	}
	
	public void setTreeSearch(Boolean gen) {
		set(Compass2Constans.TREE_SEARCH, gen);
	}
	
}
