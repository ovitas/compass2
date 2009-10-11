/**
 * 
 */
package no.ovitas.compass2.service.factory;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.service.ConfigurationManager;

import no.ovitas.compass2.util.CompassUtil;
import no.ovitas.compass2.util.lucene.ContentIndexer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author magyar
 *
 */
public class ContentIndexerFactory {

	private static ContentIndexerFactory instance = null;
	protected ConfigurationManager configurationManager;
	protected ContentIndexer indexer = null;;
	private Log log = LogFactory.getLog(getClass());
	
	public ContentIndexerFactory(){
		ApplicationContext context = CompassUtil.getApplicationContext();
		configurationManager = (ConfigurationManager)context.getBean("configurationManager");
	}

	public ContentIndexer getIndexerImplementation(){
		if(indexer==null){
			String indexerImpl = configurationManager.getConfigParameter(Constants.LUCENE_CONTEXT_INDEXER);
			log.info("indexer configuration: "+indexerImpl);
			try{
				if(indexerImpl!=null){
					indexer = (ContentIndexer)Class.forName(indexerImpl).newInstance();
					log.info("indexer loaded!");

				}
			}catch(Exception ex){
				log.error("Error while creating ContentIndexer", ex);
			}
		}
		return indexer;
	}

	public static ContentIndexerFactory getInstance(){
		if(instance==null){
			instance = new ContentIndexerFactory();
		}
		return instance;
	}
	
	
}
