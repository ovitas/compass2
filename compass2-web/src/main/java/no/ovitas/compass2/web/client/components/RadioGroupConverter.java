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
package no.ovitas.compass2.web.client.components;

import com.extjs.gxt.ui.client.binding.Converter;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;

/**
 * @author nyari
 *
 */
public class RadioGroupConverter extends Converter {
	private RadioGroup group;
	
	public RadioGroupConverter(RadioGroup group) {
		this.group = group;
	}

	@Override
	public Object convertModelValue(Object value) {
		for (Field<?> rad: group.getAll()) {
			 if (((Radio)rad).getValueAttribute() == value) {
				 return rad;
			 }
		 }			    
		 return null;
	}

	@Override
	public Object convertFieldValue(Object value) {		 
		 return ((Radio)value).getValueAttribute();
	}
	
}
