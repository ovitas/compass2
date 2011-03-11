/**
 * 
 */
package no.ovitas.compass2.kb.store;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.exception.CompassErrorID;
import no.ovitas.compass2.exception.CompassException;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.kb.KnowledgeBase;
import no.ovitas.compass2.kb.KnowledgeBaseManager;
import no.ovitas.compass2.kb.TopicNameIndexer;
import no.ovitas.compass2.kb.store.model.DirectRelationTypeEntity;
import no.ovitas.compass2.kb.store.model.KnowledgeBaseEntity;
import no.ovitas.compass2.kb.store.service.DirectRelationManager;
import no.ovitas.compass2.kb.store.service.DirectRelationTypeManager;
import no.ovitas.compass2.kb.store.service.KnowledgeBaseDaoManager;
import no.ovitas.compass2.kb.store.service.ScopeManager;
import no.ovitas.compass2.kb.store.service.TopicExpansionManager;
import no.ovitas.compass2.kb.store.service.TopicManager;
import no.ovitas.compass2.kb.store.service.TopicNameManager;
import no.ovitas.compass2.model.MatchTopicResult;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseType;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.RelationTypeSetting;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.model.knowledgebase.Topic;
import no.ovitas.compass2.search.KnowledgeBaseResult;
import no.ovitas.compass2.search.TopicCriteria;
import no.ovitas.compass2.search.TopicListQueryResult;
import no.ovitas.compass2.search.TopicQuery;
import no.ovitas.compass2.search.TopicResult;
import no.ovitas.compass2.search.TopicResultNode;
import no.ovitas.compass2.search.TopicTreeQueryResult;
import no.ovitas.compass2.search.impl.KnowledgeBaseResultImpl;
import no.ovitas.compass2.search.impl.KnowledgeBaseTreeResultImpl;
import no.ovitas.compass2.search.impl.TopicListQueryResultImpl;
import no.ovitas.compass2.search.impl.TopicTreeQueryResultImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @class KBManagerImpl
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.04.
 * 
 */
public class KBManagerImpl implements KnowledgeBaseManager {

	private static final String IMPORT_DIR = "importDir";
	private static final String TP_METHOD = "tpMethod";

	private static final String SPECGEN_TP_METHOD = "specGen";
	private static final String TWOWAY_TP_METHOD = "twoWay";

	private static final int TRANS_PATH_BATCH_SIZE = 10;

	private Log log = LogFactory.getLog(getClass());

	private ConfigurationManager configurationManager;
	private DirectRelationManager directRelationManager;
	private DirectRelationTypeManager directRelationTypeManager;
	private KnowledgeBaseDaoManager knowledgeBaseDaoManager;
	private TopicManager topicManager;
	private TopicNameManager topicNameManager;
	private ScopeManager scopeManager;

	private File importDirectory;

	private TopicNameIndexer topicNameIndexer;

	public KBManagerImpl() {

	}

	public void init(Properties properties) throws ConfigurationException {

		String importDir = properties.getProperty(IMPORT_DIR);

		if (importDir == null) {
			throw new ConfigurationException(
					"Import Directory does not configured!");
		}

		importDirectory = new File(importDir);

		if (!importDirectory.exists() || !importDirectory.isDirectory()) {
			throw new ConfigurationException(
					"Import Directory does not well configured: " + importDir
							+ "!");
		}

		List<KnowledgeBaseEntity> all = knowledgeBaseDaoManager.getAll();

	}

	public void setConfiguration(ConfigurationManager manager) {
		this.configurationManager = manager;
	}

	/**
	 * This is a setter method for directRelationManager.
	 * 
	 * @param directRelationManager
	 *            the directRelationManager to set
	 */
	public void setDirectRelationManager(
			DirectRelationManager directRelationManager) {
		this.directRelationManager = directRelationManager;
	}

	/**
	 * This is a setter method for directRelationTypeManager.
	 * 
	 * @param directRelationTypeManager
	 *            the directRelationTypeManager to set
	 */
	public void setDirectRelationTypeManager(
			DirectRelationTypeManager directRelationTypeManager) {
		this.directRelationTypeManager = directRelationTypeManager;
	}

	/**
	 * This is a setter method for knowledgeBaseManager.
	 * 
	 * @param knowledgeBaseManager
	 *            the knowledgeBaseManager to set
	 */
	public void setKnowledgeBaseDaoManager(
			KnowledgeBaseDaoManager knowledgeBaseManager) {
		this.knowledgeBaseDaoManager = knowledgeBaseManager;
	}

	/**
	 * This is a setter method for topicManager.
	 * 
	 * @param topicManager
	 *            the topicManager to set
	 */
	public void setTopicManager(TopicManager topicManager) {
		this.topicManager = topicManager;
	}

