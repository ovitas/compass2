package no.ovitas.compass2.service.factory;
import no.ovitas.compass2.config.FullTextSearch;
import no.ovitas.compass2.config.FullTextSearchImplementation;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.FullTextSearchManager;
import no.ovitas.compass2.util.CompassUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author magyar
 * @version 1.0
 */
public class FTSFactory {

	private static FTSFactory instance = null;
	protected ConfigurationManager configurationManager;
	protected FullTextSearchManager manager = null;;
	private Log log = LogFactory.getLog(getClass());
	
	public FTSFactory(){
		ApplicationContext context = CompassUtil.getApplicationContext();
		configurationManager = (ConfigurationManager)context.getBean("configurationManager");
	}

	public FullTextSearchManager getFTSImplementation(){
		if(manager==null){
			FullTextSearchImplementation ftsImpl = configurationManager.getFullTextSearch().getFullTextSearchImplementation();
			String ftsImplClassName = ftsImpl.getClassName();
			log.info("ftsImpl configuration: "+ftsImplClassName);
			try{
				if(ftsImplClassName!=null){
					manager = (FullTextSearchManager)Class.forName(ftsImplClassName).newInstance();
					manager.setConfiguration(configurationManager);
					manager.setFTSImpl(ftsImpl);
					log.info("FTS manager initialized!");

				}
			}catch(Exception ex){
				log.error("Error while creating FullTextSearchManager", ex);
			}
		}
		return manager;
	}

	public static FTSFactory getInstance(){
		if(instance==null){
			instance = new FTSFactory();
		}
		return instance;
	}

}