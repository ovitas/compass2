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
import no.ovitas.compass2.web.client.model.FolderModel;
import no.ovitas.compass2.web.client.rpc.Compass2ServiceAsync;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.CheckCascade;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author nyari
 * 
 */
public class TopicExpansionTree extends ContentPanel {

	private TreeStore<FolderModel> treeStore;
	private BaseTreeLoader<FolderModel> treeLoader;
	private TreePanel<FolderModel> tree;

	public TreePanel<FolderModel> getTree() {
		return tree;
	}

	public TopicExpansionTree(final Compass2ServiceAsync compass2Service,
			final Long id) {
		DataProxy<List<FolderModel>> proxy = new DataProxy<List<FolderModel>>() {

			@Override
			public void load(DataReader<List<FolderModel>> reader,
					Object loadConfig, AsyncCallback<List<FolderModel>> callback) {
				compass2Service.getExpansionTreeById((FolderModel) loadConfig,
						id, callback);
			}

		};

		treeLoader = new BaseTreeLoader<FolderModel>(proxy) {

			@Override
			protected void onLoadFailure(Object loadConfig, Throwable t) {
				super.onLoadFailure(loadConfig, t);
				if (t instanceof BackEndException)
					Compass2Main.errorMessage((BackEndException) t);
				else
					Compass2Main.errorLoad(t);
			}

			@Override
			protected void onLoadSuccess(Object loadConfig,
					List<FolderModel> result) {
				super.onLoadSuccess(loadConfig, result);
				if (loadConfig != null)
					((FolderModel) loadConfig).setOpened(true);

				for (FolderModel folderModel : result) {
					tree.setChecked(folderModel, true);
					folderModel.setOpened(false);
				}
			}

			@Override
			public boolean hasChildren(FolderModel parent) {
				boolean sup = super.hasChildren(parent);
				return sup | !parent.isTopic();
			}

		};
		treeStore = new TreeStore<FolderModel>(treeLoader);
		setLayout(new FitLayout());

		setCollapsible(false);

		createGui();
		treeLoader.load();

	}

	protected void createGui() {

		tree = new TreePanel<FolderModel>(treeStore);		
		tree.setDisplayProperty(Compass2Constans.NAME);
		tree.setCheckable(true);
		tree.setCheckStyle(CheckCascade.CHILDREN);
		add(tree);
		// add(set, new FormData("100%"));
	}

	public List<Long> getUnSelectedNodes() {

		List<Long> result = new ArrayList<Long>();

		List<FolderModel> checkedSelection = tree.getCheckedSelection();

		List<FolderModel> allItems = treeStore.getAllItems();

		for (FolderModel folderModel : allItems) {

			if (!folderModel.isTopic())
				continue;

			if (!checkedSelection.contains(folderModel)) {
				result.add(folderModel.getId());
			}
		}

		return result;
	}

	public List<Long> getUnSelectedNotOpenedNodes() {

		List<Long> result = new ArrayList<Long>();

		List<FolderModel> checkedSelection = tree.getCheckedSelection();

		List<FolderModel> allItems = treeStore.getAllItems();

		for (FolderModel folderModel : allItems) {

			if (folderModel.isOpened())
				continue;

			if (!folderModel.isTopic())
				continue;

			if (!checkedSelection.contains(folderModel)) {
				result.add(folderModel.getId());
			}
		}

		return result;
	}

	public List<String> getUnSelectedNotOpenedTermNodes() {

		List<String> result = new ArrayList<String>();

		List<FolderModel> checkedSelection = tree.getCheckedSelection();

		List<FolderModel> allItems = treeStore.getRootItems();

		boolean checked;
		for (FolderModel folderModel : allItems) {

			if (folderModel.isOpened())
				continue;

			checked = false;
			for (FolderModel checkedModel : checkedSelection) {

				if (checkedModel.getTermName()
						.equals(folderModel.getTermName())) {
					checked = true;
					break;
				}
			}

			if (!checked)
				result.add(folderModel.getTermName());

		}

		return result;
	}
}
