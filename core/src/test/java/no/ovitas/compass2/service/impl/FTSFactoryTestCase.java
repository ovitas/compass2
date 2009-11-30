/**
 * 
 */
package no.ovitas.compass2.service.impl;

import no.ovitas.compass2.service.FullTextSearchManager;
import no.ovitas.compass2.service.factory.FTSFactory;

/**
 * @author magyar
 *
 */
public class FTSFactoryTestCase extends BaseManagerTestCase {

	
	public void testFTSFactory(){
		
		FTSFactory factory = FTSFactory.getInstance();
		assertNotNull(factory);
		FullTextSearchManager manager = factory.getFTSImplementation();
		assertNotNull(manager);
		
		
	}
	
}
