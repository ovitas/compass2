package no.ovitas.compass2.kbimport.rdf;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

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

public class RDFImportManagerPluginTest {

	private static final String TEST_FILE_PATH = "src/test/resources/NorwayMuseums.xml";

	private Log logger = LogFactory.getLog(RDFImportManagerPluginTest.class);

	@Test
	public void testGetMappingFile() throws Exception {
		RDFImportManagerPlugin manager = new RDFImportManagerPlugin();

		File file = new File("dir" + File.separator + "test.rdf");
		File mapping = manager.getMappingFile(file);

		Assert.assertEquals("dir" + File.separator + "test-mapping.xml",
				mapping.getPath());
	}

	@Test
	public void testGetMappingURL() throws Exception {
		RDFImportManagerPlugin manager = new RDFImportManagerPlugin();

		String rdf = "http://test.ovitas.no/data/rdf?id=9";
		String mapping = "http://test.ovitas.no/rdfmapping/rdf?id=9";
		String mappingEncoded = URLEncoder.encode(mapping, "utf-8");
		String full = rdf + "&mapping=" + mappingEncoded;
		URL fullURL = new URL(full);

		URL mappingURL = new URL(mapping);
		URL calculatedURL = manager.getMappingURL(fullURL);

		Assert.assertEquals(mappingURL, calculatedURL);
	}

	@Test
	public void testImportKBFileKnowledgeBase() throws Exception {
		File file = new File(TEST_FILE_PATH);

		CompassManagerFactory cmFactory = CompassManagerFactory.getInstance();
		CompassManager cmManager = cmFactory.getCompassManager();
		KnowledgeBase knowledgeBase = cmManager
				.newInstanceKnowledgeBase(KnowledgeBaseType.TWOWAY);
		knowledgeBase.createDefaultKnowledgeBase("a");

		Properties props = new Properties();

		RDFImportManagerPlugin manager = new RDFImportManagerPlugin();
		manager.init(props);
		manager.importKB(file, knowledgeBase);

		knowledgeBase.cleanUp();

		Assert.assertNotSame(0, knowledgeBase.getTopics().size());
		Assert.assertNotSame(0, knowledgeBase.getRelations().size());
		Assert.assertNotSame(0, knowledgeBase.getRelationTypes().size());
		Assert.assertNotSame(0, knowledgeBase.getScopes().size());

		for (Topic topic : knowledgeBase.getTopics()) {
			Assert.assertNotSame(0, topic.getNames().size());

			for (TopicName topicName : topic.getNames()) {
				Assert.assertNotNull(topicName.getName());
				Assert.assertNotNull(topicName.getScope());

				Scope scope = topicName.getScope();
				long scopeId = scope.getImportId();
				Assert.assertNotNull(knowledgeBase.findScope(scopeId));
			}
		}

		for (Scope scope : knowledgeBase.getScopes()) {
			Assert.assertNotNull(scope.getDisplayName());
		}

		for (Relation relation : knowledgeBase.getRelations()) {
			Assert.assertNotNull(relation.getSource());
			Assert.assertNotNull(relation.getTarget());
			Assert.assertNotNull(relation.getType());

			Topic source = relation.getSource();
			long sourceId = source.getImportId();
			Assert.assertNotNull(knowledgeBase.findTopic(sourceId));

			Topic target = relation.getTarget();
			long targetId = target.getImportId();
			Assert.assertNotNull(knowledgeBase.findTopic(targetId));

			RelationType relationType = relation.getType();
			long relationTypeId = relationType.getImportId();
			Assert
					.assertNotNull(knowledgeBase
							.findRelationType(relationTypeId));
		}

		for (RelationType relationType : knowledgeBase.getRelationTypes()) {
			Assert.assertNotNull(relationType.getDisplayName());
		}

		logger.debug("Import test successful: " + TEST_FILE_PATH);
	}
}