	/**
	 * This is a setter method for topicNameManager.
	 * 
	 * @param topicNameManager
	 *            the topicNameManager to set
	 */
	public void setTopicNameManager(TopicNameManager topicNameManager) {
		this.topicNameManager = topicNameManager;
	}

	public TopicTreeQueryResult getExpansionTree(TopicQuery query) {

		TopicTreeQueryResultImpl result = new TopicTreeQueryResultImpl(
				query.getTermStemPairs());

		Collection<TopicCriteria> topicCriterias = query.getTopicCriterias();

		Collection<String> terms = query.getSearchTerms();

		TopicExpansionManager expansionManager = createTopicExtensionManagerJPA();

		expansionManager.setMaxResults(query.getMaxTopicNumberToExpand());
		expansionManager.setMaxHops(query.getHopCount());
		expansionManager.setMinWeight(query.getThresholdWeight());

		long knowledgeBaseID;

		for (TopicCriteria topicCriteria : topicCriterias) {

			knowledgeBaseID = topicCriteria.getKnowledgeBase().getId();

			// Search base nodes id in lucene
			MatchTopicResult matchTopicResultObject = null;
			try {
				matchTopicResultObject = topicNameIndexer
						.getRelatedResults(terms, knowledgeBaseID,
								query.getMaxTopicNumberToExpand(),
								query.isPerfixMatch());
			} catch (IOException e) {
				throw new CompassException("Topic Name indexer throw error!", e);
			}

			// Get topic from database
			Set<Topic> baseTopics = topicManager
					.getBaseTopics(matchTopicResultObject);

			expansionManager.setDirectionFilter(topicCriteria
					.getRelationDirection());

			expansionManager.setScopeFilter(topicCriteria.getScopes());

			// Get expansions
			List<TopicResultNode> expandTree = expansionManager
					.expandTree(baseTopics);

			// Create term -> nodes map.
			KnowledgeBaseTreeResultImpl kbResultTree = new KnowledgeBaseTreeResultImpl();
			kbResultTree.setDirection(topicCriteria.getRelationDirection());
			kbResultTree.setKnowledgeBase(topicCriteria.getKnowledgeBase());

			Iterator<TopicResultNode> topicResultIterator;
			TopicResultNode node;
			Set<TopicResultNode> termNodes;
			for (Entry<String, Set<Long>> pair : matchTopicResultObject
					.getResults().entrySet()) {
				termNodes = new HashSet<TopicResultNode>(0);
				for (Long topicId : pair.getValue()) {
					topicResultIterator = expandTree.iterator();

					while (topicResultIterator.hasNext()) {
						node = topicResultIterator.next();

						if (node.getId() == topicId) {
							termNodes.add(node);
							topicResultIterator.remove();
							break;
						}
					}
				}

				if (termNodes.size() != 0)
					kbResultTree.addTerm(pair.getKey(), termNodes);

			}

			result.addTermResult(kbResultTree);
		}

		return result;
	}

