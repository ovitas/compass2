/**
 * 
 */
package no.ovitas.compass2.web.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author gyalai
 *
 */
public class ExceptionModel extends BaseModel implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Long getExceptionID() {
		return get(Compass2Constans.EXCEPTION_ID);
	}

	public void setExceptionID(long id) {
		set(Compass2Constans.EXCEPTION_ID, id);
	}
}
