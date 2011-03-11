/**
 * 
 */
package no.ovitas.compass2.kb.store.service.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import no.ovitas.compass2.kb.store.dao.DirectRelationDao;
import no.ovitas.compass2.kb.store.dao.KnowledgeBaseDao;
import no.ovitas.compass2.kb.store.dao.ScopeDao;
import no.ovitas.compass2.kb.store.dao.TopicDao;
import no.ovitas.compass2.kb.store.dao.TopicNameDao;
import no.ovitas.compass2.kb.store.model.TopicEntity;
import no.ovitas.compass2.kb.store.service.TopicManager;
import no.ovitas.compass2.model.MatchTopicResult;
import no.ovitas.compass2.model.knowledgebase.Topic;

/**
 * @class TopicManagerImpl
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.05.
 * 
 */
public class TopicManagerImpl extends GenericManagerImpl<TopicEntity, Long>
		implements TopicManager {

	private TopicDao topicDao;

	private TopicNameDao topicNameDao;

	private ScopeDao scopeDao;


	public TopicManagerImpl(TopicDao topicDao) {
		super(topicDao);
		this.topicDao = topicDao;
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
	 * This is a getter method for scopeDao.
	 * 
	 * @return the scopeDao
	 */
	public ScopeDao getScopeDao() {
		return scopeDao;
	}

	/**
	 * This is a setter method for scopeDao.
	 * 
	 * @param scopeDao
	 *            the scopeDao to set
	 */
	public void setScopeDao(ScopeDao scopeDao) {
		this.scopeDao = scopeDao;
	}

	public Set<Topic> getBaseTopics(MatchTopicResult matchTopicResultObject) {

		Set<Topic> baseTopics = new HashSet<Topic>();
		TopicEntity topic;
		for (Entry<String, Set<Long>> pair : matchTopicResultObject
				.getResults().entrySet()) {
			for (Long topicId : pair.getValue()) {
				topic = topicDao.get(topicId);
				if (topic != null) {
					topic.getNamesEntity();
					baseTopics.add(topic);
				}
			}
		}
		return baseTopics;
	}

}
