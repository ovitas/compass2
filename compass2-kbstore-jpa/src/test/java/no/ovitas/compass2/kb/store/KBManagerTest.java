package no.ovitas.compass2.kb.store;

import no.ovitas.compass2.kb.KnowledgeBase;
import no.ovitas.compass2.kb.KnowledgeBaseManager;
import no.ovitas.compass2.kb.store.service.BaseManagerTestCase;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseType;

import org.junit.Before;

public class KBManagerTest extends BaseManagerTestCase {

	private KnowledgeBaseManager kbManager;
	private KnowledgeBase kb;

	@Before
	public void setUp() {
		kbManager = (KnowledgeBaseManager) applicationContext.getBean("kbBean");
		kbManager.setTopicNameIndexer(new MockTopicNameIndexer());

		kb = kbManager
				.newInstanceKnowledgeBase(KnowledgeBaseType.TWOWAY);

		kb.createDefaultKnowledgeBase("testKB", "testKB",
				KnowledgeBaseType.TWOWAY);

		TestKnowledgeBaseCreator creator = new TestKnowledgeBaseCreator();
		creator.create(kb);
	}
}
