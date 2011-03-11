/**
 * 
 */
package no.ovitas.compass2.web.client.components;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * @author gyalai
 * 
 */
public class SelectionCheckBoxRenderer<E extends ModelData> implements
		GridCellRenderer<E> {

	private SelectionCheckBoxCellValueChangeHandler<E> handler;

	private final class ValueChangeHandlerImplementation implements
			ValueChangeHandler<Boolean> {

		private E model;

		public ValueChangeHandlerImplementation(E model) {
			this.model = model;

		}

		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			handler.onValueChanged(model, event);

		}
	}

	@Override
	public Object render(E model, String property, ColumnData config,
			int rowIndex, int colIndex, ListStore<E> store, Grid<E> grid) {

		CheckBox checkBox = new CheckBox();

		if (handler != null) {
			ValueChangeHandlerImplementation handler2 = new ValueChangeHandlerImplementation(
					model);
			checkBox.addValueChangeHandler(handler2);
		}
		return checkBox;
	}

	public void addValueChangeHandler(
			SelectionCheckBoxCellValueChangeHandler<E> handler) {
		this.handler = handler;
	}

}
