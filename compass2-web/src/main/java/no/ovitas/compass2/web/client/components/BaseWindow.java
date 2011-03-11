/**
 * Created on 2010.08.27.
 *
 * Copyright (C) 2010 Thot-Soft 2002 Ltd. All Rights Reserved.
 *
 * This software is the proprietary information of ThotSoft 2002 Ltd.
 * Use is subject to license terms.
 *
 * $Id:$
 */
package no.ovitas.compass2.web.client.components;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.user.client.Element;

/**
 * @author nyari
 *
 */
public abstract class BaseWindow extends Window {
	
	private List<AcceptedListener> listeners = new ArrayList<AcceptedListener>();

	/**
	 * Class Constructor;
	 */
	public BaseWindow(String title, int width) {
		super();
		setHeading(title);
		setWidth(width);
		//int clientHeight = (int) (com.google.gwt.user.client.Window.getClientHeight() * 0.9);
		//setHeight(clientHeight);
	}

	@Override
	protected void onRender(Element parent, int pos) {
		super.onRender(parent, pos);
		buildGui();
	}
	
	protected abstract void buildGui();
	
	/**
	 * Add new window accepted listener.
	 * 
	 * @param l	the listener
	 */
	public void addWindowAcceptedListener(AcceptedListener l) {
		listeners.add(l);
	}
	
	/**
	 * Remove an window accepted listener.
	 * 
	 * @param l	the listener
	 */
	public void removeWindowAcceptedListener(List<AcceptedListener> l) {
		listeners.remove(l);
	}
	
	/**
	 * Fire the accepted method of window accepted listeners.
	 * 
	 * @param entity
	 */
	protected void fireWindowAccepted() {
		for (AcceptedListener l : listeners) {
			l.accepted();
		}
	}

}
