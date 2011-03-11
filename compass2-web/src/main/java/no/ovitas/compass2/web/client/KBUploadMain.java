/**
 * Created on 2010.08.25.
 *
 * Copyright (C) 2010 Thot-Soft 2002 Ltd. All Rights Reserved.
 *
 * This software is the proprietary information of ThotSoft 2002 Ltd.
 * Use is subject to license terms.
 *
 * $Id:$
 */
package no.ovitas.compass2.web.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import no.ovitas.compass2.web.client.components.AcceptedListener;
import no.ovitas.compass2.web.client.components.ImportRelationTypeEditWindow;
import no.ovitas.compass2.web.client.components.ImportScopeEditWindow;
import no.ovitas.compass2.web.client.model.BackEndException;
import no.ovitas.compass2.web.client.model.BaseListModel;
import no.ovitas.compass2.web.client.model.Compass2Constans;
import no.ovitas.compass2.web.client.model.ImportWebException;
import no.ovitas.compass2.web.client.model.KnowledgeBaseModel;
import no.ovitas.compass2.web.client.rpc.Compass2Service;
import no.ovitas.compass2.web.client.rpc.Compass2ServiceAsync;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author nyari
 * 
 */
public class KBUploadMain implements EntryPoint {

	public static Compass2ServiceAsync compass2Service = (Compass2ServiceAsync) GWT
			.create(Compass2Service.class);

	private Viewport main;
	private FormPanel formPanel;
	private BaseListLoader<ListLoadResult<BaseListModel>> fileTypeListLoader;
	private ListStore<BaseListModel> fileTypeListStore;
	private BaseListLoader<ListLoadResult<BaseListModel>> fileListLoader;
	private ListStore<BaseListModel> fileListStore;
	private BaseListLoader<ListLoadResult<KnowledgeBaseModel>> kbListLoader;
	private ListStore<KnowledgeBaseModel> kbListStore;
	private TextArea kbDescriptionField;
	private ComboBox<BaseListModel> kbFileComboBox;
	private ComboBox<BaseListModel> kbFileTypeComboBox;
	private ComboBox<KnowledgeBaseModel> kbComboBox;

	private RadioGroup kbType;

	private CheckBox suggestionProvider;

