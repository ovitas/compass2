/**
 * 
 */
package no.ovitas.compass2.util.lucene;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import no.ovitas.compass2.exception.ConfigurationException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @author magyar
 *
 */
public abstract class BaseContenIndexer {

	protected IndexWriter indexWriter;
	protected String indexDirectory;
	
	
    public void init() throws ConfigurationException, IOException{
    	if(indexWriter == null){
    		if(indexDirectory!=null && !indexDirectory.isEmpty()){
    			 Directory dir = FSDirectory.getDirectory(new File(indexDirectory), null);
    			 indexWriter = new IndexWriter(dir, 
    			 new StandardAnalyzer(), true,
    			 IndexWriter.MaxFieldLength.UNLIMITED);
    			
    		}else{
    			throw new ConfigurationException("IndexDirectory and IndexWriter are not set!");
    		}
    	}
    }
	
    /* (non-Javadoc)
	 * @see no.ovitas.compass2.util.lucene.ContentIndexer#index(java.io.File, java.util.Map)
	 */
    public void index(File f, Map<String, String> additionalFields) throws Exception {
    	if (!f.isDirectory() &&
    			!f.isHidden() &&
    			f.exists() &&
    			f.canRead() &&
    			acceptFile(f)) {
    			indexFile(f, additionalFields);
    			} 
    	}


    
	protected abstract void indexFile(File f, Map<String, String> additionalFields)  throws Exception;

	
	protected boolean acceptFile(File f) { //6
		return true;
	}
    
	/* (non-Javadoc)
	 * @see no.ovitas.compass2.util.lucene.ContentIndexer#close()
	 */
	public void close() throws IOException {
	 indexWriter.close(); 
	}	

	/**
	 * @return the indexWriter
	 */
	public IndexWriter getIndexWriter() {
		return indexWriter;
	}

	/**
	 * @param indexWriter the indexWriter to set
	 */
	public void setIndexWriter(IndexWriter indexWriter) {
		this.indexWriter = indexWriter;
	}

	/**
	 * @return the indexDirectory
	 */
	public String getIndexDirectory() {
		return indexDirectory;
	}

	/**
	 * @param indexDirectory the indexDirectory to set
	 */
	public void setIndexDirectory(String indexDirectory) {
		this.indexDirectory = indexDirectory;
	}
	
}
