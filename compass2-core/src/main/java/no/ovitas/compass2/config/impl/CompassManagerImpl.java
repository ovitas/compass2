package no.ovitas.compass2.config.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import no.ovitas.compass2.config.CompassManager;
import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.config.settings.SearchField;
import no.ovitas.compass2.config.settings.SearchOptions;
import no.ovitas.compass2.exception.CompassErrorID;
import no.ovitas.compass2.exception.CompassException;
import no.ovitas.compass2.exception.ConfigParameterMissingException;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.exception.ImportException;
import no.ovitas.compass2.fts.FullTextSearchManager;
import no.ovitas.compass2.fts.factory.FTSFactory;
import no.ovitas.compass2.kb.KnowledgeBase;
import no.ovitas.compass2.kb.KnowledgeBaseManager;
import no.ovitas.compass2.kb.factory.KBFactory;
import no.ovitas.compass2.kbimport.ImportManager;
import no.ovitas.compass2.kbimport.factory.ImportManagerFactory;
import no.ovitas.compass2.lt.LanguageToolsManager;
import no.ovitas.compass2.lt.factory.LTFactory;
import no.ovitas.compass2.model.DocumentDetails;
import no.ovitas.compass2.model.ImportResult;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseType;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.RelationTypeSetting;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.search.FullTextQuery;
import no.ovitas.compass2.search.FullTextQueryResult;
import no.ovitas.compass2.search.FullTextTopicQuery;
import no.ovitas.compass2.search.QueryResult;
import no.ovitas.compass2.search.TopicListQueryResult;
import no.ovitas.compass2.search.TopicQuery;
import no.ovitas.compass2.search.TopicQueryResult;
import no.ovitas.compass2.search.TopicTreeQueryResult;
import no.ovitas.compass2.search.factory.QueryFactory;
import no.ovitas.compass2.search.impl.FullTextQueryResultImpl;
import no.ovitas.compass2.search.impl.QueryResultImpl;
import no.ovitas.compass2.search.impl.TermTopicWeightResultCollectorImpl;
import no.ovitas.compass2.suggestion.SuggestionProviderManager;
import no.ovitas.compass2.suggestion.factory.SuggestionProviderFactory;
import no.ovitas.compass2.util.SearchUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class CompassManagerImpl.
 */
/**
 * @author magyar
 * 
 */
public class CompassManagerImpl implements CompassManager {

	protected ConfigurationManager configurationManager;
	protected LanguageToolsManager ltManager;
	protected KnowledgeBaseManager kbManager;
	protected ImportManager importManager;
	protected FullTextSearchManager ftsManager;
	protected SuggestionProviderManager suggestionProviderManager;

	private Log log = LogFactory.getLog(getClass());
	private Collection<SearchField> defaultSearchFields;

	@Override
	public FullTextQueryResult search(FullTextQuery fullTextQuery) {
		return search(fullTextQuery, null);
	}

	@Override
	public FullTextQueryResult search(FullTextQuery fullTextQuery,
			FullTextTopicQuery fullTextTopicQuery) {
		FullTextQueryResult doSearch = new FullTextQueryResultImpl();
		try {
			doSearch = ftsManager.doSearch(fullTextQuery, fullTextTopicQuery);
		} catch (CompassException e) {
			log.error("Error occured when do full textSearch", e);
			throw e;
		}

		return doSearch;
	}

	@Override
	public QueryResult search(TopicQuery topicQuery, FullTextQuery fullTextQuery) {

		QueryResultImpl result = new QueryResultImpl();

		TopicQueryResult topicQueryResult = null;

		TermTopicWeightResultCollectorImpl collector = new TermTopicWeightResultCollectorImpl();

		if (topicQuery != null) {
			if (topicQuery.isTreeSearch()) {
				result.setTopicTreeQueryResult(searchTree(topicQuery));

				topicQueryResult = result.getTopicTreeQueryResult();

			} else {
				result.setTopicListQueryResult(search(topicQuery));
				topicQueryResult = result.getTopicListQueryResult();
			}

			topicQueryResult.collectTermTopicWeights(collector);
		}

		FullTextTopicQuery fullTextTopicQuery = SearchUtil.convert(collector);

		FullTextQueryResult fullTextQueryResult = search(fullTextQuery,
				fullTextTopicQuery);

		result.setFullTextQueryResult(fullTextQueryResult);

		return result;
	}

	@Override
	public TopicTreeQueryResult searchTree(TopicQuery topicQuery) {

		TopicTreeQueryResult result = null;

		try {
			result = kbManager.getExpansionTree(topicQuery);
		} catch (CompassException e) {
			log.error("Error occured when do topic query", e);
			throw e;
		}

		return result;

	}

