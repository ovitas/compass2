package no.ovitas.compass2.kbimport.xtm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.exception.CompassErrorID;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.exception.ImportException;
import no.ovitas.compass2.kb.KnowledgeBase;
import no.ovitas.compass2.kbimport.ImportManagerPlugin;
import no.ovitas.compass2.model.ImportResult;
import no.ovitas.compass2.model.ImportResultImpl;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.Relation;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.RelationTypeSetting;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.model.knowledgebase.Topic;
import no.ovitas.compass2.model.knowledgebase.TopicName;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <code>ImportManagerPlugin</code> implementation for importing XTM 1.0 type
 * knowledge base definitions.
 * 
 * @author Csaba Daniel
 */
public class XTM10ImportManagerPlugin implements ImportManagerPlugin {

	private class CreateTopicRule extends Rule {

		@Override
		public void begin(String namespace, String name, Attributes attributes)
				throws Exception {
			String id = attributes.getValue(ATTR_ID);
			if (id == null)
				id = attributes.getValue(NS_XTM10, ATTR_ID);

			topic = knowledgeBase.createTopic(id);
		}
	}

	private class CreateTopicNameRule extends Rule {

		@Override
		public void begin(String namespace, String name, Attributes attributes)
				throws Exception {
			topicName = knowledgeBase.createTopicName();
		}

		@Override
		public void end(String namespace, String name) throws Exception {
			if (topicName != null) {
				if (topicName.getName() != null) {
					knowledgeBase.addTopicName(topic, topicName);
				} else {
					knowledgeBase.removeTopicName(topicName.getImportId());
				}
			}
		}
	}

	private class SetTopicNameScopeRefRule extends Rule {

		@Override
		public void begin(String namespace, String name, Attributes attributes)
				throws Exception {
			String id = attributes.getValue(ATTR_HREF);
			id = getIdFromRef(id);
			topicName.setProperty(SCOPE_REF, id);
		}
	}

	private class SetTopicNameStringRule extends Rule {

		@Override
		public void body(String namespace, String name, String text)
				throws Exception {
			if (!StringUtils.isEmpty(text)) {
				topicName.setName(text);
			}
		}
	}

	private class CreateRelationRule extends Rule {

		@Override
		public void begin(String namespace, String name, Attributes attributes)
				throws Exception {
			relation = knowledgeBase.createRelation();
		}
	}

	private class SetRelationTypeRefRule extends Rule {

		@Override
		public void begin(String namespace, String name, Attributes attributes)
				throws Exception {
			String id = attributes.getValue(ATTR_HREF);
			id = getIdFromRef(id);
			relation.setProperty(RELATION_TYPE_REF, id);
		}
	}

	private class SetRelationTopicRefRule extends Rule {

		@Override
		public void begin(String namespace, String name, Attributes attributes)
				throws Exception {
			String id = attributes.getValue(ATTR_HREF);
			id = getIdFromRef(id);
			if (relation.getProperty(SOURCE_TOPIC_REF) == null) {
				relation.setProperty(SOURCE_TOPIC_REF, id);
			} else if (relation.getProperty(TARGET_TOPIC_REF) == null) {
				relation.setProperty(TARGET_TOPIC_REF, id);
			}
		}
	}

	private static final String NS_XTM10 = "http://www.topicmaps.org/xtm/1.0/";

	private static final String ATTR_HREF = "xlink:href";
	private static final String ATTR_ID = "id";

	private static final String XP_TOPIC = "topicMap/topic";
	private static final String XP_BASENAME = "topicMap/topic/baseName";
	private static final String XP_BASENAMESCOPETREF = "topicMap/topic/baseName/scope/topicRef";
	private static final String XP_BASENAMESTRING = "topicMap/topic/baseName/baseNameString";
	private static final String XP_ASSOCIATION = "topicMap/association";
	private static final String XP_ASSOCIATIONTYPETREF = "topicMap/association/instanceOf/topicRef";
	private static final String XP_TOPICREF = "topicMap/association/member/topicRef";

