/**
 * 
 */
package no.ovitas.compass2.config;

import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.impl.BaseManagerTestCase;

/**
 * @author csanyi
 *
 */
public class Copmass2ConfigurationHandlerTestCase extends BaseManagerTestCase {
	
	protected ConfigurationManager configurationManager;

	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}
		
	public void testConfigurationGetter(){
		
		// FullTextSearch
		FullTextSearch fts = configurationManager.getFullTextSearch();
		assertNotNull(fts);
		assertNotNull(fts.getFullTextSearchImplementation());
		assertNotNull(fts.getFullTextSearchImplementation().getParams());
		assertNotNull(fts.getContentIndexerImplementation());
		
		// LanguageToolsImplementation
		LanguageToolsImplementation ltImpl = configurationManager.getLanguageToolsImplementation();
		assertNotNull(ltImpl);
		assertNotNull(ltImpl.getParams());
		
		// KnowledgeBases
		String defaultkbName = configurationManager.getDefaultKBImplementationName();
		assertNotNull(defaultkbName);
		KnowledgeBase kb = configurationManager.getKnowledgeBase(defaultkbName);
		assertNotNull(kb);
		assertNotNull(kb.getKnowledgeBaseImplementation());
		assertNotNull(kb.getKnowledgeBaseImplementation().getParams());
		assertNotNull(kb.getExpansion());
		assertNotNull(kb.getExpansion().getAssociationTypes());
		
	}

}
