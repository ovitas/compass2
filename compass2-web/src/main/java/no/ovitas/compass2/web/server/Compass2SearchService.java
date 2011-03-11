/**
 * 
 */
package no.ovitas.compass2.web.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpSession;

import no.ovitas.compass2.config.CompassManager;
import no.ovitas.compass2.config.factory.CompassManagerFactory;
import no.ovitas.compass2.config.settings.SearchField;
import no.ovitas.compass2.exception.CompassException;
import no.ovitas.compass2.model.ConnectingType;
import no.ovitas.compass2.model.FittingType;
import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.model.MatchingType;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.search.FullTextFieldCriteria;
import no.ovitas.compass2.search.FullTextQuery;
import no.ovitas.compass2.search.FullTextQueryResult;
import no.ovitas.compass2.search.FullTextTopicQuery;
import no.ovitas.compass2.search.KnowledgeBaseTreeResult;
import no.ovitas.compass2.search.QueryResult;
import no.ovitas.compass2.search.TermFilter;
import no.ovitas.compass2.search.TermTopicWeightResultCollector;
import no.ovitas.compass2.search.TopicCriteria;
import no.ovitas.compass2.search.TopicFilter;
import no.ovitas.compass2.search.TopicQuery;
import no.ovitas.compass2.search.TopicQueryResult;
import no.ovitas.compass2.search.TopicResultNode;
import no.ovitas.compass2.search.TopicTreeFilter;
import no.ovitas.compass2.search.TopicTreeQueryResult;
import no.ovitas.compass2.search.impl.TermFilterImpl;
import no.ovitas.compass2.search.impl.TermTopicWeightResultCollectorImpl;
import no.ovitas.compass2.search.impl.TopicFilterImpl;
import no.ovitas.compass2.search.impl.TopicTreeFilterImpl;
import no.ovitas.compass2.util.SearchUtil;
import no.ovitas.compass2.web.client.model.Compass2Constans;
import no.ovitas.compass2.web.client.model.ExceptionModel;
import no.ovitas.compass2.web.client.model.FolderModel;
import no.ovitas.compass2.web.client.model.KnowledgeBaseTreeModel;
import no.ovitas.compass2.web.client.model.ResultContainerModel;
import no.ovitas.compass2.web.client.model.ResultModel;
import no.ovitas.compass2.web.client.model.ScopeTreeModel;
import no.ovitas.compass2.web.client.model.SearchModel;
import no.ovitas.compass2.web.client.model.TopicExpansionModel;
import no.ovitas.compass2.web.client.model.TreeType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;

/**
 * @class Conpass2SearchService
 * @project compass2-web
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.02.
 * 
 */
class Compass2SearchService {

	private static final long RESULT_ID = -2L;

	private static final long TOPIC_QUERY_RESULT = -3L;
	
	private static final long ERROR_ID = -4L;

	private static final String SEARCH_RESULT_STRING = "result";

	private static final int NUMBER_OF_TREE_TYPE = 3;

	private static final int AHEAD_TREE = 0;

	private static final int BACK_TREE = 1;

	private static final int TWO_WAY_TREE = 2;

	private CompassManager compassManager = null;

	private Log log = LogFactory.getLog(getClass());

	private final HttpSession session;

	public Compass2SearchService(HttpSession session) {
		this.session = session;
		updateSearchManager();
	}

