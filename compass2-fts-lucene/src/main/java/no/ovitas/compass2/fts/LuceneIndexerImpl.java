/**
 * 
 */
package no.ovitas.compass2.fts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.exception.CompassErrorID;
import no.ovitas.compass2.exception.CompassException;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.fts.factory.QueryBuilderFactory;
import no.ovitas.compass2.model.Content;
import no.ovitas.compass2.fts.model.LuceneHit;
import no.ovitas.compass2.Constants;
import no.ovitas.compass2.fts.util.PagerHitCollector;
import no.ovitas.compass2.model.DocumentDetails;
import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.model.FullTextSearchConfig;
import no.ovitas.compass2.search.FullTextFieldCriteria;
import no.ovitas.compass2.search.FullTextQuery;
import no.ovitas.compass2.search.FullTextQueryResult;
import no.ovitas.compass2.search.FullTextTopicQuery;
import no.ovitas.compass2.search.impl.FullTextQueryResultImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.StaleReaderException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * @class LuceneIndexerImpl
 * @project compass2-fts-lucene
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.07.28.
 * 
 */
public class LuceneIndexerImpl implements FTSIndexer {

	private static final String INDEXDIRECTORY_PATH = "indexdirectory-path";
	private IndexWriter indexWriter;
	private String indexDirectory;

	@SuppressWarnings("unused")
	private ConfigurationManager configuration;
	private static final Version LUCENEVERSION = Version.LUCENE_30;
	private Log log = LogFactory.getLog(getClass());

	/**
	 * Initalize the indexer object.
	 * 
	 * @param indexDir
	 *            - The indexer directory.
	 */
	public void init(Properties properties) throws ConfigurationException {

		if (properties.getProperty(INDEXDIRECTORY_PATH) != null) {

			indexDirectory = properties.getProperty(INDEXDIRECTORY_PATH);

		} else {
			throw new ConfigurationException("IndexDirectory does not set!");
		}

	}

	public void openIndexer(boolean reWrite) throws ConfigurationException {
		Directory dir;
		try {
			dir = FSDirectory.open(new File(indexDirectory), null);

			indexWriter = new IndexWriter(dir, new StandardAnalyzer(
					LUCENEVERSION), reWrite, MaxFieldLength.UNLIMITED);
		} catch (IOException e) {
			throw new ConfigurationException(
					"Error occured when try to open index writer!");
		}
	}

	/**
	 * Index the content.
	 * 
	 * @param content
	 */
	public void index(Content content) {

		Document doc = new Document();

		doc.add(new Field(Constants.ID_INDEX, UUID.randomUUID().toString(),
				Field.Store.YES, Field.Index.NOT_ANALYZED));
		Field field = null;
		for (String fieldName : content.getContentStoredFieldNames()) {
			switch (content.getType(fieldName)) {
			case DATE:
				field = new Field(fieldName, DateTools.dateToString(
						(Date) content.getValue(fieldName),
						Resolution.MILLISECOND), Store.valueOf(content
						.getStored(fieldName)), Index.valueOf(content
						.getIndexed(fieldName)));
				break;
			default:
				field = new Field(fieldName, content.getValue(fieldName)
						.toString(),
						Store.valueOf(content.getStored(fieldName)),
						Index.valueOf(content.getIndexed(fieldName)));
				break;
			}
			if (field != null) {
				doc.add(field);
			}
		}

		try {
			indexWriter.addDocument(doc);

			indexWriter.commit();
			indexWriter.optimize();
		} catch (CorruptIndexException e) {
			throw new CompassException(CompassErrorID.FTS_INDEX_ERROR,
					"Error occured when index the document!", e);
		} catch (IOException e) {
			throw new CompassException(CompassErrorID.FTS_INDEX_ERROR,
					"Error occured when index the document!", e);
		}
	}

	public String getIndexDirectory() {
		return indexDirectory;
	}

	public void setIndexDirectory(String indexDirectory) {
		this.indexDirectory = indexDirectory;
	}

	/**
	 * Close the indexer
	 */
	public void closeIndexer() {
		try {
			indexWriter.commit();
			indexWriter.optimize();
			indexWriter.close();
		} catch (IOException e) {
			throw new CompassException(
					"Error occured when close index repository: "
							+ indexDirectory, e);
		}
	}

	/**
	 * Delete an indexed document from lucene repository.
	 */
	public void deleteDocument(String docId, String uri) {
		if (indexWriter != null) {
			Term t[] = null;
			if (docId != null && !docId.isEmpty() && uri != null
					&& !uri.isEmpty()) {
				t = new Term[2];
				t[0] = new Term("ID", docId);
				t[1] = new Term("URI", uri);
			} else {
				if (docId != null && !docId.isEmpty()) {
					t = new Term[1];
					t[0] = new Term("ID", docId);
				}
				if (uri != null && !uri.isEmpty()) {
					t = new Term[1];
					t[0] = new Term("URI", uri);

				}

			}
			if (t != null) {
				try {
					indexWriter.deleteDocuments(t);
					indexWriter.optimize();
					indexWriter.commit();
				} catch (CorruptIndexException e) {
					throw new CompassException(
							CompassErrorID.FTS_INDEX_DELETE_ERROR,
							"Error occured when deleting document: " + t, e);
				} catch (IOException e) {
					throw new CompassException(
							CompassErrorID.FTS_INDEX_DELETE_ERROR,
							"Error occured when deleting document: " + t, e);
				}
			}
		}
	}

