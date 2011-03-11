/**
 * 
 */
package no.ovitas.compass2.web.client.controllers.form;

import java.util.List;

/**
 * @author gyalai
 *
 */
public class FormSearch {
	
	private String searchValue;
	
	private List selected;

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	
	public List getSelected() {
		return selected;
	}
	
	public void setSelected(List selected) {
		this.selected = selected;
	}

}
