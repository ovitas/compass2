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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import no.ovitas.compass2.model.DocumentDetails;
import no.ovitas.compass2.service.CompassManager;
import no.ovitas.compass2.service.factory.CompassManagerFactory;

import com.opensymphony.xwork2.Preparable;

/**
 * 
 */
public class FileReaderAction extends BaseAction implements Preparable {

	protected String docID;
	protected CompassManager compassManager;
	
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	public void prepare() throws Exception {
		compassManager = CompassManagerFactory.getInstance().getCompassManager();
	}
	
	
	
	public String execute(){
		DocumentDetails dd = compassManager.getDocument(docID);
		if(dd!=null){
			File f = new File(dd.getURI());
			FileInputStream fr = null;
			if(f.isFile() && f.canRead()){
				try {
					fr = new FileInputStream(f);
					getResponse().setContentType(dd.getFileType());
					getResponse().setContentLength((int) f.length());
					byte[] b = new byte[10*1024];
					while(fr.read(b)!=-1){
						getResponse().getOutputStream().write(b);	
						for(int i=0;i<10*1024;i++){
							b[i]=0;
						}
					}
					
					
				} catch (FileNotFoundException e) {
					log.error("Error occured: "+e.getMessage(),e);
				} catch (IOException e) {
					log.error("Error occured: "+e.getMessage(),e);
				}finally{
					if(fr!=null){
						try {
							fr.close();
						} catch (IOException e) {
							
							log.warn("Error occured: "+e.getMessage(),e);
						}
					}
				}
			}
			
		}
		
	   return NONE;	
	}



	/**
	 * @return the docID
	 */
	public String getDocID() {
		return docID;
	}



	/**
	 * @param docID the docID to set
	 */
	public void setDocID(String docID) {
		this.docID = docID;
	}



	/**
	 * @return the compassManager
	 */
	public CompassManager getCompassManager() {
		return compassManager;
	}



	/**
	 * @param compassManager the compassManager to set
	 */
	public void setCompassManager(CompassManager compassManager) {
		this.compassManager = compassManager;
	}




}
