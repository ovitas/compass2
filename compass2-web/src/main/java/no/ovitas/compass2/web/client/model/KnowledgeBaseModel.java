/**
 * Created on 2010.08.27.
 *
 * Copyright (C) 2010 Thot-Soft 2002 Ltd. All Rights Reserved.
 *
 * This software is the proprietary information of ThotSoft 2002 Ltd.
 * Use is subject to license terms.
 *
 * $Id:$
 */
package no.ovitas.compass2.web.client.model;

/**
 * @author nyari
 *
 */
public class KnowledgeBaseModel extends EntityModel {
	
	private static final long serialVersionUID = 1L;

	public String getDescription() {
		return get(Compass2Constans.DESCRIPTION);
	}
	
	public void setDescription(String description) {
		set(Compass2Constans.DESCRIPTION, description);
	}		

}
