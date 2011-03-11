package no.ovitas.compass2.ws.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import no.ovitas.compass2.config.CompassManager;
import no.ovitas.compass2.exception.CompassException;
import no.ovitas.compass2.model.ConnectingType;
import no.ovitas.compass2.model.FittingType;
import no.ovitas.compass2.model.MatchingType;
import no.ovitas.compass2.model.knowledgebase.RelationDirection;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.search.FullTextFieldCriteria;
import no.ovitas.compass2.search.FullTextQuery;
import no.ovitas.compass2.search.TopicCriteria;
import no.ovitas.compass2.search.TopicQuery;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ExtendedBaseRules;
import org.xml.sax.SAXException;

/**
 * Unmarshaller implementation to unmarshal Compass2 complex configuration from
 * XML.
 * 
 * @author Csaba Daniel
 */
public class CompassWSUnmarshaller {

	private static final String NS_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	private static final String XSD_SOURCE = "/compass-request.xsd";

	private static final String XP_FULL_TEXT_FIELD_CRITERIA = "full-text-query/full-text-field-criteria";
	private static final String XP_FULL_TEXT_QUERY = "full-text-query";
	private static final String XP_SCOPES = "*/scopes";
	private static final String XP_SCOPE = "*/scopes/scope";
	private static final String XP_TERM = "*/term";
	private static final String XP_TOPIC_CRITERIA = "topic-query/topic-criteria";
	private static final String XP_TOPIC_QUERY = "topic-query";

	private static final String ATTR_BOOST = "boost";
	private static final String ATTR_CONNECTION_TYPE = "connection-type";
	private static final String ATTR_FIELD_NAME = "field-name";
	private static final String ATTR_FITTING_TYPE = "fitting-type";
	private static final String ATTR_FUZZY_SEARCH = "fuzzy-search";
	private static final String ATTR_HOP_COUNT = "hop-count";
	private static final String ATTR_ID = "id";
	private static final String ATTR_KNOWLEDGE_BASE_ID = "knowledge-base-id";
	private static final String ATTR_MATCHING_TYPE = "matching-type";
	private static final String ATTR_MAX_NUMBER_OF_HITS = "max-number-of-hits";
	private static final String ATTR_MAX_TOPIC_NUMBER_TO_EXPAND = "max-topic-number-to-expand";
	private static final String ATTR_PREFIX_MATCH = "prefix-match";
	private static final String ATTR_RELATION_DIRECTION = "relation-direction";
	private static final String ATTR_RESULT_THRESHOLD = "result-threshold";
	private static final String ATTR_THRESHOLD_WEIGHT = "threshold-weight";

	private static final String[] ATTRIBUTES = new String[] { ATTR_BOOST,
			ATTR_CONNECTION_TYPE, ATTR_FIELD_NAME, ATTR_FITTING_TYPE,
			ATTR_FUZZY_SEARCH, ATTR_HOP_COUNT, ATTR_ID, ATTR_KNOWLEDGE_BASE_ID,
			ATTR_MATCHING_TYPE, ATTR_MAX_NUMBER_OF_HITS,
			ATTR_MAX_TOPIC_NUMBER_TO_EXPAND, ATTR_PREFIX_MATCH,
			ATTR_RELATION_DIRECTION, ATTR_RESULT_THRESHOLD,
			ATTR_THRESHOLD_WEIGHT };

	private static final String PROP_BOOST = "boost";
	private static final String PROP_CONNECTION_TYPE = "connectionType";
	private static final String PROP_FIELD_NAME = "fieldName";
	private static final String PROP_FITTING_TYPE = "fittingType";
	private static final String PROP_FUZZY_SEARCH = "fuzzySearch";
	private static final String PROP_HOP_COUNT = "hopCount";
	private static final String PROP_ID = "id";
	private static final String PROP_KNOWLEDGE_BASE_ID = "knowledgeBaseId";
	private static final String PROP_MATCHING_TYPE = "matchingType";
	private static final String PROP_MAX_NUMBER_OF_HITS = "maxNumberOfHits";
	private static final String PROP_MAX_TOPIC_NUMBER_TO_EXPAND = "maxTopicNumberToExpand";
	private static final String PROP_PREFIX_MATCH = "prefixMatch";
	private static final String PROP_RELATION_DIRECTION = "relationDirection";
	private static final String PROP_RESULT_THRESHOLD = "resultThreshold";
	private static final String PROP_THRESHOLD_WEIGHT = "thresholdWeight";

	private static final String[] PROPERTIES = new String[] { PROP_BOOST,
			PROP_CONNECTION_TYPE, PROP_FIELD_NAME, PROP_FITTING_TYPE,
			PROP_FUZZY_SEARCH, PROP_HOP_COUNT, PROP_ID, PROP_KNOWLEDGE_BASE_ID,
			PROP_MATCHING_TYPE, PROP_MAX_NUMBER_OF_HITS,
			PROP_MAX_TOPIC_NUMBER_TO_EXPAND, PROP_PREFIX_MATCH,
			PROP_RELATION_DIRECTION, PROP_RESULT_THRESHOLD,
			PROP_THRESHOLD_WEIGHT };