	@Override
	public FullTextQueryResult doSearch(FullTextQuery fullTextQuery,
			FullTextTopicQuery fullTextTopicQuery) {

		IndexSearcher searcher = null;
		try {
			searcher = new IndexSearcher(FSDirectory.open(new File(
					this.indexDirectory)));
		} catch (Exception e) {
			throw new CompassException("Can not connect to index repository: "
					+ this.indexDirectory, e);
		}

		QueryBuilder queryBuilder = QueryBuilderFactory.getInstance()
				.getQueryBuilder();

		queryBuilder.startCreateQuery();

		createQuery(fullTextQuery, fullTextTopicQuery, queryBuilder,
				searcher.getIndexReader());

		Query query = queryBuilder.getCreatedQuery();

		log.debug("Searched query is: " + query);

		FullTextQueryResultImpl result = new FullTextQueryResultImpl();

		try {
			PagerHitCollector phc = new PagerHitCollector(
					fullTextQuery.getMaxNumberOfHits());
			if (fullTextQuery.getResultThreshold() > 0.0) {
				phc.setResultThreshold(fullTextQuery.getResultThreshold());
			}

			try {
				searcher.search(query, phc);
				phc.runSorting();
			} catch (RuntimeException e) {
				throw new CompassException(CompassErrorID.FTS_SEARCH_ERROR,
						"Exception occured while performing search!", e);
			}

			if (!phc.getDocIds().isEmpty()) {
				List<Hit> hits = new ArrayList<Hit>();
				for (Integer docId : phc.getDocIds()) {
					try {
						hits.add(new LuceneHit(searcher.doc(docId.intValue()),
								phc.getScore(docId)));
					} catch (CompassException e) {
						log.error(e.getMessage(), e);
					}
				}
				result.setHits(hits);
			}

		} catch (IOException e) {
			throw new CompassException(CompassErrorID.FTS_SEARCH_ERROR,
					"Exception occured while performing search!", e);
		}

		return result;
	}

	private void createQuery(FullTextQuery fullTextQuery,
			FullTextTopicQuery fullTextTopicQuery, QueryBuilder queryBuilder,
			IndexReader indexReader) {

		Collection<FullTextFieldCriteria> criterias = fullTextQuery
				.getCriterias();

		boolean fuzzy = fullTextQuery.isFuzzySearch();

		for (FullTextFieldCriteria fullTextFieldCriteria : criterias) {
			queryBuilder.addCriteria(fullTextFieldCriteria, fullTextTopicQuery,
					fuzzy, indexReader);
		}

	}

	private IndexSearcher getIndexSearcher() {
		IndexSearcher searcher = null;

		try {
			searcher = new IndexSearcher(FSDirectory.open(new File(
					indexDirectory)));
		} catch (CorruptIndexException e) {
			throw new CompassException("Can not connect to index repository: "
					+ this.indexDirectory, e);
		} catch (IOException e) {
			throw new CompassException("Can not connect to index repository: "
					+ this.indexDirectory, e);
		}
		return searcher;
	}

	/**
	 * Get the document from the lucene.
	 * 
	 * @param id
	 * @return
	 */
	public DocumentDetails getDocument(String id) {

		IndexSearcher searcher = getIndexSearcher();

		if (searcher != null) {

			Query q = new TermQuery(new Term("ID", id));

			try {
				searcher.search(q, 1);
			} catch (IOException e) {
				throw new CompassException(
						CompassErrorID.FTS_GET_DOCUMENT_ERROR,
						"Exception occured when try to getDocument: " + id, e);
			}
		}
		return null;
	}

	@Override
	public void setConfiguration(ConfigurationManager manager)
			throws ConfigurationException {
		configuration = manager;
	}

	@Override
	public void updateIndex(Content content) throws ConfigurationException {
		deleteDocument((String) content.getValue(Constants.URI_INDEX));

		index(content);
	}

	@Override
	public void deleteDocument(String uri) {
	
		try {
			indexWriter.deleteDocuments(new Term(Constants.URI_INDEX,
					uri));
			
		} catch (StaleReaderException e) {
			throw new CompassException(CompassErrorID.FTS_INDEX_DELETE_ERROR,
					"Error occured when deleting document: " + uri, e);
		} catch (CorruptIndexException e) {
			throw new CompassException(CompassErrorID.FTS_INDEX_DELETE_ERROR,
					"Error occured when deleting document: " + uri, e);
		} catch (LockObtainFailedException e) {
			throw new CompassException(CompassErrorID.FTS_INDEX_DELETE_ERROR,
					"Error occured when deleting document: " + uri, e);
		} catch (IOException e) {
			throw new CompassException(CompassErrorID.FTS_INDEX_DELETE_ERROR,
					"Error occured when deleting document: " + uri, e);
		}

	}

}
