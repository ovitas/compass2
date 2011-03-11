/**
 * 
 */
package no.ovitas.compass2.web.client.components;

import no.ovitas.compass2.web.client.Compass2Main;
import no.ovitas.compass2.web.client.model.Compass2Constans;
import no.ovitas.compass2.web.client.model.SearchFieldModel;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

/**
 * @author gyalai
 * 
 */
public class TextSearchFieldWidget extends LayoutContainer {

	private MatchingFieldWidget matchingFieldWidget;
	private FittingFieldWidget fittingFieldWidget;

	public TextSearchFieldWidget(SearchFieldModel searchField,
			FormBinding formBinding) {

		matchingFieldWidget = new MatchingFieldWidget(searchField, formBinding);
		fittingFieldWidget = new FittingFieldWidget(searchField, formBinding);

		createGui(searchField, formBinding);
	}

	private void createGui(SearchFieldModel searchField, FormBinding formBinding) {

		setLayout(new RowLayout(Orientation.HORIZONTAL));
		setHeight(50);

		LayoutContainer leftLayoutContainer = new LayoutContainer();
		FormLayout leftFormLayout = new FormLayout();
		leftFormLayout.setLabelAlign(LabelAlign.TOP);
		leftLayoutContainer.setLayout(leftFormLayout);

		TextField<String> textField = new TextField<String>();
		leftLayoutContainer.add(textField, new FormData("100%"));
		textField.setFieldLabel(searchField.getLabel());
		formBinding.addFieldBinding(new FieldBinding(textField, searchField
				.getId()));

		add(leftLayoutContainer, new RowData(0.51, 1.0, new Margins()));

		LayoutContainer centerLayoutContainer = new LayoutContainer();
		FormLayout centerFormLayout = new FormLayout();
		centerFormLayout.setLabelAlign(LabelAlign.TOP);
		centerLayoutContainer.setLayout(centerFormLayout);

		CheckBox fuzzyMatch = new CheckBox();
		fuzzyMatch.setBoxLabel(Compass2Main.i18n.labelFuzzyMatch());
		fuzzyMatch.setHideLabel(true);

		fuzzyMatch.addListener(Events.Change, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent be) {
				if ((Boolean) be.getValue()) {
					fittingFieldWidget.setEnabled(false);
				} else {
					fittingFieldWidget.setEnabled(true);
				}
			};
		});

		formBinding.addFieldBinding(new FieldBinding(fuzzyMatch, searchField
				.getId() + Compass2Constans.FUZZY_MATCH));
		centerLayoutContainer.add(fuzzyMatch, new FormData("100%"));

		add(centerLayoutContainer, new RowData(0.18, 1.0, new Margins(11, 10,
				0, 10)));

		LayoutContainer rightLayoutContainer = new LayoutContainer();
		rightLayoutContainer.setLayout(new RowLayout(Orientation.VERTICAL));

		rightLayoutContainer.add(matchingFieldWidget, new RowData(1.0, 0.5, new Margins()));

		rightLayoutContainer.add(fittingFieldWidget, new RowData(1.0, 0.5, new Margins()));

		add(rightLayoutContainer, new RowData(0.35, 1.0, new Margins(0, 0, 0,
				10)));

	}
}
