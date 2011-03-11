/**
 * Created on 2010.10.01.
 *
 * Copyright (C) 2010 Thot-Soft 2002 Ltd. All Rights Reserved.
 *
 * This software is the proprietary information of ThotSoft 2002 Ltd.
 * Use is subject to license terms.
 *
 * $Id:$
 */
package no.ovitas.compass2.web.client.components;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import no.ovitas.compass2.web.client.model.FolderModel;
import no.ovitas.compass2.web.client.model.SearchModel;

/**
 * @author nyari
 *
 */
public interface ResearchListener {

	void search(SearchModel searchModel, Collection<Long> unSelectedTopics, Map<Long, Collection<Long>> unSelectedNotOpenedTopics, Map<Long, Collection<String>> unSelectedNotOpenedTerms);
	
}