	/**
	 * Search service.
	 * 
	 * @param searchModel
	 * @return
	 */
	public ResultContainerModel search(SearchModel searchModel) {

		ResultContainerModel resultModels = new ResultContainerModel();
		if (compassManager != null) {
			TopicQuery topicQuery = null;

			FullTextQuery fullTextQuery = compassManager.createFullTextQuery();

			Collection<String> terms = setupFullTextQuery(fullTextQuery,
					searchModel);

			Map<KnowledgeBaseTreeModel, Set<ScopeTreeModel>> selectedKBs = searchModel
					.getKnowledgeBaseList();

			if (selectedKBs != null && selectedKBs.size() > 0) {
				topicQuery = compassManager.createTopicQuery();

				String stem;

				for (String term : terms) {
					stem = compassManager.getStem(term);
					String spellingSuggestion = compassManager.getSpellingSuggestion(stem);
//					log.debug("Term: " + term + ", stem: " + stem + ", suggestion: " + spellingSuggestion);
					topicQuery.addTerm(term);
				}

				setupTopicQuery(topicQuery, selectedKBs, searchModel);
			}

			QueryResult queryResult = compassManager.search(topicQuery,
					fullTextQuery);

			processResult(resultModels, topicQuery, queryResult);

		} else {
			log.error("CompassManager not available!");
		}

		return resultModels;
	}

	public ResultContainerModel reSearch(SearchModel searchModel,
			Collection<Long> unSelectedTopics,
			Map<Long, Collection<Long>> unSelectedNotOpenedTopics,
			Map<Long, Collection<String>> unSelectedNotOpenedTerms) {
		ResultContainerModel resultModels = new ResultContainerModel();
		if (compassManager != null) {
			Object objectFromSessionScope = getObjectFromSessionScope(TOPIC_QUERY_RESULT);

			TermTopicWeightResultCollector collector = new TermTopicWeightResultCollectorImpl();

			if (objectFromSessionScope != null
					&& objectFromSessionScope instanceof TopicQueryResult) {

				TopicQueryResult topicResult = (TopicQueryResult) objectFromSessionScope;

				collector.addTopicFilters(createTopicFilters(unSelectedTopics));

				collector
						.addTreeTopicFilters(createTopicTreeFilters(unSelectedNotOpenedTopics));

				collector
						.addTermFilters(createTermFilters(unSelectedNotOpenedTerms));

				topicResult.collectTermTopicWeights(collector);

			}

			FullTextQuery fullTextQuery = compassManager.createFullTextQuery();

			setupFullTextQuery(fullTextQuery, searchModel);

			FullTextTopicQuery fullTextTopicQuery = SearchUtil
					.convert(collector);

			FullTextQueryResult result = compassManager.search(fullTextQuery,
					fullTextTopicQuery);

			processResult(resultModels, result);

		}
		return resultModels;
	}

	private Collection<TermFilter> createTermFilters(
			Map<Long, Collection<String>> unSelectedNotOpenedTerms) {

		Collection<TermFilter> termFilters = new ArrayList<TermFilter>(0);

		if (unSelectedNotOpenedTerms != null) {

			long kbCalculatedId;

			KnowledgeBaseDescriptor knowledgeBaseDescriptor;
			RelationDirection relationDirection = null;
			int direction;

			for (Entry<Long, Collection<String>> pair : unSelectedNotOpenedTerms
					.entrySet()) {

				kbCalculatedId = pair.getKey();

				knowledgeBaseDescriptor = compassManager
						.getKnowledgeBase(kbCalculatedId / NUMBER_OF_TREE_TYPE);

				direction = (int) (kbCalculatedId % NUMBER_OF_TREE_TYPE);

				switch (direction) {
				case AHEAD_TREE:
					relationDirection = RelationDirection.SPEC;
					break;
				case BACK_TREE:
					relationDirection = RelationDirection.GEN;
					break;
				case TWO_WAY_TREE:
					relationDirection = null;
					break;
				default:
					assert false;
				}

				for (String term : pair.getValue()) {
					termFilters.add(new TermFilterImpl(knowledgeBaseDescriptor,
							relationDirection, term));
				}
			}

		}

		return termFilters;
	}

