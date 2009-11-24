/**
 * Created on 2009.06.10.
 *
 * Copyright (C) 2009 Thot-Soft 2002 Ltd. All Rights Reserved.
 *
 * This software is the proprietary information of ThotSoft 2002 Ltd.
 * Use is subject to license terms.
 *
 * $Id:$
 */

package no.ovitas.compass2.webapp.action;

import java.io.IOException;
import java.io.PrintWriter;

import com.opensymphony.xwork2.Preparable;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.FullTextSearchManager;
import no.ovitas.compass2.service.factory.FTSFactory;

/**
 * 
 */
public class FTSAction extends BaseAction implements Preparable{

	protected ConfigurationManager configurationManager;

	
   public void prepare(){
	   
   }
	
	public String execute(){
		FTSFactory ff = FTSFactory.getInstance();
		FullTextSearchManager fts = ff.getFTSImplementation();
		String docRoot = configurationManager.getFullTextSearch().getFullTextSearchImplementation().getParams().getParam(Constants.DOCUMENT_REPOSITORY).getName();
		try {
			writeResponse("Processing documents in: "+docRoot+"\n");
			fts.addDocument(true, 100, docRoot);
			writeResponse("Documents processed\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ConfigurationException e) {
			
			e.printStackTrace();
		}
		
		
		return NONE;
	}
    protected void writeResponse(String response) throws IOException {
        PrintWriter out = getResponse().getWriter();
        out.append(response);
    }

	/**
	 * @return the configurationManager
	 */
	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	/**
	 * @param configurationManager the configurationManager to set
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}     
	
}
