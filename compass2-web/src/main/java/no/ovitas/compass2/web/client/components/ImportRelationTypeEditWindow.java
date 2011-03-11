/**
 * Created on 2010.08.27.
 *
 * Copyright (C) 2010 Thot-Soft 2002 Ltd. All Rights Reserved.
 *
 * This software is the proprietary information of ThotSoft 2002 Ltd.
 * Use is subject to license terms.
 *
 * $Id:$
 */
package no.ovitas.compass2.web.client.components;

import java.util.ArrayList;
import java.util.List;

import no.ovitas.compass2.web.client.Compass2Main;
import no.ovitas.compass2.web.client.model.BackEndException;
import no.ovitas.compass2.web.client.model.Compass2Constans;
import no.ovitas.compass2.web.client.model.RelationTypesModel;
import no.ovitas.compass2.web.client.rpc.Compass2ServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author nyari
 * 
 */
public class ImportRelationTypeEditWindow extends BaseWindow {

	private ListStore<RelationTypesModel> listStore;
	private EditorGrid<RelationTypesModel> grid;
	private BaseListLoader<ListLoadResult<RelationTypesModel>> listLoader;
	private Button finishButton;
	private FormPanel formPanel;
	private Compass2ServiceAsync compass2Service;
	private ContentPanel contentPanel;

	/**
	 * @param title
	 * @param width
	 */
	public ImportRelationTypeEditWindow(
			final Compass2ServiceAsync compass2Service) {
		super(Compass2Main.i18n.titleEditRelationTipes(), 600);
		this.compass2Service = compass2Service;
		DataProxy<List<RelationTypesModel>> proxy = new RpcProxy<List<RelationTypesModel>>() {
			@Override
			public void load(Object loadConfig,
					AsyncCallback<List<RelationTypesModel>> callback) {
				compass2Service.getImportedRelationTypes(callback);
			}
		};
		listLoader = new BaseListLoader<ListLoadResult<RelationTypesModel>>(
				proxy) {

			@Override
			protected void onLoadFailure(Object loadConfig, Throwable t) {
				super.onLoadFailure(loadConfig, t);
				if (t instanceof BackEndException)
					Compass2Main.errorMessage((BackEndException) t);
				else
					Compass2Main.errorLoad(t);
			}

		};
		listStore = new ListStore<RelationTypesModel>(listLoader);

		contentPanel = new ContentPanel();
		contentPanel.setHeaderVisible(false);
		contentPanel.setBodyBorder(false);

		formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		formPanel.setBodyBorder(false);
	}

	@Override
	protected void buildGui() {

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig(Compass2Constans.RELATION_NAME,
				Compass2Main.i18n.columnRelationName(), 180);
		configs.add(column);

		column = new ColumnConfig(Compass2Constans.REF,
				Compass2Main.i18n.columnRef(), 50);
		configs.add(column);

		NumberField weightAheadField = new NumberField();
		weightAheadField.setPropertyEditorType(Double.class);
		weightAheadField.setFormat(NumberFormat.getDecimalFormat());
		column = new ColumnConfig(Compass2Constans.WEIGHT_AHEAD,
				Compass2Main.i18n.columnWeightAhead(), 80);
		column.setNumberFormat(NumberFormat.getDecimalFormat());
		column.setEditor(new CellEditor(weightAheadField));
		configs.add(column);

		NumberField weightAbackField = new NumberField();
		weightAbackField.setPropertyEditorType(Double.class);
		weightAbackField.setFormat(NumberFormat.getDecimalFormat());
		column = new ColumnConfig(Compass2Constans.WEIGHT_ABACK,
				Compass2Main.i18n.columnWeightAback(), 80);
		column.setNumberFormat(NumberFormat.getDecimalFormat());
		column.setEditor(new CellEditor(weightAbackField));
		configs.add(column);

		ColumnModel columnModel = new ColumnModel(configs);

		grid = new EditorGrid<RelationTypesModel>(listStore, columnModel);
		grid.setAutoExpandColumn(Compass2Constans.RELATION_NAME);
		grid.setHeight(500);
		grid.setBorders(true);
		grid.setLoadMask(true);

		listLoader.load();

		formPanel.add(grid, new FormData("100%"));

		contentPanel.add(formPanel);

		contentPanel.setButtonAlign(HorizontalAlignment.CENTER);

		finishButton = new Button(Compass2Main.i18n.buttonFinish());
		finishButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				formPanel.mask(Compass2Main.i18n.messageUpload());
				finishButton.disable();
				List<RelationTypesModel> modifiedRelationTypes = new ArrayList<RelationTypesModel>();
				for (Record r : listStore.getModifiedRecords()) {
					modifiedRelationTypes.add((RelationTypesModel) r.getModel());
				}
				compass2Service.uploadRelationTypes(modifiedRelationTypes,
						new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void v) {
								formPanel.unmask();
								hide();
								fireWindowAccepted();
							}

							@Override
							public void onFailure(Throwable t) {
								if (t instanceof BackEndException)
									Compass2Main.errorMessage((BackEndException) t);
								else
									Compass2Main.errorMessage(t);
							}
						});

			}

		});

		contentPanel.addButton(finishButton);

		Button cancelButton = new Button(Compass2Main.i18n.buttonCancel());
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				hide();
			}
		});

		contentPanel.addButton(cancelButton);

		add(contentPanel);
	}

}
