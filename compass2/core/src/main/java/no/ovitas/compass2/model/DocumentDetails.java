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

package no.ovitas.compass2.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 */
public class DocumentDetails implements Serializable {

	   protected String fileType;
	   protected String URI;
	   protected String ID;
	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}
	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	/**
	 * @return the uRI
	 */
	public String getURI() {
		return URI;
	}
	/**
	 * @param uri the uRI to set
	 */
	public void setURI(String uri) {
		URI = uri;
	}
	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}
	/**
	 * @param id the iD to set
	 */
	public void setID(String id) {
		ID = id;
	}
	
}
