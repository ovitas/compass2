package no.ovitas.compass2.service.factory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import no.ovitas.compass2.service.FullTextSearchManager;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.util.CompassUtil;

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
			String ftsImpl = configurationManager.getConfigParameter("no.ovitas.compass2.service.FTSImplementation");
			log.info("ftsImpl configuration: "+ftsImpl);
			try{
				if(ftsImpl!=null){
					manager = (FullTextSearchManager)Class.forName(ftsImpl).newInstance();
					manager.setConfiguration(configurationManager);
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