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
public class LuceneFTSManagerImplTestCase extends BaseManagerTestCase {

	
	public void testLuceneFTSManager() throws Exception {
		FTSFactory ff = FTSFactory.getInstance();
		FullTextSearchManager fts = ff.getFTSImplementation();
		String docRoot = "/var/share/temporary/config/docs";
		fts.addDocument(true, 2, docRoot);
		log.info("docs: "+docRoot);
	}
}
