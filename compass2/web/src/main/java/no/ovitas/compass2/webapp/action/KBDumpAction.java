/**
 * 
 */
package no.ovitas.compass2.webapp.action;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.dao.xml.KBBuilderXtmDaoXml;
import no.ovitas.compass2.model.KnowledgeBaseHolder;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.ExportDomainModelManager;
import no.ovitas.compass2.service.factory.KBFactory;
import no.ovitas.compass2.service.impl.ExportDomainModel2PlainTextManagerImpl;

import com.opensymphony.xwork2.Preparable;

/**
 * @author magyar
 *
 */
public class KBDumpAction extends BaseAction implements Preparable {

	private static final Log log = LogFactory.getLog(KBBuilderXtmDaoXml.class);
	
	protected ExportDomainModelManager exportDomainModelManager;
	
	public void setExportDomainModelManager(
			ExportDomainModelManager exportDomainModelManager) {
		this.exportDomainModelManager = exportDomainModelManager;
	}

	/**
	 * 
	 */
	public KBDumpAction() {
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
		try {
			writeResponse("KB dump started...\n");
			log.info("File name: "+this.exportDomainModelManager.exportModel());
			writeResponse("KB dump created...");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NONE;
	}

}
