package no.ovitas.compass2.web.client.controllers.form;

import java.util.Collection;

import no.ovitas.compass2.config.settings.SearchField;
import no.ovitas.compass2.config.settings.SearchOptions;

public class DefaultSearchConfig {
	
	private SearchOptions searchOptions;
	
	private Collection<SearchField> searchFields;

	public SearchOptions getSearchOptions() {
		return searchOptions;
	}

	public void setSearchOptions(SearchOptions searchOptions) {
		this.searchOptions = searchOptions;
	}

	public Collection<SearchField> getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(Collection<SearchField> searchFields) {
		this.searchFields = searchFields;
	}
}
