/**
 * 
 */
package no.ovitas.compass2.kb.store.service.impl;

import java.util.ArrayList;
import java.util.List;

import no.ovitas.compass2.kb.store.EntityModel;
import no.ovitas.compass2.kb.store.dao.DirectRelationDao;
import no.ovitas.compass2.kb.store.dao.DirectRelationTypeDao;
import no.ovitas.compass2.kb.store.dao.KnowledgeBaseDao;
import no.ovitas.compass2.kb.store.dao.ScopeDao;
import no.ovitas.compass2.kb.store.dao.TopicDao;
import no.ovitas.compass2.kb.store.dao.TopicNameDao;
import no.ovitas.compass2.kb.store.model.KnowledgeBaseEntity;
import no.ovitas.compass2.kb.store.service.KnowledgeBaseDaoManager;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBase;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;

/**
 * @class KnowledgeBaseDaoManagerImpl
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.05.
 * 
 */
public class KnowledgeBaseDaoManagerImpl extends
		GenericManagerImpl<KnowledgeBaseEntity, Long> implements
		KnowledgeBaseDaoManager {

	private KnowledgeBaseDao knowledgeBaseDao;

	private TopicDao topicDao;

	private TopicNameDao topicNameDao;

	private DirectRelationDao directRelationDao;

	private DirectRelationTypeDao directRelationTypeDao;
	
	private ScopeDao scopeDao;

	public KnowledgeBaseDaoManagerImpl(KnowledgeBaseDao knowledgeBaseDao) {
		super(knowledgeBaseDao);
		this.knowledgeBaseDao = knowledgeBaseDao;

	}

	public List<KnowledgeBaseDescriptor> listActiveKnowledgeBases() {

		List<KnowledgeBaseDescriptor> result = new ArrayList<KnowledgeBaseDescriptor>();
		
		List<KnowledgeBaseEntity> all = knowledgeBaseDao.getAll();
		
		for (KnowledgeBaseEntity knowledgeBaseEntity : all) {
			if (knowledgeBaseEntity.isActive()) {
				result.add(knowledgeBaseEntity);
			}
		}
		
		return result;
	}

	/**
	 * This is a setter method for topicDao.
	 * 
	 * @param topicDao
	 *            the topicDao to set
	 */
	public void setTopicDao(TopicDao topicDao) {
		this.topicDao = topicDao;
	}

	/**
	 * This is a getter method for topicDao.
	 * 
	 * @return the topicDao
	 */
	public TopicDao getTopicDao() {
		return topicDao;
	}

	/**
	 * This is a setter method for topicNameDao.
	 * 
	 * @param topicNameDao
	 *            the topicNameDao to set
	 */
	public void setTopicNameDao(TopicNameDao topicNameDao) {
		this.topicNameDao = topicNameDao;
	}

	/**
	 * This is a getter method for topicNameDao.
	 * 
	 * @return the topicNameDao
	 */
	public TopicNameDao getTopicNameDao() {
		return topicNameDao;
	}

	/**
	 * This is a setter method for directRelationDao.
	 * 
	 * @param directRelationDao
	 *            the directRelationDao to set
	 */
	public void setDirectRelationDao(DirectRelationDao directRelationDao) {
		this.directRelationDao = directRelationDao;
	}

	/**
	 * This is a getter method for directRelationDao.
	 * 
	 * @return the directRelationDao
	 */
	public DirectRelationDao getDirectRelationDao() {
		return directRelationDao;
	}

	/**
	 * This is a setter method for directRelationTypeDao.
	 * 
	 * @param directRelationTypeDao
	 *            the directRelationTypeDao to set
	 */
	public void setDirectRelationTypeDao(
			DirectRelationTypeDao directRelationTypeDao) {
		this.directRelationTypeDao = directRelationTypeDao;
	}

	/**
	 * This is a setter method for scopeDao.
	 * @param scopeDao the scopeDao to set
	 */
	public void setScopeDao(ScopeDao scopeDao) {
		this.scopeDao = scopeDao;
	}

	/**
	 * This is a getter method for directRelationTypeDao.
	 * 
	 * @return the directRelationTypeDao
	 */
	public DirectRelationTypeDao getDirectRelationTypeDao() {
		return directRelationTypeDao;
	}


	public Long getKnowledgeBase(String kbName) {
		return knowledgeBaseDao.getKnowledgeBase(kbName).getId();
	}

	private void removeAll(KnowledgeBaseEntity knowledgeBaseEntity) {

		Long kbId = knowledgeBaseEntity.getId();
		
		directRelationDao.deleteAllInKnowledgeBase(kbId);
		
		directRelationTypeDao.deleteAllInKnowledgeBase(kbId);
		
		topicNameDao.deleteAllInKnowledgeBase(kbId);
		
		topicDao.deleteAllInKnowledgeBase(kbId);
		
		scopeDao.deleteAllInKnowledgeBase(kbId);

	}


	public Long updateKnowledgeBaseEntity(KnowledgeBase knowledgeBase,
			String kbName) {
		KnowledgeBaseEntity knowledgeBaseEntity = knowledgeBaseDao
				.getKnowledgeBase(kbName);

//		knowledgeBaseEntity.setDescription(knowledgeBase.getDescription());

		save(knowledgeBaseEntity);
		return knowledgeBaseEntity.getId();
	}

	public String getDisplayName(Long kbEntityID) {

		return knowledgeBaseDao.getDisplayName(kbEntityID);
	}
	
	public void saveKnowledgeBaseEntity(EntityModel em) {
		KnowledgeBaseEntity knowledgeBaseEntity = em.getKnowledgeBaseEntity();
		knowledgeBaseEntity.setActive(false);
		if (em.isExistingKnowledgeBase()) {
			removeAll(knowledgeBaseEntity);
			save(knowledgeBaseEntity);
		} else {
			persist(knowledgeBaseEntity);
		}
	}
	
	public void importKnowledgeBase(EntityModel em) {		
		scopeDao.persistAll(em.getScopeEnities());
		topicDao.persistAll(em.getTopicEnities());
		topicNameDao.persistAll(em.getTopicNameEnities());		
		directRelationTypeDao.persistAll(em.getDirectRelationTypeEnities());		
		directRelationDao.persistAll(em.getDirecRelationEnities());
		
	}

	public void deleteKnowledgeBase(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		KnowledgeBaseEntity knowledgeBaseEntity = knowledgeBaseDao.get(knowledgeBaseDescriptor.getId());
		removeAll(knowledgeBaseEntity);
		
		remove(knowledgeBaseEntity.getId());
	}

}
