package no.ovitas.compass2.kb.store;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import no.ovitas.compass2.kb.KnowledgeBase;
import no.ovitas.compass2.model.knowledgebase.Relation;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.model.knowledgebase.Topic;
import no.ovitas.compass2.model.knowledgebase.TopicName;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Test knowledge base creator utility.
 * 
 * @author Csaba Daniel
 */
public class TestKnowledgeBaseCreator {

	private class BaseRule extends Rule {

		protected KnowledgeBase kb;

		public BaseRule(KnowledgeBase kb) {
			this.kb = kb;
		}
	}

	private class ScopeRule extends BaseRule {

		public ScopeRule(KnowledgeBase kb) {
			super(kb);
		}

		@Override
		public void begin(String namespace, String name, Attributes attributes)
				throws Exception {
			String externalId = attributes.getValue("external-id");
			String displayName = attributes.getValue("display-name");

			Scope scope = kb.createScope(externalId);
			scope.setDisplayName(displayName);

			scopes.put(externalId, scope);
		}
	}

	private class RelationTypeRule extends BaseRule {

		public RelationTypeRule(KnowledgeBase kb) {
			super(kb);
		}

		@Override
		public void begin(String namespace, String name, Attributes attributes)
				throws Exception {
			String externalId = attributes.getValue("external-id");
			String displayName = attributes.getValue("display-name");
			double weight = Double.valueOf(attributes.getValue("weight"));
			double genWeight = Double.valueOf(attributes
					.getValue("generalization-weight"));

			RelationType relationType = kb.createRelationType(externalId);
			relationType.setDisplayName(displayName);
			relationType.setWeight(weight);
			relationType.setGeneralizationWeight(genWeight);

			relationTypes.put(externalId, relationType);
		}
	}

	private class TopicRule extends BaseRule {

		public TopicRule(KnowledgeBase kb) {
			super(kb);
		}

		@Override
		public void begin(String namespace, String name, Attributes attributes)
				throws Exception {
			String externalId = attributes.getValue("external-id");

			Topic topic = kb.createTopic(externalId);

			topics.put(externalId, topic);

			digester.push(topic);
		}

		@Override
		public void end(String namespace, String name) throws Exception {
			digester.pop();
		}
	}

	private class TopicNameRule extends BaseRule {

		public TopicNameRule(KnowledgeBase kb) {
			super(kb);
		}

		@Override
		public void begin(String namespace, String name, Attributes attributes)
				throws Exception {
			String displayName = attributes.getValue("display-name");
			String scopeRef = attributes.getValue("scope-ref");
			Scope scope = scopes.get(scopeRef);
			Topic topic = (Topic) digester.peek();

			TopicName topicName = kb.createTopicName();
			topicName.setName(displayName);

			kb.addScope(topicName, scope);
			kb.addTopicName(topic, topicName);
		}
	}

	private class RelationRule extends BaseRule {

		public RelationRule(KnowledgeBase kb) {
			super(kb);
		}

		@Override
		public void begin(String namespace, String name, Attributes attributes)
				throws Exception {
			String relationTypeRef = attributes.getValue("relation-type-ref");
			RelationType relationType = relationTypes.get(relationTypeRef);

			String sourceTopicRef = attributes.getValue("source-topic-ref");
			Topic sourceTopic = topics.get(sourceTopicRef);

			String targetTopicRef = attributes.getValue("target-topic-ref");
			Topic targetTopic = topics.get(targetTopicRef);

			Relation relation = kb.createRelation();

			kb.addRelationType(relation, relationType);
			kb.addTopicsToRelation(sourceTopic, targetTopic, relation);
		}
	}

	private static final String FILE_PATH = "src/test/resources/testKnowledgeBase.xml";

	private Map<String, Scope> scopes = new HashMap<String, Scope>();
	private Map<String, RelationType> relationTypes = new HashMap<String, RelationType>();
	private Map<String, Topic> topics = new HashMap<String, Topic>();

	/**
	 * Create a test knowledge base within the specified
	 * <code>KnowledgeBase</code> object.
	 * 
	 * @param kb
	 *            the <code>KnowledgeBase</code> object to build the test
	 *            knowledge base in
	 * @return the <code>KnowledgeBase</code> object with the test knowledge
	 *         base
	 */
	public KnowledgeBase create(KnowledgeBase kb) {
		try {
			Digester digester = new Digester();

			digester.addRule("knowledge-base/scope", new ScopeRule(kb));
			digester.addRule("knowledge-base/relation-type",
					new RelationTypeRule(kb));
			digester.addRule("knowledge-base/topic", new TopicRule(kb));
			digester.addRule("knowledge-base/topic/topic-name",
					new TopicNameRule(kb));
			digester.addRule("knowledge-base/relation", new RelationRule(kb));

			File file = new File(FILE_PATH);
			digester.parse(file);

			return kb;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (SAXException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
