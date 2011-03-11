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
 * Determines how the given searching field content is attached to the each other.
 */
public enum MatchingType {	
	
	MATCH_ALL("Match All"),
	MATCH_ANY("Match Any");
	
	private String title;
	
	private MatchingType(String title) {
		this.title = title; 
	}	
	
	public String getTitle() {		
		return title;
	}
	
}
