/**
 * 
 */
package no.ovitas.compass2.util.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.util.XmlUtil;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

/**
 * @author magyar
 *
 */
public class TikaIndexer extends BaseContenIndexer implements ContentIndexer {

	
	public TikaIndexer(String indexDir) throws IOException {
		indexDirectory = indexDir;
		 Directory dir = FSDirectory.getDirectory(new File(indexDir), null);
		 indexWriter = new IndexWriter(dir, 
		 new StandardAnalyzer(), true,
		 IndexWriter.MaxFieldLength.UNLIMITED);
		
	}
	
	public TikaIndexer(IndexWriter writer){
		this.indexWriter = writer;
	}
	
		public TikaIndexer() {
		super();
		
	}

	
 	    
	protected Document getDocument(File f, ContentHandler handler, Metadata metadata, Map<String, String> additionalFields) throws Exception {
		Document doc = new Document();
		doc.add(new Field("ID",UUID.randomUUID().toString(),Field.Store.YES, Field.Index.NOT_ANALYZED));
		if(metadata.get(Metadata.CONTENT_TYPE)!=null && !metadata.get(Metadata.CONTENT_TYPE).isEmpty()){
		 doc.add(new Field(Constants.FILE_TYPE_INDEX, metadata.get(Metadata.CONTENT_TYPE),Field.Store.YES, Field.Index.NOT_ANALYZED)); //7
		}else{
		 doc.add(new Field(Constants.FILE_TYPE_INDEX, "uknown",Field.Store.YES, Field.Index.NOT_ANALYZED)); //7
		}

		if(metadata.get(Metadata.TITLE)!=null && !metadata.get(Metadata.TITLE).isEmpty()){
		 doc.add(new Field(Constants.TITLE_INDEX, metadata.get(Metadata.TITLE),Field.Store.YES, Field.Index.ANALYZED)); //7
		}else{
			doc.add(new Field(Constants.TITLE_INDEX, f.getName(),Field.Store.YES, Field.Index.ANALYZED)); //7
		}
		
		doc.add(new Field(Constants.CONTENT_INDEX, handler.toString(),Field.Store.NO,Field.Index.ANALYZED)); //7
		doc.add(new Field(Constants.URI_INDEX, f.getCanonicalPath(), //8
		Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(Constants.LAST_MODIFIED_INDEX,DateTools.dateToString(new Date(System.currentTimeMillis()), DateTools.Resolution.MILLISECOND),Field.Store.YES, Field.Index.NOT_ANALYZED));
		
		if (additionalFields != null) {
			String contentType = metadata.get(Metadata.CONTENT_TYPE);
			if (contentType != null && contentType.endsWith("/xml")) {
				Map<String, String> additonalFieldValues = XmlUtil.parseXPath(f, additionalFields);
				for (Map.Entry<String, String> item : additonalFieldValues.entrySet()) {
					doc.add(new Field(item.getKey(), item.getValue(), Field.Store.NO, Field.Index.ANALYZED));
				}
			}
		}
		return doc;
	}    
	
	protected void indexFile(File f, Map<String, String> additionalFields) throws Exception {
		Metadata metadata = new Metadata();
		metadata.set(Metadata.RESOURCE_NAME_KEY,f.getCanonicalPath());
		InputStream is = new FileInputStream(f);
		Parser parser = new AutoDetectParser();
		ContentHandler handler = new BodyContentHandler();		
		try {
			parser.parse(is, handler, metadata);
		} finally {
			is.close();
		}
		
		Document doc = getDocument(f,handler,metadata, additionalFields);
		if (doc != null) {
			indexWriter.addDocument(doc); 
		}
	}

}
