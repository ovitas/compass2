package no.ovitas.compass2.ws.server;

import java.util.Collection;

import no.ovitas.compass2.config.CompassManager;
import no.ovitas.compass2.exception.CompassException;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.search.FullTextFieldCriteria;
import no.ovitas.compass2.search.FullTextQuery;
import no.ovitas.compass2.search.TopicCriteria;
import no.ovitas.compass2.search.TopicQuery;

import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.Digester;
import org.xml.sax.Attributes;

/**
 * Compass2 implementation of the <code>ObjectCreationFactory</code> interface
 * using its abstract implementation.
 * 
 * @author Csaba Daniel
 */
public class CompassObjectCreationFactory extends AbstractObjectCreationFactory {

	private static final String ATTR_ID = "id";
	private static final String ATTR_KNOWLEDGE_BASE_ID = "knowledge-base-id";
	private static final String ATTR_FIELD_NAME = "field-name";

	private CompassManager manager;
	private Class<?> clazz;

	/**
	 * Create a factory instance with the provided creation helper objects.
	 * 
	 * @param manager
	 *            the underlying <code>CompassManager</code> object
	 * @param digester
	 *            the underlying <code>Digester</code> object
	 * @param clazz
	 *            the class of the object to be created
	 */
	public CompassObjectCreationFactory(CompassManager manager,
			Digester digester, Class<?> clazz) {
		this.manager = manager;
		this.digester = digester;
		this.clazz = clazz;
	}

	@Override
	public Object createObject(Attributes attributes) throws Exception {
		if (TopicQuery.class.equals(clazz)) {
			TopicQuery query = manager.createTopicQuery();
			return query;
		} else if (FullTextQuery.class.equals(clazz)) {
			FullTextQuery query = manager.createFullTextQuery();
			return query;
		} else if (TopicCriteria.class.equals(clazz)) {
			TopicQuery query = (TopicQuery) digester.peek();
			
			long knowledgeBaseId = Long.valueOf(attributes
					.getValue(ATTR_KNOWLEDGE_BASE_ID));
			KnowledgeBaseDescriptor kbd = manager
			.getKnowledgeBase(knowledgeBaseId);
			TopicCriteria criteria = query.createTopicCriteria(kbd);

			return criteria;
		} else if (FullTextFieldCriteria.class.equals(clazz)) {
			FullTextQuery query = (FullTextQuery) digester.peek();

			String fieldName = attributes.getValue(ATTR_FIELD_NAME);
			FullTextFieldCriteria criteria = query.createCriteria(fieldName);

			return criteria;
		} else if (Scope.class.equals(clazz)) {
			long id = Long.valueOf(attributes.getValue(ATTR_ID));

			TopicCriteria criteria = (TopicCriteria) digester.peek(1);
			KnowledgeBaseDescriptor kb = criteria.getKnowledgeBase();

			Collection<Scope> scopes = manager.getScopes(kb);
			for (Scope scope : scopes) {
				if (id == scope.getId()) {
					return scope;
				}
			}

			throw new CompassException("Scope not found with id: " + id);
		} else {
			throw new CompassException("Invalid object creation call");
		}
	}
}
