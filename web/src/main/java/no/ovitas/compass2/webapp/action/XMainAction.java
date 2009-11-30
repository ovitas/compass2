/**
 * 
 */
package no.ovitas.compass2.webapp.action;

import com.opensymphony.xwork2.Preparable;

/**
 * @author magyar
 *
 */

public class XMainAction extends BaseAction implements Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3798443758150268456L;

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	public void prepare() throws Exception {
		// TODO Auto-generated method stub

	}
	
	public String execute(){
		return SUCCESS;
	}

}
