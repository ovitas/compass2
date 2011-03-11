/**
 * 
 */
package no.ovitas.compass2.web.client.model;

import java.io.Serializable;

/**
 * @author gyalai
 *
 */
public class BackEndException extends Exception implements Serializable {

	private Long errorId = 101L;

	public BackEndException(String msg) {
		super(msg);
	}
	
	public BackEndException() {
		super();
	}
	
	public BackEndException(String msg, long errorId) {
		super(msg);
		this.errorId = errorId;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Long getErrorID() {
		return errorId;
	}
		

}
