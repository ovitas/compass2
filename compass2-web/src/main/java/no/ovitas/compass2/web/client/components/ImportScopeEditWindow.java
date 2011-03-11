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
import no.ovitas.compass2.web.client.model.EntityModel;
import no.ovitas.compass2.web.client.rpc.Compass2ServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author nyari
 * 
 */
public class ImportScopeEditWindow extends BaseWindow {

	private ListStore<EntityModel> listStore;
	private Grid<EntityModel> grid;
	private BaseListLoader<ListLoadResult<EntityModel>> scopeListLoader;
	private Button nextButton;
	private FormPanel formPanel;
	private Compass2ServiceAsync compass2Service;
	private ContentPanel contentPanel;

	/**
	 * Class Constructor.
	 * 
	 * @param compass2Service
	 * @param title
	 * @param width
	 */
	public ImportScopeEditWindow(final Compass2ServiceAsync compass2Service) {
		super(Compass2Main.i18n.titleEditScopes(), 500);
		this.compass2Service = compass2Service;
		DataProxy<List<EntityModel>> scopeProxy = new RpcProxy<List<EntityModel>>() {
			@Override
			public void load(Object loadConfig,
					AsyncCallback<List<EntityModel>> callback) {
				compass2Service.getImportedScopes(callback);
			}
		};
		scopeListLoader = new BaseListLoader<ListLoadResult<EntityModel>>(
				scopeProxy) {

			@Override
			protected void onLoadFailure(Object loadConfig, Throwable t) {
				super.onLoadFailure(loadConfig, t);
				if (t instanceof BackEndException)
					Compass2Main.errorMessage((BackEndException) t);
				else
					Compass2Main.errorLoad(t);
			}

		};
		listStore = new ListStore<EntityModel>(scopeListLoader);
		scopeListLoader.load();

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

		final CheckBoxSelectionModel<EntityModel> selectionModel = new CheckBoxSelectionModel<EntityModel>();
		selectionModel.setSelectionMode(SelectionMode.MULTI);
		configs.add(selectionModel.getColumn());

		ColumnConfig column = new ColumnConfig(Compass2Constans.LABEL,
				Compass2Main.i18n.columnScope(), 150);
		configs.add(column);

		ColumnModel columnModel = new ColumnModel(configs);

		grid = new Grid<EntityModel>(listStore, columnModel);
		grid.setAutoExpandColumn(Compass2Constans.LABEL);
		grid.setLoadMask(true);
		grid.setSelectionModel(selectionModel);
		grid.addPlugin(selectionModel);
		grid.setBorders(true);
		grid.setHeight(500);

		grid.getSelectionModel().addSelectionChangedListener(
				new SelectionChangedListener<EntityModel>() {

					@Override
					public void selectionChanged(
							SelectionChangedEvent<EntityModel> se) {
						if (se.getSelection() != null
								&& se.getSelection().size() > 0) {
							nextButton.enable();
						} else {
							nextButton.disable();
						}
					}

				});

		formPanel.add(grid, new FormData("100%"));

		contentPanel.add(formPanel);

		contentPanel.setButtonAlign(HorizontalAlignment.CENTER);

		nextButton = new Button(Compass2Main.i18n.buttonNext());
		nextButton.disable();
		nextButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				formPanel.mask(Compass2Main.i18n.messageUpload());
				nextButton.disable();
				compass2Service.uploadScopes(grid.getSelectionModel()
						.getSelectedItems(), new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void arg0) {
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

		contentPanel.addButton(nextButton);

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
