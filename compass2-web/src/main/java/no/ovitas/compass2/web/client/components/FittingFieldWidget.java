/**
 * 
 */
package no.ovitas.compass2.web.client.components;

import no.ovitas.compass2.model.FittingType;
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
public class FittingFieldWidget extends LayoutContainer {

	private RadioGroup radioGroup;

	public FittingFieldWidget(SearchFieldModel searchField,
			FormBinding formBinding) {
		
		createGui(searchField, formBinding);
	}

	private void createGui(SearchFieldModel searchField, FormBinding formBinding) {

		setLayout(new FormLayout());

		radioGroup = new RadioGroup();
		radioGroup.setSelectionRequired(true);
		radioGroup.setHideLabel(true);

		Radio rdWholeWord = new Radio();
		rdWholeWord.setHideLabel(true);
		rdWholeWord.setBoxLabel(FittingType.WHOLE_WORD.getTitle());
		rdWholeWord.setValueAttribute(FittingType.WHOLE_WORD.name());
		radioGroup.add(rdWholeWord);

		Radio rdPrefix = new Radio();
		rdPrefix.setHideLabel(true);
		rdPrefix.setBoxLabel(FittingType.PREFIX.getTitle());
		rdPrefix.setValueAttribute(FittingType.PREFIX.name());
		radioGroup.add(rdPrefix);

		FieldBinding fieldBinding = new FieldBinding(radioGroup,
				searchField.getId() + Compass2Constans.FIT_TYPE);
		fieldBinding.setConverter(new RadioGroupConverter(radioGroup));
		formBinding.addFieldBinding(fieldBinding);

		add(radioGroup, new FormData("100%"));
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		radioGroup.setEnabled(enabled);
	}

}
