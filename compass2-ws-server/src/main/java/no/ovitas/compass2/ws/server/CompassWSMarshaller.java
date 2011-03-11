package no.ovitas.compass2.ws.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;

import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.search.KnowledgeBaseResult;
import no.ovitas.compass2.search.KnowledgeBaseTreeResult;
import no.ovitas.compass2.search.TopicResult;
import no.ovitas.compass2.search.TopicResultNode;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.oxm.XmlMappingException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * <code>Marshaller</code> implementation to marshal Compass web service
 * responses into XML.
 * <p>
 * <i>Note: This is an internal implementation and designed only for marshalling
 * some Compass specific objects.</i>
 * </p>
 * 
 * @author Csaba Daniel
 */
public class CompassWSMarshaller implements Marshaller {

	private static final Class<?>[] supportedClasses = { Map.class,
			Collection.class, KnowledgeBaseDescriptor.class,
			RelationType.class, Scope.class, TopicResult.class, Term.class,
			Hit.class, KnowledgeBaseResult.class, KnowledgeBaseTreeResult.class };

	@Override
	public void marshal(Object graph, Result result) throws IOException,
			XmlMappingException {
		try {
			Element responseElem = createResponseElement();
			marshal(responseElem, graph);

			Document document = responseElem.getOwnerDocument();
			Source source = new DOMSource(document);

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();

			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			throw new MarshallingFailureException(e.getMessage(), e);
		} catch (ParserConfigurationException e) {
			throw new MarshallingFailureException(e.getMessage(), e);
		} catch (TransformerFactoryConfigurationError e) {
			throw new MarshallingFailureException(e.getMessage(), e);
		} catch (TransformerException e) {
			throw new MarshallingFailureException(e.getMessage(), e);
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		for (Class<?> supportedClass : supportedClasses) {
			if (supportedClass.isAssignableFrom(clazz))
				return true;
		}
		return false;
	}

	private Element createResponseElement() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.newDocument();

		Element responseElem = document.createElement("compass-response");
		document.appendChild(responseElem);

		return responseElem;
	}

	@SuppressWarnings("unchecked")
	private void marshal(Element parentElem, Object object)
			throws MarshallingFailureException {
		if (object == null)
			return;

		if (object instanceof Map) {
			marshal(parentElem, (Map) object);
		} else if (object instanceof Collection) {
			marshal(parentElem, (Collection) object);
		} else if (object instanceof KnowledgeBaseDescriptor) {
			marshal(parentElem, (KnowledgeBaseDescriptor) object);
		} else if (object instanceof RelationType) {
			marshal(parentElem, (RelationType) object);
		} else if (object instanceof Scope) {
			marshal(parentElem, (Scope) object);
		} else if (object instanceof TopicResult) {
			marshal(parentElem, (TopicResult) object);
		} else if (object instanceof Term) {
			marshal(parentElem, (Term) object);
		} else if (object instanceof Hit) {
			marshal(parentElem, (Hit) object);
		} else if (object instanceof KnowledgeBaseResult) {
			marshal(parentElem, (KnowledgeBaseResult) object);
		} else if (object instanceof KnowledgeBaseTreeResult) {
			marshal(parentElem, (KnowledgeBaseTreeResult) object);
		} else {
			throw new MarshallingFailureException("Unsupported type: "
					+ object.getClass().getName());
		}
	}

