package no.ovitas.compass2.service.impl;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.model.DocumentDetails;
import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.model.LuceneHit;

import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.FullTextSearchManager;
import no.ovitas.compass2.service.factory.ContentIndexerFactory;
import no.ovitas.compass2.util.lucene.ContentIndexer;
import no.ovitas.compass2.util.lucene.PagerHitCollector;


/**
 * @author magyar
 * @version 1.0
 * 
 */
public class LuceneFTSManagerImpl implements FullTextSearchManager {

	protected ConfigurationManager configManager;
    private Log log = LogFactory.getLog(getClass());
	private Map<String, String> fields = new HashMap<String, String>();
    
    
	public void setConfiguration(ConfigurationManager manager){
		configManager = manager;
	}
	
	public LuceneFTSManagerImpl(){

	}


	
	
	/**
	 * 
	 * @param reindex
	 * @param depth
	 * @param uri
	 */
	public void addDocument(boolean reindex, int depth, String dir)throws ConfigurationException{
		String indexDir = configManager.getConfigParameter(Constants.LUCENE_FTS_INDEX_DIR);
		log.debug("indexDir: " + indexDir);
		log.debug("dir: " + dir);
		File f = new File(dir);
		if(f.isFile()){
				try {
					ContentIndexer indexer = ContentIndexerFactory.getInstance().getIndexerImplementation();
					indexer.setIndexWriter(getWriter(indexDir,reindex));
					indexer.init();
					indexer.index(f, fields);
					indexer.close();
				} catch (IOException e) {
					log.error("Error occured: "+e.getMessage(),e);
				} catch (Exception e) {
					log.error("Error occured: "+e.getMessage(),e);
				}
		}else{
			if(f.isDirectory()){
				try{
					ContentIndexer indexer = ContentIndexerFactory.getInstance().getIndexerImplementation();
					indexer.setIndexWriter(getWriter(indexDir,reindex));
					indexer.init();
					this.uploadFiles(f, depth, indexer);
					indexer.close();
				} catch (IOException e) {
					log.error("Error occured: "+e.getMessage(),e);
				}


			}
		}
		
		log.debug("file: " + f.toString());

	}

	protected IndexWriter getWriter(String indexDir, boolean reindex){
		 try {
			 Directory dir = FSDirectory.getDirectory(new File(indexDir), null);
			return new IndexWriter(dir, 
			 new StandardAnalyzer(), reindex,
			 IndexWriter.MaxFieldLength.UNLIMITED);
		} catch (CorruptIndexException e) {
			log.error("Error occured: "+e.getMessage(),e);
		} catch (LockObtainFailedException e) {
			log.error("Error occured: "+e.getMessage(),e);
		} catch (IOException e) {
			log.error("Error occured: "+e.getMessage(),e);
		}
		return null;
		
	}
	
	/**
	 * 
	 * @param xpathExpression
	 * @param fieldName
	 */
	public void addDocumentField(String xpathExpression, String fieldName){
		fields.put(fieldName, xpathExpression);
	}

	/**
	 * 
	 * @param docId
	 * @param uri
	 */
	public void deleteDocument(String docId, String uri){
		String indexDir = configManager.getConfigParameter(Constants.LUCENE_FTS_INDEX_DIR);
		if(indexDir!=null){
		 IndexWriter writer = getWriter(indexDir,false);
		 if(writer!=null){
			 Term t[] = null;
			 if(docId!=null && !docId.isEmpty() && uri!=null && !uri.isEmpty()){
			  t = new Term[2];
			  t[0] = new Term("ID",docId);
			  t[1] = new Term("URI", uri); 
			 }else{
				 if(docId!=null && !docId.isEmpty()){
					t = new Term[1]; 
					t[0] = new Term("ID",docId);
				 }
				 if(uri!=null && !uri.isEmpty()){
					t = new Term[1]; 
				    t[0] = new Term("URI", uri); 
					 
				 }
				 
			 }
             if(t!=null){
				 try {
					writer.deleteDocuments(t);
					writer.optimize();
					writer.commit();
				} catch (CorruptIndexException e) {
					log.error("Error occured: "+e.getMessage(),e);
				} catch (IOException e) {
					log.error("Error occured: "+e.getMessage(),e);
				}
             }
 			try {
				writer.close();
			} catch (CorruptIndexException e) {
				log.error("Error occured: "+e.getMessage(),e);
			} catch (IOException e) {
				log.error("Error occured: "+e.getMessage(),e);
			}
		 }
		}


	}

