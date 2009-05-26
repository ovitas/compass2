/**
 * 
 */
package no.ovitas.compass2.service.impl;

import no.ovitas.compass2.service.KnowledgeBaseManager;
import no.ovitas.compass2.service.factory.KBFactory;

/**
 * @author magyar
 *
 */
public class KBFactoryTestCase extends BaseManagerTestCase {
	
	public void testKBFactory(){
		
		KBFactory factory = KBFactory.getInstance();
		assertNotNull(factory);
		KnowledgeBaseManager manager = factory.getKBImplementation();
		assertNotNull(manager);
		
		
	}

}
