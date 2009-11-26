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
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.config.FullTextSearchImplementation;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.model.DocumentDetails;
import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.model.LuceneHit;

import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.FullTextSearchManager;
import no.ovitas.compass2.service.factory.ContentIndexerFactory;
import no.ovitas.compass2.util.XPair;
import no.ovitas.compass2.util.lucene.ContentIndexer;
import no.ovitas.compass2.util.lucene.PagerHitCollector;



/**
 * @author magyar
 * @version 1.0
 * 
 */
public class LuceneFTSManagerImpl implements FullTextSearchManager {

	protected int allHitNumber;
	protected ConfigurationManager configManager;
    private Log log = LogFactory.getLog(getClass());
	private Map<String, String> fields = new HashMap<String, String>();

	private double resultThreshold;
	private int maxNumberOfHits;
	private FullTextSearchImplementation ftsImpl;
	private String indexDirectory;
	private boolean fuzzySearch;
    
	
	public void setFuzzySearch(boolean fuzzySearch){
		this.fuzzySearch = fuzzySearch;
	}
	@Override
	public void setFTSImpl(FullTextSearchImplementation ftsImpl) {
		this.ftsImpl = ftsImpl;
		
	}
	
    
	public void setConfiguration(ConfigurationManager manager){
		configManager = manager;
	}
	
	public LuceneFTSManagerImpl(){
		maxNumberOfHits = 100;
		fuzzySearch = false;
	}


	
	
	/**
	 * 
	 * @param reindex
	 * @param depth
	 * @param uri
	 */
	public void addDocument(boolean reindex, int depth, String dir)throws ConfigurationException{
		
		log.debug("indexDir: " + indexDirectory);
		log.debug("dir: " + dir);
		File f = new File(dir);
		if(f.isFile()){
				try {
					ContentIndexer indexer = ContentIndexerFactory.getInstance().getIndexerImplementation();
					indexer.setIndexWriter(getWriter(indexDirectory,reindex));
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
					indexer.setIndexWriter(getWriter(indexDirectory,reindex));
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
		String indexDir = getFTSImplementationParamValue(Constants.INDEXDIRECTORY_PATH);
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
	 * Get parameter value of specified paramName in FullTextSearchImplementation
	 * @param paramName
	 * @return the value
	 */
	private String getFTSImplementationParamValue(String paramName) {
		return configManager.getFullTextSearch().getFullTextSearchImplementation().getParams().getParam(paramName).getValue();
	}


	private String formatDouble(Double d){
		return "";
	}
	/**
	 * 
	 * @param searchTopics
	 */
	public List<Hit> doSearch(List<List<XPair<String, Double>>> searchItems, int pageNum){
		StringBuffer queryString = new StringBuffer();
		for (List<XPair<String,Double>> itemSet : searchItems) {
			if (queryString.length() > 0) queryString.append(" ");
			
			boolean firstItem = true;
			for (XPair<String,Double> item : itemSet) {
				if (firstItem) firstItem = false;
				else queryString.append(" OR ");
				String itemString = this.normalizeSearchString(item.getKey());
				String boostString = formatDouble(item.getValue());
				queryString.append("\"" + itemString + "\"^"+boostString+(fuzzySearch ? "~" : "" ));
			}
		}
		if(log.isDebugEnabled()){
			log.debug("Search query string is: "+queryString);
		}
		return doSearch(queryString.toString(), pageNum);
	}

	private String normalizeSearchString(String search){
		String r = "";
		r = search.replace(":", "");
		r = r.replace("+", "");
		r = r.replace("-", "");
		r = r.replace("?", "");
		r = r.replace("\\", "");
		r = r.replace("!", "");
		r = r.replace("&&", "");
		r = r.replace("||", "");
		r = r.replace("^", "");
		return r;
	}
	
	/**
	 * 
	 * @param search
	 * @throws ParseException 
	 */
	public List<Hit> doSearch(String s, int pageNum){
		
		String search = normalizeSearchString(s);
		
		Query q = null;
		IndexSearcher searcher = null;
		try {
			searcher = new IndexSearcher(this.indexDirectory);
		} catch (Exception e) {
			log.fatal("Fatal error occured: "+e.getMessage(),e);
			return null;
		}
		if(search!=null && !search.isEmpty()){
			if(search.trim().contains("*")){
			 q =new WildcardQuery(new Term(Constants.CONTENT_INDEX,search));	
			}else{

				if(this.fuzzySearch && !search.contains("~")){
					search +="~";
				}
				if(search.contains("~")){
					 q =new FuzzyQuery(new Term(Constants.CONTENT_INDEX,search));	
				}
				QueryParser qp = new QueryParser(Constants.CONTENT_INDEX,new SimpleAnalyzer());
				try {
						
					q = qp.parse(search);
				} catch (ParseException e) {
					log.fatal("Fatal error occured: "+e.getMessage(),e);
					return null;
				}
				
			}
			try {
				PagerHitCollector phc = new PagerHitCollector(this.maxNumberOfHits);
				if(resultThreshold>0.0){
				 phc.setResultThreshold(resultThreshold);
				}
				try{
				 searcher.search(q,phc);
				 phc.runSorting();
				 this.allHitNumber = phc.getHitCounter();
				}catch(RuntimeException e){
					log.error("Error occured: ",e);
				}
				// Choose best max number of hits
				
				
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
		
		Query q = new TermQuery(new Term("ID",id));
		
		IndexSearcher searcher = null;
		try {
			searcher = new IndexSearcher(this.indexDirectory);
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


	@Override
	public void setResultThreshold(double resultThreshold) {
		this.resultThreshold = resultThreshold;
	
		
	}

	public int getMaxNumberOfHits() {
		return maxNumberOfHits;
	}

	public void setMaxNumberOfHits(int maxNumberOfHits) {
		this.maxNumberOfHits = maxNumberOfHits;
	}


	public void init() throws ConfigurationException {
		this.resultThreshold = 0.0;
		this.resultThreshold = configManager.getResult().getResultThreshold();
		this.indexDirectory = getFTSImplementationParamValue(Constants.INDEXDIRECTORY_PATH);
		if(configManager.getResult().getMaxNumberOfHits()>0){
		 maxNumberOfHits= configManager.getResult().getMaxNumberOfHits();
		}
			
	}


	public int getAllHitNumber() {
		return allHitNumber;
	}

	

}