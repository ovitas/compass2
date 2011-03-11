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

import no.ovitas.compass2.web.client.model.BackEndException;
import no.ovitas.compass2.web.client.model.BaseListModel;
import no.ovitas.compass2.web.client.model.EntityModel;
import no.ovitas.compass2.web.client.model.ExceptionModel;
import no.ovitas.compass2.web.client.model.FolderModel;
import no.ovitas.compass2.web.client.model.ImportWebException;
import no.ovitas.compass2.web.client.model.KnowledgeBaseModel;
import no.ovitas.compass2.web.client.model.KnowledgeBaseTreeModel;
import no.ovitas.compass2.web.client.model.RelationTypesModel;
import no.ovitas.compass2.web.client.model.ResultContainerModel;
import no.ovitas.compass2.web.client.model.ResultModel;
import no.ovitas.compass2.web.client.model.SearchConfigModel;
import no.ovitas.compass2.web.client.model.SearchFieldModel;
import no.ovitas.compass2.web.client.model.SearchModel;
import no.ovitas.compass2.web.client.model.SelectedTreeModel;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author nyari
 *
 */
@RemoteServiceRelativePath("../compass2Service")
public interface Compass2Service extends RemoteService {

	List<RelationTypesModel> getImportedRelationTypes() throws BackEndException;
	void uploadScopes(List<EntityModel> scopes) throws BackEndException;
	void uploadRelationTypes(List<RelationTypesModel> modifiedRelationTypes) throws BackEndException;
	List<EntityModel> getImportedScopes() throws BackEndException;
	List<SearchFieldModel> getAdvancedFields() throws BackEndException;
	List<BaseListModel> getKBFileTypes() throws BackEndException;
	List<BaseListModel> getKBFiles() throws BackEndException;
	List<KnowledgeBaseModel> getKBList() throws BackEndException;
	ResultContainerModel getSearchResult(SearchModel searchModel) throws BackEndException;
	ResultContainerModel getSearchResult(SearchModel searchModel, Collection<Long> selectedTopics, Map<Long, Collection<Long>> unSelectedNotOpenedTopics, Map<Long, Collection<String>> unSelectedNotOpenedTerms) throws BackEndException;
	void uploadKnowledgeBase(BaseListModel file, BaseListModel fileType, KnowledgeBaseModel kb, String description, Boolean twoWay, Boolean suggestionUpload) throws BackEndException;
	
	List<FolderModel> getExpansionTreeById(FolderModel config, Long id) throws BackEndException;
	List<ResultModel> getResultById(Long id) throws BackEndException;
	
	void startup();
	List<RelationTypesModel> getRelationTypes(
			KnowledgeBaseTreeModel knowledgeBase) throws BackEndException;
	void updateRelationTypes(List<RelationTypesModel> relationTypes,
			KnowledgeBaseTreeModel knowledgeBase) throws BackEndException;
	List<SelectedTreeModel> getKBTree(SelectedTreeModel model) throws BackEndException;
	SearchConfigModel getDefaultSearchConfig() throws BackEndException;
	
}
