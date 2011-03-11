package no.ovitas.compass2.exception;

/**
 * Base exception class of the Compass2 application. All exceptions within the
 * application should subclass this one.
 * <p>
 * In the Compass2 application we differentiate technical and business
 * exceptions. Technical exceptions cannot be mitigated by the end user of the
 * application, therefore the detailed error message is pure technical (as
 * appear in the underlying exception. However the business exception should be
 * handled by the end user, therefore a separate error message should be
 * prepared for them.
 * </p>
 * <p>
 * The technical exceptions should be created with one of the constructors of
 * the superclass, namely:
 * </p>
 * <ul>
 * <li>{@link #CompassException(String)}</li>
 * <li>{@link #CompassException(Throwable)}</li>
 * <li>{@link #CompassException(String, Throwable)}</li>
 * </ul>
 * <p>
 * The business exceptions should be created with the additional constructors,
 * namely:
 * </p>
 * <ul>
 * <li>{@link #CompassException(long, String)}</li>
 * <li>{@link #CompassException(long, Throwable)}</li>
 * <li>{@link #CompassException(long, String, Throwable)}</li>
 * </ul>
 * <p>
 * whereas the <code>long</code> parameter is the appropriate business error id
 * from {@link CompassErrorID} constants.
 * </p>
 * 
 * @author Csaba Daniel
 * 
 */
public class CompassException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	protected long errorID = 1;

	/**
	 * Create a <b>technical</b> exception with the detailed technical message.
	 * 
	 * @param message
	 *            the detailed technical message
	 */
	public CompassException(String message) {
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
	public CompassException(Throwable cause) {
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
	public CompassException(String message, Throwable cause) {
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
	public CompassException(long errorID, String message) {
		super(message);
		this.errorID = errorID;
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
	public CompassException(long errorID, Throwable cause) {
		super(cause);
		this.errorID = errorID;
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
	public CompassException(long errorID, String message, Throwable cause) {
		super(message, cause);
		this.errorID = errorID;
	}

	/**
	 * Get if this is a business exception.
	 * 
	 * @return <code>true</code> if this is a business exception,
	 *         <code>false</code> otherwise
	 */
	public boolean isBusiness() {
		return errorID != -1;
	}

	/**
	 * Get if this is a technical exception.
	 * 
	 * @return <code>true</code> if this is a technical exception,
	 *         <code>false</code> otherwise
	 */
	public boolean isTechnical() {
		return errorID == -1;
	}

	/**
	 * Get the business error id of this exception.
	 * <p>
	 * For technical exceptions this value is <code>-1</code>, for business
	 * exceptions the value should be one of the constants defined in
	 * {@link CompassErrorID}.
	 * </p>
	 * 
	 * @return the business error id
	 */
	public long getErrorID() {
		return errorID;
	}
}
