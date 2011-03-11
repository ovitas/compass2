/**
 * 
 */
package no.ovitas.compass2.web.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;
import no.ovitas.compass2.config.CompassManager;
import no.ovitas.compass2.config.factory.CompassManagerFactory;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.exception.ImportException;
import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;
import no.ovitas.compass2.search.FullTextFieldCriteria;
import no.ovitas.compass2.search.FullTextQuery;
import no.ovitas.compass2.search.FullTextQueryResult;
import no.ovitas.compass2.search.QueryResult;
import no.ovitas.compass2.search.TopicCriteria;
import no.ovitas.compass2.search.TopicQuery;

import org.junit.Test;

/**
 * @class Compass2ServiceImplTest
 * @project compass2-web
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.09.02.
 * 
 */
public class Compass2ServiceImplTest extends TestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test method for
	 * {@link no.ovitas.compass2.web.server.Compass2ServiceImpl#getResults(no.ovitas.compass2.web.client.model.SearchModel)}
	 * .
	 */
	@Test
	public void testFullTextGetResults() {
		// CompassManager compassManager = CompassManagerFactory.getInstance()
		// .getCompassManager();
		//
		// FullTextQuery createFullTextQuery = compassManager
		// .createFullTextQuery();
		//
		// FullTextFieldCriteria createCriteria = createFullTextQuery
		// .createCriteria("content");
		//
		// createCriteria.addTerm("norway");
		//
		// FullTextQueryResult search =
		// compassManager.search(createFullTextQuery);

	}

	@Test
	public void testAllGetResults() {
		// CompassManager compassManager = CompassManagerFactory.getInstance()
		// .getCompassManager();
		//
		// TopicQuery createTopicQuery = compassManager.createTopicQuery();
		//
		// createTopicQuery.addTerm("museum");
		//
		// TopicCriteria createTopicCriteria = createTopicQuery
		// .createTopicCriteria();
		//
		// Collection<KnowledgeBaseDescriptor> listKnowledgeBases =
		// compassManager
		// .listKnowledgeBases();
		//
		// KnowledgeBaseDescriptor desc = null;
		//
		// for (KnowledgeBaseDescriptor knowledgeBaseDescriptor :
		// listKnowledgeBases) {
		// if (knowledgeBaseDescriptor.getDisplayName().equals("teszt")) {
		// desc = knowledgeBaseDescriptor;
		// break;
		// }
		// }
		//
		// createTopicCriteria.setKnowledgeBaseID(desc.getId());
		//
		// createTopicCriteria.setRelationDirection(RelationDirection.SPEC);
		//
		// createTopicCriteria.setScopeIds(compassManager.getScopes(desc));
		//
		// createTopicQuery.setTreeSearch(true);
		//
		// FullTextQuery createFullTextQuery = compassManager
		// .createFullTextQuery();
		//
		// FullTextFieldCriteria createCriteria = createFullTextQuery
		// .createCriteria("content");
		//
		// createCriteria.addTerm("museum");
		//
		// QueryResult search = compassManager.search(createTopicQuery,
		// createFullTextQuery);

	}

	@Test
	public void testTopicGetResults() {
		CompassManager compassManager = CompassManagerFactory.getInstance()
				.getCompassManager();

		TopicQuery createTopicQuery = compassManager.createTopicQuery();

		createTopicQuery.addTerm("oslo");

		KnowledgeBaseDescriptor desc = null;
		
		Collection<KnowledgeBaseDescriptor> listKnowledgeBases = compassManager
		.listKnowledgeBases();
		for (KnowledgeBaseDescriptor knowledgeBaseDescriptor : listKnowledgeBases) {
			if (knowledgeBaseDescriptor.getDisplayName().startsWith("visitnorway o")) {
				desc = knowledgeBaseDescriptor;
				break;
			}
			
		}
		TopicCriteria createTopicCriteria = createTopicQuery
				.createTopicCriteria(desc);

//		createTopicQuery.setMaxTopicNumberToExpand(1);
		createTopicCriteria.setRelationDirection(RelationDirection.SPEC);

		createTopicCriteria.setScopes(compassManager.getScopes(desc));

//		createTopicQuery.setTreeSearch(false);
//		createTopicQuery.setMaxTopicNumberToExpand(10);
//		createTopicQuery.setHopCount(2);
//		createTopicQuery.setThresholdWeight(0.0001);

		FullTextQuery createFullTextQuery = compassManager
				.createFullTextQuery();

		FullTextFieldCriteria createCriteria = createFullTextQuery
				.createCriteria("content");

		createCriteria.addTerm("oslo");
		
		createCriteria = createFullTextQuery
		.createCriteria("title");

		createCriteria.addTerm("oslo");
		
		createCriteria = createFullTextQuery
		.createCriteria("keyword");

		createCriteria.addTerm("oslo");
		
	//		createFullTextQuery.setResultThreshold(0.00000001);
	//		createFullTextQuery.setMaxNumberOfHits(10000000);

		QueryResult search = compassManager.search(createTopicQuery,
				createFullTextQuery);
		
		Collection<Hit> hits = search.getFullTextQueryResult().getHits();
		
//		FullTextQueryResult search = compassManager.search(createFullTextQuery);
//		
//		Collection<Hit> hits = search.getHits();
		
		List<String> onlyOne= new ArrayList<String>();
		
		for (Hit hit : hits) {
			if (onlyOne.contains(hit.getURI())) {
				System.out.println(hit.getURI() + " " + hit.getTitle());
			} else {
				onlyOne.add(hit.getURI());
			}
		}
		

		// TopicListQueryResult search = compassManager
		// .search(createTopicQuery);
		//
		// TermTopicWeightResultCollectorImpl collector = new
		// TermTopicWeightResultCollectorImpl();
		//
		// search.collectTermTopicWeights(collector);
		//
		// collector.getClass();

	}
	
	
	@Test
	public void testConfigIndex() {
//		CompassManager compassManager = CompassManagerFactory.getInstance().getCompassManager();
//		
//		compassManager.indexDocuments("/media/work/product/compass/test/configuration.xml", "vsnorway");
	}
	
	@Test
	public void testSpellChecker() {
//		CompassManager compassManager = CompassManagerFactory.getInstance().getCompassManager();
//		
//		String spellingSuggestion = compassManager.getSpellingSuggestion("history");
	}

	@Test
	public void testUpload() throws ImportException {
		// CompassManager compassManager = CompassManagerFactory.getInstance()
		// .getCompassManager();
		//
		// Collection<File> filesFromKnowledgeBaseImportDirectory =
		// compassManager
		// .getFilesFromKnowledgeBaseImportDirectory();
		//
		// KnowledgeBase knowledgeBase = compassManager
		// .newInstanceEntityModelCreatorFactory(KnowledgeBaseType.TWOWAY);
		//
		// knowledgeBase.createDefaultKnowledgeBase("vstest");
		//
		// compassManager.uploadKnowledgeBase("/media/work/product/compass/data/kb/visitnorway.xtm",
		// "XTM10", knowledgeBase);
		//
		// Collection<Scope> importedScopes =
		// compassManager.getImportedScopes();
		// Set<Long> selecetedScopes = new HashSet<Long>();
		// for (Scope scope : importedScopes) {
		//
		// selecetedScopes.add(scope.getImportId());
		//
		// }
		// //
		// compassManager.startImportKBProcess(selecetedScopes);
		//
		// List<RelationTypeSetting> list = new
		// ArrayList<RelationTypeSetting>();
		// RelationTypeSetting setting;
		// for (RelationType relationType : compassManager
		// .getRelationTypesInImportedKnowledgeBase()) {
		// setting = new RelationTypeSetting(relationType.getImportId(),
		// relationType.getExternalId(), relationType.getWeight(),
		// relationType.getGeneralizationWeight());
		// list.add(setting);
		// }
		//
		// compassManager.storeImportedKnowledgeBase(list);
	}

	@Test
	public void testIndexConfig() throws ConfigurationException {

		// CompassManager compassManager = CompassManagerFactory.getInstance()
		// .getCompassManager();
		//
		// compassManager.indexDocuments("/media/work/workspace/compass/compass2-fts-lucene/src/test/resources/configuration.xml",
		// "vsnorway");

	}
	
}
