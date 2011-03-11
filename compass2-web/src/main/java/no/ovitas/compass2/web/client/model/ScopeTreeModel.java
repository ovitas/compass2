/**
 * 
 */
package no.ovitas.compass2.web.client.model;

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;

/**
 * @author gyalai
 *
 */
public class ScopeTreeModel extends SelectedTreeModel implements Serializable {

	@Override
	public void add(ModelData child) {
		throw new UnsupportedOperationException("ScopeTreeNode only leaf node.");
	}
	
	public Long getId() {
		return get(Compass2Constans.ID);
	}
	
	public void setId(Long id) {
		set(Compass2Constans.ID, id);
	}
	
	public String getLabel() {
		return get(Compass2Constans.LABEL);
	}
	
	public void setLabel(String label) {
		set(Compass2Constans.LABEL, label);
	}
	
	public String getValue() {
		return get(Compass2Constans.VALUE);
	}
	
	public void setValue(String value) {
		set(Compass2Constans.VALUE, value);
	}
	
}