	private static final String M_ADD_TERM = "addTerm";
	private static final String M_SET_SCOPES = "setScopes";
	private static final String M_ADD = "add";

	private CompassManager manager;

	/**
	 * Construct a new unmarshaller with the specified manager.
	 * 
	 * @param manager
	 *            the <code>CompassManager</code> to use during unmarshalling
	 */
	public CompassWSUnmarshaller(CompassManager manager) {
		this.manager = manager;
	}

	/**
	 * Unmarshal an object from the specified XML string.
	 * 
	 * @param xml
	 *            the object descriptor in XML
	 * @return the object unmarshalled
	 * @throws CompassException
	 *             if any technical error occurs
	 */
	@SuppressWarnings("unchecked")
	public <T> T unmarshal(String xml, Class<T> clazz) {
		try {
			Digester digester = getDigester();
			registerEnumConverter();

			digester.addFactoryCreate(XP_TOPIC_QUERY,
					new CompassObjectCreationFactory(manager, digester,
							TopicQuery.class));
			digester.addSetProperties(XP_TOPIC_QUERY, ATTRIBUTES, PROPERTIES);

			digester.addFactoryCreate(XP_TOPIC_CRITERIA,
					new CompassObjectCreationFactory(manager, digester,
							TopicCriteria.class));
			digester
					.addSetProperties(XP_TOPIC_CRITERIA, ATTRIBUTES, PROPERTIES);

			digester.addCallMethod(XP_TERM, M_ADD_TERM, 0);

			digester.addObjectCreate(XP_SCOPES, ArrayList.class);
			digester.addSetNext(XP_SCOPES, M_SET_SCOPES);

			digester.addFactoryCreate(XP_SCOPE,
					new CompassObjectCreationFactory(manager, digester,
							Scope.class));
			digester.addSetProperties(XP_SCOPE, ATTRIBUTES, PROPERTIES);

			digester.addSetNext(XP_SCOPE, M_ADD);

			digester.addFactoryCreate(XP_FULL_TEXT_QUERY,
					new CompassObjectCreationFactory(manager, digester,
							FullTextQuery.class));
			digester.addSetProperties(XP_FULL_TEXT_QUERY, ATTRIBUTES,
					PROPERTIES);

			digester.addFactoryCreate(XP_FULL_TEXT_FIELD_CRITERIA,
					new CompassObjectCreationFactory(manager, digester,
							FullTextFieldCriteria.class));
			digester.addSetProperties(XP_FULL_TEXT_FIELD_CRITERIA, ATTRIBUTES,
					PROPERTIES);

			InputStream in = new ByteArrayInputStream(xml.getBytes());
			Object resultObj = digester.parse(in);

			if (clazz.isAssignableFrom(resultObj.getClass())) {
				return (T) resultObj;
			}

			throw new org.compass.core.CompassException(
					"Incompatible types, expected " + clazz.getName() + " got "
							+ resultObj.getClass().getName());
		} catch (IOException e) {
			throw new CompassException(e.getMessage(), e);
		} catch (SAXException e) {
			throw new CompassException(e.getMessage(), e);
		}
	}

	/**
	 * Get the underlying <code>CompassManager</code>.
	 * 
	 * @return the underlying <code>CompassManager</code>
	 */
	public CompassManager getCompassManager() {
		return manager;
	}

	/**
	 * Set the underlying <code>CompassManager</code>.
	 * 
	 * @param manager
	 *            the underlying <code>CompassManager</code> to set
	 */
	public void setCompassManager(CompassManager manager) {
		this.manager = manager;
	}

	private Schema getSchema() throws SAXException {
		InputStream in = CompassWSUnmarshaller.class
				.getResourceAsStream(XSD_SOURCE);
		StreamSource ss = new StreamSource(in);

		SchemaFactory sf = SchemaFactory.newInstance(NS_XML_SCHEMA);
		Schema schema = sf.newSchema(ss);

		return schema;
	}

	private Digester getDigester() throws SAXException {
		Digester digester = new Digester();

		Schema schema = getSchema();
		digester.setXMLSchema(schema);

		digester.setNamespaceAware(true);

		ExtendedBaseRules rules = new ExtendedBaseRules();
		digester.setRules(rules);

		return digester;
	}

	@SuppressWarnings("unchecked")
	private void registerEnumConverter() {
		Converter enumConverter = new Converter() {

			@Override
			public Object convert(Class type, Object value) {
				return Enum.valueOf(type, (String) value);
			}
		};

		ConvertUtils.register(enumConverter, ConnectingType.class);
		ConvertUtils.register(enumConverter, FittingType.class);
		ConvertUtils.register(enumConverter, MatchingType.class);
		ConvertUtils.register(enumConverter, RelationDirection.class);
	}
}
