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

import no.ovitas.compass2.service.FullTextSearchManager;
import no.ovitas.compass2.service.factory.FTSFactory;

/**
 * 
 */
public class FTSAction extends BaseAction implements Preparable{

	
   public void prepare(){
	   
   }
	
	public String execute(){
		FTSFactory ff = FTSFactory.getInstance();
		FullTextSearchManager fts = ff.getFTSImplementation();
		String docRoot = "/app/compass/data";
		writeResponse("Processing documents in: "+docRoot+"\n");
		fts.addDocument(true, 2, docRoot);
		writeResponse("Documents processed\n");
		
		
		return NONE;
	}
    protected void writeResponse(String response) throws IOException {
        PrintWriter out = getResponse().getWriter();
        out.append(response);
    }     
	
}
