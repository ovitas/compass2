/**
 * 
 */
package no.ovitas.compass2.config.settings;

import no.ovitas.compass2.model.FullTextFieldType;

/**
 * @class SearchField
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.07.29.
 * 
 */
public class SearchField implements Setting {

	protected String indexField;

	protected String searchField;

	protected String type;
	
	protected boolean defaultIndex;

	public String dumpOut(String ind) {
		// TODO Auto-generated method stub
		return ind + " SearchField: index-field: " + indexField
				+ ", search-field: " + searchField + ", type: " + type + ", default: " + defaultIndex + "\n";
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public FullTextFieldType getSearchType() {
		String type2 = type.toUpperCase();

		return FullTextFieldType.valueOf(type2);
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	public String getSearchField() {
		return searchField;
	}

	public void setIndexField(String indexField) {
		this.indexField = indexField;
	}

	public String getIndexField() {
		return indexField;
	}

	public void addDefault(String value) {
		if (value != null && "false".equals(value)) {
			defaultIndex = false;
		} else {
			defaultIndex = true;
		}
	}
	
	public void setDefaultIndex(boolean defaultIndex) {
		this.defaultIndex = defaultIndex;
	}

	public boolean isDefaultIndex() {
		return defaultIndex;
	}

	
}