	@Override
	public TopicListQueryResult search(TopicQuery topicQuery) {

		TopicListQueryResult result = null;

		try {
			result = kbManager.getExpansionList(topicQuery);
		} catch (CompassException e) {
			log.error("Error occured when do topic query", e);
			throw e;
		}

		return result;

	}

	public CompassManagerImpl() {
	}

	public void init(Properties properties) throws ConfigurationException {

		log.info("Start Initializing Compass2Manager...");
		ftsManager = FTSFactory.getInstance().getFTSImplementation();
		if (ftsManager == null) {
			log.warn("FullText Search Manager is not available. The FullText Search plugin cann't be used.");
		}
		ltManager = LTFactory.getInstance().getLTImplementation();
		if (ltManager == null) {
			log.warn("Language Tool Manager is not available. The Language Tool plugin cann't be used.");

		}

		suggestionProviderManager = SuggestionProviderFactory.getInstance()
				.getSuggestionProviderManager();
		if (suggestionProviderManager == null) {
			log.warn("Suggestion Provider Manager is not available. The Suggestion Provider plugin cann't be used.");
		}

		kbManager = KBFactory.getInstance().getKBImplementation();
		if (kbManager == null) {
			throw new ConfigurationException("KBManager is null");
		}

		importManager = ImportManagerFactory.getInstance().getImportManager();
		if (importManager == null) {
			throw new ConfigurationException("ImportManager is null");
		}

		log.info("Finished Initalizing Compass2Manager!");
	}

	public void setConfiguration(ConfigurationManager configurationManager)
			throws ConfigurationException {
		this.configurationManager = configurationManager;
	}

	/**
	 * 
	 * @param userSearch
	 */
	public String getSpellingSuggestion(String userSearch) {
		return ltManager.getSpellingSuggestion(userSearch);
	}

	/**
	 * 
	 * @param userSearch
	 */
	public List<String> getSpellingSuggestions(String userSearch) {
		return ltManager.getSpellingSuggestions(userSearch);
	}

	public DocumentDetails getDocument(String id) throws ConfigurationException {
		if (ftsManager != null) {
			return ftsManager.getDocument(id);
		}
		return null;
	}

	public List<SearchField> getAvailableSearchFields() {
		return configurationManager.getSearchFields().getFields();
	}

	public Collection<SearchField> getDefaultSearchFields() {

		if (defaultSearchFields == null) {

			defaultSearchFields = new ArrayList<SearchField>();
			for (SearchField field : configurationManager.getSearchFields()
					.getFields()) {
				if (field.isDefaultIndex()) {
					defaultSearchFields.add(field);
				}
			}
		}
		return defaultSearchFields;
	}

	@Override
	public void importKnowledgeBase(String file, String type,
			KnowledgeBase entityModelCreatorFactory) throws ImportException {

		File importFile = new File(file);

		if (!importFile.exists() || !importFile.isFile()) {
			throw new CompassException(CompassErrorID.IMP_IO_ERROR,
					"Import file not exist or not file!");
		}

		try {
			importManager.importKB(importFile, type, entityModelCreatorFactory);
		} catch (ImportException e) {
			log.error("File is not uploaded.", e);
			throw e;
		}

	}

	@Override
	public Collection<String> getAvailableUploadTypes() {

		return importManager.listAvailableImportPlugins();
	}

	@Override
	public Collection<Scope> getImportedScopes() {

		return importManager.getScopes();
	}

	@Override
	public Collection<KnowledgeBaseDescriptor> listKnowledgeBases() {
		return kbManager.listActiveKnowledgeBases();
	}

	@Override
	public KnowledgeBaseDescriptor getKnowledgeBase(long id) {
		return kbManager.getKnowledgeBase(id);
	}

	@Override
	public Collection<RelationType> getImportedRelationTypes() {

		return importManager.getRelationTypes();
	}

	@Override
	public void filterImportedKnowledgeBase(Collection<Long> scopesInner) {
		importManager.filterImportedKnowledgeBase(scopesInner);

	}

	@Override
	public KnowledgeBase storeImportedKnowledgeBase(
			Collection<RelationTypeSetting> relationTypeSettings) {
		try {
			log.info("Started Saving knowledge base model!");

			importManager.updateRelationTypes(relationTypeSettings);

			ImportResult resultModel = importManager.getResultModel();

			KnowledgeBase knowledgeBase = resultModel.getEntityFactory();
			kbManager.storeKnowledgeBase(knowledgeBase);

			log.info("Finished Saving knowledge base model!");

			return knowledgeBase;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			if (e instanceof CompassException) {
				throw (CompassException) e;
			}

			throw new CompassException(
					"Error occured when try to store knowledge base!", e);
		}

	}

