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

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author nyari
 *
 */
public class ResultModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	public void setTitle(String title) {
		set(Compass2Constans.TITLE, title);
	}
	
	public String getTitle() {
		return get(Compass2Constans.TITLE);
	}
	
	public void setHtmlLink(String htmlLink) {
		set(Compass2Constans.HTML_LINK, htmlLink);
	}
	
	public String getHtmlLink() {
		return get(Compass2Constans.HTML_LINK);
	}
	
	public void setScore(Float score) {
		set(Compass2Constans.SCORE, score);
	}
	
	public Float getScore() {
		return get(Compass2Constans.SCORE);
	}


}
