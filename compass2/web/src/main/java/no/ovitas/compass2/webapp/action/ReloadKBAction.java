/**
 * 
 */
package no.ovitas.compass2.webapp.action;

import java.io.IOException;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.factory.KBFactory;

import com.opensymphony.xwork2.Preparable;

/**
 * @author magyar
 *
 */
public class ReloadKBAction extends BaseAction implements Preparable {

	protected ConfigurationManager configurationManager;
	
	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * 
	 */
	public ReloadKBAction() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	@Override
	public void prepare() throws Exception {
		// TODO Auto-generated method stub

	}
	
	public String execute(){
		String deafultkbName = configurationManager.getDefaultKBImplementationName();
		String kbfile = configurationManager.getKnowledgeBase(deafultkbName).getKnowledgeBaseImplementation().getParams().getParam(Constants.FILE_PATH).getName();
		try {
			writeResponse("KB loading started...");
			KBFactory.getInstance().getKBImplementation().importKB(kbfile);
			writeResponse("KB loaded...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NONE;
	}

}
