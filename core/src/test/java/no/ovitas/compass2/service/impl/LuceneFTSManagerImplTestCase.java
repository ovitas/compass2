/**
 * 
 */
package no.ovitas.compass2.service.impl;

import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import no.ovitas.compass2.service.FullTextSearchManager;
import no.ovitas.compass2.service.factory.FTSFactory;

/**
 * @author magyar
 *
 */
public class LuceneFTSManagerImplTestCase extends BaseManagerTestCase {

	protected ResourceBundle rb;
	protected String documentRepo; 

	
	public LuceneFTSManagerImplTestCase() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LuceneFTSManagerImplTestCase(String name) {
		setName(name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onSetUp() throws Exception {
        ResourceBundle rb = ResourceBundle.getBundle("compass2");
        documentRepo = rb.getString("document.repository.location");
	}

	@Test
    public void testLuceneFTSManager() throws Exception {
	 
		FTSFactory ff = FTSFactory.getInstance();
		FullTextSearchManager fts = ff.getFTSImplementation();
		log.info("docs: "+documentRepo);
		fts.addDocument(true, 100, documentRepo);
		
	}
}