	private Collection<TopicTreeFilter> createTopicTreeFilters(
			Map<Long, Collection<Long>> unSelectedNotOpenedTopics) {

		Collection<TopicTreeFilter> treeFilters = new ArrayList<TopicTreeFilter>(
				0);
		if (unSelectedNotOpenedTopics != null) {
			long kbCalculatedId;

			KnowledgeBaseDescriptor knowledgeBaseDescriptor;
			RelationDirection relationDirection = null;
			int direction;
			for (Entry<Long, Collection<Long>> pair : unSelectedNotOpenedTopics
					.entrySet()) {

				kbCalculatedId = pair.getKey();

				knowledgeBaseDescriptor = compassManager
						.getKnowledgeBase(kbCalculatedId / NUMBER_OF_TREE_TYPE);

				direction = (int) (kbCalculatedId % NUMBER_OF_TREE_TYPE);

				switch (direction) {
				case AHEAD_TREE:
					relationDirection = RelationDirection.SPEC;
					break;
				case BACK_TREE:
					relationDirection = RelationDirection.GEN;
					break;
				case TWO_WAY_TREE:
					relationDirection = null;
					break;
				default:
					assert false;
				}

				for (Long topicId : pair.getValue()) {
					treeFilters.add(new TopicTreeFilterImpl(topicId,
							knowledgeBaseDescriptor, relationDirection));
				}
			}
		}

		return treeFilters;
	}

	private Collection<TopicFilter> createTopicFilters(
			Collection<Long> unSelectedTopics) {

		Collection<TopicFilter> filters = new ArrayList<TopicFilter>(0);

		if (unSelectedTopics != null) {
			for (Long topicId : unSelectedTopics) {
				filters.add(new TopicFilterImpl(topicId));
			}
		}

		return filters;
	}

	private void processResult(ResultContainerModel resultModels,
			FullTextQueryResult result) {

		Collection<Hit> hits = result.getHits();

		saveSessionScope(getResultModels(hits), RESULT_ID);

		resultModels.setSearchResultId(RESULT_ID);

		resultModels.setSearchResultNumber(hits.size());
		
		resultModels.setError(false);
		
	}

	private void processResult(ResultContainerModel resultModels,
			TopicQuery topicQuery, QueryResult queryResult) {
		Collection<Hit> hits = queryResult.getFullTextQueryResult().getHits();

		saveSessionScope(getResultModels(hits), RESULT_ID);

		resultModels.setSearchResultId(RESULT_ID);

		resultModels.setSearchResultNumber(hits.size());

		if (topicQuery != null && topicQuery.isTreeSearch()) {
			TopicTreeQueryResult topicTreeQueryResult = queryResult
					.getTopicTreeQueryResult();

			Collection<KnowledgeBaseTreeResult> kbResults = topicTreeQueryResult
					.getResult();

			List<TopicExpansionModel> topicExpansionModels = getTopicExpansionModels(kbResults);

			saveSessionScope(topicTreeQueryResult, TOPIC_QUERY_RESULT);

			resultModels.setTopicExpansions(topicExpansionModels);
		}
		
		resultModels.setError(false);

	}

	private List<TopicExpansionModel> getTopicExpansionModels(
			Collection<KnowledgeBaseTreeResult> kbResults) {
		List<TopicExpansionModel> result = new ArrayList<TopicExpansionModel>();

		TopicExpansionModel model = null;
		for (KnowledgeBaseTreeResult knowledgeBaseTreeResult : kbResults) {
			for (TopicExpansionModel topicExpansionModel : result) {

				if (topicExpansionModel.getKnowledgeBaseId().longValue() == knowledgeBaseTreeResult
						.getKnowledgeBase().getId()) {
					model = topicExpansionModel;
					break;
				}
			}

			if (model == null) {

				model = new TopicExpansionModel();
				model.setKnowledgeBaseId(knowledgeBaseTreeResult
						.getKnowledgeBase().getId());
				model.setKnowledgeBaseName(knowledgeBaseTreeResult
						.getKnowledgeBase().getDisplayName());
				model.setTrees(new HashMap<TreeType, Long>());

				result.add(model);
			}

			long treeid = model.getKnowledgeBaseId() * NUMBER_OF_TREE_TYPE;

			TreeType type = null;

			if (knowledgeBaseTreeResult.getDirection() != null) {

				switch (knowledgeBaseTreeResult.getDirection()) {
				case SPEC:
					treeid += AHEAD_TREE;
					type = TreeType.AHEAD_TREE;
					break;
				case GEN:
					treeid += BACK_TREE;
					type = TreeType.BACK_TREE;
					break;
				default:
					assert false;
				}
			} else {
				treeid += TWO_WAY_TREE;
				type = TreeType.TWO_WAY_TREE;

			}

			model.getTrees().put(type, treeid);

			saveSessionScope(knowledgeBaseTreeResult, treeid);

			model = null;
		}
		
		return result;
	}

