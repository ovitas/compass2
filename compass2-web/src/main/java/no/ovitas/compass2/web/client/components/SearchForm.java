/**
 * Created on 2010.07.16.
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.ovitas.compass2.model.ConnectingType;
import no.ovitas.compass2.model.FittingNumberType;
import no.ovitas.compass2.model.FittingType;
import no.ovitas.compass2.model.MatchingType;
import no.ovitas.compass2.web.client.Compass2Main;
import no.ovitas.compass2.web.client.model.BackEndException;
import no.ovitas.compass2.web.client.model.Compass2Constans;
import no.ovitas.compass2.web.client.model.KnowledgeBaseModel;
import no.ovitas.compass2.web.client.model.KnowledgeBaseTreeModel;
import no.ovitas.compass2.web.client.model.ScopeTreeModel;
import no.ovitas.compass2.web.client.model.SearchConfigModel;
import no.ovitas.compass2.web.client.model.SearchFieldModel;
import no.ovitas.compass2.web.client.model.SearchModel;
import no.ovitas.compass2.web.client.model.SelectedTreeModel;
import no.ovitas.compass2.web.client.rpc.Compass2ServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.CheckChangedEvent;
import com.extjs.gxt.ui.client.event.CheckChangedListener;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.extjs.gxt.ui.client.widget.treegrid.WidgetTreeGridCellRenderer;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.CheckCascade;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author nyari
 * 
 */
public class SearchForm extends LayoutContainer {

	protected SearchModel searchModel;
	protected FormBinding simpleFormBinding;
	protected FormBinding advancedFormBinding;
	protected List<SearchFieldModel> advancedSearchFields;
	protected FormPanel simpleForm;
	protected FormPanel advancedForm;
	private BaseTreeLoader<SelectedTreeModel> kbTreeLoaderSimple;
	private TreeStore<SelectedTreeModel> kbTreeStoreSimple;
	private TreeGrid<SelectedTreeModel> kbGridSimple;
	// private CheckBoxSelectionModel<BaseTreeModel> kbSelectionModelSimple;
	private BaseTreeLoader<SelectedTreeModel> kbTreeLoaderAdvanced;
	private TreeStore<SelectedTreeModel> kbTreeStoreAdvanced;
	private TreeGrid<SelectedTreeModel> kbGridAdvanced;
	// private CheckBoxSelectionModel<BaseTreeModel> kbSelectionModelAdvanced;
	private List<SearchListener> listeners = new ArrayList<SearchListener>();
	private SearchConfigModel defaultSearchConfigModel;

	public SearchForm(List<SearchFieldModel> advancedSearchFields,
			SearchConfigModel searchConfigModel,
			final Compass2ServiceAsync service) {
		super();
		this.advancedSearchFields = advancedSearchFields;
		defaultSearchConfigModel = searchConfigModel;

		setBorders(false);

		simpleForm = new FormPanel();
		simpleFormBinding = new FormBinding(simpleForm);

		advancedForm = new FormPanel();
		advancedFormBinding = new FormBinding(advancedForm);

		DataProxy<List<SelectedTreeModel>> kbProxySimple = new RpcProxy<List<SelectedTreeModel>>() {
			@Override
			public void load(Object loadConfig,
					AsyncCallback<List<SelectedTreeModel>> callback) {
				service.getKBTree((SelectedTreeModel) loadConfig, callback);

			}
		};
		kbTreeLoaderSimple = new BaseTreeLoader<SelectedTreeModel>(
				kbProxySimple) {

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
					List<SelectedTreeModel> result) {
				super.onLoadSuccess(loadConfig, result);
				for (SelectedTreeModel tree : result) {
					tree.setSelected(false);
				}
			}

			@Override
			public boolean hasChildren(SelectedTreeModel parent) {
				if (parent instanceof KnowledgeBaseTreeModel) {
					return true;
				}
				return false;
			}

		};
		kbTreeStoreSimple = new TreeStore<SelectedTreeModel>(kbTreeLoaderSimple);
		DataProxy<List<SelectedTreeModel>> kbProxyAdvanced = new RpcProxy<List<SelectedTreeModel>>() {
			@Override
			public void load(Object loadConfig,
					AsyncCallback<List<SelectedTreeModel>> callback) {
				service.getKBTree((SelectedTreeModel) loadConfig, callback);

			}
		};

