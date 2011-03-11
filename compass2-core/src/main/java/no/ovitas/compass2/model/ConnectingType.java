/**
 * Created on 2010.08.04.
 *
 * Copyright (C) 2010 Thot-Soft 2002 Ltd. All Rights Reserved.
 *
 * This software is the proprietary information of ThotSoft 2002 Ltd.
 * Use is subject to license terms.
 *
 * $Id:$
 */
package no.ovitas.compass2.model;

/**
 * @author nyari
 * Determines how the given searching field is attached to the previous.
 */
public enum ConnectingType {
	
	AND("And"),
	OR("Or"),
	NOT("Not"),
	FIRST("");
	
	private String title;
	
	private ConnectingType(String title) {
		this.title = title; 
	}	
	
	public String getTitle() {		
		return title;
	}
	
}
