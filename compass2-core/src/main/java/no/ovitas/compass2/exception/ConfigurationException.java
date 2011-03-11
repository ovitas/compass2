/**
 * 
 */
package no.ovitas.compass2.exception;

/**
 * @author magyar
 * 
 */
public class ConfigurationException extends CompassException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a <b>technical</b> exception with the detailed technical message.
	 * 
	 * @param message
	 *            the detailed technical message
	 */
	public ConfigurationException(String message) {
		super(message);
	}

	/**
	 * Create a <b>technical</b> exception with the root cause.
	 * 
	 * @param message
	 *            the detailed technical message
	 * @param cause
	 *            the cause of this exception
	 */
	public ConfigurationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Create a <b>technical</b> exception with the detailed technical message
	 * and root cause.
	 * 
	 * @param message
	 *            the detailed technical message
	 * @param cause
	 *            the cause of this exception
	 */
	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a <b>business</b> exception with the business error id and
	 * detailed technical message.
	 * 
	 * @param errorID
	 *            the unique identifier of the business error as defined in
	 *            {@link CompassErrorID}
	 * @param message
	 *            the detailed technical message
	 */
	public ConfigurationException(long errorID, String message) {
		super(errorID, message);
	}

	/**
	 * Create a <b>business</b> exception with the business error id and root
	 * cause.
	 * 
	 * @param errorID
	 *            the unique identifier of the business error as defined in
	 *            {@link CompassErrorID}
	 * @param cause
	 *            the cause of this exception
	 */
	public ConfigurationException(long errorID, Throwable cause) {
		super(errorID, cause);
	}

	/**
	 * Create a <b>business</b> exception with the business error id, detailed
	 * technical message and root cause.
	 * 
	 * @param errorID
	 *            the unique identifier of the business error as defined in
	 *            {@link CompassErrorID}
	 * @param message
	 *            the detailed technical message
	 * @param cause
	 *            the cause of this exception
	 */
	public ConfigurationException(long errorID, String message, Throwable cause) {
		super(errorID, message, cause);
	}
}
