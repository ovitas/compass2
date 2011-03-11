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
import no.ovitas.compass2.web.client.model.ResultModel;
import no.ovitas.compass2.web.client.rpc.Compass2ServiceAsync;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author nyari
 * 
 */
public class SearchResultGrid extends LayoutContainer {

	private ListStore<ResultModel> listStore;
	private BaseListLoader<ListLoadResult<ResultModel>> loader;

	public SearchResultGrid(final Compass2ServiceAsync compass2Service,
			final Long id) {
		DataProxy<List<ResultModel>> proxy = new DataProxy<List<ResultModel>>() {

			@Override
			public void load(DataReader<List<ResultModel>> reader,
					Object loadConfig, AsyncCallback<List<ResultModel>> callback) {
				compass2Service.getResultById(id, callback);
			}

		};

		loader = new BaseListLoader<ListLoadResult<ResultModel>>(proxy) {

			@Override
			protected void onLoadFailure(Object loadConfig, Throwable t) {
				super.onLoadFailure(loadConfig, t);
				if (t instanceof BackEndException)
					Compass2Main.errorMessage((BackEndException) t);
				else
					Compass2Main.errorLoad(t);
			}

		};
		listStore = new ListStore<ResultModel>(loader);
		createGui();
		loader.load();
	}

	protected void createGui() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig(Compass2Constans.TITLE,
				Compass2Main.i18n.columnTitle(), 550);
		GridCellRenderer<ResultModel> titleRenderer = new GridCellRenderer<ResultModel>() {

			@Override
			public Object render(ResultModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ResultModel> store, Grid<ResultModel> grid) {
				Html html = new Html("<a target=\"_blank\" href=\""
						+ model.getHtmlLink() + "\">" + model.getTitle()
						+ "</a>");
				return html;
			}
		};
		column.setRenderer(titleRenderer);
		configs.add(column);

		column = new ColumnConfig(Compass2Constans.SCORE,
				Compass2Main.i18n.columnScore(), 50);
		column.setNumberFormat(NumberFormat.getDecimalFormat());
		configs.add(column);

		ColumnModel columnModel = new ColumnModel(configs);

		Grid<ResultModel> grid = new Grid<ResultModel>(listStore, columnModel);
		// grid.setAutoExpandColumn(Compass2Constans.TITLE);
		grid.setAutoExpandMax(2000);
		grid.getView().setAutoFill(true);
		grid.getView().setForceFit(true);
		grid.setBorders(false);
		grid.setAutoHeight(true);
		// grid.setAutoWidth(true);
		add(grid);

	}
}
