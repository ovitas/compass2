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
package no.ovitas.compass2.web.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.ovitas.compass2.config.CompassManager;
import no.ovitas.compass2.config.factory.CompassManagerFactory;
import no.ovitas.compass2.config.settings.SearchField;
import no.ovitas.compass2.config.settings.SearchOptions;
import no.ovitas.compass2.exception.CompassException;
import no.ovitas.compass2.exception.ImportException;
import no.ovitas.compass2.kb.KnowledgeBase;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseType;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.RelationTypeSetting;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.web.client.model.BackEndException;
import no.ovitas.compass2.web.client.model.BaseListModel;
import no.ovitas.compass2.web.client.model.Compass2Constans;
import no.ovitas.compass2.web.client.model.EntityModel;
import no.ovitas.compass2.web.client.model.ExceptionModel;
import no.ovitas.compass2.web.client.model.FolderModel;
import no.ovitas.compass2.web.client.model.ImportWebException;
import no.ovitas.compass2.web.client.model.KnowledgeBaseModel;
import no.ovitas.compass2.web.client.model.KnowledgeBaseTreeModel;
import no.ovitas.compass2.web.client.model.RelationTypesModel;
import no.ovitas.compass2.web.client.model.ResultContainerModel;
import no.ovitas.compass2.web.client.model.ResultModel;
import no.ovitas.compass2.web.client.model.ScopeTreeModel;
import no.ovitas.compass2.web.client.model.SearchConfigModel;
import no.ovitas.compass2.web.client.model.SearchFieldModel;
import no.ovitas.compass2.web.client.model.SearchModel;
import no.ovitas.compass2.web.client.model.SelectedTreeModel;
import no.ovitas.compass2.web.client.rpc.Compass2Service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author nyari
 * 
 */
