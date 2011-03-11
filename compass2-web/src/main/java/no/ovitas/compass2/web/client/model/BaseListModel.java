/**
 * Created on 2010.08.25.
 *
 * Copyright (C) 2010 Thot-Soft 2002 Ltd. All Rights Reserved.
 *
 * This software is the proprietary information of ThotSoft 2002 Ltd.
 * Use is subject to license terms.
 *
 * $Id:$
 */
package no.ovitas.compass2.web.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author nyari
 *
 */
public class BaseListModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	public String getLabel() {
		return get(Compass2Constans.LABEL);
	}
	
	public void setLabel(String label) {
		set(Compass2Constans.LABEL, label);
	}
	
	public String getValue() {
		return get(Compass2Constans.VALUE);
	}
	
	public void setValue(String value) {
		set(Compass2Constans.VALUE, value);
	}

}
