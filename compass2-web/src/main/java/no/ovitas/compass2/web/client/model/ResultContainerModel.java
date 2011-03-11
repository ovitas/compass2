/**
 * 
 */
package no.ovitas.compass2.web.client.model;

import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @class ResultContainerModel
 * @project compass2-web
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.09.02.
 * 
 */
public class ResultContainerModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	public void setTopicExpansions(List<TopicExpansionModel> topicExpansions) {
		set(Compass2Constans.TOPIC_EXPANSIONS, topicExpansions);
	}
	
	public List<TopicExpansionModel> getTopicExpansions() {
		return get(Compass2Constans.TOPIC_EXPANSIONS);
	}
	
	public void setSearchResultId(Long id) {
		set(Compass2Constans.RESULT_MODEL_ID, id);
	}
	
	public Long getSearchResultId() {
		return get(Compass2Constans.RESULT_MODEL_ID);
	}
	
	public void setSearchResultNumber(Integer number) {
		set(Compass2Constans.RESULT_NUMBER, number);
	}
	
	public Integer getSearchResultNumber() {
		return get(Compass2Constans.RESULT_NUMBER);
	}
	
	public Boolean hasError() {
		return get(Compass2Constans.HAS_ERROR);
	}
	
	public void setError(Boolean error) {
		set(Compass2Constans.HAS_ERROR, error);
	}
	
}
