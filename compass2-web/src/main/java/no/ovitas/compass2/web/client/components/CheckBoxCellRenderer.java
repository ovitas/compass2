/**
 * 
 */
package no.ovitas.compass2.web.client.components;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.treegrid.WidgetTreeGridCellRenderer;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author gyalai
 * 
 */
public class CheckBoxCellRenderer<E extends ModelData> implements
		GridCellRenderer<E> {

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

	private CheckBoxCellValueChangeHandler<E> handler;
	private Class excludedClass;


	public void addValueChangeHandler(CheckBoxCellValueChangeHandler<E> handler) {
		this.handler = handler;
	}

	@Override
	public Object render(E model, String property, ColumnData config,
			int rowIndex, int colIndex, ListStore<E> store, Grid<E> grid) {
		CheckBox checkBox = new CheckBox();

		Object prop = model.get(property);
		
		if (excludedClass != null && excludedClass.equals(model.getClass())) {
			return "";
		}

		if (prop == null || (prop instanceof Boolean && (Boolean) prop != true)) {
			checkBox.setEnabled(false);
		} else {
			checkBox.setEnabled(true);
		}

		if (handler != null) {
			ValueChangeHandlerImplementation handler2 = new ValueChangeHandlerImplementation(model);
			checkBox.addValueChangeHandler(handler2);
		}
		return checkBox;
	}
	
	public void addExcludeType(@SuppressWarnings("rawtypes") Class excludedClass) {
		this.excludedClass = excludedClass;		
	}

}
