package no.ovitas.compass2.kbimport.xtm;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import no.ovitas.compass2.config.CompassManager;
import no.ovitas.compass2.config.factory.CompassManagerFactory;
import no.ovitas.compass2.kb.KnowledgeBase;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseType;
import no.ovitas.compass2.model.knowledgebase.Relation;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.model.knowledgebase.Topic;
import no.ovitas.compass2.model.knowledgebase.TopicName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

public class XTM10ImportManagerPluginTest {

	private static final String TEST_FILE_PATH = "src/test/resources/xtm10.xml";
	private static final String TEST_FILE2_PATH = "c:/home/kb/visitnorway.xtm";

	private Log logger = LogFactory.getLog(XTM10ImportManagerPlugin.class);

	@Test
	public void testImportKB() throws Exception {
		File file = new File(TEST_FILE2_PATH);

		CompassManagerFactory cmFactory = CompassManagerFactory.getInstance();
		CompassManager cmManager = cmFactory.getCompassManager();
		KnowledgeBase knowledgeBase = cmManager
				.newInstanceKnowledgeBase(KnowledgeBaseType.TWOWAY);
		knowledgeBase.createDefaultKnowledgeBase("a");

		XTM10ImportManagerPlugin manager = new XTM10ImportManagerPlugin();
		manager.importKB(file, knowledgeBase);
		List<Long> scopeIds = new ArrayList<Long>();
		for (Scope scope : manager.getScopes()) {
			if ("EN".equals(scope.getDisplayName())
					|| "NO;NO".equals(scope.getDisplayName()))
				scopeIds.add(scope.getImportId());
		}

		manager.filterImportedKnowledgeBase(scopeIds);
		manager.cleanUp();

		Assert.assertNotSame(0, knowledgeBase.getTopics().size());
		Assert.assertNotSame(0, knowledgeBase.getRelations().size());
		Assert.assertNotSame(0, knowledgeBase.getRelationTypes().size());
		Assert.assertNotSame(0, knowledgeBase.getScopes().size());

		for (Topic topic : knowledgeBase.getTopics()) {
			if (topic.getNames().size() == 0) {
				logger.debug("Invalid topic: " + topic);
			}
			Assert.assertNotSame(0, topic.getNames().size());

			for (TopicName topicName : topic.getNames()) {
				if (topicName.getName() == null) {
					logger.debug("Invalid topic name for topic: " + topic);
				}
				Assert.assertNotNull(topicName.getName());
			}
		}

		for (Relation relation : knowledgeBase.getRelations()) {
			if (relation.getSource() == null || relation.getTarget() == null
					|| relation.getType() == null) {
				logger.debug("Invalid relation: " + relation);
			}
			if (!knowledgeBase.getRelationTypes().contains(relation.getType())) {
				logger.debug("Invalid relation: "
						+ relation.getType().getExternalId());
			}
			Assert.assertNotNull(relation.getSource());
			Assert.assertNotNull(relation.getTarget());
			Assert.assertNotNull(relation.getType());
		}

		for (RelationType relationType : knowledgeBase.getRelationTypes()) {
			if (relationType.getDisplayName() == null) {
				logger.debug("Invalid relation type: "
						+ relationType.getExternalId());
			}
			Assert.assertNotNull(relationType.getDisplayName());
		}

		logger.debug("Import and integrity check completed for: "
				+ TEST_FILE2_PATH);
	}

	@Test
	public void testImportKB2() throws Exception {
		File file = new File(TEST_FILE_PATH);

		CompassManagerFactory cmFactory = CompassManagerFactory.getInstance();
		CompassManager cmManager = cmFactory.getCompassManager();
		KnowledgeBase knowledgeBase = cmManager
				.newInstanceKnowledgeBase(KnowledgeBaseType.TWOWAY);
		knowledgeBase.createDefaultKnowledgeBase("a");

		XTM10ImportManagerPlugin manager = new XTM10ImportManagerPlugin();
		manager.importKB(file, knowledgeBase);

		Scope english = knowledgeBase.findScopeByExternalId("t003");
		Collection<Long> scopes = Arrays.asList(english.getImportId());
		knowledgeBase.filterScopes(scopes);

		knowledgeBase.cleanUp();
	}

	@Test
	public void emFactoryTest() throws Exception {
		CompassManagerFactory cmFactory = CompassManagerFactory.getInstance();
		CompassManager cmManager = cmFactory.getCompassManager();
		KnowledgeBase knowledgeBase = cmManager
				.newInstanceKnowledgeBase(KnowledgeBaseType.TWOWAY);
		Assert.assertNotNull(knowledgeBase);
	}
}