	private TopicExpansionManager createTopicExtensionManagerJPA() {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath*:/applicationContext.xml",
						"classpath*:/applicationContext-dao.xml",
						"classpath*:/applicationContext-service.xml" });

		return (TopicExpansionManager) context
				.getBean("topicExpansionManagerJPA");
	}

	public TopicListQueryResult getExpansionList(TopicQuery query) {

		TopicListQueryResultImpl result = new TopicListQueryResultImpl(
				query.getTermStemPairs());

		Collection<TopicCriteria> topicCriterias = query.getTopicCriterias();

		Collection<String> terms = query.getSearchTerms();

		TopicExpansionManager expansionManager = createTopicExtensionManagerJPA();

		expansionManager.setMaxResults(query.getMaxTopicNumberToExpand());
		expansionManager.setMaxHops(query.getHopCount());
		expansionManager.setMinWeight(query.getThresholdWeight());

		long knowledgeBaseID;

		for (TopicCriteria topicCriteria : topicCriterias) {

			knowledgeBaseID = topicCriteria.getKnowledgeBase().getId();

			// Search base nodes id in lucene
			MatchTopicResult matchTopicResultObject;
			try {
				matchTopicResultObject = topicNameIndexer
						.getRelatedResults(terms, knowledgeBaseID,
								query.getMaxTopicNumberToExpand(),
								query.isPerfixMatch());
			} catch (IOException e) {
				throw new CompassException("Topic Name indexer throw error!", e);
			}

			// Get topic from database
			Set<Topic> baseTopics = topicManager
					.getBaseTopics(matchTopicResultObject);

			expansionManager.setDirectionFilter(topicCriteria
					.getRelationDirection());

			expansionManager.setScopeFilter(topicCriteria.getScopes());

			// Get expansions
			List<TopicResult> expandList = expansionManager
					.expandList(baseTopics);

			KnowledgeBaseResultImpl kbResults = new KnowledgeBaseResultImpl();

			kbResults.setKnowledgeBase(topicCriteria.getKnowledgeBase());

			kbResults.setDirection(topicCriteria.getRelationDirection());

			// Create term -> nodes map.
			Iterator<TopicResult> topicResultIterator;
			TopicResult node;
			Set<TopicResult> termNodes;
			for (Entry<String, Set<Long>> pair : matchTopicResultObject
					.getResults().entrySet()) {
				termNodes = new HashSet<TopicResult>(0);
				for (Long topicId : pair.getValue()) {
					topicResultIterator = expandList.iterator();

					while (topicResultIterator.hasNext()) {
						node = topicResultIterator.next();

						if (node.getBaseTopicId() == topicId) {
							termNodes.add(node);
							topicResultIterator.remove();
						}
					}
				}

				if (termNodes.size() != 0)
					kbResults.addTerm(pair.getKey(), termNodes);

			}

			result.addKnowledgeBaseResult(kbResults);

		}

		return result;

	}

	/**
	 * Modify the relationType weight.
	 * 
	 * @param relationType
	 * @param newWeight
	 * @param kb
	 */
	public void updateRelationTypeWeight(RelationTypeSetting relationType,
			KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		directRelationTypeManager.updateRelationsWeight(relationType,
				knowledgeBaseDescriptor);
	}

	/**
	 * Get all uploaded knowledge base.
	 * 
	 * @return
	 */
	public List<KnowledgeBaseDescriptor> listActiveKnowledgeBases() {

		return knowledgeBaseDaoManager.listActiveKnowledgeBases();
	}

	public KnowledgeBaseDescriptor getKnowledgeBase(long id) {
		return getKnowledgeBaseEntity(id);
	}

	KnowledgeBaseEntity getKnowledgeBaseEntity(Long id) {
		return knowledgeBaseDaoManager.get(id);
	}

	/**
	 * Save a new knowledge base or update an exist.
	 * 
	 * @param holder
	 */
	public void storeKnowledgeBase(KnowledgeBase modelFactory) {

		EntityModel em = (EntityModel) modelFactory;

		log.info("Save or update knowledgeBase entity started!");
		knowledgeBaseDaoManager.saveKnowledgeBaseEntity(em);
		log.info("Save or update knowledgeBase entity finished!");

		log.info("Save base entities started!");
		knowledgeBaseDaoManager.importKnowledgeBase(em);
		log.info("Save base entities finisheded!");

		try {
			log.info("Remove old index from index repository!");
			topicNameIndexer.removeKnowledgeBase(modelFactory
					.getKnowledgeBaseDescriptor());
			log.info("Add new index to index repository!");
			topicNameIndexer.saveKnowledgeBase(modelFactory);
		} catch (IOException e) {
			throw new CompassException(CompassErrorID.KB_SAVE_ERROR,
					"Error occured when save entity!", e);
		}

		KnowledgeBaseEntity knowledgeBaseEntity = em.getKnowledgeBaseEntity();

		knowledgeBaseEntity.setActive(true);
		knowledgeBaseDaoManager.save(knowledgeBaseEntity);
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<RelationType> getRelationTypes(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor) {

		Collection relationTypes = new HashSet();

		Collection<DirectRelationTypeEntity> directRelationTypesFromKnowledgeBase = directRelationTypeManager
				.getDirectRelationTypesFromKnowledgeBase(knowledgeBaseDescriptor);
		relationTypes = directRelationTypesFromKnowledgeBase;

		return relationTypes;
	}

	public void deleteKnowledgeBase(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		knowledgeBaseDaoManager.deleteKnowledgeBase(knowledgeBaseDescriptor);
	}

	/**
	 * This is a setter method for scopeManager.
	 * 
	 * @param scopeManager
	 *            the scopeManager to set
	 */
	public void setScopeManager(ScopeManager scopeManager) {
		this.scopeManager = scopeManager;
	}

	public KnowledgeBase newInstanceKnowledgeBase(
			KnowledgeBaseType knowledgeBaseType) {
		return newModelInstance(knowledgeBaseType);
	}

	private KnowledgeBaseJPAImpl newModelInstance(
			KnowledgeBaseType knowledgeBaseType) {
		return new KnowledgeBaseJPAImpl(this, knowledgeBaseType);
	}

	Collection<DirectRelationTypeEntity> getDirectRelationTypes(
			KnowledgeBaseEntity kbEntity) {
		return directRelationTypeManager
				.getDirectRelationTypesFromKnowledgeBase(kbEntity);
	}

	public Collection<File> getFilesFromImportDirectory() {
		return Arrays.asList(importDirectory.listFiles());
	}

	public Collection<Scope> getScopes(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		return scopeManager.getScopesInKnowledgeBase(knowledgeBaseDescriptor);
	}

	public void setTopicNameIndexer(TopicNameIndexer topicNameIndexer) {
		this.topicNameIndexer = topicNameIndexer;

	}

}