	/**
	 * 
	 * @param searchTopics
	 */
	public List<Hit> doSearch(List<Set<String>> searchItems, int pageNum){
		StringBuffer queryString = new StringBuffer();
		for (Set<String> itemSet : searchItems) {
			if (queryString.length() > 0) queryString.append(" ");
			
			if (itemSet.size() > 0) queryString.append("(");
			boolean firstItem = true;
			for (String item : itemSet) {
				if (firstItem) firstItem = false;
				else queryString.append(" OR ");
				queryString.append("\"" + item + "\"");
			}
			if (itemSet.size() > 0) queryString.append(")");
		}
		return doSearch(queryString.toString(), pageNum);
	}

	/**
	 * 
	 * @param search
	 * @throws ParseException 
	 */
	public List<Hit> doSearch(String search, int pageNum){
		String pageMaxHits = configManager.getConfigParameter(Constants.LUCENE_FTS_MAX_HITS_PER_QUERY);
		String indexDir = configManager.getConfigParameter(Constants.LUCENE_FTS_INDEX_DIR);
		int maxHitsNum = 100;
		try{
			maxHitsNum= Integer.parseInt(pageMaxHits);
		}catch(Exception ex){
			
		}
		
		Query q = null;
		IndexSearcher searcher = null;
		try {
			searcher = new IndexSearcher(indexDir);
		} catch (Exception e) {
			log.fatal("Fatal error occured: "+e.getMessage(),e);
			return null;
		}
		if(search!=null && !search.isEmpty()){
			if(search.trim().startsWith("*")){
			 q =new WildcardQuery(new Term(Constants.CONTENT_INDEX,search));	
			}else{
				QueryParser qp = new QueryParser(Constants.CONTENT_INDEX,new SimpleAnalyzer());
				try {
					q = qp.parse(search);
				} catch (ParseException e) {
					log.fatal("Fatal error occured: "+e.getMessage(),e);
					return null;
				}
				
			}
			try {
				PagerHitCollector phc = new PagerHitCollector((pageNum-1)*maxHitsNum, pageNum*maxHitsNum);
				try{
				 searcher.search(q,phc);
				}catch(RuntimeException e){
					//
				}
				if(!phc.getDocIds().isEmpty()){
					List<Hit> hits = new ArrayList<Hit>();
					for(Integer docId : phc.getDocIds()){
						
						hits.add(new LuceneHit(searcher.doc(docId.intValue()),phc.getScore(docId)));
					}
					return hits;
				}
				
			} catch (IOException e) {
				log.fatal("Fatal error occured: "+e.getMessage(),e);
				return null;
			}
		}
		return null;
	}
	
	
	protected void uploadFiles(File directory, int depth, ContentIndexer indexer){
		File ff[] = directory.listFiles();
		if(ff!=null && ff.length>0){
			try {
				for(File file : ff){
					if(!file.isDirectory()){
						log.debug("processing file:"+file.getCanonicalPath());
						try{
							String name = file.getAbsolutePath();
							if(!name.toLowerCase().endsWith("jpg") && !name.toLowerCase().endsWith("ico") && !name.toLowerCase().endsWith("png") && !name.toLowerCase().contains(".axd")){
					          indexer.index(file, fields);
							}
						}catch(Exception ex){
							log.error("File not indexed: "+file.getAbsolutePath(),ex);
							
						}
					}else{
						if(depth>0 && !file.getName().equals("..") && !file.getName().equals(".")){
						 uploadFiles(file,depth-1,indexer);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("Error occured: "+e.getMessage(),e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("Error occured: "+e.getMessage(),e);
			}
		}
	}

	public DocumentDetails getDocument(String id) {
		String indexDir = configManager.getConfigParameter(Constants.LUCENE_FTS_INDEX_DIR);
		Query q = new TermQuery(new Term("ID",id));
		
		IndexSearcher searcher = null;
		try {
			searcher = new IndexSearcher(indexDir);
			Hits hits = searcher.search(q);
			if(hits.length()>0){
				Document d = hits.doc(0);
				DocumentDetails dd = new DocumentDetails();
				dd.setFileType(d.get(Constants.FILE_TYPE_INDEX));
				dd.setURI(d.get(Constants.URI_INDEX));
				dd.setID(d.get(Constants.ID_INDEX));
				return dd;
			}
			
		
		} catch (Exception e) {
			log.fatal("Fatal error occured: "+e.getMessage(),e);
			return null;
		}finally{
			if(searcher!=null){
				try {
					searcher.close();
				} catch (IOException e) {
					log.error("Error occured: "+e.getMessage(),e);
				}
			}
		}
		

		return null;
	}
	

}