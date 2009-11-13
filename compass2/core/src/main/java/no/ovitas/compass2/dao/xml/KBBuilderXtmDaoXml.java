package no.ovitas.compass2.dao.xml;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.springframework.context.ApplicationContext;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.dao.KBBuilderDao;
import no.ovitas.compass2.model.KnowledgeBaseHolder;
import no.ovitas.compass2.model.Relation;
import no.ovitas.compass2.model.RelationType;
import no.ovitas.compass2.model.Topic;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.util.CompassUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

public class KBBuilderXtmDaoXml implements KBBuilderDao {

	// xmlns="http://www.topicmaps.org/xtm/1.0/"
	private static final Log log = LogFactory.getLog(KBBuilderXtmDaoXml.class);
	
	public static final String ID_ATTR         = "id";
	public static final String TOPIC_NODE         = "topic";
	public static final String BASENAME_NODE      = "baseName";
	public static final String BASENAMESTING_NODE = "baseNameString";
	public static final String SCOPE_NODE         = "scope";
	public static final String TOPICREF_NODE      = "topicRef";
	public static final String HREF_NODE          = "xlink:href";
	public static final String ASSOCIATION_NODE   = "association";
	public static final String MEMBER_NODE        = "member";
	public static final String INSTANCEOF_NODE    = "instanceOf";
	public static final String ROLE_SPEC_NODE    = "roleSpec";
	
	public static final String SCOPE_ID           = "#t-4491300";
	
	
	public KBBuilderXtmDaoXml() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see no.ovitas.compass2.dao.KBBuilderDao#buildKB(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public KnowledgeBaseHolder buildKB(String kbAccessString) {
		log.info("KBBuilderXtmDaoXml.buildKB");
		ApplicationContext context = CompassUtil.getApplicationContext();
		ConfigurationManager configManager = (ConfigurationManager)context.getBean("configurationManager");
		String sUseRandomWeight = configManager.getConfigParameter(Constants.USE_RANDOM_WEIGHT);
		boolean useRandomWeight = false;
		if (sUseRandomWeight != null){
			if (sUseRandomWeight.trim().equals("true")) {
				useRandomWeight = true;
			}
		}
		
		KnowledgeBaseHolder kbh = new KnowledgeBaseHolder();
		
		Map<String, Topic> topicMap = new HashMap<String, Topic>();
		Topic actTopic = null;
		Relation actRelation = null;

