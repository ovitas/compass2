/**
 * Created on 2010.07.26.
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
import no.ovitas.compass2.web.client.model.KnowledgeBaseModel;
import no.ovitas.compass2.web.client.model.KnowledgeBaseTreeModel;
import no.ovitas.compass2.web.client.model.RelationTypesModel;
import no.ovitas.compass2.web.client.rpc.Compass2ServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author nyari
 * 
 */
public class RelationTypesGrid extends LayoutContainer {

	private ListStore<RelationTypesModel> listStore = new ListStore<RelationTypesModel>();
	private EditorGrid<RelationTypesModel> grid;
	private Compass2ServiceAsync compass2Service;
	private BaseListLoader<ListLoadResult<RelationTypesModel>> loader;
	private KnowledgeBaseTreeModel knowledgeBase;
	private ContentPanel contentPanel;

	public RelationTypesGrid(final Compass2ServiceAsync service,
			KnowledgeBaseTreeModel kb) {
		this.knowledgeBase = kb;
		this.compass2Service = service;
		DataProxy<List<RelationTypesModel>> relationTypesProxy = new DataProxy<List<RelationTypesModel>>() {

			@Override
			public void load(DataReader<List<RelationTypesModel>> reader,
					Object loadConfig,
					AsyncCallback<List<RelationTypesModel>> callback) {
				compass2Service.getRelationTypes(knowledgeBase, callback);
			}

		};

		loader = new BaseListLoader<ListLoadResult<RelationTypesModel>>(
				relationTypesProxy) {

			@Override
			protected void onLoadFailure(Object loadConfig, Throwable t) {
				super.onLoadFailure(loadConfig, t);
				if (t instanceof BackEndException)
					Compass2Main.errorMessage((BackEndException) t);
				else
					Compass2Main.errorLoad(t);
			}

		};
		listStore = new ListStore<RelationTypesModel>(loader);
		createGui();
		loader.load();
	}

	protected void createGui() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig(Compass2Constans.EXTERNAL_ID,
				Compass2Main.i18n.columnId(), 90);
		configs.add(column);

		column = new ColumnConfig(Compass2Constans.RELATION_NAME,
				Compass2Main.i18n.columnRelationName(), 280);
		configs.add(column);

		column = new ColumnConfig(Compass2Constans.REF,
				Compass2Main.i18n.columnRef(), 50);
		configs.add(column);

		NumberField weightAheadField = new NumberField();
		weightAheadField.setFormat(NumberFormat.getDecimalFormat());
		column = new ColumnConfig(Compass2Constans.WEIGHT_AHEAD,
				Compass2Main.i18n.columnWeightAhead(), 80);
		column.setNumberFormat(NumberFormat.getDecimalFormat());
		column.setEditor(new CellEditor(weightAheadField));
		configs.add(column);

		NumberField weightAbackField = new NumberField();
		weightAbackField.setFormat(NumberFormat.getDecimalFormat());
		column = new ColumnConfig(Compass2Constans.WEIGHT_ABACK,
				Compass2Main.i18n.columnWeightAback(), 80);
		column.setNumberFormat(NumberFormat.getDecimalFormat());
		column.setEditor(new CellEditor(weightAbackField));
		configs.add(column);

		ColumnModel columnModel = new ColumnModel(configs);

		grid = new EditorGrid<RelationTypesModel>(listStore, columnModel);
		grid.setAutoExpandColumn(Compass2Constans.RELATION_NAME);
		grid.setAutoExpandMax(1000);
		grid.setAutoHeight(true);
		grid.setAutoWidth(true);
		grid.setBorders(false);

		contentPanel = new ContentPanel();
		contentPanel.setHeaderVisible(false);
		contentPanel.setButtonAlign(HorizontalAlignment.CENTER);
		contentPanel.setBorders(false);
		contentPanel.setBodyBorder(false);
		contentPanel.add(grid);

		contentPanel.addButton(new Button(Compass2Main.i18n.buttonReset(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						listStore.rejectChanges();
					}
				}));

		contentPanel.addButton(new Button(Compass2Main.i18n.buttonSave(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						contentPanel.mask(Compass2Main.i18n.messageSave());
						List<Record> records = listStore.getModifiedRecords();
						List<RelationTypesModel> relationTypesModified = new ArrayList<RelationTypesModel>();
						for (Record record : records) {
							relationTypesModified
									.add((RelationTypesModel) record.getModel());
						}
						if (relationTypesModified.size() > 0) {
							compass2Service.updateRelationTypes(
									relationTypesModified, knowledgeBase,
									new AsyncCallback<Void>() {

										@Override
										public void onFailure(Throwable arg0) {
											if (arg0 instanceof BackEndException)
												Compass2Main.errorMessage((BackEndException) arg0);
											else
												Compass2Main.errorMessage(arg0);
											contentPanel.unmask();
										}

										@Override
										public void onSuccess(Void arg0) {
											contentPanel.unmask();
										}

									});
						}
						listStore.commitChanges();
					}
				}));

		add(contentPanel);

	}
}
