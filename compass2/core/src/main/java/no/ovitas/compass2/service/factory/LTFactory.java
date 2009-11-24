package no.ovitas.compass2.service.factory;
import no.ovitas.compass2.config.LanguageToolsImplementation;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.LanguageToolsManager;
import no.ovitas.compass2.util.CompassUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author magyar
 * @version 1.0
 */
public class LTFactory {

	private static LTFactory instance = null;
	protected ConfigurationManager configurationManager;
	protected LanguageToolsManager manager = null;;
	private Log log = LogFactory.getLog(getClass());
	
	public LTFactory(){

		ApplicationContext context = CompassUtil.getApplicationContext();		
		configurationManager = (ConfigurationManager)context.getBean("configurationManager");
	}

	public LanguageToolsManager getLTImplementation(){
		if(manager==null){
			LanguageToolsImplementation lt = configurationManager.getLanguageToolsImplementation();
			String ltImpl = lt.getClassName();
			log.info("ltImpl configuration: "+ltImpl);
			try{
				if(ltImpl!=null){
					manager = (LanguageToolsManager)Class.forName(ltImpl).newInstance();
					manager.setConfiguration(configurationManager);
					manager.setLanguageToolsImpl(lt);
				}
			}catch(Exception ex){
				log.error("Error while creating LanguageToolsManager", ex);
			}
		}
		return manager;
	}

	public static LTFactory getInstance(){
		if(instance==null){
			instance = new LTFactory();
		}
		return instance;
	}


}