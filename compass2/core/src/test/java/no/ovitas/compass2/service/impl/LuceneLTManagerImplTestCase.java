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
public class LuceneLTManagerImplTestCase extends BaseManagerTestCase {

	
	public void testInitSpelchecker()throws Exception{
		LTFactory ltFactory = LTFactory.getInstance();
		LanguageToolsManager manager = ltFactory.getLTImplementation();
		manager.initSpellchecker();
	}
}
