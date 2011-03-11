package no.ovitas.compass2.ws.server;

import java.io.Serializable;

/**
 * This class wraps a term (word) for serialization purposes.
 * 
 * @author Csaba Daniel
 */
public class Term implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	/**
	 * Create a term without name.
	 */
	public Term() {
	}

	/**
	 * Create a term with the specified name.
	 * 
	 * @param name
	 *            the name of the term
	 */
	public Term(String name) {
		this.name = name;
	}

	/**
	 * Get the name of the term.
	 * 
	 * @return the name of the term
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the term.
	 * 
	 * @param name
	 *            the name of the term
	 */
	public void setName(String name) {
		this.name = name;
	}
}
