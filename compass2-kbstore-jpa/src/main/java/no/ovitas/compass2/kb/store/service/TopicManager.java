/**
 * 
 */
package no.ovitas.compass2.kb.store.service;

import java.io.IOException;
import java.util.Set;

import no.ovitas.compass2.kb.store.model.TopicEntity;
import no.ovitas.compass2.model.MatchTopicResult;
import no.ovitas.compass2.model.knowledgebase.Topic;


/**
 * @class TopicManager
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.08.04.
 * 
 */
public interface TopicManager extends GenericManager<TopicEntity, Long> {

	Set<Topic> getBaseTopics(MatchTopicResult matchTopicResultObject);

}
