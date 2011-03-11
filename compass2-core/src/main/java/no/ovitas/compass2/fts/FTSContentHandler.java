package no.ovitas.compass2.fts;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import no.ovitas.compass2.Manager;
import no.ovitas.compass2.config.settings.Field;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.model.Content;

import org.xml.sax.SAXException;

/**
 * @class FTSContentHandler
 * @project compass2-fts-lucene
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version 1.0
 * @date 2010.07.28.
 * 
 */
public interface FTSContentHandler extends Manager {

	/**
	 * Return with the content of the file.
	 * 
	 * @param file
	 *            the file
	 * @return the content of the file
	 * @throws ConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws UnsupportedEncodingException
	 */
	public Content getContent(File file) throws ConfigurationException,
			SAXException, UnsupportedEncodingException,
			ParserConfigurationException, IOException;

	/**
	 * Set the field what we want to contain the content.
	 * 
	 * @param fields
	 *            the wanted fields
	 */
	public void setFields(Collection<Field> fields);

}
