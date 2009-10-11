/**
 * 
 */
package no.ovitas.compass2.util.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @author magyar
 *
 */
public class TxtIndexer extends BaseContenIndexer implements ContentIndexer{

	
	
	
	
	
    
    protected boolean acceptFile(File f) { //6
    	return f.getName().toLowerCase().endsWith(".txt");
    }
    protected Document getDocument(File f) throws Exception {
    	Document doc = new Document();
    	doc.add(new Field("ID",UUID.randomUUID().toString(),Field.Store.YES, Field.Index.NOT_ANALYZED));
    	doc.add(new Field("filetype", "text",Field.Store.NO, Field.Index.NOT_ANALYZED)); //7
    	doc.add(new Field("title", f.getName(),Field.Store.YES, Field.Index.ANALYZED)); //7
    	doc.add(new Field("content", new FileReader(f))); //7
    	doc.add(new Field("URI", f.getCanonicalPath(), //8
    	Field.Store.YES, Field.Index.NOT_ANALYZED));
    	doc.add(new Field("lastModified",DateTools.dateToString(new Date(System.currentTimeMillis()), DateTools.Resolution.MILLISECOND),Field.Store.YES, Field.Index.NOT_ANALYZED));
    	return doc;
    }    
	private void indexFile(File f) throws Exception {
		Document doc = getDocument(f);
		if (doc != null) {
		  indexWriter.addDocument(doc); 
		 }
		}
	@Override
	protected void indexFile(File f, Map<String, String> additionalFields)
			throws Exception {
		Document doc = getDocument(f);
		if (doc != null) {
		  indexWriter.addDocument(doc); 
		 }
		
	}
}
