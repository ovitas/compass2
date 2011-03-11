/**
 * 
 */
package no.ovitas.compass2.kb.store.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import no.ovitas.compass2.kb.store.dao.DirectRelationDao;
import no.ovitas.compass2.kb.store.dao.DirectRelationTypeDao;
import no.ovitas.compass2.kb.store.dao.KnowledgeBaseDao;
import no.ovitas.compass2.kb.store.dao.TopicDao;
import no.ovitas.compass2.kb.store.model.DirectRelationEntity;
import no.ovitas.compass2.kb.store.model.DirectRelationTypeEntity;
import no.ovitas.compass2.kb.store.model.KnowledgeBaseEntity;
import no.ovitas.compass2.kb.store.service.DirectRelationManager;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.Relation;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.Topic;


/**
 * @class DirectRelationManagerImpl
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.05.
 * 
 */
public class DirectRelationManagerImpl extends
		GenericManagerImpl<DirectRelationEntity, Long> implements
		DirectRelationManager {

	private DirectRelationDao directRelationDao;

	public DirectRelationManagerImpl(DirectRelationDao directRelationDao) {
		super(directRelationDao);
		this.directRelationDao = directRelationDao;
	}

	public DirectRelationEntity getRelation(String startTopicID,
			String endTopicID, KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		return directRelationDao.getRelation(startTopicID, endTopicID, knowledgeBaseDescriptor.getId());
	}

	public List<Long> getAllInKnowledgeBase(KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		List<DirectRelationEntity> relations = directRelationDao.getAllFromKnowledgeBase(knowledgeBaseDescriptor.getId());
		
		List<Long> reletionIDs = new ArrayList<Long>();
		
		for (DirectRelationEntity directRelationEntity : relations) {
			reletionIDs.add(directRelationEntity.getId());
		}
		
		return reletionIDs;
	}

	


}