public class Compass2ServiceImpl extends RemoteServiceServlet implements
		Compass2Service {

	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(getClass());

	public List<SearchFieldModel> getAdvancedFields() throws BackEndException {

		List<SearchFieldModel> result = new ArrayList<SearchFieldModel>();
		try {
			CompassManager compassManager = CompassManagerFactory.getInstance()
					.getCompassManager();

			if (compassManager != null) {

				result = convert(compassManager.getAvailableSearchFields());
			} else {
				log.error("CompassManager not available!");
			}
		} catch (Exception e) {
			log.error("Error occurence when load advanced search fields!", e);
			if (e instanceof CompassException) {
				throw new BackEndException(
						"Error occurence when load advanced search fields!",
						((CompassException) e).getErrorID());
			}
			throw new BackEndException(
					"Error occurence when load advanced search fields!");
		}

		return result;
	}

	private List<SearchFieldModel> convert(
			Collection<SearchField> availableSearchFields) {

		List<SearchFieldModel> model = new ArrayList<SearchFieldModel>();

		SearchFieldModel field;

		for (SearchField searchField : availableSearchFields) {
			field = new SearchFieldModel();
			field.setId(searchField.getIndexField());
			field.setLabel(searchField.getSearchField());
			field.setType(searchField.getSearchType());
			model.add(field);
		}

		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.ovitas.compass2.web.client.rpc.Compass2Service#getKBFileTypes()
	 */
	@Override
	public List<BaseListModel> getKBFileTypes() throws BackEndException {

		List<BaseListModel> models = new ArrayList<BaseListModel>();
		try {

			CompassManager compassManager = CompassManagerFactory.getInstance()
					.getCompassManager();

			if (compassManager != null) {
				Collection<java.lang.String> availableUploadTypes = compassManager
						.getAvailableUploadTypes();
				BaseListModel model;
				for (String type : availableUploadTypes) {
					model = new BaseListModel();
					model.setLabel(type.toUpperCase());
					model.setValue(type);

					models.add(model);
				}
			} else {
				log.error("CompassManager not available!");
			}
		} catch (Exception e) {
			log.error("Some error occured when request supported file types.",
					e);
			if (e instanceof CompassException) {
				throw new BackEndException(
						"Some error occured when request supported file types.",
						((CompassException) e).getErrorID());
			}
			throw new BackEndException(
					"Some error occured when request supported file types.");
		}

		return models;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * no.ovitas.compass2.web.client.rpc.Compass2Service#getImportedRelationTypes
	 * ()
	 */
	@Override
	public List<RelationTypesModel> getImportedRelationTypes()
			throws BackEndException {
		List<RelationTypesModel> relationTypes = new ArrayList<RelationTypesModel>();
		try {
			CompassManager compassManager = CompassManagerFactory.getInstance()
					.getCompassManager();
			if (compassManager != null) {
				Collection<RelationType> relationTypesFromImportedKnowledgeBase = compassManager
						.getImportedRelationTypes();
				relationTypes = createRelationTypesModel(relationTypesFromImportedKnowledgeBase);

			} else {
				log.error("CompassManager not available!");
			}
		} catch (Exception e) {
			log.error("Error occured when try to get imported relation types!",
					e);
			if (e instanceof CompassException) {
				throw new BackEndException(
						"Error occured when try to get imported relation types!",
						((CompassException) e).getErrorID());
			}
			throw new BackEndException(
					"Error occured when try to get imported relation types!");
		}

		return relationTypes;
	}

	/**
	 * @param relType
	 * @return
	 */
	private List<RelationTypesModel> createRelationTypesModel(
			Collection<RelationType> relType) {
		List<RelationTypesModel> relationTypes = new ArrayList<RelationTypesModel>();
		RelationTypesModel relation;

		for (RelationType relationType : relType) {
			relation = new RelationTypesModel();
			relation.setRelationName(relationType.getDisplayName());
			relation.setId(relationType.getImportId());
			relation.setExternalId(relationType.getExternalId());
			relation.setRef(relationType.getOccurrence());
			relation.setWeightAback(relationType.getGeneralizationWeight());
			relation.setWeightAhead(relationType.getWeight());
			relationTypes.add(relation);
		}
		return relationTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * no.ovitas.compass2.web.client.rpc.Compass2Service#getImportedScopes()
	 */
	@Override
	public List<EntityModel> getImportedScopes() throws BackEndException {
		Collection<Scope> importedScopes = null;
		List<EntityModel> models = new ArrayList<EntityModel>();
		try {
			CompassManager compassManager = CompassManagerFactory.getInstance()
					.getCompassManager();
			if (compassManager != null) {
				importedScopes = compassManager.getImportedScopes();
			} else {
				log.error("CompassManager not available!");
			}

			EntityModel model;
			if (importedScopes != null) {
				for (Scope scope : importedScopes) {
					model = new EntityModel();
					model.setLabel(scope.getDisplayName());
					model.setValue(scope.getExternalId());
					model.setId(scope.getImportId());
					models.add(model);

				}
			}
		} catch (Exception e) {
			log.error("Error occured when try to get imported scopes!", e);
			if (e instanceof CompassException) {
				throw new BackEndException(
						"Error occured when try to get imported scopes!",
						((CompassException) e).getErrorID());
			}
			throw new BackEndException(
					"Error occured when try to get imported scopes!");
		}
		return models;
	}

	@Override
	public void uploadScopes(List<EntityModel> scopes) throws BackEndException {

		Set<Long> scopesInner = new HashSet<Long>();

		try {
			CompassManager compassManager = CompassManagerFactory.getInstance()
					.getCompassManager();
			if (compassManager != null) {

				for (EntityModel baseListModel : scopes) {

					scopesInner.add(baseListModel.getId());
				}
				compassManager.filterImportedKnowledgeBase(scopesInner);
			} else {
				log.error("CompassManager not available!");
			}
		} catch (ImportException e) {
			log.error("Some error occured when process filter scopes!", e);
			if (e instanceof CompassException) {
				throw new BackEndException(
						"Some error occured when process filter scopes!",
						((CompassException) e).getErrorID());
			}
			throw new BackEndException(
					"Some error occured when process filter scopes!");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * no.ovitas.compass2.web.client.rpc.Compass2Service#uploadRelationTypes
	 * (java.util.List)
	 */
	@Override
	public void uploadRelationTypes(
			List<RelationTypesModel> modifiedRelationTypes)
			throws BackEndException {

		try {
			log.info("Modified relation types size: "
					+ modifiedRelationTypes.size());
			Set<RelationTypeSetting> types = createRelationType(modifiedRelationTypes);
			log.info("Modified relation types size after convert: "
					+ modifiedRelationTypes.size());
			CompassManager compassManager = CompassManagerFactory.getInstance()
					.getCompassManager();
			if (compassManager != null) {
				KnowledgeBase importedKnowledgeBase = compassManager.storeImportedKnowledgeBase(types);
				
				if (importedKnowledgeBase.isSuggestionUpload()) {
					compassManager.indexKnowledgeBaseForSuggestion(importedKnowledgeBase);
				}
			} else {
				log.error("CompassManager not available!");
			}
		} catch (Exception e) {
			log.error("Store fail!", e);
			if (e instanceof CompassException) {
				throw new BackEndException("Store fail!",
						((CompassException) e).getErrorID());
			}

			throw new BackEndException("Store fail!");
		}

	}

	/**
	 * @param modifiedRelationTypes
	 * @return
	 */
	private Set<RelationTypeSetting> createRelationType(
			List<RelationTypesModel> modifiedRelationTypes) {
		Set<RelationTypeSetting> types = new HashSet<RelationTypeSetting>();

		RelationTypeSetting type;
		for (RelationTypesModel relationTypesModel : modifiedRelationTypes) {
			type = new RelationTypeSetting();
			type.setImportId(relationTypesModel.getId());
			type.setExternalId(relationTypesModel.getExternalId());
			type.setWeight(relationTypesModel.getWeightAhead());
			type.setGeneralizationWeight(relationTypesModel.getWeightAback());

			types.add(type);
		}
		return types;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.ovitas.compass2.web.client.rpc.Compass2Service#getKBList()
	 */
	@Override
	public List<KnowledgeBaseModel> getKBList() throws BackEndException {

		List<KnowledgeBaseModel> kbList = new ArrayList<KnowledgeBaseModel>();
		try {
			CompassManager compassManager = CompassManagerFactory.getInstance()
					.getCompassManager();
			if (compassManager != null) {

				Collection<KnowledgeBaseDescriptor> listKnowledgeBases = compassManager
						.listKnowledgeBases();

				KnowledgeBaseModel kb;
				for (KnowledgeBaseDescriptor knowledgeBaseDescriptor : listKnowledgeBases) {
					kb = new KnowledgeBaseModel();
					kb.setLabel(knowledgeBaseDescriptor.getDisplayName());
					kb.setValue(knowledgeBaseDescriptor.getDisplayName());
					kb.setId(knowledgeBaseDescriptor.getId());
					kb.setDescription(knowledgeBaseDescriptor.getDescription());
					kbList.add(kb);
				}

			} else {
				log.error("CompassManager not available!");
			}
		} catch (Exception e) {
			log.error(
					"Some error occured when process get knowledge base list!",
					e);
			if (e instanceof CompassException) {
				throw new BackEndException("Some error occured when process get knowledge base list!",
						((CompassException) e).getErrorID());
			}
			throw new BackEndException(
					"Some error occured when process get knowledge base list!");
		}

		return kbList;
	}

	@Override
	public ResultContainerModel getSearchResult(SearchModel searchModel)
			throws BackEndException {
		ResultContainerModel resultModel = new ResultContainerModel();
		try {

			resultModel = new Compass2SearchService(this
					.getThreadLocalRequest().getSession()).search(searchModel);

		} catch (Exception e) {
			log.error("Error happend while searching!", e);
			if (e instanceof CompassException) {
				throw new BackEndException("Error happend while searching!",
						((CompassException) e).getErrorID());
			}
			throw new BackEndException("Error happend while searching!");
		}

		return resultModel;
	}

	@Override
	public ResultContainerModel getSearchResult(SearchModel searchModel,
			Collection<Long> selectedTopics,
			Map<Long, Collection<Long>> unSelectedNotOpenedTopics,
			Map<Long, Collection<String>> unSelectedNotOpenedTerms)
			throws BackEndException {
		ResultContainerModel resultModel = new ResultContainerModel();
		try {

			resultModel = new Compass2SearchService(this
					.getThreadLocalRequest().getSession()).reSearch(
					searchModel, selectedTopics, unSelectedNotOpenedTopics,
					unSelectedNotOpenedTerms);

		} catch (Exception e) {
			log.error("Error happend while searching!", e);
			if (e instanceof CompassException) {
				throw new BackEndException("Error happend while searching!",
						((CompassException) e).getErrorID());
			}
			throw new BackEndException("Error happend while searching!");
		}

		return resultModel;
	}

	@Override
	public List<BaseListModel> getKBFiles() throws BackEndException {

		List<BaseListModel> result = new ArrayList<BaseListModel>();

		BaseListModel model;

		try {
			CompassManager compassManager = CompassManagerFactory.getInstance()
					.getCompassManager();
			if (compassManager != null) {
				Collection<File> filesFromKnowledgeBaseImportDirectory = compassManager
						.getFilesFromKnowledgeBaseImportDirectory();

				for (File file : filesFromKnowledgeBaseImportDirectory) {
					model = new BaseListModel();
					model.setLabel(file.getName());
					model.setValue(file.getAbsolutePath());
					result.add(model);
				}

			} else {
				log.error("CompassManager not available!");
			}
		} catch (Exception e) {
			log.error(
					"Exception occured when getFiles form knowledge base upload dir!",
					e);
			if (e instanceof CompassException) {
				throw new BackEndException("Exception occured when getFiles form knowledge base upload dir!",
						((CompassException) e).getErrorID());
			}
			throw new BackEndException(
					"Exception occured when getFiles form knowledge base upload dir!");
		}
		return result;
	}

	@Override
	public void uploadKnowledgeBase(BaseListModel file, BaseListModel fileType,
			KnowledgeBaseModel kb, String description, Boolean twoWay, Boolean suggestionUpload)
			throws BackEndException {
		CompassManager compassManager = CompassManagerFactory.getInstance()
				.getCompassManager();
		try {
			if (compassManager != null) {
				KnowledgeBase knowledgeBase = compassManager
						.newInstanceKnowledgeBase((twoWay) ? KnowledgeBaseType.TWOWAY
								: KnowledgeBaseType.SPECGEN);
				
				if (kb.getId() != Compass2Constans.NOT_IN_DATABASE) {
					knowledgeBase.loadDefaultKnowledgeBase(kb.getId());
				} else {
					knowledgeBase.createDefaultKnowledgeBase(kb.getLabel(),
							description);
				}
				
				knowledgeBase.setSuggestionUpload(suggestionUpload);

				compassManager.importKnowledgeBase(file.getValue(),
						fileType.getValue(), knowledgeBase);

			} else {
				log.error("CompassManager not available!");
			}
		} catch (Exception e) {
			log.error("Exception occured when start processing the file!", e);
			if (e instanceof CompassException) {
				throw new BackEndException("Exception occured when start processing the file!",
						((CompassException) e).getErrorID());
			}
			
			throw new BackEndException("Exception occured when start processing the file!");
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<FolderModel> getExpansionTreeById(FolderModel config, Long kbId)
			throws BackEndException {
		List<FolderModel> result = new ArrayList<FolderModel>();
		try {

			result = new Compass2SearchService(this.getThreadLocalRequest()
					.getSession()).getExpansionTreeNodeById(config, kbId);

			for (FolderModel folderModel : result) {
				folderModel.setOpened(true);
			}
		} catch (Exception e) {
			log.error("Error happend when get nodes!", e);
			if (e instanceof CompassException) {
				throw new BackEndException("Error happend when get nodes!",
						((CompassException) e).getErrorID());
			}
			
			throw new BackEndException("Error happend when get nodes!");
		}
		return result;
	}

	@Override
	public List<ResultModel> getResultById(Long id) throws BackEndException {
		List<ResultModel> result = new ArrayList<ResultModel>();
		try {
			log.info("Result by id: " + id);
			result = new Compass2SearchService(this.getThreadLocalRequest()
					.getSession()).getResultById(id);

		} catch (Exception e) {
			log.error("Error happend when get results!", e);
			if (e instanceof CompassException) {
				throw new BackEndException("Error happend when get results!",
						((CompassException) e).getErrorID());
			}
			throw new BackEndException("Error happend when get results!");
		}

		return result;
	}

	@Override
	public void startup() {
		CompassManagerFactory.getInstance().getCompassManager();

	}

	@Override
	public List<RelationTypesModel> getRelationTypes(
			KnowledgeBaseTreeModel knowledgeBase) throws BackEndException {
		List<RelationTypesModel> relationTypes = new ArrayList<RelationTypesModel>();

		try {
			CompassManager compassManager = CompassManagerFactory.getInstance()
					.getCompassManager();
			if (compassManager != null) {

				KnowledgeBaseDescriptor knowledgeBaseDescriptor = getKnowledgeBaseDescriptor(
						knowledgeBase.getId(), compassManager);

				if (knowledgeBaseDescriptor != null) {

					Collection<RelationType> relType = compassManager
							.getRelationTypes(knowledgeBaseDescriptor);

					relationTypes = createRelationTypesModel(relType);

				} else {
					log.error("No knowledge base available with id: "
							+ knowledgeBase.getId());
				}
			} else {
				log.error("CompassManager not available!");
			}
		} catch (Exception e) {
			log.error("Error occurred while try to get relationTypes!", e);
			if (e instanceof CompassException) {
				throw new BackEndException("Error occurred while try to get relationTypes!",
						((CompassException) e).getErrorID());
			}
			throw new BackEndException(
					"Error occurred while try to get relationTypes!");
		}

		return relationTypes;
	}

	static KnowledgeBaseDescriptor getKnowledgeBaseDescriptor(Long kbId,
			CompassManager compassManager) {
		KnowledgeBaseDescriptor knowledgeBaseDescriptor = null;

		Collection<KnowledgeBaseDescriptor> listKnowledgeBases = compassManager
				.listKnowledgeBases();

		for (KnowledgeBaseDescriptor kbDesc : listKnowledgeBases) {
			if (kbDesc.getId() == kbId.longValue()) {
				knowledgeBaseDescriptor = kbDesc;
				break;
			}
		}
		return knowledgeBaseDescriptor;
	}

	@Override
	public void updateRelationTypes(List<RelationTypesModel> relationTypes,
			KnowledgeBaseTreeModel knowledgeBase) throws BackEndException {
		Set<RelationTypeSetting> createRelationType = createRelationType(relationTypes);

		CompassManager compassManager = CompassManagerFactory.getInstance()
				.getCompassManager();
		try {
			if (compassManager != null) {
				KnowledgeBaseDescriptor knowledgeBaseDescriptor = getKnowledgeBaseDescriptor(
						knowledgeBase.getId(), compassManager);

				if (knowledgeBaseDescriptor != null) {

					compassManager.updateRelationTypes(createRelationType,
							knowledgeBaseDescriptor);
				} else {
					log.error("KnowledgeBase wiht specified id doesn't available! ID: "
							+ knowledgeBase.getId());
				}

			} else {
				log.error("CompassManager not available!");
			}
		} catch (Exception e) {
			log.error("Error occured when call updateRelationTypes!", e);
			if (e instanceof CompassException) {
				throw new BackEndException("Error occured when call updateRelationTypes!",
						((CompassException) e).getErrorID());
			}
			throw new BackEndException(
					"Error occured when call updateRelationTypes!");
		}

	}

	@Override
	public List<SelectedTreeModel> getKBTree(SelectedTreeModel model)
			throws BackEndException {

		List<SelectedTreeModel> kbList = new ArrayList<SelectedTreeModel>();
		try {
			if (model == null) {
				CompassManager compassManager = CompassManagerFactory
						.getInstance().getCompassManager();
				if (compassManager != null) {
					try {
						Collection<KnowledgeBaseDescriptor> listKnowledgeBases = compassManager
								.listKnowledgeBases();

						KnowledgeBaseTreeModel kb;
						for (KnowledgeBaseDescriptor knowledgeBaseDescriptor : listKnowledgeBases) {
							kb = new KnowledgeBaseTreeModel();
							kb.setLabel(knowledgeBaseDescriptor
									.getDisplayName());
							kb.setValue(knowledgeBaseDescriptor
									.getDisplayName());
							kb.setId(knowledgeBaseDescriptor.getId());
							kb.setDescription(knowledgeBaseDescriptor
									.getDescription());

							kb.setAheadTree(false);
							kb.setAllTree(false);
							kb.setBackTree(false);

							if (knowledgeBaseDescriptor.getType().equals(
									KnowledgeBaseType.TWOWAY)) {
								kb.setHasAllTree(true);
							} else {
								kb.setHasAllTree(false);
							}

							kb.setHasAheadTree(true);
							kb.setHasBackTree(true);

							kbList.add(kb);
						}
					} catch (Exception e) {
						log.error("Exception occured when getKBList!", e);
					}
				} else {
					log.error("CompassManager not available!");
				}

			} else {
				if (model instanceof KnowledgeBaseTreeModel) {
					CompassManager compassManager = CompassManagerFactory
							.getInstance().getCompassManager();
					if (compassManager != null) {
						try {
							KnowledgeBaseDescriptor knowledgeBaseDescriptor = getKnowledgeBaseDescriptor(
									((KnowledgeBaseTreeModel) model).getId(),
									compassManager);
							Collection<Scope> listScope = compassManager
									.getScopes(knowledgeBaseDescriptor);

							((KnowledgeBaseTreeModel) model)
									.setLoadedChild(true);
							ScopeTreeModel stn;
							for (Scope scope : listScope) {
								stn = new ScopeTreeModel();
								stn.setLabel(scope.getDisplayName());
								stn.setValue(scope.getDisplayName());
								stn.setId(scope.getId());

								kbList.add(stn);
								model.add(stn);
							}
						} catch (Exception e) {
							log.error("Exception occured when getKBList!", e);
						}
					} else {
						log.error("CompassManager not available!");
					}

				}
			}
		} catch (Exception e) {
			log.error("Error occured when get knowledge base trees!");
			if (e instanceof CompassException) {
				throw new BackEndException("Error occured when get knowledge base trees!",
						((CompassException) e).getErrorID());
			}
			throw new BackEndException(
					"Error occured when get knowledge base trees!");
		}

		return kbList;
	}

	@Override
	public SearchConfigModel getDefaultSearchConfig() throws BackEndException {
		SearchConfigModel model = new SearchConfigModel();
		try {
			CompassManager compassManager = CompassManagerFactory.getInstance()
					.getCompassManager();

			if (compassManager != null) {

				SearchOptions defaultConfig = compassManager
						.getDefaultSearchConfig();
				log.info(defaultConfig);
				model.setFuzzySearchValue(defaultConfig.isFuzzyMatch());
				model.setTreeSearch(defaultConfig.isTreeSearch());
				model.setHopCount(defaultConfig.getHopCount());
				model.setMaxNumberOfHits(defaultConfig.getMaxNumberOfHits());
				model.setMaxNumberOfTopicToExpand(defaultConfig
						.getMaxTopicNumToExpand());
				model.setPrefixMatch(defaultConfig.isPrefixMatch());
				model.setResultTreshold(new Double(defaultConfig
						.getResultThreshold()));
				model.setTresholdWeight(new Double(defaultConfig
						.getExpansionThreshold()));

			} else {
				log.error("CompassManager not available!");
				model.setFuzzySearchValue(false);
				model.setTreeSearch(false);
				model.setHopCount(1);
				model.setMaxNumberOfHits(100);
				model.setMaxNumberOfTopicToExpand(5);
				model.setPrefixMatch(true);
				model.setResultTreshold(0.02);
				model.setTresholdWeight(0.02);
			}
		} catch (Exception e) {
			log.error("Error occured when get default search parameters!");
			if (e instanceof CompassException) {
				throw new BackEndException("Error occured when get default search parameters!",
						((CompassException) e).getErrorID());
			}
			throw new BackEndException(
					"Error occured when get default search parameters!");
		}
		return model;
	}

}
