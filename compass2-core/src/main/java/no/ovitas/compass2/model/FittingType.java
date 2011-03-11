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
 * Determines the fit of the given text search field.
 */
public enum FittingType {
	
	WHOLE_WORD("Whole word"),
	PREFIX("Prefix");
	
	private String title;
	
	private FittingType(String title) {
		this.title = title; 
	}	
	
	public String getTitle() {		
		return title;
	}
	
}
