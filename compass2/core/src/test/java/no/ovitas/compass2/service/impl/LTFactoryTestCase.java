/**
 * 
 */
package no.ovitas.compass2.service.impl;

import no.ovitas.compass2.service.LanguageToolsManager;
import no.ovitas.compass2.service.factory.LTFactory;

/**
 * @author magyar
 *
 */
public class LTFactoryTestCase extends BaseManagerTestCase {

	
	public void testLTFactory(){
		
		LTFactory factory = LTFactory.getInstance();
		assertNotNull(factory);
		LanguageToolsManager manager = factory.getLTImplementation();
		assertNotNull(manager);
		
		
	}
	
}
