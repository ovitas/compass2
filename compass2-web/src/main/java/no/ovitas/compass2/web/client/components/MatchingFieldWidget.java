/**
 * 
 */
package no.ovitas.compass2.web.client.components;

import no.ovitas.compass2.model.MatchingType;
import no.ovitas.compass2.web.client.model.Compass2Constans;
import no.ovitas.compass2.web.client.model.SearchFieldModel;

import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

/**
 * @author gyalai
 *
 */
public class MatchingFieldWidget extends LayoutContainer {

	private RadioGroup radioGroup;

	public MatchingFieldWidget(SearchFieldModel searchField,
			FormBinding formBinding) {
		
		createGui(searchField, formBinding);
	}

	private void createGui(SearchFieldModel searchField, FormBinding formBinding) {
		setLayout(new FormLayout());
		
		radioGroup = new RadioGroup();
		radioGroup.setSelectionRequired(true);
		radioGroup.setHideLabel(true);

		Radio rdMatchAll = new Radio();
		rdMatchAll.setHideLabel(true);
		rdMatchAll.setBoxLabel(MatchingType.MATCH_ALL.getTitle());
		rdMatchAll.setValueAttribute(MatchingType.MATCH_ALL.name());
		radioGroup.add(rdMatchAll);

		Radio rdMatchAny = new Radio();
		rdMatchAny.setHideLabel(true);
		rdMatchAny.setBoxLabel(MatchingType.MATCH_ANY.getTitle());
		rdMatchAny.setValueAttribute(MatchingType.MATCH_ANY.name());
		radioGroup.add(rdMatchAny);

		FieldBinding fieldBinding = new FieldBinding(radioGroup,
				searchField.getId() + Compass2Constans.MATCH_TYPE);
		fieldBinding.setConverter(new RadioGroupConverter(radioGroup));
		formBinding.addFieldBinding(fieldBinding);

		add(radioGroup, new FormData("100%"));
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		radioGroup.setEnabled(enabled);
	}

}
