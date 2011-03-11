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
package no.ovitas.compass2.web.client.rpc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import no.ovitas.compass2.web.client.model.BaseListModel;
import no.ovitas.compass2.web.client.model.EntityModel;
import no.ovitas.compass2.web.client.model.ExceptionModel;
import no.ovitas.compass2.web.client.model.FolderModel;
import no.ovitas.compass2.web.client.model.KnowledgeBaseModel;
import no.ovitas.compass2.web.client.model.KnowledgeBaseTreeModel;
import no.ovitas.compass2.web.client.model.RelationTypesModel;
import no.ovitas.compass2.web.client.model.ResultContainerModel;
import no.ovitas.compass2.web.client.model.ResultModel;
import no.ovitas.compass2.web.client.model.SearchConfigModel;
import no.ovitas.compass2.web.client.model.SearchFieldModel;
import no.ovitas.compass2.web.client.model.SearchModel;
import no.ovitas.compass2.web.client.model.SelectedTreeModel;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author nyari
 * 
 */
public interface Compass2ServiceAsync {

	/**
	 * @param relationTypes
	 * @param callback
	 */
	void updateRelationTypes(List<RelationTypesModel> relationTypes, KnowledgeBaseTreeModel knowledgeBase, 
			AsyncCallback<Void> callback);

	void getAdvancedFields(AsyncCallback<List<SearchFieldModel>> callback);

	void getKBFileTypes(AsyncCallback<List<BaseListModel>> callback);

	void getImportedRelationTypes(
			AsyncCallback<List<RelationTypesModel>> callback);

	void getImportedScopes(AsyncCallback<List<EntityModel>> callback);

	void uploadScopes(List<EntityModel> scopes, AsyncCallback<Void> callback);

	void uploadRelationTypes(List<RelationTypesModel> modifiedRelationTypes,
			AsyncCallback<Void> callback);

	void getKBList(AsyncCallback<List<KnowledgeBaseModel>> callback);

	void getSearchResult(SearchModel searchModel,
			AsyncCallback<ResultContainerModel> callback);


	void getKBFiles(AsyncCallback<List<BaseListModel>> callback);


	void uploadKnowledgeBase(BaseListModel value, BaseListModel value2,
			KnowledgeBaseModel value3, String value4,
			Boolean twoWay, Boolean suggestionUpload, AsyncCallback<Void> asyncCallback);


	void getExpansionTreeById(FolderModel config, Long id, AsyncCallback<List<FolderModel>> callback);


	void getResultById(Long id, AsyncCallback<List<ResultModel>> callback);


	void getSearchResult(SearchModel searchModel,
			Collection<Long> selectedTopics, Map<Long, Collection<Long>> unSelectedNotOpenedTopics, Map<Long, Collection<String>> unSelectedNotOpenedTerms,
			AsyncCallback<ResultContainerModel> callback);


	void startup(AsyncCallback<Void> callback);


	void getRelationTypes(KnowledgeBaseTreeModel knowledgeBase,
			AsyncCallback<List<RelationTypesModel>> callback);


	void getKBTree(SelectedTreeModel loadConfig, AsyncCallback<List<SelectedTreeModel>> callback);


	void getDefaultSearchConfig(AsyncCallback<SearchConfigModel> callback);
		
}
