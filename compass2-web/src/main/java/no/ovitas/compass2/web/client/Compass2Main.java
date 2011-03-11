/**
 * Created on Jul 15, 2010
 *
 * Copyright (C) 2010 Thot-Soft 2002 Ltd. All Rights Reserved.
 *
 * This software is the proprietary information of Thot-Soft 2002 Ltd.
 * Use is subject to license terms.
 *
 * $Id:$
 */
package no.ovitas.compass2.web.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

import no.ovitas.compass2.web.client.components.RelationTypesGrid;
import no.ovitas.compass2.web.client.components.ResearchListener;
import no.ovitas.compass2.web.client.components.SearchForm;
import no.ovitas.compass2.web.client.components.SearchListener;
import no.ovitas.compass2.web.client.components.SearchResultGrid;
import no.ovitas.compass2.web.client.components.TopicExpansions;
import no.ovitas.compass2.web.client.i18n.Compass2I18n;
import no.ovitas.compass2.web.client.model.BackEndException;
import no.ovitas.compass2.web.client.model.Compass2Constans;
import no.ovitas.compass2.web.client.model.ExceptionModel;
import no.ovitas.compass2.web.client.model.FolderModel;
import no.ovitas.compass2.web.client.model.KnowledgeBaseTreeModel;
import no.ovitas.compass2.web.client.model.ResultContainerModel;
import no.ovitas.compass2.web.client.model.SearchConfigModel;
import no.ovitas.compass2.web.client.model.SearchFieldModel;
import no.ovitas.compass2.web.client.model.SearchModel;
import no.ovitas.compass2.web.client.model.SelectedTreeModel;
import no.ovitas.compass2.web.client.rpc.Compass2Service;
import no.ovitas.compass2.web.client.rpc.Compass2ServiceAsync;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author barath
 * 
 */
public class Compass2Main implements EntryPoint {

	public static Compass2I18n i18n = GWT.create(Compass2I18n.class);

	public static Compass2ServiceAsync compass2Service = (Compass2ServiceAsync) GWT
			.create(Compass2Service.class);

	private ContentPanel right;
	private ContentPanel left;
	private Viewport main;
	private LayoutContainer relationTypesLayout;
	private List<SearchFieldModel> advancedSearchFields;
	private List<KnowledgeBaseTreeModel> knowledgeBasesForRelationTypes = new ArrayList<KnowledgeBaseTreeModel>();

	private SearchConfigModel defaultSearchConfigModel;
	private ContentPanel resultsPanel;

