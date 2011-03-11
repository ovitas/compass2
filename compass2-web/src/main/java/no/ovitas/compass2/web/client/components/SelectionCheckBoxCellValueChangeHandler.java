/**
 * 
 */
package no.ovitas.compass2.web.client.components;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.event.logical.shared.ValueChangeEvent;

/**
 * @author gyalai
 *
 */
public abstract class SelectionCheckBoxCellValueChangeHandler<E extends ModelData> {

	public abstract void onValueChanged(E model, ValueChangeEvent<Boolean> event);
}