	private static final String SCOPE_REF = "scopeRef";
	private static final String RELATION_TYPE_REF = "relationTypeRef";
	private static final String SOURCE_TOPIC_REF = "sourceTopicRef";
	private static final String TARGET_TOPIC_REF = "targetTopicRef";

	private static final String DEFAULT_SCOPE = "Default Scope";

	private ImportResultImpl importResult = new ImportResultImpl();
	private KnowledgeBase knowledgeBase;
	private Topic topic;
	private TopicName topicName;
	private Relation relation;

	/**
	 * Create an import manager implementation.
	 */
	public XTM10ImportManagerPlugin() {
	}

	@Override
	public void init(Properties params) throws ConfigurationException {
	}

	@Override
	public void importKB(File file, KnowledgeBase knowledgeBase)
			throws ImportException {
		try {
			InputStream in = new FileInputStream(file);

			importKB(in, knowledgeBase);
		} catch (FileNotFoundException e) {
			throw new ImportException(CompassErrorID.IMP_RES_MISSING, e
					.getMessage(), e);
		}
	}

	@Override
	public void importKB(InputStream in, KnowledgeBase knowledgeBase)
			throws ImportException {
		this.knowledgeBase = knowledgeBase;

		parse(in);
		process();
	}

	@Override
	public void importKB(URL url, KnowledgeBase knowledgeBase)
			throws ImportException {
		try {
			URLConnection conn = url.openConnection();
			InputStream in = conn.getInputStream();

			importKB(in, knowledgeBase);
		} catch (IOException e) {
			throw new ImportException(CompassErrorID.IMP_IO_ERROR, e
					.getMessage(), e);
		}
	}

	void parse(InputStream in) throws ImportException {
		try {
			Digester dg = new Digester();

			dg.setNamespaceAware(true);
			dg.setRuleNamespaceURI(NS_XTM10);

			dg.addRule(XP_TOPIC, new CreateTopicRule());
			dg.addRule(XP_BASENAME, new CreateTopicNameRule());
			dg.addRule(XP_BASENAMESCOPETREF, new SetTopicNameScopeRefRule());
			dg.addRule(XP_BASENAMESTRING, new SetTopicNameStringRule());
			dg.addRule(XP_ASSOCIATION, new CreateRelationRule());
			dg.addRule(XP_ASSOCIATIONTYPETREF, new SetRelationTypeRefRule());
			dg.addRule(XP_TOPICREF, new SetRelationTopicRefRule());

			dg.parse(in);
		} catch (IOException e) {
			throw new ImportException(CompassErrorID.IMP_IO_ERROR, e
					.getMessage(), e);
		} catch (SAXException e) {
			throw new ImportException(CompassErrorID.IMP_INVALID_KB_FORMAT, e
					.getMessage(), e);
		}
	}

	void process() {
		processTopicNames();
		processRelations();
		processScopeNames();
		processRelationTypeNames();
	}

	@Override
	public void setConfiguration(ConfigurationManager manager)
			throws ConfigurationException {
	}

	@Override
	public void filterImportedKnowledgeBase(Collection<Long> scopes) {
		knowledgeBase.filterScopes(scopes);
	}

	@Override
	public void cleanUp() {
		knowledgeBase.cleanUp();
	}

	@Override
	public KnowledgeBaseDescriptor getKnowledgeBaseDescriptor() {
		return knowledgeBase.getKnowledgeBaseDescriptor();
	}

	@Override
	public Collection<RelationType> getRelationTypes() {
		return knowledgeBase.getRelationTypes();
	}

	@Override
	public ImportResult getResultModel() {
		importResult.setEntityFactory(knowledgeBase);
		return importResult;
	}

	@Override
	public Collection<Scope> getScopes() {
		return knowledgeBase.getScopes();
	}

