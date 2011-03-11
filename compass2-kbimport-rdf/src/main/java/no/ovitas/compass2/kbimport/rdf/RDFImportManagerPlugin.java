package no.ovitas.compass2.kbimport.rdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Properties;

import javax.xml.namespace.QName;

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
import org.apache.commons.digester.ExtendedBaseRules;
import org.apache.commons.digester.Rule;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <code>ImportManagerPlugin</code> implementation for importing RDF type
 * knowledge base definitions.
 * 
 * @author Csaba Daniel
 */
public class RDFImportManagerPlugin implements ImportManagerPlugin {

	private class RDFDescriptionRule extends Rule {

		@Override
		public void begin(String namespace, String name, Attributes attributes)
				throws Exception {
			String topicUri = attributes.getValue(NS_RDF, ATTR_ABOUT);

			if (!StringUtils.isEmpty(topicUri)) {
				topic = knowledgeBase.findTopicByExternalId(topicUri);
				if (topic == null)
					topic = knowledgeBase.createTopic(topicUri);
			}
		}

		@Override
		public void end(String namespace, String name) throws Exception {
			// Cleanup
			topic = null;
		}
	}

	private class PredicateRule extends Rule {

		private boolean isNameGenElement = false;
		private String lang;

		@Override
		public void begin(String namespace, String name, Attributes attributes)
				throws Exception {
			// Skip if there is no active topic
			if (topic == null)
				return;

			QName qname = new QName(namespace, name);
			// Name generating element
			if (mapping.hasNameGenElement(qname)) {
				isNameGenElement = true;
				// Store lang attribute for scoping
				lang = attributes.getValue(ATTR_LANG);
				// Relation generating element
			} else if (mapping.hasRelationGenElement(qname)) {
				// Fetch relation data
				String targetTopicRef = attributes.getValue(NS_RDF,
						ATTR_RESOURCE);
				if (!StringUtils.isEmpty(targetTopicRef)) {
					// Fetch/create the relation type
					String predicateUri = namespace.concat(name);

					RelationType relationType = knowledgeBase
							.findRelationTypeByExternalId(predicateUri);
					if (relationType == null) {
						String label = mapping
								.getRelationGenElementLabel(qname);

						relationType = knowledgeBase
								.createRelationType(predicateUri);
						relationType.addDisplayName(label);
					}
					// Create relation
					Relation relation = knowledgeBase.createRelation();
					knowledgeBase.addRelationType(relation, relationType);
					relation.setProperty(SOURCE_TOPIC_REF, topic
							.getExternalId());
					relation.setProperty(TARGET_TOPIC_REF, targetTopicRef);
				}
			}
		}

		@Override
		public void body(String namespace, String name, String text)
				throws Exception {
			// Name generating element
			if (isNameGenElement) {
				if (!StringUtils.isEmpty(text)) {
					Scope scope = null;
					// Fetch scope
					if (!StringUtils.isEmpty(lang)) {
						scope = knowledgeBase.findScopeByExternalId(lang);

						if (scope == null) {
							scope = knowledgeBase.createScope(lang);
							scope.setDisplayName(lang);
						}
					} else {
						scope = knowledgeBase
								.findScopeByExternalId(DEFAULT_SCOPE);

						if (scope == null) {
							scope = knowledgeBase.createScope(DEFAULT_SCOPE);
							scope.setDisplayName(DEFAULT_SCOPE);
						}
					}
					// Create topic name
					TopicName topicName = knowledgeBase.createTopicName();
					topicName.setName(text);
					knowledgeBase.addScope(topicName, scope);
					knowledgeBase.addTopicName(topic, topicName);
				}
			}
		}

		@Override
		public void end(String namespace, String name) throws Exception {
			// Cleanup
			lang = null;
			isNameGenElement = false;
		}
	}

	private static final String NS_RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static final String XP_RDF_DESCRIPTION = "RDF/Description";
	private static final String XP_PREDICATE = "RDF/Description/*";
	private static final String ATTR_ABOUT = "about";
	private static final String ATTR_RESOURCE = "resource";
	private static final String ATTR_LANG = "xml:lang";
	private static final String SOURCE_TOPIC_REF = "sourceTopicRef";
	private static final String TARGET_TOPIC_REF = "targetTopicRef";
	private static final String DEFAULT_SCOPE = "Default Scope";
	private static final String MAPPING_EXTENSION = "-mapping.xml";
	private static final String MAPPING_PARAM = "mapping=";

	private RDFImportMapping mapping;
	private KnowledgeBase knowledgeBase;
	private Topic topic;

	/**
	 * Create an import manager plugin implementation.
	 */
	public RDFImportManagerPlugin() {
	}

	@Override
	public void init(Properties properties) throws ConfigurationException {
	}

	@Override
	public void setConfiguration(ConfigurationManager manager)
			throws ConfigurationException {
	}

	@Override
	public void cleanUp() {
		knowledgeBase.cleanUp();
	}

	@Override
	public void filterImportedKnowledgeBase(Collection<Long> scopes) {
		knowledgeBase.filterScopes(scopes);
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
		ImportResultImpl result = new ImportResultImpl();
		result.setEntityFactory(knowledgeBase);
		return result;
	}

