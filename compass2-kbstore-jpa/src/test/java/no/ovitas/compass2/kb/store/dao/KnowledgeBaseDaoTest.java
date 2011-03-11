/**
 * 
 */
package no.ovitas.compass2.kb.store.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import no.ovitas.compass2.kb.store.model.KnowledgeBaseEntity;

/**
 * @class KnowledgeBaseDaoTest
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.08.02.
 * 
 */
public class KnowledgeBaseDaoTest extends BaseDaoTestCase {

	Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private GenericDao<KnowledgeBaseEntity, Long> knowledgeBaseDao; 
	
	
	@Test
	public void testAdd() {
		KnowledgeBaseEntity entity = new KnowledgeBaseEntity();
		entity.setDisplayName("jepp");
		entity.setDescription("Talán sikerült");
		knowledgeBaseDao.save(entity);
	}


	/**
	 * This is a setter method for knowledgeBaseDao.
	 * @param knowledgeBaseDao the knowledgeBaseDao to set
	 */
	public void setKnowledgeBaseDao(GenericDao<KnowledgeBaseEntity, Long> knowledgeBaseDao) {
		this.knowledgeBaseDao = knowledgeBaseDao;
	}


	/**
	 * This is a getter method for knowledgeBaseDao.
	 * @return the knowledgeBaseDao
	 */
	public GenericDao<KnowledgeBaseEntity, Long> getKnowledgeBaseDao() {
		return knowledgeBaseDao;
	}
	
}
