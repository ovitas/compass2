/**
 * 
 */
package no.ovitas.compass2.kb.store.dao;

import java.util.List;

import no.ovitas.compass2.kb.store.model.KnowledgeBaseEntity;
import no.ovitas.compass2.kb.store.model.TopicEntity;
import no.ovitas.compass2.kb.store.model.TopicNameEntity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @class TopicDaoTest
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.08.03.
 * 
 */
public class TopicDaoTest extends BaseDaoTestCase {

	Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private TopicDao topicDao;
	
	@Autowired
	private KnowledgeBaseDao knowledgeBaseDao; 
	
	@Test
	public void testAdd() {
		TopicEntity entity = new TopicEntity();
		
		KnowledgeBaseEntity kb = new KnowledgeBaseEntity();
		
		kb.setDisplayName("Hee");
		kb.setDescription("Remény!");
		
		kb = knowledgeBaseDao.save(kb);
		
		entity.setKnowledgeBase(kb);
		
		entity.setExternalId("Test");
		
		TopicEntity save = topicDao.save(entity);
		
		assertEquals(save, entity);
	}
	
	@Test
	public void testFindAllTopicsInKnowledgeBase() {
//		List<TopicEntity> findAllTopicsInKnowledgeBase = topicDao.getAllFromKnowledgeBase("default");
//		
//		writeOut(findAllTopicsInKnowledgeBase);
	}

	private void writeOut(List<TopicEntity> findAllTopicsInKnowledgeBase) {
		for (TopicEntity topic : findAllTopicsInKnowledgeBase) {
			log.info(topic);
			
//			for (TopicNameEntity topicn : topic.getNames()) {
//				log.info(topicn);
//			}
		}
	}
	
	@Test
	public void testSameTopic() {
//		List<TopicEntity> sameTopic = topicDao.sameTopic("Test");
//		log.info("Same topics...:");
//		writeOut(sameTopic);
	}
	
	@Test
	public void testEqualTopic() {
//		List<TopicEntity> sameTopic = topicDao.equalTopic("Test");
//		log.info("Equal topics...:");
//		writeOut(sameTopic);
	}
	
}
