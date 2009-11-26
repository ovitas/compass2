/**
 * 
 */
package no.ovitas.compass2.dao;

import java.util.List;
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
		Map<String, List<Topic>> topics = kbh.getTopics();
		for(String topicName : topics.keySet()){
			List<Topic> t = topics.get(topicName);
			for(Topic topic : t){
				logger.debug("Topic: .[name].="+topic.getName());
				logger.debug(" Alternative names: " );
				for(String altn : topic.getAlternativeNames()){
					logger.debug("  an: "+altn);
				}
			}
					
			
		}
		
	}
}
