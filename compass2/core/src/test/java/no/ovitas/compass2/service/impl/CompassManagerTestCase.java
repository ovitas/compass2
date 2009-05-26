/**
 * 
 */
package no.ovitas.compass2.service.impl;

import java.util.List;

import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.model.ResultObject;
import no.ovitas.compass2.service.CompassManager;
import no.ovitas.compass2.service.factory.CompassManagerFactory;

/**
 * @author magyar
 *
 */
public class CompassManagerTestCase extends BaseManagerTestCase {


	
	public void testSimpleLuceneSearch() throws Exception {
		CompassManager compassManager = CompassManagerFactory.getInstance().getCompassManager();		
		String searchTerm="lucene:egy";
		log.info("searchTerm: "+searchTerm);
		ResultObject result = compassManager.search(searchTerm,1, 1, false, false);
		List<Hit> hits =result.getHits();
		assertNotNull(hits);
		assertTrue(hits.size()>0);
		for(Hit hit : hits){
			log.info("Hit: "+hit.toString());
		}
		
	}
}