	@Override
	public void onModuleLoad() {
		compass2Service.startup(new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void arg0) {
				init();
				createGui();
				fileTypeListLoader.load();
			}

			@Override
			public void onFailure(Throwable arg0) {
				Compass2Main.errorMessage(arg0);
			}

		});
	}

	private void init() {
		DataProxy<List<BaseListModel>> proxy = new RpcProxy<List<BaseListModel>>() {
			@Override
			public void load(Object loadConfig,
					AsyncCallback<List<BaseListModel>> callback) {
				compass2Service.getKBFileTypes(callback);

			}
		};
		fileTypeListLoader = new BaseListLoader<ListLoadResult<BaseListModel>>(
				proxy) {

			@Override
			protected void onLoadFailure(Object loadConfig, Throwable t) {
				super.onLoadFailure(loadConfig, t);
				Compass2Main.errorLoad(t);
			}

		};
		fileTypeListStore = new ListStore<BaseListModel>(fileTypeListLoader);

		DataProxy<List<BaseListModel>> proxyFile = new RpcProxy<List<BaseListModel>>() {
			@Override
			public void load(Object loadConfig,
					AsyncCallback<List<BaseListModel>> callback) {
				compass2Service.getKBFiles(callback);

			}
		};
		fileListLoader = new BaseListLoader<ListLoadResult<BaseListModel>>(
				proxyFile);
		
		fileListLoader.addLoadListener(new LoadListener() {
			
			
			
			@Override
			public void loaderLoad(LoadEvent le) {
				super.loaderLoad(le);
				
				List<BaseListModel> models = le.getData();
				
				Iterator<BaseListModel> iterator = models.iterator();
				
				BaseListModel model;
				while (iterator.hasNext()) {
					model = iterator.next();
					
					if (model.getLabel().contains("-mapping.xml")) {
						iterator.remove();
					}
				}
				
			}
			
			@Override
			public void loaderLoadException(LoadEvent le) {
				super.loaderLoadException(le);
				Compass2Main.errorLoad(le.exception);
			}
		});
	
		fileListStore = new ListStore<BaseListModel>(fileListLoader);

		DataProxy<List<KnowledgeBaseModel>> kbProxy = new RpcProxy<List<KnowledgeBaseModel>>() {
			@Override
			public void load(Object loadConfig,
					AsyncCallback<List<KnowledgeBaseModel>> callback) {
				compass2Service.getKBList(callback);
			}
		};
		kbListLoader = new BaseListLoader<ListLoadResult<KnowledgeBaseModel>>(
				kbProxy) {

			@Override
			protected void onLoadFailure(Object loadConfig, Throwable t) {
				super.onLoadFailure(loadConfig, t);
				Compass2Main.errorLoad(t);
			}

		};
		
		
		kbListStore = new ListStore<KnowledgeBaseModel>(kbListLoader);
	}

	private void createGui() {
		main = new Viewport();
		main.setLayout(new RowLayout(Orientation.HORIZONTAL));

		formPanel = new FormPanel();
		formPanel.setHeading(Compass2Main.i18n.titleKBUpload());
		formPanel.setHeight(275);

		FormLayout form = new FormLayout();
		form.setLabelWidth(150);
		LayoutContainer layoutContainer = new LayoutContainer(form);
		layoutContainer.setStyleAttribute("padding-right", "10px");

		kbFileComboBox = new ComboBox<BaseListModel>();
		kbFileComboBox.setFieldLabel(Compass2Main.i18n.labelKBFile());
		kbFileComboBox.setValueField(Compass2Constans.VALUE);
		kbFileComboBox.setDisplayField(Compass2Constans.LABEL);
		kbFileComboBox.setEditable(false);
		kbFileComboBox.setTriggerAction(TriggerAction.ALL);
		kbFileComboBox.setStore(fileListStore);
		kbFileComboBox.setAllowBlank(false);

		layoutContainer.add(kbFileComboBox, new FormData("100%"));

		kbFileTypeComboBox = new ComboBox<BaseListModel>();
		kbFileTypeComboBox.setFieldLabel(Compass2Main.i18n.labelKBFileType());
		kbFileTypeComboBox.setValueField(Compass2Constans.VALUE);
		kbFileTypeComboBox.setDisplayField(Compass2Constans.LABEL);
		kbFileTypeComboBox.setEditable(false);
		kbFileTypeComboBox.setTriggerAction(TriggerAction.ALL);
		kbFileTypeComboBox.setStore(fileTypeListStore);
		kbFileTypeComboBox.setAllowBlank(false);

		layoutContainer.add(kbFileTypeComboBox, new FormData("100%"));

		kbComboBox = new ComboBox<KnowledgeBaseModel>();
		kbComboBox.setFieldLabel(Compass2Main.i18n.labelKB());
		kbComboBox.setValueField(Compass2Constans.VALUE);
		kbComboBox.setDisplayField(Compass2Constans.VALUE);
		kbComboBox.setEditable(true);
		kbComboBox.setTriggerAction(TriggerAction.ALL);
		kbComboBox.setStore(kbListStore);
		kbComboBox.setForceSelection(false);
		kbComboBox.setAllowBlank(false);
		kbComboBox
				.addSelectionChangedListener(new SelectionChangedListener<KnowledgeBaseModel>() {

					@Override
					public void selectionChanged(
							SelectionChangedEvent<KnowledgeBaseModel> se) {
						kbDescriptionField.setValue(se.getSelectedItem()
								.getDescription());
					}
				});

		layoutContainer.add(kbComboBox, new FormData("100%"));

		kbDescriptionField = new TextArea();
		kbDescriptionField.setName(Compass2Constans.KB_DESCRIPTION);
		layoutContainer.add(kbDescriptionField, new FormData("100%"));
		kbDescriptionField
				.setFieldLabel(Compass2Main.i18n.labelKBDescription());

		kbType = new RadioGroup();

		kbType.setFieldLabel(Compass2Main.i18n.labelKBType());

		Radio specGen = new Radio();
		specGen.setName(Compass2Constans.SPEC_GEN_TYPE);
		specGen.setItemId(Compass2Constans.SPEC_GEN_TYPE);// ,
															// Compass2Main.i18n.labelSpecGenType()
		specGen.setBoxLabel(Compass2Main.i18n.labelSpecGenType());

		kbType.add(specGen);

		Radio twoway = new Radio();
		twoway.setName(Compass2Constans.TWO_WAY);
		twoway.setItemId(Compass2Constans.TWO_WAY);// ,
													// Compass2Main.i18n.labelSpecGenType()
		twoway.setBoxLabel(Compass2Main.i18n.labelTwoWayType());

		kbType.add(twoway);

		kbType.setSelectionRequired(true);

		layoutContainer.add(kbType, new FormData("100%"));
		
		suggestionProvider = new CheckBox();
		suggestionProvider.setBoxLabel(Compass2Main.i18n.labelAddSuggestion());
		suggestionProvider.setHideLabel(true);
		
		layoutContainer.add(suggestionProvider, new FormData("100%"));

		Button uploadButton = new Button(Compass2Main.i18n.buttonUpload());
		uploadButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				if (formPanel.isValid()) {
					
					
					
					formPanel.mask(Compass2Main.i18n.messageUpload());
					KnowledgeBaseModel kb;
					if (kbComboBox.getValue() == null) {
						kb = new KnowledgeBaseModel();
						kb.setLabel(kbComboBox.getRawValue());
						kb.setId(Compass2Constans.NOT_IN_DATABASE);
					} else {
						kb = kbComboBox.getValue();
					}

					boolean twoWay = kbType.getValue().getItemId()
							.equals(Compass2Constans.TWO_WAY);

					compass2Service.uploadKnowledgeBase(
							kbFileComboBox.getValue(),
							kbFileTypeComboBox.getValue(), kb,
							kbDescriptionField.getValue(), twoWay, suggestionProvider.getValue(),
							new AsyncCallback<Void>() {

								@Override
								public void onSuccess(Void v) {
									formPanel.unmask();
									ImportScopeEditWindow window = new ImportScopeEditWindow(
											compass2Service);
									window.addWindowAcceptedListener(new AcceptedListener() {

										@Override
										public void accepted() {
											ImportRelationTypeEditWindow window = new ImportRelationTypeEditWindow(
													compass2Service);
											window.addWindowAcceptedListener(new AcceptedListener() {
												
												@Override
												public void accepted() {
													Compass2Main.errorMessage(Compass2Main.i18n.messageUploadFinished());
												}
												
											});											
											window.show();
										}

									});
									window.show();
								}

								@Override
								public void onFailure(Throwable t) {
									
									if (t instanceof BackEndException) {
										Compass2Main.errorMessage((BackEndException)t);
									} else 
									Compass2Main.errorMessage(t);
									
									formPanel.unmask();
									
								}
							});
				}
			}

		});

		layoutContainer.add(uploadButton, new FormData("100%"));

		formPanel.add(layoutContainer);

		main.add(formPanel, new RowData(0.5, -1, new Margins(20, 5, 5, 20)));

		RootPanel.get().add(main);
	}

}
