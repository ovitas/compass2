/**
 * 
 */
package no.ovitas.compass2.web.client.model;

import java.io.Serializable;

/**
 * @author gyalai
 *
 */
public class ImportWebException extends Exception implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImportWebException() {
		super();
	}
	
	public ImportWebException(String msg) {
		super(msg);
	}
	
	
}
