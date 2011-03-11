/**
 * Created on 2010.08.06.
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
 *
 */
public enum FittingNumberType {

	EQUAL("="),
	LESS_THAN("<="),
	GREATER_THAN(">=");
	
	private String title;
	
	private FittingNumberType(String title) {
		this.title = title; 
	}	
	
	public String getTitle() {		
		return title;
	}
	
}