	private Collection<String> setupFullTextQuery(FullTextQuery fullTextQuery,
			SearchModel searchModel) {

		Set<String> terms = new HashSet<String>(0);

		setupQueryConfigs(fullTextQuery, searchModel);

		if (!searchModel.isAdvancedSearch()) {
			String term = searchModel.getSearch();

			Collection<String> splittedTerms = SearchUtil
					.splitSearchString(term);
			
			splittedTerms = SearchUtil.toLowerCase(splittedTerms);

			Collection<SearchField> defaultSearchFields = compassManager
					.getDefaultSearchFields();
			FullTextFieldCriteria fullTextFieldCriteria;
			for (SearchField searchField : defaultSearchFields) {
				fullTextFieldCriteria = fullTextQuery
						.createCriteria(searchField.getIndexField());
				fullTextFieldCriteria.addTerms(splittedTerms);
			}

			terms.addAll(splittedTerms);
		} else {
			Collection<SearchField> searchFields = compassManager
					.getAvailableSearchFields();
			ConnectingType connectionType = null;
			MatchingType matchingType = null;
			FittingType fittingType = null;
			boolean fuzzy = false;
			FullTextFieldCriteria fullTextFieldCriteria;
			for (SearchField searchField : searchFields) {
				String id = searchField.getIndexField();

				connectionType = ConnectingType.valueOf((String) searchModel
						.get(id + Compass2Constans.CONNECTION_TYPE));

				switch (searchField.getSearchType()) {
				case DATEINTERVAL:
					break;
				case INTEGER:
				case FLOAT:
					break;
				case STRING:
					if (searchModel.get(id) != null
							&& !((String) searchModel.get(id)).isEmpty()) {

						fittingType = FittingType.valueOf((String) searchModel
								.get(id + Compass2Constans.FIT_TYPE));

						matchingType = MatchingType
								.valueOf((String) searchModel.get(id
										+ Compass2Constans.MATCH_TYPE));

						fuzzy = (Boolean)searchModel.get(id + Compass2Constans.FUZZY_MATCH);
					}

					break;
				}

				String expression = (String) searchModel.get(id);

				if (matchingType != null && fittingType != null
						&& expression != null && !expression.isEmpty()) {

					Collection<String> splittedTerms = SearchUtil
							.splitSearchString(expression);
					
					splittedTerms = SearchUtil.toLowerCase(splittedTerms);

					fullTextFieldCriteria = fullTextQuery
							.createCriteria(searchField.getIndexField());

					fullTextFieldCriteria.addTerms(splittedTerms);

					fullTextFieldCriteria.setFittingType(fittingType);
					fullTextFieldCriteria.setMatchingType(matchingType);
					fullTextFieldCriteria.setFuzzySearch(fuzzy);
					
					fullTextFieldCriteria.setBoost(1);

					if (connectionType != null
							&& !connectionType.equals(ConnectingType.FIRST)) {
						fullTextFieldCriteria.setConnectionType(connectionType);
					}

					terms.addAll(splittedTerms);
				}

			}
		}

		return terms;
	}

	private void setupQueryConfigs(FullTextQuery fullTextQuery,
			SearchModel searchModel) {

		fullTextQuery.setFuzzySearch(searchModel.getFuzzyMatch());
		fullTextQuery.setMaxNumberOfHits(searchModel.getMaxNumberOfHits());
		fullTextQuery.setResultThreshold(searchModel.getResultThreshold());

	}

