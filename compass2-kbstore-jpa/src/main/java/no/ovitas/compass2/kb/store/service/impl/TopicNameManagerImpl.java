/**
 * 
 */
package no.ovitas.compass2.kb.store.service.impl;

import no.ovitas.compass2.kb.store.dao.TopicNameDao;
import no.ovitas.compass2.kb.store.model.TopicNameEntity;
import no.ovitas.compass2.kb.store.service.TopicNameManager;

/**
 * @class TopicNameManagerImpl
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.08.05.
 * 
 */
public class TopicNameManagerImpl extends GenericManagerImpl<TopicNameEntity, Long> implements
		TopicNameManager {

	private TopicNameDao topicNameDao;

	public TopicNameManagerImpl(TopicNameDao topicNameDao) {
		super(topicNameDao);
		this.topicNameDao = topicNameDao;
	}
	
}