		SAXReader saxReader = new SAXReader();
		try {
			// Create document from source
			Document document = saxReader.read(kbAccessString);
			
			// Examine Topics
			List<Element> topicList = document.selectNodes("//" + TOPIC_NODE);
		    log.info("topic count: " + topicList.size());
		    
			Iterator topicIterator = topicList.iterator();
			while(topicIterator.hasNext()){
				Element topicElement = (Element)topicIterator.next();
				String topicId = topicElement.attributeValue(ID_ATTR);
				
				// Get instanceOf tag
				Node instanceOfNode = topicElement.selectSingleNode(INSTANCEOF_NODE + "/" + TOPICREF_NODE + "/@" + HREF_NODE);
				String instanceOf = (instanceOfNode != null) ? instanceOfNode.getStringValue() : "";
				
				// Select topic name
				List<Element> baseNameList = topicElement.selectNodes(BASENAME_NODE);
				Iterator baseNameIterator = baseNameList.iterator();
				
				// Iterate over base names
				while(baseNameIterator.hasNext()){
					Element basNameElement = (Element)baseNameIterator.next();
					
					// scope tag and baseNameString tag
					Node scopeNode = basNameElement.selectSingleNode(SCOPE_NODE + "/" + TOPICREF_NODE + "/@" + HREF_NODE);
					Node baseNameNode = basNameElement.selectSingleNode(BASENAMESTING_NODE);
					
					String scope = (scopeNode != null) ? scopeNode.getStringValue() : "";
					String baseName = (baseNameNode != null) ? baseNameNode.getStringValue() : "";
					
					//log.debug("topic -> id: " + topicId + ", instanceOf: " + instanceOf + ", scope: " + scope + ", baseNameString: " + baseName);
					
					// Set topic properties and add to topicMap
					//actTopic.setName("name");
					topicId = "#" + topicId;
					actTopic = topicMap.get(topicId);
					if (actTopic == null) {
						actTopic = new Topic();
						topicMap.put(topicId, actTopic);
					}
				}
				

			}
			
			// Examine associations
			List<Element> assocList = document.selectNodes("//" + ASSOCIATION_NODE);
			log.info("association count: " + assocList.size());
			Iterator assocIterator = assocList.iterator();
			
			while(assocIterator.hasNext()){
				Element assocElement = (Element)assocIterator.next();
				String assocId = assocElement.attributeValue(ID_ATTR);
				
				// Get instanceOf tag
				Node instanceOfNode = assocElement.selectSingleNode(INSTANCEOF_NODE + "/" + TOPICREF_NODE + "/@" + HREF_NODE);
				String instanceOf = (instanceOfNode != null) ? instanceOfNode.getStringValue() : "";
				
				// Get scope tag
				Node scopeNode = assocElement.selectSingleNode(SCOPE_NODE + "/" + TOPICREF_NODE + "/@" + HREF_NODE);
				String scope = (scopeNode != null) ? scopeNode.getStringValue() : "";
				
				// Select member tags
				List<Element> memberList = assocElement.selectNodes(MEMBER_NODE);
				Iterator memberIterator = memberList.iterator();
				
				// Iterate over member tags
				while(memberIterator.hasNext()){
					Element memberElement = (Element)memberIterator.next();
									
					// roleSpec tag and topicRef tag
					List<Element> roleSpecList = memberElement.selectNodes(ROLE_SPEC_NODE + "/" + TOPICREF_NODE + "/@" + HREF_NODE);
					List<Element> topicRefList = memberElement.selectNodes(TOPICREF_NODE + "/@" + HREF_NODE);
					int maxSize = (roleSpecList.size() > topicRefList.size()) ? roleSpecList.size() : topicRefList.size();

					Attribute roleSpecAttr = null;
					Attribute topicRefAttr = null;
					
					for (int i = 0; i < maxSize; i++) {
						
						if (i < roleSpecList.size()) roleSpecAttr = (Attribute)roleSpecList.get(i);
						if (i < topicRefList.size()) topicRefAttr = (Attribute)topicRefList.get(i);

						String roleSpec = (roleSpecAttr != null) ? roleSpecAttr.getValue() : "";
						String topicRef = (topicRefAttr != null) ? topicRefAttr.getValue() : "";
						
						//log.debug("association -> id: " + assocId + ", instanceOf: " + instanceOf + ", scope: " + scope + ", source: " +roleSpec + ", target: " + topicRef);
						
						// TODO create RelationType
						
					}
				}
			}
			
		} catch (DocumentException e) {
			log.error("Error parsing document: " + kbAccessString + " - " +e.getMessage());
		}
		
		
		return kbh;
	}

	/* (non-Javadoc)
	 * @see no.ovitas.compass2.dao.KBBuilderDao#buildKB(java.lang.String)
	 */