	public void onModuleLoad() {
		compass2Service.startup(new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void arg0) {
				compass2Service
						.getAdvancedFields(new AsyncCallback<List<SearchFieldModel>>() {

							@Override
							public void onFailure(Throwable arg0) {
								if (arg0 instanceof BackEndException)
									errorMessage((BackEndException) arg0);
								else
									errorMessage(arg0);
							}

							@Override
							public void onSuccess(List<SearchFieldModel> arg0) {
								advancedSearchFields = arg0;
								compass2Service
										.getDefaultSearchConfig(new AsyncCallback<SearchConfigModel>() {

											@Override
											public void onSuccess(
													SearchConfigModel result) {
												defaultSearchConfigModel = result;
												createGui();
											}

											@Override
											public void onFailure(
													Throwable caught) {
												if (caught instanceof BackEndException)
													errorMessage((BackEndException) caught);
												else
													Compass2Main
															.errorLoad(caught);

											}
										});
							}
						});
			}

			@Override
			public void onFailure(Throwable arg0) {
				if (arg0 instanceof BackEndException)
					errorMessage((BackEndException) arg0);
				else
					errorMessage(arg0);
			}
		});

	}

	private void createGui() {
		main = new Viewport();
		main.setLayout(new BorderLayout());
		left = new ContentPanel(new RowLayout(Orientation.VERTICAL));
		right = new ContentPanel(new RowLayout(Orientation.VERTICAL));

		ContentPanel searchPanel = new ContentPanel();
		searchPanel.setCollapsible(true);
		searchPanel.setHeading(Compass2Main.i18n.titleSearch());
		searchPanel.setScrollMode(Scroll.AUTO);
		SearchForm searchForm = new SearchForm(advancedSearchFields,
				defaultSearchConfigModel, compass2Service) {

			@Override
			protected void loadRelationTypes(List<SelectedTreeModel> selectedKB) {
				super.loadRelationTypes(selectedKB);

				List<KnowledgeBaseTreeModel> results = new ArrayList<KnowledgeBaseTreeModel>();
				boolean update = false;
				for (BaseTreeModel baseTreeModel : selectedKB) {
					if (baseTreeModel instanceof KnowledgeBaseTreeModel) {
						results.add((KnowledgeBaseTreeModel) baseTreeModel);

						if (!knowledgeBasesForRelationTypes
								.contains(baseTreeModel)) {
							update = true;
						}
					}
				}
				for (KnowledgeBaseTreeModel knowledgeBaseTreeModel : knowledgeBasesForRelationTypes) {
					if (!results.contains(knowledgeBaseTreeModel)) {
						update = true;
					}
				}
				if (update) {
					knowledgeBasesForRelationTypes = results;
					createRelationTypesPanel();
				}
			}

		};
		searchForm.addSearchListener(new SearchListener() {

			@Override
			public void search(final SearchModel searchModel) {
				
				if (!searchModel.isAdvancedSearch() && (searchModel.getSearch() == null || "".equals(searchModel.getSearch()))) {
					return;
				}
				
				left.mask(Compass2Main.i18n.messageSearch());
				compass2Service.getSearchResult(searchModel,
						new AsyncCallback<ResultContainerModel>() {

							@Override
							public void onSuccess(
									ResultContainerModel searchResult) {

								createTopicExpansionsAndResultsPanel(
										searchModel, searchResult);
								left.unmask();
							}

							@Override
							public void onFailure(Throwable arg0) {
								if (arg0 instanceof BackEndException)
									errorMessage((BackEndException) arg0);
								else
									errorMessage(arg0);

								left.unmask();
							}
						});
			}

		});

		searchPanel.add(searchForm);
		searchPanel.setBodyBorder(false);

		relationTypesLayout = new LayoutContainer(new RowLayout(
				Orientation.VERTICAL));

		left.add(searchPanel, new RowData(1, 0, new Margins(0, 15, 0, 0)));
		left.add(relationTypesLayout, new RowData(1, 0, new Margins(10, 15, 0,
				0)));
		left.setScrollMode(Scroll.AUTO);
		left.setBorders(false);

		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 600);
		westData.setCollapsible(true);
		westData.setMargins(new Margins(20, 0, 20, 20));
		main.add(left, westData);
		left.setBodyBorder(false);
		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
		centerData.setMargins(new Margins(20, 20, 20, 0));
		main.add(right, centerData);
		right.setHeaderVisible(false);
		right.setBodyBorder(false);
		right.setScrollMode(Scroll.AUTO);

		createRelationTypesPanel();

		RootPanel.get().add(main);
	}

	private void createRelationTypesPanel() {
		relationTypesLayout.removeAll();
		for (KnowledgeBaseTreeModel kb : knowledgeBasesForRelationTypes) {
			ContentPanel relationTypesPanel = new ContentPanel(new RowLayout(
					Orientation.VERTICAL));
			relationTypesPanel.setCollapsible(true);
			relationTypesPanel.setScrollMode(Scroll.AUTO);
			relationTypesPanel.setHeading(Compass2Main.i18n
					.titleRelationTypes() + " - " + kb.getLabel());
			RelationTypesGrid relationTypesGrid = new RelationTypesGrid(
					compass2Service, kb);
			relationTypesPanel.add(relationTypesGrid);
			relationTypesLayout.add(relationTypesPanel);
		}
		left.layout();
	}

	private void createTopicExpansionsAndResultsPanel(SearchModel searchModel,
			ResultContainerModel searchResult) {
		right.removeAll();
		if (searchResult.getTopicExpansions() != null) {
			TopicExpansions topicExpansions = new TopicExpansions(
					searchResult.getTopicExpansions(), searchModel,
					compass2Service);
			topicExpansions.addSearchListener(new ResearchListener() {

				@Override
				public void search(final SearchModel searchModel,
						Collection<Long> unSelectedtopics,
						Map<Long, Collection<Long>> unSelectedNotOpenedTopics,
						Map<Long, Collection<String>> unSelectedNotOpenedTerms) {
					left.mask(Compass2Main.i18n.messageSearch());
					right.mask(Compass2Main.i18n.messageSearch());
					compass2Service.getSearchResult(searchModel,
							unSelectedtopics, unSelectedNotOpenedTopics,
							unSelectedNotOpenedTerms,
							new AsyncCallback<ResultContainerModel>() {

								@Override
								public void onSuccess(
										ResultContainerModel searchResult) {
									right.unmask();
									createResultsPanel(searchModel,
											searchResult);
									left.unmask();
								}

								@Override
								public void onFailure(Throwable arg0) {
									if (arg0 instanceof BackEndException)
										errorMessage((BackEndException) arg0);
									else
										errorMessage(arg0);
									left.unmask();
								}
							});
				}

			});
			right.add(topicExpansions, new RowData(1, -1, new Margins(0, 15,
					10, 20)));
		}
		right.add(createResultPanel(searchResult), new RowData(1, -1,
				new Margins(0, 15, 0, 20)));
		main.layout();
	}

	private void createResultsPanel(SearchModel searchModel,
			ResultContainerModel searchResult) {
		right.remove(resultsPanel);
		right.add(createResultPanel(searchResult), new RowData(1, -1,
				new Margins(0, 15, 0, 20)));
		main.layout();
	}

	private Widget createResultPanel(ResultContainerModel searchResult) {
		resultsPanel = new ContentPanel(new RowLayout(Orientation.VERTICAL));
		resultsPanel.setCollapsible(true);
		resultsPanel.setHeading(Compass2Main.i18n.titleResult() + " - "
				+ Compass2Main.i18n.titleResultsFounded() + " : "
				+ searchResult.getSearchResultNumber());
		resultsPanel.setScrollMode(Scroll.AUTO);
		SearchResultGrid searchResultGrid = new SearchResultGrid(
				compass2Service, searchResult.getSearchResultId());
		resultsPanel.add(searchResultGrid, new RowData(1, -1));
		return resultsPanel;
	}

	/**
	 * Show the error message.
	 * 
	 * @param arg0
	 *            The throwable error
	 */
	public static void errorMessage(Throwable t) {
		t.printStackTrace(System.out);
		t.printStackTrace(System.err);
		MessageBox.alert(i18n.titleError(), t.getMessage(), null);
	}

	/**
	 * Show the error message.
	 * 
	 * @param arg0
	 *            The throwable error
	 */
	public static void errorMessage(BackEndException t) {
		t.printStackTrace(System.out);
		t.printStackTrace(System.err);
		MessageBox.alert(i18n.titleError(),
				i18n.getString(Compass2Constans.ERROR_LABEL + t.getErrorID()),
				null);
	}

	/**
	 * Show the error message.
	 * 
	 * @param arg0
	 *            The string error
	 */
	public static void errorMessage(String s) {
		MessageBox.alert(i18n.titleError(), s.toString(), null);
	}

	public static void errorLoad(Throwable t) {
		t.printStackTrace(System.out);
		t.printStackTrace(System.err);
		MessageBox.alert(i18n.titleLoadError(), i18n.messageLoadError(), null);
	}

}