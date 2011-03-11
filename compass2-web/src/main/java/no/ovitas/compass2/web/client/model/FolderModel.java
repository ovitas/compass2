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

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author nyari
 * 
 */
public class FolderModel extends BaseTreeModel implements IsSerializable {

	private static final long serialVersionUID = 1L;

	public FolderModel() {
	
	}
	
	public FolderModel(Long id) {
		set(Compass2Constans.ID, id);
	}

	public FolderModel(String name, Long id) {
		set(Compass2Constans.NAME, name);
		set(Compass2Constans.ID, id);
	}

	public FolderModel(String name, BaseTreeModel[] children, Long id) {
		this(name, id);
		for (int i = 0; i < children.length; i++) {
			add(children[i]);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FolderModel) {
			if (obj != null && getId() != null && getId().equals(((FolderModel) obj).getId())) {
				return true;
			}
		}
		
		return false;
	}

	public Long getId() {
		return get(Compass2Constans.ID);
	}

	public String getName() {
		return get(Compass2Constans.NAME);
	}
	
	public void setName(String name) {
		set(Compass2Constans.NAME, name);
	}
	
	public String getTermName() {
		return get(Compass2Constans.TERM_NAME);
	}
	
	public void setTermName(String name) {
		set(Compass2Constans.TERM_NAME, name);
	}
	
	public String getStemName() {
		return get(Compass2Constans.STEM_NAME);
	}
	
	public void setStemName(String name) {
		set(Compass2Constans.STEM_NAME, name);
	}

	public String toString() {
		return getName();
	}
	
	public void setTopic(Boolean isTopic) {
		set(Compass2Constans.TOPIC_NODE_IS_TOPIC, isTopic);
	}
	
	public Boolean isTopic() {
		return get(Compass2Constans.TOPIC_NODE_IS_TOPIC);
	}
	
	
	public Boolean isOpened() {
		return get(Compass2Constans.IS_OPENED);
	}

	public void setOpened(Boolean opened) {
		set(Compass2Constans.IS_OPENED, opened);
	}
	
}
