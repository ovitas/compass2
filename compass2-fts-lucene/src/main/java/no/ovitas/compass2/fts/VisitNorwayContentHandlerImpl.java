/**
 * 
 */
package no.ovitas.compass2.fts;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.model.Content;
import no.ovitas.compass2.fts.parser.CompassDOMParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * @class VisitNorwayContentHandlerImpl
 * @project compass2-fts-lucene
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.30.
 * 
 */
public class VisitNorwayContentHandlerImpl extends
		AbstractFTSContentHandlerImpl {

	@Override
	public Content getContent(File f) throws SAXException, UnsupportedEncodingException, ParserConfigurationException, IOException {

		CompassDOMParser parser = new CompassDOMParser();
		Map<String, String> data = null;

		data = parser.parse(f);

		Map<String, Object> createdContent = createContent(getURL(f), data);

		Map<String, Object> filterContent = filterContent(createdContent);

		return new Content(filterContent, fields);
	}

	private Map<String, Object> createContent(String url,
			Map<String, String> data) {
		
		Map<String, Object> content = new HashMap<String, Object>();

		String contentString = "";

		if (data.get(Constants.DATA_INDEX) != null) {
			contentString += data.get(Constants.DATA_INDEX);
		}
		if (data.get(Constants.SUBHEADING_INDEX) != null) {
			contentString += data.get(Constants.SUBHEADING_INDEX);
		}

		content.put(Constants.URI_INDEX, url);
		content.put(Constants.FILE_TYPE_INDEX, "html");
		content.put(Constants.CONTENT_INDEX, contentString);
		content.put(Constants.TITLE_INDEX, data.get(Constants.TITLE_INDEX));
		content.put(Constants.KEYWORD_INDEX, data.get(Constants.KEYWORD_INDEX));
		content.put(Constants.LAST_MODIFIED_INDEX,
				new Date(System.currentTimeMillis()));

		return content;
	}

	protected String getURL(File f) {
		String filePath = f.getAbsolutePath();
		int index = filePath.indexOf("www");

		String url;
		if (index != -1) {

			url = "http://" + filePath.substring(index);
		} else {
			url = "http://" + filePath;
		}
		url = url.replaceAll("\\\\", "/");
		url = url.replaceAll("index\\.html", "");
		return url;
	}

}