	private void setupTopicQuery(TopicQuery topicQuery,
			Map<KnowledgeBaseTreeModel, Set<ScopeTreeModel>> selectedKBs,
			SearchModel searchModel) {

		setupQueryConfigs(topicQuery, searchModel);

		KnowledgeBaseTreeModel knowledgeBaseTreeModel;

		Collection<KnowledgeBaseDescriptor> knowledgeBaseDescriptors = compassManager
				.listKnowledgeBases();

		KnowledgeBaseDescriptor knowldegBaseDescriptor;

		for (Entry<KnowledgeBaseTreeModel, Set<ScopeTreeModel>> pair : selectedKBs
				.entrySet()) {

			knowledgeBaseTreeModel = pair.getKey();
			knowldegBaseDescriptor = getKnowldegBaseDescriptor(
					knowledgeBaseTreeModel.getId(), knowledgeBaseDescriptors);

			if (knowldegBaseDescriptor == null)
				continue;

			if (knowledgeBaseTreeModel.isAheadTree()) {

				addKnowledgeBaseCriteria(topicQuery, knowldegBaseDescriptor,
						pair, RelationDirection.SPEC);

			}

			if (knowledgeBaseTreeModel.isBackTree()) {

				addKnowledgeBaseCriteria(topicQuery, knowldegBaseDescriptor,
						pair, RelationDirection.GEN);

			}

			if (knowledgeBaseTreeModel.isAllTree()) {

				addKnowledgeBaseCriteria(topicQuery, knowldegBaseDescriptor,
						pair, null);

			}
		}
	}

	private KnowledgeBaseDescriptor getKnowldegBaseDescriptor(Long id,
			Collection<KnowledgeBaseDescriptor> knowledgeBaseDescriptors) {

		for (KnowledgeBaseDescriptor knowledgeBaseDescriptor : knowledgeBaseDescriptors) {
			if (knowledgeBaseDescriptor.getId() == id.longValue()) {
				return knowledgeBaseDescriptor;
			}
		}

		return null;
	}

	private void addKnowledgeBaseCriteria(TopicQuery topicQuery,
			KnowledgeBaseDescriptor knowledgeBaseDescriptor,
			Entry<KnowledgeBaseTreeModel, Set<ScopeTreeModel>> pair,
			RelationDirection direction) {
		Set<ScopeTreeModel> scopesNode;
		TopicCriteria criteria;
		Collection<Scope> scopes;

		scopesNode = pair.getValue();
		criteria = topicQuery.createTopicCriteria(knowledgeBaseDescriptor);

		scopes = compassManager.getScopes(knowledgeBaseDescriptor);
		if (scopesNode != null && scopesNode.size() > 0) {
			scopes = filterScopes(scopesNode, scopes);
		}

		criteria.setScopes(scopes);

		criteria.setRelationDirection(direction);
	}

	private Collection<Scope> filterScopes(Set<ScopeTreeModel> scopesNode,
			Collection<Scope> scopes) {

		Set<Scope> filteredScope = new HashSet<Scope>();

		for (ScopeTreeModel scopeTreeModel : scopesNode) {

			for (Scope scope : scopes) {

				if (scopeTreeModel.getId().longValue() == scope.getId()) {
					filteredScope.add(scope);
					break;
				}
			}
		}

		return filteredScope;
	}

	private void setupQueryConfigs(TopicQuery topicQuery,
			SearchModel searchModel) {

		topicQuery.setHopCount(searchModel.getHopCount());
		topicQuery.setMaxTopicNumberToExpand(searchModel
				.getMaxTopicNumberToExpand());
		topicQuery.setPrefixMatch(searchModel.getTopicPrefixMatch());
		topicQuery.setThresholdWeight(searchModel.getExpansionThreshold());
		topicQuery.setTreeSearch(searchModel.isTreeSearch());

	}

	private Long saveSessionScope(Object object, long id) {

		session.setAttribute(SEARCH_RESULT_STRING + id, object);

		return id;
	}

	private Object getObjectFromSessionScope(Long id) {
		return session.getAttribute(SEARCH_RESULT_STRING + id);
	}

