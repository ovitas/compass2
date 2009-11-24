package no.ovitas.compass2.service.factory;
import no.ovitas.compass2.Constants;
import no.ovitas.compass2.config.KnowledgeBase;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.KnowledgeBaseManager;
import no.ovitas.compass2.util.CompassUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author magyar
 * @version 1.0
 */
public class KBFactory {

	private static KBFactory instance = null;
	protected ConfigurationManager configurationManager;
	protected String kbFile;
	protected boolean loadOnStartup;
	private Log log = LogFactory.getLog(getClass());
	
	protected KnowledgeBaseManager manager = null;;

	public KBFactory(){
		ApplicationContext context = CompassUtil.getApplicationContext();
		configurationManager = (ConfigurationManager)context.getBean("configurationManager");
		if(configurationManager!=null){
			String defaultkbName = configurationManager.getDefaultKBImplementationName();
			String los = getKbImplementationParamValue(defaultkbName, Constants.LOAD_ON_STARTUP);
			if(los==null){
				this.loadOnStartup = false;
			}else{
				if(los.trim().equals("true")){
					this.loadOnStartup = true;
				}else{
					this.loadOnStartup = false;
				}
			}
			kbFile = getKbImplementationParamValue(defaultkbName, Constants.FILE_PATH);
		}
	}

	/**
	 * Return the value of name parameter of the KnowledgeBaseImplementation
	 * @param kbName the knowledge base name
	 * @param name
	 * @return the value
	 */
	private String getKbImplementationParamValue(String kbName,
			String name) {
		return configurationManager.getKnowledgeBase(kbName).getKnowledgeBaseImplementation().getParams().getParam(name).getName();
	}

	public KnowledgeBaseManager getKBImplementation(){
		if(manager==null){
			String defaultkbName = configurationManager.getDefaultKBImplementationName();
			KnowledgeBase kb = configurationManager.getKnowledgeBase(defaultkbName);
			String kbImplClassName = kb.getKnowledgeBaseImplementation().getClassName();
			log.info("kbImpl configuration: "+kbImplClassName);
			try{
				if(kbImplClassName!=null){
					manager = (KnowledgeBaseManager)Class.forName(kbImplClassName).newInstance();
					manager.setConfiguration(configurationManager);
					manager.setKnowledgeBaseImpl(kb.getKnowledgeBaseImplementation());
					if(this.loadOnStartup){
						manager.importKB(kbFile);
					}
					
					
				}
			}catch(Exception ex){
				log.error("Error while creating KnowLedgeBaseManager", ex);
			}
		}
		return manager;
	}

	public static KBFactory getInstance(){
		if(instance==null){
			instance = new KBFactory();
		}
		return instance;
	}


}