package no.ovitas.compass2.util;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Custom error handler class
 * @author csanyi
 *
 */
public class CompassDOMErrorHandler implements org.xml.sax.ErrorHandler {
	
	/**
	 * Error
	 */
	public void error(SAXParseException exception) throws SAXException {
		System.out.println("Error: " + exception.getMessage());
	}

	/**
	 * Fatal
	 */
	public void fatalError(SAXParseException exception) throws SAXException {
		System.out.println("Fatal: " + exception.getMessage());
	}

	/**
	 * Warning
	 */
	public void warning(SAXParseException exception) throws SAXException {
		System.out.println("Warning: " + exception.getMessage());
	}
}