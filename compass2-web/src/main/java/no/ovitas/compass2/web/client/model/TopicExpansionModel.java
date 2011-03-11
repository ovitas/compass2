/**
 * Created on 2010.09.06.
 *
 * Copyright (C) 2010 Thot-Soft 2002 Ltd. All Rights Reserved.
 *
 * This software is the proprietary information of ThotSoft 2002 Ltd.
 * Use is subject to license terms.
 *
 * $Id:$
 */
package no.ovitas.compass2.web.client.model;

import java.util.Map;

import no.ovitas.compass2.model.knowledgebase.RelationDirection;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author nyari
 *
 */
public class TopicExpansionModel extends BaseModel implements IsSerializable {
	
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private TreeType treeType;

	public void setTrees(Map<TreeType, Long> treeIDS) {
		set(Compass2Constans.TREE_IDS, treeIDS);
	}
	
	public Map<TreeType, Long> getTrees() {
		return get(Compass2Constans.TREE_IDS);
	}

	public void setKnowledgeBaseName(String name) {
		set(Compass2Constans.KB_NAME, name);
	}
	
	public String getKnowledgeBaseName() {
		return get(Compass2Constans.KB_NAME);
	}
	
	public void setKnowledgeBaseId(Long id) {
		set(Compass2Constans.KB_ID, id);
	}
	
	public Long getKnowledgeBaseId() {
		return get(Compass2Constans.KB_ID);
	}
	
}
