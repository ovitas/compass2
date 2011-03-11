package no.ovitas.compass2.fts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.exception.CompassErrorID;
import no.ovitas.compass2.exception.CompassException;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.model.Content;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * @class TikaFTSContentHandlerImpl
 * @project compass2-fts-lucene
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.07.28.
 * 
 */
public class TikaFTSContentHandlerImpl extends AbstractFTSContentHandlerImpl {

	/**
	 * Get content from the file. And filter with fields.
	 */
	public Content getContent(File f) throws ConfigurationException {
		InputStream stream;

		try {
			stream = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			throw new CompassException(CompassErrorID.FTS_INVALID_CONTENT_URL_ERROR,
					"Document not readable: " + f, e);
		}

		ContentHandler handler = new BodyContentHandler();

		Metadata met = new Metadata();

		ParseContext context = new ParseContext();

		Parser parser = new AutoDetectParser();

		Map<String, Object> content;

		try {
			parser.parse(stream, handler, met, context);

			content = createContent(met, handler, f);
		} catch (IOException e) {
			throw new CompassException(CompassErrorID.FTS_CONTENT_PARSE_ERROR, "Exception when parsing document: " + f, e);
		} catch (SAXException e) {
			throw new CompassException(CompassErrorID.FTS_CONTENT_PARSE_ERROR, "Exception when parsing document: " + f, e);
		} catch (TikaException e) {
			throw new CompassException(CompassErrorID.FTS_CONTENT_PARSE_ERROR, "Exception when parsing document: " + f, e);
		}

		Map<String, Object> filterContent = filterContent(content);

		return new Content(filterContent, fields);

	}

	private Map<String, Object> createContent(Metadata met,
			ContentHandler handler, File file) throws IOException {
		Map<String, Object> content = new TreeMap<String, Object>();

		String contentType = met.get(Metadata.CONTENT_TYPE);
		if (contentType != null && !contentType.isEmpty()) {
			content.put(Constants.FILE_TYPE_INDEX, contentType);
		} else {
			content.put(Constants.FILE_TYPE_INDEX, "unknown");
		}

		String title = met.get(Metadata.TITLE);
		if (title != null && !title.isEmpty()) {
			content.put(Constants.TITLE_INDEX, title);
		} else {
			content.put(Constants.TITLE_INDEX, file.getName());
		}

		content.put(Constants.CONTENT_INDEX, handler.toString());
		content.put(Constants.URI_INDEX, file.getCanonicalPath());
		content.put(Constants.LAST_MODIFIED_INDEX,
				new Date(System.currentTimeMillis()));
		content.put(Constants.KEYWORD_INDEX, "");

		return content;
	}

}
