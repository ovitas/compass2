/**
 * 
 */
package no.ovitas.compass2.dao;

import java.util.Map;

import no.ovitas.compass2.model.KnowledgeBaseHolder;
import no.ovitas.compass2.model.Topic;
import no.ovitas.compass2.service.impl.BaseManagerTestCase;

/**
 * @author magyar
 *
 */
public class KBLoaderDaoTestCase extends BaseManagerTestCase {

	protected KBBuilderDao kbBuilderDao;

	public void setKbBuilderDao(KBBuilderDao kbBuilderDao) {
		this.kbBuilderDao = kbBuilderDao;
	}
	
	public void testBuildKB(){
		KnowledgeBaseHolder kbh = kbBuilderDao.buildKB("src\\test\\resources\\compass-basetest.xml");
		Map<String, Topic> topics = kbh.getTopics();
		for(String topicName : topics.keySet()){
			Topic t = topics.get(topicName);
			logger.debug("Topic: .[name].="+t.getName());
			logger.debug(" Alternative names: " );
			for(String altn : t.getAlternativeNames()){
				logger.debug("  an: "+altn);
			}
					
			
		}
		
	}
}
