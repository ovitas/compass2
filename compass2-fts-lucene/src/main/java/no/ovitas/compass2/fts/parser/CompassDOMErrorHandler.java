package no.ovitas.compass2.fts.parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Custom error handler class
 * @author csanyi
 *
 */
public class CompassDOMErrorHandler implements org.xml.sax.ErrorHandler {
	private Log log = LogFactory.getLog(getClass());
	/**
	 * Error
	 */
	public void error(SAXParseException exception) throws SAXException {
		log.error(exception.getMessage());
	}

	/**
	 * Fatal
	 */
	public void fatalError(SAXParseException exception) throws SAXException {
		log.fatal(exception.getMessage());
	}

	/**
	 * Warning
	 */
	public void warning(SAXParseException exception) throws SAXException {
		log.warn(exception.getMessage());
	}
}