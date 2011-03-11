/**
 * 
 */
package no.ovitas.compass2.web.client.model;

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

/**
 * @author gyalai
 *
 */
public class SelectedTreeModel extends BaseTreeModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public Boolean isSelected() {
		return get(Compass2Constans.IS_SELECTED);
	}
	
	public void setSelected(Boolean selected) {
		set(Compass2Constans.IS_SELECTED, selected);
	}
}