	@Override
	public void updateRelationTypes(
			Collection<RelationTypeSetting> relationTypeSettings) {
		for (RelationTypeSetting setting : relationTypeSettings) {
			Long importId = setting.getImportId();

			RelationType relationType = knowledgeBase
					.findRelationType(importId);
			if (relationType != null) {
				relationType.setWeight(setting.getWeight());
				relationType.setGeneralizationWeight(setting
						.getGeneralizationWeight());
			}
		}
	}

	private void processTopicNames() {
		Scope defaultScope = null;
		// Iterate over topics and their topic names
		for (Topic topic : knowledgeBase.getTopics()) {
			Collection<TopicName> topicNameColl = topic.getNames();
			if (topicNameColl != null) {
				for (TopicName topicName : topic.getNames()) {
					// Check scope string reference
					String scopeRef = (String) topicName.getProperty(SCOPE_REF);
					if (scopeRef != null) {
						// Check if the scope topic exists
						Topic scopeTopic = knowledgeBase
								.findTopicByExternalId(scopeRef);
						if (scopeTopic != null) {
							// Set topic name scope
							Scope scope = knowledgeBase
									.findScopeByExternalId(scopeRef);
							if (scope == null)
								scope = knowledgeBase.createScope(scopeRef);
							knowledgeBase.addScope(topicName, scope);
						}
					} else {
						if (defaultScope == null) {
							defaultScope = knowledgeBase
									.createScope(DEFAULT_SCOPE);
							defaultScope.addDisplayName(DEFAULT_SCOPE);
						}
						knowledgeBase.addScope(topicName, defaultScope);
					}
				}
			}
		}
	}

	private void processRelations() {
		// Iterate over relations
		for (Relation relation : knowledgeBase.getRelations()) {
			// Set relation type
			String relationTypeRef = (String) relation
					.getProperty(RELATION_TYPE_REF);
			if (relationTypeRef != null) {
				Topic relationTypeTopic = knowledgeBase
						.findTopicByExternalId(relationTypeRef);
				if (relationTypeTopic != null) {
					RelationType relationType = knowledgeBase
							.findRelationTypeByExternalId(relationTypeRef);
					if (relationType == null)
						relationType = knowledgeBase
								.createRelationType(relationTypeRef);
					knowledgeBase.addRelationType(relation, relationType);
				}
			}
			// Set topics
			String sourceRef = (String) relation.getProperty(SOURCE_TOPIC_REF);
			String targetRef = (String) relation.getProperty(TARGET_TOPIC_REF);
			if (sourceRef != null && targetRef != null) {
				Topic source = knowledgeBase.findTopicByExternalId(sourceRef);
				Topic target = knowledgeBase.findTopicByExternalId(targetRef);
				if (source != null && target != null) {
					knowledgeBase.addTopicsToRelation(source, target, relation);
				}
			}
		}
	}

	private void processScopeNames() {
		// Iterate over scopes
		Collection<Scope> scopes = knowledgeBase.getScopes();

		Iterator<Scope> scopeItr = scopes.iterator();

		while (scopeItr.hasNext()) {
			Scope scope = scopeItr.next();
			String id = scope.getExternalId();
			// Find related topic and pass over its names
			Topic topic = knowledgeBase.findTopicByExternalId(id);
			if (topic != null) {
				for (TopicName topicName : topic.getNames()) {
					String topicNameString = topicName.getName();
					scope.addDisplayName(topicNameString);
				}
			}

			if (scope.getDisplayName() == null
					|| "".equals(scope.getDisplayName())) {
				scopeItr.remove();
			}

		}
	}

	private void processRelationTypeNames() {
		// Iterate over relation types
		for (RelationType relationType : knowledgeBase.getRelationTypes()) {
			String id = relationType.getExternalId();
			// Find related topic and pass over its names
			Topic topic = knowledgeBase.findTopicByExternalId(id);
			if (topic != null) {
				for (TopicName topicName : topic.getNames()) {
					String topicNameString = topicName.getName();
					relationType.addDisplayName(topicNameString);
				}
			}
		}
	}

	private String getIdFromRef(String ref) {
		return ref.replaceFirst("#", "");
	}
}