/*	public KnowledgeBaseHolder buildKB(String kbAccessString) {
		
		ApplicationContext context = CompassUtil.getApplicationContext();
		ConfigurationManager configManager = (ConfigurationManager)context.getBean("configurationManager");
		String sUseRandomWeight = configManager.getConfigParameter(Constants.USE_RANDOM_WEIGHT);
		boolean useRandomWeight = false;
		if (sUseRandomWeight != null){
			if (sUseRandomWeight.trim().equals("true")) {
				useRandomWeight = true;
			}
		}
		
		KnowledgeBaseHolder kbh = new KnowledgeBaseHolder();
		
		Map<String, Topic> topicMap = new HashMap<String, Topic>();
		
		XMLInputFactory f = XMLInputFactory.newInstance();
		f.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,Boolean.TRUE);
		f.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,Boolean.FALSE);
		//set the IS_COALESCING property to true , if application desires to
		//get whole text data as one event.            
		f.setProperty(XMLInputFactory.IS_COALESCING , Boolean.TRUE);
       
		try {
    	   
			XMLEventReader xmlr = f.createXMLEventReader(new  FileInputStream(kbAccessString));
			XMLEvent event = null;
			Topic    actTopic = null;
			Relation actRelation = null;
			boolean  goodScope = false;
			boolean  useContent = false;
			String   content = null;
			String   href    = null;
			int      memberIdx = -1;
			
			while (xmlr.hasNext()) {
				event = xmlr.nextEvent();
    	
				if(event.getEventType() == XMLEvent.START_ELEMENT) {
					StartElement se = (StartElement)event;
					String name = se.getName().getLocalPart();
					if(name.equals(TOPIC_NODE)){
						String id = "#" + se.getAttributeByName(new QName("id")).getValue();
						actTopic = topicMap.get(id);
						if (actTopic == null) {
							actTopic = new Topic();
							topicMap.put(id, actTopic);
						}
						content = null;
					}
					else if (name.equals(ASSOCIATION_NODE)){
						actRelation = new Relation();
						memberIdx = 0;
					}
					else if (name.equals(TOPICREF_NODE)){
						href = se.getAttributeByName(new QName("http://www.w3.org/1999/xlink", "href", "xlink")).getValue();
					}
					else if (name.equals(BASENAMESTING_NODE)){
						useContent = true;
						content = null;
					}
					else if (name.equals(BASENAME_NODE)){
						goodScope = false;
					}
					else if (name.equals(MEMBER_NODE)){
						href = null;
					}
				}

				if (event.getEventType() == XMLEvent.CDATA || 
						event.getEventType() == XMLEvent.CHARACTERS){
					if (useContent) {
						Characters c = event.asCharacters();
						content = c.getData();
					}
					useContent = false;
				}
			
				if (event.getEventType() == XMLEvent.END_ELEMENT) {
					EndElement se = (EndElement)event;
					String name = se.getName().getLocalPart();
					
					if(name.equals(TOPIC_NODE)){
						if (actTopic.getName() == null) actTopic.setName(content);
						if (actTopic.getName() != null) kbh.addTopic(actTopic);
						actTopic = null;
					}
					else if (name.equals(ASSOCIATION_NODE)) {
						if (actRelation.getSource() == null || actRelation.getTarget() == null) {
							// do not use this
						} else {
							actRelation.getSource().addRelation(actRelation);
							actRelation.getTarget().addRelation(actRelation);
							kbh.addRelation(actRelation);
						}
						actRelation = null;
					}
					else if (name.equals(BASENAMESTING_NODE)) {
						if (goodScope) 
							actTopic.setName(content);
					}
					else if (name.equals(SCOPE_NODE)){
						goodScope = href.equals(SCOPE_ID);
					}
					else if (name.equals(BASENAME_NODE)){
						goodScope = false;
					}
					else if (name.equals(INSTANCEOF_NODE)){
						if (actRelation != null && href != null) {
							RelationType relationType = kbh.findRelationType(href);
							if (relationType == null) {
								relationType = new RelationType();
								relationType.setId(href);
								kbh.addRelationType(relationType);
							}
							actRelation.setRelationType(relationType);
						}
					}
					else if (name.equals(MEMBER_NODE)){
						if (memberIdx >= 0 && memberIdx < 2) {
							Topic topic = topicMap.get(href);
							if (topic == null) {
								topic = new Topic();
								topicMap.put(href, topic);
							}
							if (memberIdx == 0) actRelation.setSource(topic);
							else			    actRelation.setTarget(topic);
							memberIdx++;
						}
					}
				}
			}
			
			// gathering relation types
			for (Map.Entry<String, RelationType> entry : kbh.getRelationTypes().entrySet()) {
				Topic topic = topicMap.get(entry.getKey());
				if (topic != null) {
					RelationType relationType = entry.getValue();
					relationType.setRelationName(topic.getName());
					relationType.setWeight(.5 + (useRandomWeight ? (Math.random() - .5) * .2 : 0));
				}
			}
			
			// set id as names for unknown topics 
			for (Map.Entry<String, Topic> entry : topicMap.entrySet()) {
				Topic topic = entry.getValue();
				if (topic.getName() == null) topic.setName(entry.getKey());
			}

		} catch(Exception ex){
			ex.printStackTrace();
		}

		return kbh;
	}*/

}
