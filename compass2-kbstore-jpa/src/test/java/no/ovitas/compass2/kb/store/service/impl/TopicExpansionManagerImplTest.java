package no.ovitas.compass2.kb.store.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import no.ovitas.compass2.kb.store.model.DirectRelationEntity;
import no.ovitas.compass2.kb.store.model.DirectRelationTypeEntity;
import no.ovitas.compass2.kb.store.model.KnowledgeBaseEntity;
import no.ovitas.compass2.kb.store.model.ScopeEntity;
import no.ovitas.compass2.kb.store.model.TopicEntity;
import no.ovitas.compass2.kb.store.model.TopicNameEntity;
import no.ovitas.compass2.kb.store.model.TopicResultNodeImpl;
import no.ovitas.compass2.search.TopicResult;
import no.ovitas.compass2.search.TopicResultNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TopicExpansionManagerImplTest {

	private TopicExpansionManagerImpl manager;

	private KnowledgeBaseEntity kb;

	private ScopeEntity s1;
	private ScopeEntity s2;

	private TopicEntity t1;
	private TopicEntity t2;
	private TopicEntity t3;
	private TopicEntity t4;
	private TopicEntity t5;
	private TopicEntity t6;
	private TopicEntity t7;

	private DirectRelationTypeEntity rt1;
	private DirectRelationTypeEntity rt2;
	private DirectRelationTypeEntity rt3;

	private DirectRelationEntity r1;
	private DirectRelationEntity r2;
	private DirectRelationEntity r3;
	private DirectRelationEntity r4;
	private DirectRelationEntity r5;
	private DirectRelationEntity r6;

	@Before
	public void setUp() throws Exception {
		manager = new TopicExpansionManagerImpl();

		kb = new KnowledgeBaseEntity();
		kb.setDisplayName("kb");

		s1 = createScope(1, "s1", kb);
		s2 = createScope(2, "s2", kb);

		t1 = createTopic(1, "t1", new ScopeEntity[] { s1 }, kb);
		t2 = createTopic(2, "t2", new ScopeEntity[] { s1, s2 }, kb);
		t3 = createTopic(3, "t3", new ScopeEntity[] { s1, s2 }, kb);
		t4 = createTopic(4, "t4", new ScopeEntity[] { s1, s2 }, kb);
		t5 = createTopic(5, "t5", new ScopeEntity[] { s1, s2 }, kb);
		t6 = createTopic(6, "t6", new ScopeEntity[] { s1 }, kb);
		t7 = createTopic(7, "t7", new ScopeEntity[] { s1 }, kb);

		rt1 = createRelationType(1, "rt1", 0.9, 0.7, kb);
		rt2 = createRelationType(2, "rt2", 0.7, 0.9, kb);
		rt3 = createRelationType(3, "rt3", 0.5, 0.5, kb);

		r1 = createRelation(1, rt1, t4, t1);
		r2 = createRelation(2, rt1, t1, t5);
		r3 = createRelation(3, rt3, t2, t5);
		r4 = createRelation(4, rt2, t6, t2);
		r5 = createRelation(5, rt1, t6, t7);
		r6 = createRelation(6, rt3, t2, t3);

		t1.addRelation(r1);
		t4.addRelation(r1);
		t1.addRelation(r2);
		t5.addRelation(r2);
		t2.addRelation(r3);
		t5.addRelation(r3);
		t6.addRelation(r4);
		t2.addRelation(r4);
		t6.addRelation(r5);
		t7.addRelation(r5);
		t2.addRelation(r6);
		t3.addRelation(r6);
	}

	private ScopeEntity createScope(long id, String name, KnowledgeBaseEntity kb) {
		ScopeEntity scope = new ScopeEntity();

		scope.setDisplayName(name);
		scope.setExternalId(name);
		scope.setId(id);
		scope.setImportId(id);
		scope.setKnowledgeBase(kb);

		return scope;
	}

	private TopicEntity createTopic(long id, String name, ScopeEntity[] scopes,
			KnowledgeBaseEntity kb) {
		TopicEntity topic = new TopicEntity();

		topic.setExternalId(name);
		topic.setId(id);
		topic.setImportId(id);
		topic.setKnowledgeBase(kb);

		int i = 0;
		for (ScopeEntity scope : scopes) {
			TopicNameEntity topicName = new TopicNameEntity();

			topicName.setId(10 * id + i);
			topicName.setImportId(10 * id + i);
			topicName.setName(name.concat("^").concat(scope.getDisplayName()));
			topicName.setScopeEntity(scope);

			topic.addTopicName(topicName);

			i++;
		}

		return topic;
	}

	private DirectRelationTypeEntity createRelationType(long id, String name,
			double weight, double genWeight, KnowledgeBaseEntity kb) {
		DirectRelationTypeEntity relationType = new DirectRelationTypeEntity();

		relationType.setActive(true);
		relationType.setDisplayName(name);
		relationType.setExternalId(name);
		relationType.setGeneralizationWeight(genWeight);
		relationType.setId(id);
		relationType.setImportId(id);
		relationType.setKnowledgeBaseEntity(kb);
		relationType.setOccurrence(Integer.valueOf(Long.toString(id)));
		relationType.setWeight(weight);

		return relationType;
	}

	private DirectRelationEntity createRelation(long id,
			DirectRelationTypeEntity relationType, TopicEntity startTopic,
			TopicEntity endTopic) {
		DirectRelationEntity relation = new DirectRelationEntity();

		relation.setEndTopic(endTopic);
		relation.setId(id);
		relation.setImportId(id);
		relation.setRelationType(relationType);
		relation.setStartTopic(startTopic);

		return relation;
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testExpandList() throws Exception {
		manager.setMaxHops(1);
		manager.setMaxResults(5);
		manager.setMinWeight(0.6);

		List<TopicResult> results = manager.expandList((Collection) Arrays
				.asList(t1, t2, t3));
		Assert.assertEquals(5, results.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testExpandTree() throws Exception {
		manager.setMaxHops(1);
		manager.setMaxResults(5);
		manager.setMinWeight(0.6);

		List<TopicResultNode> results = manager.expandTree((Collection) Arrays
				.asList(t1, t2, t3));
		Assert.assertEquals(3, results.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testExpandListWithResultFilter() throws Exception {
		manager.setMaxHops(2);
		manager.setMaxResults(4);
		manager.setMinWeight(0.2);

		List<TopicResult> results = manager.expandList((Collection) Arrays
				.asList(t1, t2, t3));
		Assert.assertEquals(4, results.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetRelatedTopics() throws Exception {
		TopicResultNodeImpl tr2 = new TopicResultNodeImpl(t2);

		tr2.setBaseTopicId(2);
		tr2.setHops(0);
		tr2.setNames(manager.filterTopicNames(t2));
		tr2.setWeight(1);

		manager.setMaxHops(1);
		manager.setMinWeight(0.6);
		manager.setScopeFilter((Collection) Arrays.asList(s1));

		Collection<TopicResultNodeImpl> results = manager.getRelatedTopics(tr2);

		Assert.assertEquals(1, results.size());
		Assert.assertEquals(t6, results.iterator().next().getTopic());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFilterTopicNames() throws Exception {
		manager.setScopeFilter((Collection) Arrays.asList(s1));

		Collection<String> names = manager.filterTopicNames(t2);

		Assert.assertEquals(1, names.size());
		Assert.assertEquals("t2^s1", names.iterator().next());
	}
}
