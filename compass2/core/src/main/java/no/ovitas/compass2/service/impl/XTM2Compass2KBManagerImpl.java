/**
 * Created on 2009.05.26.
 *
 * Copyright (C) 2009 Thot-Soft 2002 Ltd. All Rights Reserved.
 *
 * This software is the proprietary information of ThotSoft 2002 Ltd.
 * Use is subject to license terms.
 *
 * $Id:$
 */

package no.ovitas.compass2.service.impl;

import no.ovitas.compass2.service.KnowledgeBaseManager;

/**
 * 
 */
public class XTM2Compass2KBManagerImpl extends DefaultKBManagerImpl implements
		KnowledgeBaseManager {

	public XTM2Compass2KBManagerImpl() {
		super("kbBuilderXtmDao");
		
	}
	
	@Override
	public void importKB(String kb){
		super.importKB(kb);
	}

}
