/**
 * 
 */
package no.ovitas.compass2.dao.xml;

import java.io.FileInputStream;

import no.ovitas.compass2.dao.KBBuilderDao;
import no.ovitas.compass2.model.KnowledgeBaseHolder;
import no.ovitas.compass2.model.Relation;
import no.ovitas.compass2.model.RelationType;
import no.ovitas.compass2.model.Topic;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * @author magyar
 *
 */
public class KBBuilderDaoXml implements KBBuilderDao {

	public static final String TOPIC_NODE = "topic";
	public static final String RELATION_NODE = "relation";
	public static final String RELATION_TYPE_NODE = "relationtype";
	public static final String SEARCH_NODE = "search";
	public static final String NAME_NODE = "name";
	
	
	
	public KBBuilderDaoXml() {
		super();
		// TODO Auto-generated constructor stub
	}


	/* (non-Javadoc)
	 * @see no.ovitas.compass2.dao.KBBuilderDao#buildKB(java.lang.String)
	 */
	public KnowledgeBaseHolder buildKB(String kbAccessString) {
		KnowledgeBaseHolder kbh = new KnowledgeBaseHolder();
		
	   XMLInputFactory f = XMLInputFactory.newInstance();
       f.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,Boolean.TRUE);
       f.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,Boolean.FALSE);
       //set the IS_COALESCING property to true , if application desires to
       //get whole text data as one event.            
       f.setProperty(XMLInputFactory.IS_COALESCING , Boolean.TRUE);
       
       try {
    	   
    	   XMLEventReader xmlr = f.createXMLEventReader(new  FileInputStream(kbAccessString));
    	   XMLEvent event = null;
    	   Relation actRelation=null;
    	   boolean inName = false;
    	   Topic actTopic=null;
    	   
    	   while (xmlr.hasNext()) {
    		   event = xmlr.nextEvent();
    	
    		   if(event.getEventType() == XMLEvent.START_ELEMENT) {
    			   StartElement se = (StartElement)event;
    			   String name = se.getName().getLocalPart();
    			   if(name.equals(TOPIC_NODE)){
    				   actTopic = null;
    			   }
    			   else if (name.equals(RELATION_NODE)){
    				   actRelation = new Relation();
    				   String rn = se.getAttributeByName(new QName("type")).getValue();
    				   actRelation.setRelationType(kbh.findRelationType(rn));
    			   }
    			   else if (name.equals(NAME_NODE)){
    				   inName = true;
    			   }
    			   else if (name.equals(RELATION_TYPE_NODE)){
    				   RelationType rt = new RelationType();
    				   rt.setId(se.getAttributeByName(new QName("id")).getValue());
    				   rt.setRelationName(se.getAttributeByName(new QName("name")).getValue());
    				   String w = se.getAttributeByName(new QName("weight")).getValue();
    				   rt.setWeight(Float.parseFloat(w));
    				   kbh.addRelationType(rt);
    			   }
    		   }
    		   if (event.getEventType()==XMLEvent.CDATA || 
    				   event.getEventType() == XMLEvent.CHARACTERS){
    			   if (inName) {
    				   Characters c = event.asCharacters();
	    			   String tname = c.getData();
	    			   if (actTopic == null){
	    				   actTopic = kbh.findTopic(tname);
	    				   if (actTopic == null){
	    					   actTopic = new Topic();
	    					   actTopic.setName(tname);
	    					   kbh.addTopic(actTopic);
	    				   }
	    				   actTopic.addRelation(actRelation);
	    			   } else {
	    				   actTopic.addAlternativeName(tname);
	    			   }
    			   }
    		   }
    		   if (event.getEventType() == XMLEvent.END_ELEMENT) {
    			   EndElement se = (EndElement)event;
    			   String name = se.getName().getLocalPart();
    			   
    			   if(name.equals(TOPIC_NODE)){
    				   if(actRelation.getSource()==null){
    					   actRelation.setSource(actTopic);
    				   }else{
    					   actRelation.setTarget(actTopic);
    				   }
    				   actTopic = null;
    			   }
    			   else if (name.equals(RELATION_NODE)){
    				   kbh.addRelation(actRelation);
    				   actRelation = null;
    			   }
    			   else if(name.equals(RELATION_TYPE_NODE)){
    			   }
    			   else if(name.equals(NAME_NODE)){
    				   inName = false;
    			   }
    		   }
    	   }
       } catch(Exception ex){
    	   ex.printStackTrace();
       }

       return kbh;
	}

}