	@Override
	public Collection<Scope> getScopes() {
		return knowledgeBase.getScopes();
	}

	@Override
	public void importKB(File file, KnowledgeBase knowledgeBase)
			throws ImportException {
		try {
			File mappingFile = getMappingFile(file);
			loadMapping(mappingFile);

			FileInputStream in = new FileInputStream(file);
			importKBStream(in, knowledgeBase);
		} catch (FileNotFoundException e) {
			throw new ImportException(CompassErrorID.IMP_RES_MISSING, e
					.getMessage(), e);
		}
	}

	@Override
	public void importKB(InputStream in, KnowledgeBase knowledgeBase)
			throws ImportException {
		throw new UnsupportedOperationException("java.io.InputStream based "
				+ "import is not supported by this plugin");
	}

	@Override
	public void importKB(URL url, KnowledgeBase knowledgeBase)
			throws ImportException {
		try {
			URL mappingURL = getMappingURL(url);
			loadMapping(mappingURL);

			URLConnection conn = url.openConnection();
			InputStream in = conn.getInputStream();
			importKBStream(in, knowledgeBase);
		} catch (IOException e) {
			throw new ImportException(CompassErrorID.IMP_IO_ERROR, e
					.getMessage(), e);
		}
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

	File getMappingFile(File file) throws ImportException {
		if (file == null)
			throw new ImportException("Invalid mapping file");

		String mappingFileName = file.getName();
		String mappingFilePath = file.getPath();

		int extSepPos = mappingFileName.lastIndexOf(".");
		if (extSepPos > 0) {
			mappingFileName = mappingFileName.substring(0, extSepPos);
			mappingFileName = mappingFileName.concat(MAPPING_EXTENSION);
		}

		int fileSepPos = mappingFilePath.lastIndexOf(File.separator);
		if (fileSepPos > 0) {
			mappingFilePath = mappingFilePath.substring(0, fileSepPos + 1);
			mappingFilePath = mappingFilePath.concat(mappingFileName);
		} else {
			mappingFilePath = mappingFileName;
		}

		return new File(mappingFilePath);
	}

	URL getMappingURL(URL url) throws ImportException {
		try {
			if (url == null)
				throw new ImportException("Invalid mapping URL");

			String query = url.getQuery();

			int mappingParamPos = query.indexOf(MAPPING_PARAM);
			if (mappingParamPos == -1)
				throw new ImportException("Mapping parameter is missing");

			int mappingParamEndPos = query.indexOf("&", mappingParamPos);
			if (mappingParamEndPos == -1)
				mappingParamEndPos = query.length();

			String mappingParam = query.substring(mappingParamPos
					+ MAPPING_PARAM.length(), mappingParamEndPos);
			String mapping = URLDecoder.decode(mappingParam, "utf-8");

			URL mappingURL = new URL(mapping);
			return mappingURL;
		} catch (UnsupportedEncodingException e) {
			throw new ImportException(CompassErrorID.IMP_INVALID_RES_URL, e
					.getMessage(), e);
		} catch (MalformedURLException e) {
			throw new ImportException(CompassErrorID.IMP_INVALID_RES_URL, e
					.getMessage(), e);
		}
	}

	private void importKBStream(InputStream in, KnowledgeBase knowledgeBase)
			throws ImportException {
		this.knowledgeBase = knowledgeBase;
		parse(in);
		process();
	}

	private void loadMapping(File file) throws ImportException {
		try {
			FileInputStream in = new FileInputStream(file);
			loadMapping(in);
		} catch (FileNotFoundException e) {
			throw new ImportException(CompassErrorID.IMP_MAP_RES_MISSING, e
					.getMessage(), e);
		}
	}

	private void loadMapping(URL url) throws ImportException {
		try {
			URLConnection conn = url.openConnection();
			conn.setDoInput(true);
			InputStream in = conn.getInputStream();
			loadMapping(in);
		} catch (IOException e) {
			throw new ImportException(CompassErrorID.IMP_IO_ERROR, e
					.getMessage(), e);
		}
	}

	private void loadMapping(InputStream in) throws ImportException {
		mapping = new RDFImportMapping();
		mapping.load(in);
	}

	private void parse(InputStream in) throws ImportException {
		try {
			Digester digester = new Digester();
			digester.setNamespaceAware(true);
			digester.setRules(new ExtendedBaseRules());

			digester.addRule(XP_RDF_DESCRIPTION, new RDFDescriptionRule());
			digester.addRule(XP_PREDICATE, new PredicateRule());

			digester.parse(in);
		} catch (IOException e) {
			throw new ImportException(CompassErrorID.IMP_IO_ERROR, e
					.getMessage(), e);
		} catch (SAXException e) {
			throw new ImportException(CompassErrorID.IMP_INVALID_KB_FORMAT, e
					.getMessage(), e);
		}
	}

	private void process() {
		processRelations();
	}

	private void processRelations() {
		for (Relation relation : knowledgeBase.getRelations()) {
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
}