	private void updateSearchManager() {
		compassManager = CompassManagerFactory.getInstance()
				.getCompassManager();
	}

	private List<ResultModel> getResultModels(Collection<Hit> hits) {
		List<ResultModel> models = new LinkedList<ResultModel>();
		ResultModel model;
		for (Hit hit : hits) {
			model = new ResultModel();
			model.setTitle(hit.getTitle());
			model.setHtmlLink(hit.getURI());
			model.setScore(hit.getScore());
			models.add(model);
		}

		return models;
	}

	@SuppressWarnings("unchecked")
	public List<ResultModel> getResultById(Long id) {

		return (List<ResultModel>) getObjectFromSessionScope(id);
	}

	public List<FolderModel> getExpansionTreeNodeById(FolderModel config,
			Long kbId) {
		List<FolderModel> models = new ArrayList<FolderModel>(0);

		if (config == null) {
			models = createTermFolderModels(kbId);
		} else {
			models = createTopicFolderModel(config, kbId);
		}

		return models;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<FolderModel> createTopicFolderModel(FolderModel config,
			Long kbId) {

		KnowledgeBaseTreeResult knowledgeBaseResult = (KnowledgeBaseTreeResult) getObjectFromSessionScope(kbId);

		if (!config.isTopic()) {
			Set<TopicResultNode> termResult = knowledgeBaseResult
					.getTermResult(config.getStemName());

			convertTopicResultNodeToFolderModel(config, termResult);

		}

		return (List) config.getChildren();
	}

	private void convertTopicResultNodeToFolderModel(FolderModel config,
			Collection<TopicResultNode> termResult) {
		FolderModel topic;
		for (TopicResultNode topicResultNode : termResult) {
			topic = new FolderModel(topicResultNode.getId());
			topic.setName(concatTopicNames(topicResultNode.getNames()));
			topic.setTopic(true);
			// topic.setParent(config);
			config.add(topic);

			if (topicResultNode.getChildren() != null) {
				convertTopicResultNodeToFolderModel(topic,
						topicResultNode.getChildren());
			}

		}
	}

	private String concatTopicNames(Collection<String> names) {
		StringBuffer buffer = new StringBuffer();

		for (String name : names) {
			buffer.append(name);
			buffer.append(";");
		}

		return buffer.toString();
	}

	private List<FolderModel> createTermFolderModels(Long kbId) {

		List<FolderModel> result = new ArrayList<FolderModel>(0);

		KnowledgeBaseTreeResult knowledgeBaseResult = (KnowledgeBaseTreeResult) getObjectFromSessionScope(kbId);

		Collection<String> terms = knowledgeBaseResult.getTerms();

		FolderModel termFolder;

		String term = null;
		Map<String, String> termWithStemPair = new HashMap<String, String>(0);
		Map<String, String> stemWithTermPair = new HashMap<String, String>(0);

		if (compassManager != null) {
			Object objectFromSessionScope = getObjectFromSessionScope(TOPIC_QUERY_RESULT);

			TermTopicWeightResultCollector collector = new TermTopicWeightResultCollectorImpl();

			if (objectFromSessionScope != null
					&& objectFromSessionScope instanceof TopicQueryResult) {

				TopicQueryResult topicResult = (TopicQueryResult) objectFromSessionScope;

				termWithStemPair = topicResult.getTermWithStemPair();

			}
		}
		
		for (Entry<String, String> pair : termWithStemPair.entrySet()) {
			stemWithTermPair.put(pair.getValue(), pair.getKey());
		}

		for (String stem : terms) {
			term = null;
			termFolder = new FolderModel();
			termFolder.setTopic(false);

			
			term = stemWithTermPair.get(stem);

			if (term != null) {
				termFolder.setName(term + "[" + stem + "]");
				termFolder.setTermName(term);
			} else {
				termFolder.setName(stem);
				termFolder.setTermName(stem);
			}

			termFolder.setStemName(stem);
			
			result.add(termFolder);
		}

		return result;
	}

}