		if (advancedSearchFields != null && advancedSearchFields.size() > 0) {
			kbTreeLoaderAdvanced = new BaseTreeLoader<SelectedTreeModel>(
					kbProxyAdvanced) {

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
						List<SelectedTreeModel> result) {
					super.onLoadSuccess(loadConfig, result);
					for (SelectedTreeModel tree : result) {
						tree.setSelected(false);
					}
				}

				@Override
				public boolean hasChildren(SelectedTreeModel parent) {
					if (parent instanceof KnowledgeBaseTreeModel) {
						return true;
					}
					return false;
				}

			};
			kbTreeStoreAdvanced = new TreeStore<SelectedTreeModel>(
					kbTreeLoaderAdvanced);

		}

		createDefaultSearchModel();
		createGui();

		kbTreeLoaderSimple.load();

		if (advancedSearchFields != null && advancedSearchFields.size() > 0) {
			kbTreeLoaderAdvanced.load();
		}
	}

	protected void createDefaultSearchModel() {
		searchModel = new SearchModel();
		searchModel.setHopCount(defaultSearchConfigModel.getHopCount());
		searchModel.setMaxTopicNumberToExpand(defaultSearchConfigModel
				.getMaxNumberOfTopicToExpand());
		searchModel.setExpansionThreshold(defaultSearchConfigModel
				.getTresholdWeight());
		searchModel.setResultThreshold(defaultSearchConfigModel
				.getResultTreshold());
		searchModel.setMaxNumberOfHits(defaultSearchConfigModel
				.getMaxNumberOfHits());
		searchModel.setTopicPrefixMatch(defaultSearchConfigModel
				.getPrefixMatch());
		searchModel.setFuzzyMatch(defaultSearchConfigModel
				.getFuzzySearchValue());
		searchModel.setTreeSearch(defaultSearchConfigModel.getTreeSearch());
		if (advancedSearchFields != null && advancedSearchFields.size() > 0) {
			int i = 0;
			for (SearchFieldModel searchField : advancedSearchFields) {
				switch (searchField.getType()) {
				case STRING:
					searchModel.set(searchField.getId(), "");
					searchModel.set(searchField.getId()
							+ Compass2Constans.FUZZY_MATCH, false);
					if (i == 0) {
						searchModel.set(searchField.getId()
								+ Compass2Constans.CONNECTION_TYPE,
								ConnectingType.FIRST.name());
					} else {
						searchModel.set(searchField.getId()
								+ Compass2Constans.CONNECTION_TYPE,
								ConnectingType.AND.name());
					}
					searchModel.set(searchField.getId()
							+ Compass2Constans.MATCH_TYPE,
							MatchingType.MATCH_ALL.name());
					searchModel.set(searchField.getId()
							+ Compass2Constans.FIT_TYPE,
							FittingType.WHOLE_WORD.name());
					break;
				case INTEGER:
					searchModel.set(searchField.getId(), new Integer(0));
					if (i == 0) {
						searchModel.set(searchField.getId()
								+ Compass2Constans.CONNECTION_TYPE,
								ConnectingType.FIRST.name());
					} else {
						searchModel.set(searchField.getId()
								+ Compass2Constans.CONNECTION_TYPE,
								ConnectingType.AND.name());
					}
					searchModel.set(searchField.getId()
							+ Compass2Constans.FIT_TYPE,
							FittingNumberType.EQUAL.name());
					break;
				case FLOAT:
					searchModel.set(searchField.getId(), new Float(0.0));
					if (i == 0) {
						searchModel.set(searchField.getId()
								+ Compass2Constans.CONNECTION_TYPE,
								ConnectingType.FIRST.name());
					} else {
						searchModel.set(searchField.getId()
								+ Compass2Constans.CONNECTION_TYPE,
								ConnectingType.AND.name());
					}
					searchModel.set(searchField.getId()
							+ Compass2Constans.FIT_TYPE,
							FittingNumberType.EQUAL.name());
					break;
				case DATEINTERVAL:
					DateWrapper dateWrapper = new DateWrapper();
					searchModel.set(searchField.getId()
							+ Compass2Constans.DATE_INTERVALLUM_START,
							dateWrapper.asDate());
					searchModel.set(searchField.getId()
							+ Compass2Constans.DATE_INTERVALLUM_END,
							dateWrapper.asDate());
					if (i == 0) {
						searchModel.set(searchField.getId()
								+ Compass2Constans.CONNECTION_TYPE,
								ConnectingType.FIRST.name());
					} else {
						searchModel.set(searchField.getId()
								+ Compass2Constans.CONNECTION_TYPE,
								ConnectingType.AND.name());
					}
					break;
				}
				i++;
			}
		}
		simpleFormBinding.bind(searchModel);
		advancedFormBinding.bind(searchModel);
	}

	protected void createSimpleForm(final FormPanel formPanel,
			FormBinding formBinding) {
		formPanel.setHeaderVisible(false);
		formPanel.setButtonAlign(HorizontalAlignment.RIGHT);
		formPanel.setBodyBorder(false);
		formPanel.setBorders(false);

		FieldSet baseFieldSet = new FieldSet();
		baseFieldSet.setLayout(new RowLayout(Orientation.VERTICAL));

		LayoutContainer baseLayoutContainer = new LayoutContainer(
				new RowLayout(Orientation.HORIZONTAL));
		baseLayoutContainer.setHeight(170);

		LayoutContainer leftLayoutContainer = new LayoutContainer();
		FormLayout leftFormLayout = new FormLayout();
		leftFormLayout.setLabelAlign(LabelAlign.TOP);
		leftLayoutContainer.setLayout(leftFormLayout);

		NumberField hopCount = new NumberField();
		hopCount.setFieldLabel(Compass2Main.i18n.labelHopCount());
		hopCount.setAllowBlank(false);
		hopCount.setAllowDecimals(false);
		hopCount.setAllowNegative(false);
		hopCount.setPropertyEditorType(Integer.class);
		formBinding.addFieldBinding(new FieldBinding(hopCount,
				Compass2Constans.HOP_COUNT));
		leftLayoutContainer.add(hopCount, new FormData("100%"));

		NumberField expansionThreshold = new NumberField();
		expansionThreshold.setFieldLabel(Compass2Main.i18n
				.labelExpansionThreshold());
		expansionThreshold.setAllowBlank(false);
		expansionThreshold.setAllowDecimals(true);
		expansionThreshold.setAllowNegative(false);
		expansionThreshold.setFormat(NumberFormat.getFormat("#.00#"));
		expansionThreshold.setPropertyEditorType(Double.class);
		formBinding.addFieldBinding(new FieldBinding(expansionThreshold,
				Compass2Constans.EXPANSION_THRESHOLD));
		leftLayoutContainer.add(expansionThreshold, new FormData("100%"));

		CheckBox topicPrefixMatch = new CheckBox();
		topicPrefixMatch.setBoxLabel(Compass2Main.i18n.labelTopicPrefixMatch());
		topicPrefixMatch.setHideLabel(true);
		formBinding.addFieldBinding(new FieldBinding(topicPrefixMatch,
				Compass2Constans.TOPIC_PREFIX_MATCH));
		leftLayoutContainer.add(topicPrefixMatch, new FormData("100%"));

		CheckBox fuzzyMatch = new CheckBox();
		fuzzyMatch.setBoxLabel(Compass2Main.i18n.labelFuzzyMatch());
		fuzzyMatch.setHideLabel(true);
		formBinding.addFieldBinding(new FieldBinding(fuzzyMatch,
				Compass2Constans.FUZZY_MATCH));
		leftLayoutContainer.add(fuzzyMatch, new FormData("100%"));

		CheckBox treeSearch = new CheckBox();
		treeSearch.setBoxLabel(Compass2Main.i18n.labelTreeSearch());
		treeSearch.setHideLabel(true);
		formBinding.addFieldBinding(new FieldBinding(treeSearch,
				Compass2Constans.TREE_SEARCH));
		leftLayoutContainer.add(treeSearch, new FormData("100%"));

		baseLayoutContainer.add(leftLayoutContainer, new RowData(0.5, 1.0,
				new Margins(0, 5, 0, 0)));

		LayoutContainer rightLayoutContainer = new LayoutContainer();
		FormLayout rightFormLayout = new FormLayout();
		rightFormLayout.setLabelAlign(LabelAlign.TOP);
		rightLayoutContainer.setLayout(rightFormLayout);

		NumberField maxTopicNumberToExpand = new NumberField();
		maxTopicNumberToExpand.setFieldLabel(Compass2Main.i18n
				.labelMaxTopicNumberToExpand());
		maxTopicNumberToExpand.setAllowBlank(false);
		maxTopicNumberToExpand.setAllowDecimals(false);
		maxTopicNumberToExpand.setAllowNegative(false);
		maxTopicNumberToExpand.setPropertyEditorType(Integer.class);
		formBinding.addFieldBinding(new FieldBinding(maxTopicNumberToExpand,
				Compass2Constans.MAX_TOPIC_NUMBER_TO_EXPAND));
		rightLayoutContainer.add(maxTopicNumberToExpand, new FormData("100%"));

		NumberField resultThreshold = new NumberField();
		resultThreshold.setFieldLabel(Compass2Main.i18n.labelResultThreshold());
		resultThreshold.setAllowBlank(false);
		resultThreshold.setAllowDecimals(true);
		resultThreshold.setAllowNegative(false);
		resultThreshold.setFormat(NumberFormat.getFormat("#.00#"));
		resultThreshold.setPropertyEditorType(Double.class);
		formBinding.addFieldBinding(new FieldBinding(resultThreshold,
				Compass2Constans.RESULT_THRESHOLD));
		rightLayoutContainer.add(resultThreshold, new FormData("100%"));

		NumberField maxNumberOfHits = new NumberField();
		maxNumberOfHits.setFieldLabel(Compass2Main.i18n.labelMaxNumberOfHits());
		maxNumberOfHits.setAllowBlank(false);
		maxNumberOfHits.setAllowDecimals(false);
		maxNumberOfHits.setAllowNegative(false);
		maxNumberOfHits.setPropertyEditorType(Integer.class);
		formBinding.addFieldBinding(new FieldBinding(maxNumberOfHits,
				Compass2Constans.MAX_NUMBER_OF_HITS));
		rightLayoutContainer.add(maxNumberOfHits, new FormData("100%"));

		baseLayoutContainer.add(rightLayoutContainer, new RowData(0.5, 1.0,
				new Margins(0, 0, 0, 5)));

		baseFieldSet.add(baseLayoutContainer);

		FieldSet kbField = new FieldSet();
		kbField.setHeading(Compass2Main.i18n.titleKBGroup());

		// kbSelectionModelSimple = new CheckBoxSelectionModel<BaseTreeModel>();
		// kbSelectionModelSimple.setSelectionMode(SelectionMode.MULTI);
		// kbSelectionModelSimple
		// .addSelectionChangedListener(new
		// SelectionChangedListener<BaseTreeModel>() {
		//
		// @Override
		// public void selectionChanged(
		// SelectionChangedEvent<BaseTreeModel> se) {
		// loadRelationTypes(se.getSelection());
		// }
		// });

		kbGridSimple = createKnowledgeBaseGrid(kbTreeStoreSimple);

		// kbGridSimple.setSelectionModel(kbSelectionModelSimple);

		kbGridSimple.setBorders(false);
		kbField.add(kbGridSimple, new FormData("100%"));

		baseFieldSet.add(kbField, new FormData("100%"));

		formPanel.add(baseFieldSet, new FormData("100%"));
		baseFieldSet.setHeading(Compass2Main.i18n.titleBaseSettings());

		TextField<String> searchText = new TextField<String>();
		searchText.setFieldLabel(Compass2Main.i18n.labelSearch());
		searchText.setAllowBlank(true);
		formBinding.addFieldBinding(new FieldBinding(searchText,
				Compass2Constans.SEARCH));
		formPanel.add(searchText, new FormData("100%"));

		Button searchButton = new Button(Compass2Main.i18n.buttonSearch());
		formPanel.add(searchButton, new FormData("100%"));
		searchButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				if (formPanel.isValid()) {
					searchModel.setAdvancedSearch(false);
					Map<KnowledgeBaseTreeModel, Set<ScopeTreeModel>> knowledgeBase = new HashMap<KnowledgeBaseTreeModel, Set<ScopeTreeModel>>();

					List<SelectedTreeModel> checkedSelection = getSelectedKBTreeModels(kbTreeStoreSimple);

					for (BaseTreeModel baseTreeModel : checkedSelection) {
						if (baseTreeModel instanceof KnowledgeBaseTreeModel) {
							knowledgeBase.put(
									(KnowledgeBaseTreeModel) baseTreeModel,
									new HashSet<ScopeTreeModel>(0));
						}
					}

					KnowledgeBaseTreeModel parent;
					Set<ScopeTreeModel> scopeTree;
					for (BaseTreeModel baseTreeModel : checkedSelection) {
						if (baseTreeModel instanceof ScopeTreeModel) {
							if (baseTreeModel.getParent() != null
									&& baseTreeModel.getParent() instanceof KnowledgeBaseTreeModel) {
								parent = (KnowledgeBaseTreeModel) baseTreeModel
										.getParent();

								scopeTree = knowledgeBase.get(parent);

								if (scopeTree != null) {
									scopeTree
											.add((ScopeTreeModel) baseTreeModel);
								}
							}
						}
					}

					searchModel.setKnowledgeBaseList(knowledgeBase);
					fireSearch(searchModel);
				}
			}

		});

	}

	private List<SelectedTreeModel> getSelectedKBTreeModels(
			TreeStore<SelectedTreeModel> kbTreeStoreSimple) {

		List<SelectedTreeModel> selectedModels = new ArrayList<SelectedTreeModel>();

		List<SelectedTreeModel> allItems = kbTreeStoreSimple.getAllItems();

		for (SelectedTreeModel baseTreeModel : allItems) {
			if (baseTreeModel.isSelected()) {
				selectedModels.add(baseTreeModel);
			}
		}

		return selectedModels;
	}

	protected void createAdvancedForm(final FormPanel formPanel,
			FormBinding formBinding) {

		formPanel.setHeaderVisible(false);
		formPanel.setButtonAlign(HorizontalAlignment.RIGHT);
		formPanel.setBodyBorder(false);
		formPanel.setBorders(false);

		FieldSet baseFieldSet = new FieldSet();
		baseFieldSet.setLayout(new RowLayout(Orientation.VERTICAL));

		LayoutContainer baseLayoutContainer = new LayoutContainer(
				new RowLayout(Orientation.HORIZONTAL));
		baseLayoutContainer.setHeight(145);

		LayoutContainer leftLayoutContainer = new LayoutContainer();
		FormLayout leftFormLayout = new FormLayout();
		leftFormLayout.setLabelAlign(LabelAlign.TOP);
		leftLayoutContainer.setLayout(leftFormLayout);

		NumberField hopCount = new NumberField();
		hopCount.setFieldLabel(Compass2Main.i18n.labelHopCount());
		hopCount.setAllowBlank(false);
		hopCount.setAllowDecimals(false);
		hopCount.setAllowNegative(false);
		hopCount.setPropertyEditorType(Integer.class);
		formBinding.addFieldBinding(new FieldBinding(hopCount,
				Compass2Constans.HOP_COUNT));
		leftLayoutContainer.add(hopCount, new FormData("100%"));

		NumberField expansionThreshold = new NumberField();
		expansionThreshold.setFieldLabel(Compass2Main.i18n
				.labelExpansionThreshold());
		expansionThreshold.setAllowBlank(false);
		expansionThreshold.setAllowDecimals(true);
		expansionThreshold.setAllowNegative(false);
		expansionThreshold.setFormat(NumberFormat.getFormat("#.00#"));
		expansionThreshold.setPropertyEditorType(Double.class);
		formBinding.addFieldBinding(new FieldBinding(expansionThreshold,
				Compass2Constans.EXPANSION_THRESHOLD));
		leftLayoutContainer.add(expansionThreshold, new FormData("100%"));

		CheckBox topicPrefixMatch = new CheckBox();
		topicPrefixMatch.setBoxLabel(Compass2Main.i18n.labelTopicPrefixMatch());
		topicPrefixMatch.setHideLabel(true);
		formBinding.addFieldBinding(new FieldBinding(topicPrefixMatch,
				Compass2Constans.TOPIC_PREFIX_MATCH));
		leftLayoutContainer.add(topicPrefixMatch, new FormData("100%"));

		CheckBox treeSearch = new CheckBox();
		treeSearch.setBoxLabel(Compass2Main.i18n.labelTreeSearch());
		treeSearch.setHideLabel(true);
		formBinding.addFieldBinding(new FieldBinding(treeSearch,
				Compass2Constans.TREE_SEARCH));
		leftLayoutContainer.add(treeSearch, new FormData("100%"));

		baseLayoutContainer.add(leftLayoutContainer, new RowData(0.5, 1.0,
				new Margins(0, 0, 0, 0)));

		LayoutContainer rightLayoutContainer = new LayoutContainer();
		FormLayout rightFormLayout = new FormLayout();
		rightFormLayout.setLabelAlign(LabelAlign.TOP);
		rightLayoutContainer.setLayout(rightFormLayout);

		NumberField maxTopicNumberToExpand = new NumberField();
		maxTopicNumberToExpand.setFieldLabel(Compass2Main.i18n
				.labelMaxTopicNumberToExpand());
		maxTopicNumberToExpand.setAllowBlank(false);
		maxTopicNumberToExpand.setAllowDecimals(false);
		maxTopicNumberToExpand.setAllowNegative(false);
		maxTopicNumberToExpand.setPropertyEditorType(Integer.class);
		formBinding.addFieldBinding(new FieldBinding(maxTopicNumberToExpand,
				Compass2Constans.MAX_TOPIC_NUMBER_TO_EXPAND));
		rightLayoutContainer.add(maxTopicNumberToExpand, new FormData("100%"));

		NumberField resultThreshold = new NumberField();
		resultThreshold.setFieldLabel(Compass2Main.i18n.labelResultThreshold());
		resultThreshold.setAllowBlank(false);
		resultThreshold.setAllowDecimals(true);
		resultThreshold.setAllowNegative(false);
		resultThreshold.setFormat(NumberFormat.getFormat("#.00#"));
		resultThreshold.setPropertyEditorType(Double.class);
		formBinding.addFieldBinding(new FieldBinding(resultThreshold,
				Compass2Constans.RESULT_THRESHOLD));
		rightLayoutContainer.add(resultThreshold, new FormData("100%"));

		NumberField maxNumberOfHits = new NumberField();
		maxNumberOfHits.setFieldLabel(Compass2Main.i18n.labelMaxNumberOfHits());
		maxNumberOfHits.setAllowBlank(false);
		maxNumberOfHits.setAllowDecimals(false);
		maxNumberOfHits.setAllowNegative(false);
		maxNumberOfHits.setPropertyEditorType(Integer.class);
		formBinding.addFieldBinding(new FieldBinding(maxNumberOfHits,
				Compass2Constans.MAX_NUMBER_OF_HITS));
		rightLayoutContainer.add(maxNumberOfHits, new FormData("100%"));

		baseLayoutContainer.add(rightLayoutContainer, new RowData(0.5, 1.0,
				new Margins(0, 0, 0, 10)));

		baseFieldSet.add(baseLayoutContainer);

		FieldSet kbField = new FieldSet();
		kbField.setHeading(Compass2Main.i18n.titleKBGroup());

		// kbSelectionModelAdvanced = new
		// CheckBoxSelectionModel<SelectedTreeModel>();
		// kbSelectionModelAdvanced.setSelectionMode(SelectionMode.MULTI);
		// kbSelectionModelAdvanced
		// .addSelectionChangedListener(new
		// SelectionChangedListener<SelectedTreeModel>() {
		//
		// @Override
		// public void selectionChanged(
		// SelectionChangedEvent<SelectedTreeModel> se) {
		// loadRelationTypes(se.getSelection());
		// }
		// });

		kbGridAdvanced = createKnowledgeBaseGrid(kbTreeStoreAdvanced);

		kbGridAdvanced.setBorders(false);
		kbField.add(kbGridAdvanced, new FormData("100%"));

		baseFieldSet.add(kbField, new FormData("100%"));

		formPanel.add(baseFieldSet, new FormData("100%"));
		baseFieldSet.setHeading(Compass2Main.i18n.titleBaseSettings());

		FieldSet advancedFieldSet = new FieldSet();
		if (advancedSearchFields != null && advancedSearchFields.size() > 0) {
			int i = 0;
			for (SearchFieldModel searchField : advancedSearchFields) {
				switch (searchField.getType()) {
				case STRING:
					if (i != 0) {
						advancedFieldSet.add(
								createConnectField(searchField,
										advancedFormBinding), new RowData(1.0,
										25.0, new Margins()));
					}
					advancedFieldSet.add(new TextSearchFieldWidget(searchField,
							advancedFormBinding), new RowData(1.0, 50.0,
							new Margins()));
					break;
				case FLOAT:
					if (i != 0) {
						advancedFieldSet.add(
								createConnectField(searchField,
										advancedFormBinding), new RowData(1.0,
										25.0, new Margins()));
					}
					advancedFieldSet.add(
							createFloatField(searchField, advancedFormBinding),
							new RowData(1.0, 25.0, new Margins()));
					break;
				case INTEGER:
					if (i != 0) {
						advancedFieldSet.add(
								createConnectField(searchField,
										advancedFormBinding), new RowData(1.0,
										25.0, new Margins()));
					}
					advancedFieldSet
							.add(createIntegerField(searchField,
									advancedFormBinding), new RowData(1.0,
									25.0, new Margins()));
					break;
				case DATEINTERVAL:
					if (i != 0) {
						advancedFieldSet.add(
								createConnectField(searchField,
										advancedFormBinding), new RowData(1.0,
										25.0, new Margins()));
					}
					advancedFieldSet.add(
							createDateIntervalField(searchField,
									advancedFormBinding), new RowData(1.0,
									25.0, new Margins()));
					break;
				}
				i++;
			}
		}
		formPanel.add(advancedFieldSet, new FormData("100%"));
		advancedFieldSet.setHeading(Compass2Main.i18n.titleAdvancedSettings());

		Button searchButton = new Button(Compass2Main.i18n.buttonSearch());
		formPanel.add(searchButton, new FormData("100%"));
		searchButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				if (formPanel.isValid()) {
					searchModel.setAdvancedSearch(true);

					Map<KnowledgeBaseTreeModel, Set<ScopeTreeModel>> knowledgeBase = new HashMap<KnowledgeBaseTreeModel, Set<ScopeTreeModel>>();

					List<SelectedTreeModel> checkedSelection = getSelectedKBTreeModels(kbTreeStoreAdvanced);

					for (BaseTreeModel baseTreeModel : checkedSelection) {
						if (baseTreeModel instanceof KnowledgeBaseTreeModel) {
							knowledgeBase.put(
									(KnowledgeBaseTreeModel) baseTreeModel,
									new HashSet<ScopeTreeModel>(0));
						}
					}

					KnowledgeBaseTreeModel parent;
					Set<ScopeTreeModel> scopeTree;
					for (BaseTreeModel baseTreeModel : checkedSelection) {
						if (baseTreeModel instanceof ScopeTreeModel) {
							if (baseTreeModel.getParent() != null
									&& baseTreeModel.getParent() instanceof KnowledgeBaseTreeModel) {
								parent = (KnowledgeBaseTreeModel) baseTreeModel
										.getParent();

								scopeTree = knowledgeBase.get(parent);

								if (scopeTree != null) {
									scopeTree
											.add((ScopeTreeModel) baseTreeModel);
								}
							}
						}
					}

					searchModel.setKnowledgeBaseList(knowledgeBase);
					fireSearch(searchModel);
				}
			}
		});
	}

	protected TreeGrid<SelectedTreeModel> createKnowledgeBaseGrid(
			final TreeStore<SelectedTreeModel> kbTreeStoreSimple2) {

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig selectionColumn = new ColumnConfig();

		SelectionCheckBoxRenderer<SelectedTreeModel> selectionRenderer = new SelectionCheckBoxRenderer<SelectedTreeModel>();
		selectionRenderer
				.addValueChangeHandler(new SelectionCheckBoxCellValueChangeHandler<SelectedTreeModel>() {

					@Override
					public void onValueChanged(SelectedTreeModel model,
							ValueChangeEvent<Boolean> event) {

						model.setSelected(event.getValue());
						loadRelationTypes(getSelectedKBTreeModels(kbTreeStoreSimple2));
					}
				});

		selectionColumn.setRenderer(selectionRenderer);

		selectionColumn.setWidth(80);

		configs.add(selectionColumn);

		ColumnConfig column = new ColumnConfig(Compass2Constans.LABEL,
				Compass2Main.i18n.labelKBScopeName(), 200);
		column.setRenderer(new TreeGridCellRenderer<BaseTreeModel>());
		configs.add(column);

		column = new ColumnConfig(Compass2Constans.HAS_AHEAD_TREE,
				Compass2Main.i18n.labelAheadTree(), 80);
		CheckBoxCellRenderer<BaseTreeModel> renderer = new CheckBoxCellRenderer<BaseTreeModel>();
		renderer.addExcludeType(ScopeTreeModel.class);
		renderer.addValueChangeHandler(new CheckBoxCellValueChangeHandler<BaseTreeModel>() {

			@Override
			public void onValueChanged(BaseTreeModel model,
					ValueChangeEvent<Boolean> event) {

				if (model instanceof KnowledgeBaseTreeModel) {
					((KnowledgeBaseTreeModel) model).setAheadTree(event
							.getValue());
				}

			}
		});
		column.setRenderer(renderer);
		configs.add(column);

		column = new ColumnConfig(Compass2Constans.HAS_BACK_TREE,
				Compass2Main.i18n.labelBackTree(), 80);
		renderer = new CheckBoxCellRenderer<BaseTreeModel>();
		renderer.addExcludeType(ScopeTreeModel.class);
		renderer.addValueChangeHandler(new CheckBoxCellValueChangeHandler<BaseTreeModel>() {

			@Override
			public void onValueChanged(BaseTreeModel model,
					ValueChangeEvent<Boolean> event) {

				if (model instanceof KnowledgeBaseTreeModel) {
					((KnowledgeBaseTreeModel) model).setBackTree(event
							.getValue());
				}

			}
		});
		column.setRenderer(renderer);
		configs.add(column);

		column = new ColumnConfig(Compass2Constans.HAS_ALL_TREE,
				Compass2Main.i18n.labelAllTree(), 80);
		renderer = new CheckBoxCellRenderer<BaseTreeModel>();
		renderer.addExcludeType(ScopeTreeModel.class);
		renderer.addValueChangeHandler(new CheckBoxCellValueChangeHandler<BaseTreeModel>() {

			@Override
			public void onValueChanged(BaseTreeModel model,
					ValueChangeEvent<Boolean> event) {

				if (model instanceof KnowledgeBaseTreeModel) {
					((KnowledgeBaseTreeModel) model).setAllTree(event
							.getValue());
				}

			}
		});
		column.setRenderer(renderer);
		configs.add(column);

		ColumnModel columModel = new ColumnModel(configs);

		final TreeGrid<SelectedTreeModel> grid = new TreeGrid<SelectedTreeModel>(
				kbTreeStoreSimple2, columModel);

		grid.setHeight(200);
		grid.setBorders(true);

		// grid.addCheckListener(new CheckChangedListener<BaseTreeModel>() {
		//
		// @Override
		// public void checkChanged(CheckChangedEvent<BaseTreeModel> event) {
		// super.checkChanged(event);
		// loadRelationTypes(grid.getCheckedSelection());
		// }
		//
		// });

		return grid;
	}

	protected void loadRelationTypes(List<SelectedTreeModel> list) {
		// default action
	}

	protected Widget createDateIntervalField(SearchFieldModel searchField,
			FormBinding formBinding) {
		LayoutContainer layoutContainer = new LayoutContainer();
		layoutContainer.setLayout(new RowLayout(Orientation.VERTICAL));
		layoutContainer.setHeight(45);

		LayoutContainer labelLayoutContainer = new LayoutContainer();
		FormLayout labelFormLayout = new FormLayout();
		labelFormLayout.setHideLabels(true);
		labelLayoutContainer.setLayout(labelFormLayout);
		labelLayoutContainer.setHeight(20);

		labelLayoutContainer.add(new LabelField(searchField.getLabel()));

		layoutContainer.add(labelLayoutContainer);

		LayoutContainer dateLayoutContainer = new LayoutContainer();
		dateLayoutContainer.setLayout(new RowLayout(Orientation.HORIZONTAL));
		dateLayoutContainer.setHeight(25);

		LayoutContainer fromLayoutContainer = new LayoutContainer();
		FormLayout fromFormLayout = new FormLayout();
		fromFormLayout.setLabelWidth(35);
		fromLayoutContainer.setLayout(fromFormLayout);

		DateField fromDateField = new DateField();
		fromLayoutContainer.add(fromDateField, new FormData("100%"));
		fromDateField.setFieldLabel(Compass2Main.i18n.labelStartDate());
		fromDateField.getPropertyEditor().setFormat(
				DateTimeFormat.getShortDateFormat());
		dateLayoutContainer.add(fromLayoutContainer, new RowData(0.5, 1.0,
				new Margins()));
		formBinding.addFieldBinding(new FieldBinding(fromDateField, searchField
				.getId() + Compass2Constans.DATE_INTERVALLUM_START));

		LayoutContainer toLayoutContainer = new LayoutContainer();
		FormLayout toFormLayout = new FormLayout();
		toFormLayout.setLabelWidth(15);
		toFormLayout.setLabelSeparator("");
		toLayoutContainer.setLayout(toFormLayout);

		DateField toDateField = new DateField();
		toLayoutContainer.add(toDateField, new FormData("100%"));
		toDateField.setFieldLabel(Compass2Main.i18n.labelEndDate());
		dateLayoutContainer.add(toLayoutContainer, new RowData(0.5, 1.0,
				new Margins(0, 0, 0, 10)));
		formBinding.addFieldBinding(new FieldBinding(toDateField, searchField
				.getId() + Compass2Constans.DATE_INTERVALLUM_END));

		layoutContainer.add(dateLayoutContainer);

		return layoutContainer;
	}

	protected Widget createFloatField(SearchFieldModel searchField,
			FormBinding formBinding) {
		LayoutContainer layoutContainer = new LayoutContainer();
		layoutContainer.setLayout(new RowLayout(Orientation.HORIZONTAL));
		layoutContainer.setHeight(25);

		LayoutContainer fieldLayoutContainer = new LayoutContainer();
		fieldLayoutContainer.setLayout(new FormLayout());

		NumberField floatField = new NumberField();
		floatField.setAllowBlank(false);
		floatField.setAllowDecimals(true);
		floatField.setAllowNegative(false);
		floatField.setPropertyEditorType(Float.class);
		fieldLayoutContainer.add(floatField, new FormData("70%"));
		floatField.setFieldLabel(searchField.getLabel());
		formBinding.addFieldBinding(new FieldBinding(floatField, searchField
				.getId()));

		layoutContainer.add(fieldLayoutContainer, new RowData(0.7, 1,
				new Margins()));

		layoutContainer.add(createFittingNumberField(searchField, formBinding),
				new RowData(0.30, 1.0, new Margins(0, 0, 0, 10)));

		return layoutContainer;
	}

	protected Widget createIntegerField(SearchFieldModel searchField,
			FormBinding formBinding) {
		LayoutContainer layoutContainer = new LayoutContainer();
		layoutContainer.setLayout(new RowLayout(Orientation.HORIZONTAL));
		layoutContainer.setHeight(25);

		LayoutContainer fieldLayoutContainer = new LayoutContainer();
		fieldLayoutContainer.setLayout(new FormLayout());

		NumberField integerField = new NumberField();
		integerField.setAllowBlank(false);
		integerField.setAllowDecimals(false);
		integerField.setAllowNegative(false);
		integerField.setPropertyEditorType(Integer.class);
		fieldLayoutContainer.add(integerField, new FormData("70%"));
		integerField.setFieldLabel(searchField.getLabel());
		formBinding.addFieldBinding(new FieldBinding(integerField, searchField
				.getId()));

		layoutContainer.add(fieldLayoutContainer, new RowData(0.7, 1,
				new Margins()));

		layoutContainer.add(createFittingNumberField(searchField, formBinding),
				new RowData(0.30, 1.0, new Margins(0, 0, 0, 10)));

		return layoutContainer;
	}

	protected Widget createFittingNumberField(SearchFieldModel searchField,
			FormBinding formBinding) {
		LayoutContainer layoutContainer = new LayoutContainer();
		layoutContainer.setLayout(new FormLayout());

		RadioGroup radioGroup = new RadioGroup();
		radioGroup.setSelectionRequired(true);
		radioGroup.setHideLabel(true);

		Radio rdEqual = new Radio();
		rdEqual.setHideLabel(true);
		rdEqual.setBoxLabel(FittingNumberType.EQUAL.getTitle());
		rdEqual.setValueAttribute(FittingNumberType.EQUAL.name());
		radioGroup.add(rdEqual);

		Radio rdLesserThan = new Radio();
		rdLesserThan.setHideLabel(true);
		rdLesserThan.setBoxLabel(FittingNumberType.LESS_THAN.getTitle());
		rdLesserThan.setValueAttribute(FittingNumberType.LESS_THAN.name());
		radioGroup.add(rdLesserThan);

		Radio rdGreaterThan = new Radio();
		rdGreaterThan.setHideLabel(true);
		rdGreaterThan.setBoxLabel(FittingNumberType.GREATER_THAN.getTitle());
		rdGreaterThan.setValueAttribute(FittingNumberType.GREATER_THAN.name());
		radioGroup.add(rdGreaterThan);

		FieldBinding fieldBinding = new FieldBinding(radioGroup,
				searchField.getId() + Compass2Constans.FIT_TYPE);
		fieldBinding.setConverter(new RadioGroupConverter(radioGroup));
		formBinding.addFieldBinding(fieldBinding);

		layoutContainer.add(radioGroup, new FormData("100%"));

		return layoutContainer;
	}

	protected Widget createConnectField(SearchFieldModel searchField,
			FormBinding formBinding) {
		LayoutContainer layoutContainer = new LayoutContainer();
		layoutContainer.setLayout(new FormLayout());

		RadioGroup radioGroup = new RadioGroup();
		radioGroup.setSelectionRequired(true);
		radioGroup.setHideLabel(true);

		Radio rdAnd = new Radio();
		radioGroup.add(rdAnd);
		rdAnd.setBoxLabel(ConnectingType.AND.getTitle());
		rdAnd.setValueAttribute(ConnectingType.AND.name());

		Radio rdOr = new Radio();
		radioGroup.add(rdOr);
		rdOr.setBoxLabel(ConnectingType.OR.getTitle());
		rdOr.setValueAttribute(ConnectingType.OR.name());

		Radio rdNot = new Radio();
		radioGroup.add(rdNot);
		rdNot.setBoxLabel(ConnectingType.NOT.getTitle());
		rdNot.setValueAttribute(ConnectingType.NOT.name());

		FieldBinding fieldBinding = new FieldBinding(radioGroup,
				searchField.getId() + Compass2Constans.CONNECTION_TYPE);
		fieldBinding.setConverter(new RadioGroupConverter(radioGroup));
		formBinding.addFieldBinding(fieldBinding);

		layoutContainer.add(radioGroup, new FormData("100%"));

		return layoutContainer;
	}

	protected void createGui() {
		TabPanel tabPanel = new TabPanel();
		TabItem simpleTab = new TabItem(Compass2Main.i18n.titleSimple());
		createSimpleForm(simpleForm, simpleFormBinding);
		simpleTab.add(simpleForm);
		simpleTab.setBorders(false);
		simpleTab.addListener(Events.Select, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				loadRelationTypes(getSelectedKBTreeModels(kbTreeStoreSimple));
			}
		});
		tabPanel.add(simpleTab);
		if (advancedSearchFields != null && advancedSearchFields.size() > 0) {
			TabItem advancedTab = new TabItem(Compass2Main.i18n.titleAdvanced());
			createAdvancedForm(advancedForm, advancedFormBinding);
			advancedTab.add(advancedForm);
			advancedTab.setBorders(false);
			advancedTab.addListener(Events.Select, new Listener<BaseEvent>() {

				@Override
				public void handleEvent(BaseEvent be) {
					loadRelationTypes(getSelectedKBTreeModels(kbTreeStoreSimple));
				}
			});
			tabPanel.add(advancedTab);
		}
		tabPanel.setAutoHeight(true);
		tabPanel.setBorders(false);
		add(tabPanel);
	}

	public void addSearchListener(SearchListener l) {
		listeners.add(l);
	}

	public void removeSearchListener(SearchListener l) {
		listeners.remove(l);
	}

	public void fireSearch(SearchModel searchModel) {
		for (SearchListener l : listeners) {
			l.search(searchModel);
		}
	}

}
