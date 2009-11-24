/**
 * 
 */
package no.ovitas.compass2.util.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.config.FullTextSearchImplementation;
import no.ovitas.compass2.util.CompassDOMParser;
import no.ovitas.compass2.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

/**
 * @author magyar
 *
 */
public class VisitNorwayIndexer extends BaseContenIndexer implements ContentIndexer {

	private static Log logger = LogFactory.getLog(VisitNorwayIndexer.class);
	

	public VisitNorwayIndexer() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected Document getDocument(String url,  Map<String, String> data, Map<String, String> additionalFields) throws Exception {
		Document doc = new Document();
		doc.add(new Field("ID",UUID.randomUUID().toString(),Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(Constants.FILE_TYPE_INDEX, "html",Field.Store.YES, Field.Index.NOT_ANALYZED)); //7

		doc.add(new Field(Constants.TITLE_INDEX, data.get(Constants.TITLE_INDEX),Field.Store.YES, Field.Index.ANALYZED)); //7
		
		doc.add(new Field(Constants.CONTENT_INDEX, data.get(Constants.DATA_INDEX)+data.get(Constants.SUBHEADING_INDEX),Field.Store.NO,Field.Index.ANALYZED)); //7
		doc.add(new Field(Constants.URI_INDEX, url, //8
		Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(Constants.LAST_MODIFIED_INDEX,DateTools.dateToString(new Date(System.currentTimeMillis()), DateTools.Resolution.MILLISECOND),Field.Store.YES, Field.Index.NOT_ANALYZED));
		
		return doc;
	}    
	
	@Override
	protected void indexFile(File f, Map<String, String> additionalFields) throws Exception {
		try {
			CompassDOMParser parser = new CompassDOMParser();
			Map<String,String> data = parser.parse(f); 
			Document doc = getDocument(getURL(f),data, additionalFields);
			if (doc != null) {
				logger.info("Indexing file:"+f.getCanonicalPath());

				indexWriter.addDocument(doc); 
			}
			
		}catch(Exception ex){
			logger.fatal("Cannot parse the input file: "+f.getAbsolutePath(),ex);
			
			
		}
		
	}

	protected String getURL(File f){
		String filePath = f.getAbsolutePath();
		int index = filePath.indexOf("www");
		
		String url = "http://"+filePath.substring(index);
		url = url.replaceAll("\\\\", "/");
		url = url.replaceAll("index\\.html", "");
		return url;
	}
	/* (non-Javadoc)
	 * @see no.ovitas.compass2.util.lucene.BaseContenIndexer#acceptFile(java.io.File)
	 */
	@Override
	protected boolean acceptFile(File f) {
		// TODO Auto-generated method stub
		return f.getName().endsWith(".html") || f.getName().endsWith(".htm");
	}


}