	@SuppressWarnings("unchecked")
	private void marshal(Element parentElem, Map map)
			throws MarshallingFailureException {
		Document doc = parentElem.getOwnerDocument();

		Set entries = map.entrySet();
		Iterator iter = entries.iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();

			Object key = entry.getKey();
			Object value = entry.getValue();

			Element entryElem = doc.createElement("entry");
			parentElem.appendChild(entryElem);

			if (key.getClass().isPrimitive()) {
				entryElem.setAttribute("key", String.valueOf(key));
			} else {
				Element keyElem = doc.createElement("key");
				entryElem.appendChild(keyElem);

				marshal(keyElem, key);
			}

			Element valueElem = doc.createElement("value");
			entryElem.appendChild(valueElem);

			marshal(valueElem, value);
		}
	}

	@SuppressWarnings("unchecked")
	private void marshal(Element parentElem, Collection collection)
			throws MarshallingFailureException {
		for (Object item : collection) {
			marshal(parentElem, item);
		}
	}

	private void marshal(Element parentElem, KnowledgeBaseDescriptor kb)
			throws MarshallingFailureException {
		Document doc = parentElem.getOwnerDocument();

		Element kbElem = doc.createElement("knowledge-base");
		parentElem.appendChild(kbElem);

		Attr idAttr = doc.createAttribute("id");
		idAttr.setValue(String.valueOf(kb.getId()));
		kbElem.setAttributeNodeNS(idAttr);

		Element kbNameElem = doc.createElement("name");
		kbElem.appendChild(kbNameElem);

		Text kbNameText = doc.createTextNode(kb.getDisplayName());
		kbNameElem.appendChild(kbNameText);

		if (kb.getDescription() != null) {
			Element kbDescElem = doc.createElement("description");
			kbElem.appendChild(kbDescElem);

			Text kbDescText = doc.createTextNode(kb.getDescription());
			kbDescElem.appendChild(kbDescText);
		}
	}

	private void marshal(Element parentElem, RelationType type)
			throws MarshallingFailureException {
		Document doc = parentElem.getOwnerDocument();

		Element rtElem = doc.createElement("relation-type");
		parentElem.appendChild(rtElem);

		Element rtNameElem = doc.createElement("name");
		rtElem.appendChild(rtNameElem);

		Text rtNameText = doc.createTextNode(type.getDisplayName());
		rtNameElem.appendChild(rtNameText);

		Element rtWeightElem = doc.createElement("weight");
		rtElem.appendChild(rtWeightElem);

		Text rtWeightText = doc
				.createTextNode(String.valueOf(type.getWeight()));
		rtWeightElem.appendChild(rtWeightText);

		Element rtGenWeightElem = doc.createElement("generalization-weight");
		rtElem.appendChild(rtGenWeightElem);

		Text rtGenWeightText = doc.createTextNode(String.valueOf(type
				.getGeneralizationWeight()));
		rtGenWeightElem.appendChild(rtGenWeightText);

		Element rtOccurElem = doc.createElement("occurrence");
		rtElem.appendChild(rtOccurElem);

		Text rtOccurText = doc.createTextNode(String.valueOf(type
				.getOccurrence()));
		rtOccurElem.appendChild(rtOccurText);
	}

	private void marshal(Element parentElem, Scope scope) {
		Document doc = parentElem.getOwnerDocument();

		Element scopeElem = doc.createElement("scope");
		parentElem.appendChild(scopeElem);

		scopeElem.setAttribute("id", String.valueOf(scope.getId()));

		Element dispNameElem = doc.createElement("display-name");
		scopeElem.appendChild(dispNameElem);

		Text dispNameText = doc.createTextNode(scope.getDisplayName());
		dispNameElem.appendChild(dispNameText);
	}

	private void marshal(Element parentElem, TopicResult result) {
		Document doc = parentElem.getOwnerDocument();

		Element topicElem = doc.createElement("topic");
		parentElem.appendChild(topicElem);

		topicElem.setAttribute("id", String.valueOf(result.getId()));

		for (String topicName : result.getNames()) {
			Element topicNameElem = doc.createElement("name");
			topicElem.appendChild(topicNameElem);

			Text topicNameText = doc.createTextNode(topicName);
			topicNameElem.appendChild(topicNameText);
		}

		Element topicWeightElem = doc.createElement("weight");
		topicElem.appendChild(topicWeightElem);

		Text topicWeightText = doc.createTextNode(String.valueOf(result
				.getWeight()));
		topicWeightElem.appendChild(topicWeightText);

		if (result instanceof TopicResultNode) {
			TopicResultNode resultNode = (TopicResultNode) result;

			if (resultNode.getChildren().size() > 0) {

				Element childrenElem = doc.createElement("children");
				topicElem.appendChild(childrenElem);

				for (TopicResultNode child : resultNode.getChildren()) {
					marshal(childrenElem, child);
				}
			}
		}
	}

	private void marshal(Element parentElem, Term result) {
		Document doc = parentElem.getOwnerDocument();

		Element termElem = doc.createElement("term");
		parentElem.appendChild(termElem);

		String termName = result.getName();

		if (termName != null) {
			Text termNameText = doc.createTextNode(termName);
			termElem.appendChild(termNameText);
		}
	}

	private void marshal(Element parentElem, Hit hit) {
		Document doc = parentElem.getOwnerDocument();

		Element hitElem = doc.createElement("hit");
		parentElem.appendChild(hitElem);

		hitElem.setAttribute("id", hit.getID());

		Element titleElem = doc.createElement("title");
		hitElem.appendChild(titleElem);

		Text titleText = doc.createTextNode(hit.getTitle());
		titleElem.appendChild(titleText);

		Element uriElem = doc.createElement("uri");
		hitElem.appendChild(uriElem);

		Text uriText = doc.createTextNode(hit.getURI());
		uriElem.appendChild(uriText);

		Element fileTypeElem = doc.createElement("file-type");
		hitElem.appendChild(fileTypeElem);

		Text fileTypeText = doc.createTextNode(hit.getFileType());
		fileTypeElem.appendChild(fileTypeText);

		Element lastModElem = doc.createElement("last-modified");
		hitElem.appendChild(lastModElem);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String lastMod = df.format(hit.getLastModified());

		Text lastModText = doc.createTextNode(lastMod);
		lastModElem.appendChild(lastModText);

		Element scoreElem = doc.createElement("score");
		hitElem.appendChild(scoreElem);

		Text scoreText = doc.createTextNode(hit.getScoreStr());
		scoreElem.appendChild(scoreText);
	}

	private void marshal(Element parentElem, KnowledgeBaseResult result) {
		Document doc = parentElem.getOwnerDocument();

		Element kbElem = doc.createElement("knowledge-base");
		parentElem.appendChild(kbElem);

		kbElem.setAttribute("id", String.valueOf(result.getKnowledgeBase()
				.getId()));

		if (!result.getResult().isEmpty()) {
			Map<String, Set<TopicResult>> resultMap = result.getResult();
			Set<String> resultKeys = resultMap.keySet();

			for (String resultKey : resultKeys) {
				Element termElem = doc.createElement("term");
				kbElem.appendChild(termElem);

				termElem.setAttribute("name", resultKey);

				Set<TopicResult> resultValues = resultMap.get(resultKey);

				if (!resultValues.isEmpty()) {
					marshal(termElem, resultValues);
				}
			}
		}
	}

	private void marshal(Element parentElem, KnowledgeBaseTreeResult result) {
		Document doc = parentElem.getOwnerDocument();

		Element kbElem = doc.createElement("knowledge-base");
		parentElem.appendChild(kbElem);

		kbElem.setAttribute("id", String.valueOf(result.getKnowledgeBase()
				.getId()));

		if (!result.getResult().isEmpty()) {
			Map<String, Set<TopicResultNode>> resultMap = result.getResult();
			Set<String> resultKeys = resultMap.keySet();

			for (String resultKey : resultKeys) {
				Element termElem = doc.createElement("term");
				kbElem.appendChild(termElem);

				termElem.setAttribute("name", resultKey);

				Set<TopicResultNode> resultValues = resultMap.get(resultKey);

				if (!resultValues.isEmpty()) {
					marshal(termElem, resultValues);
				}
			}
		}
	}
}
