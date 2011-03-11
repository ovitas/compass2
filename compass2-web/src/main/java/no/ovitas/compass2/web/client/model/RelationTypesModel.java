/**
 * Created on 2010.07.26.
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
public class RelationTypesModel extends EntityModel {

	private static final long serialVersionUID = 1L;
	
	public void setExternalId(String id) {
		set(Compass2Constans.EXTERNAL_ID, id);
	}
	
	public String getExternalId() {
		return get(Compass2Constans.EXTERNAL_ID);
	}
	
	public void setRelationName(String relationName) {
		set(Compass2Constans.RELATION_NAME, relationName);
	}
	
	public String getRelationName() {
		return get(Compass2Constans.RELATION_NAME);
	}
	
	public void setRef(Integer ref) {
		set(Compass2Constans.REF, ref);
	}
	
	public Integer getRef() {
		return get(Compass2Constans.REF);
	}
	
	public void setWeightAhead(Double weightAhead) {
		set(Compass2Constans.WEIGHT_AHEAD, weightAhead);
	}
	
	public Double getWeightAhead() {
		return get(Compass2Constans.WEIGHT_AHEAD);
	}
	
	public void setWeightAback(Double weightAback) {
		set(Compass2Constans.WEIGHT_ABACK, weightAback);
	}
	
	public Double getWeightAback() {
		return get(Compass2Constans.WEIGHT_ABACK);
	}

}