	@Override
	public Collection<RelationType> getRelationTypes(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		try {
			return kbManager.getRelationTypes(knowledgeBaseDescriptor);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			if (e instanceof CompassException) {
				throw (CompassException) e;
			}

			throw new CompassException(
					"Error occured when try to get relation types from knowledgebase id: "
							+ knowledgeBaseDescriptor.getId() + "!", e);
		}
	}

	@Override
	public void updateRelationTypes(
			Collection<RelationTypeSetting> createRelationType,
			KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		try {
			for (RelationTypeSetting relationTypeSetting : createRelationType) {

				kbManager.updateRelationTypeWeight(relationTypeSetting,
						knowledgeBaseDescriptor);

			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			if (e instanceof CompassException) {
				throw (CompassException) e;
			}

			throw new CompassException(
					"Error occured when try to update relation types from knowledgebase id: "
							+ knowledgeBaseDescriptor.getId() + "!", e);
		}
	}

	@Override
	public KnowledgeBase newInstanceKnowledgeBase(
			KnowledgeBaseType knowledgeBaseType) {
		return kbManager.newInstanceKnowledgeBase(knowledgeBaseType);
	}

	@Override
	public void indexDocument(boolean reindex, int depth, String dir,
			String contentHandlerType) {
		try {
			ftsManager.indexDocument(reindex, depth, dir, contentHandlerType);
		} catch (CompassException e) {
			log.error("Error occured when index document: " + dir + "!", e);
			throw e;
		}
	}

	@Override
	public Collection<File> getFilesFromKnowledgeBaseImportDirectory() {
		try {
			return kbManager.getFilesFromImportDirectory();
		} catch (Exception e) {
			log.error("Error occured when files list from import directory", e);
			throw new CompassException(
					"Error occured when files list from import directory", e);
		}
	}

	@Override
	public Collection<String> getSuggestions(String word,
			int maxSuggestionNumber) {
		if (suggestionProviderManager != null) {
			try {
				return suggestionProviderManager.getSuggestions(word,
						maxSuggestionNumber);
			} catch (Exception e) {
				log.error("Error occured when getSuggestion!", e);
				throw new CompassException("Error occured when getSuggestion!",
						e);
			}
		}

		throw new CompassException("Suggestion Provider Manager not available.");
	}

	@Override
	public void indexKnowledgeBaseForSuggestion(KnowledgeBase knowledgeBase) {
		if (suggestionProviderManager != null) {
			try {
				suggestionProviderManager
						.indexKnowledgeBaseForSuggestion(knowledgeBase);
			} catch (Exception e) {
				log.error(
						"Error occured when indexKnowledgeBaseForSuggestion!",
						e);
				throw new CompassException(
						"Error occured when indexKnowledgeBaseForSuggestion!",
						e);
			}
		} else {
			log.error(
					"Suggestion Provider Manager not available.");
			throw new CompassException(
					"Suggestion Provider Manager not available.");
		}
	}

	@Override
	public Collection<Scope> getScopes(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		try {
			return kbManager.getScopes(knowledgeBaseDescriptor);
		} catch (Exception e) {
			log.error("Error occured when get scopes!", e);

			if (e instanceof CompassException) {
				throw (CompassException) e;
			}

			throw new CompassException("Error occured when get scopes!", e);
		}
	}

	@Override
	public void reReadConfig() throws ConfigurationException {
		defaultSearchFields = null;
		configurationManager.initConfig();
		clearFactories();
		init(null);
	}

	private void clearFactories() {
		FTSFactory.clear();
		KBFactory.clear();
		LTFactory.clear();
		SuggestionProviderFactory.clear();
		ImportManagerFactory.clear();
	}

	@Override
	public SearchOptions getDefaultSearchConfig() {
		return configurationManager.getSearchOptions();
	}

	@Override
	public void indexDocuments(String configPath, String contentHandlerType) {
		try {
			ftsManager.indexDocuments(configPath, contentHandlerType);
		} catch (Exception e) {
			log.error("Error occured when index document: " + configPath + "!",
					e);

			if (e instanceof CompassException) {
				throw (CompassException) e;
			}

			throw new CompassException("Error occured when get scopes!", e);
		}

	}

	@Override
	public TopicQuery createTopicQuery() {
		return QueryFactory.getInstance().createTopicQuery();
	}

	@Override
	public FullTextQuery createFullTextQuery() {
		return QueryFactory.getInstance().createFullTextQuery();
	}

	@Override
	public String getStem(String word) {
		if (ltManager != null)
			return ltManager.getStem(word);
		return word;
	}
}
