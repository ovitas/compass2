/**
 * 
 */
package no.ovitas.compass2.suggestion;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.exception.CompassException;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.kb.KnowledgeBase;
import no.ovitas.compass2.model.knowledgebase.Relation;
import no.ovitas.compass2.model.knowledgebase.Topic;
import no.ovitas.compass2.model.knowledgebase.TopicName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * @class SuggestionProviderIml
 * @project compass2-fts-lucene
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.09.21.
 * 
 */
public class SuggestionProviderManagerImpl implements SuggestionProviderManager {

	private static final String KNOWLEDGE_BASE = "knowledgeBase";
	private ConfigurationManager manager;
	private String indexDirectory;
	private static final String INDEXDIRECTORY_PATH = "indexdirectory-path";
	private static final Version LUCENEVERSION = Version.LUCENE_30;
	private static final String TOPIC = "topic";
	private Log log = LogFactory.getLog(getClass());

	@Override
	public void setConfiguration(ConfigurationManager manager)
			throws ConfigurationException {
		this.manager = manager;
	}

	@Override
	public void init(Properties properties) throws ConfigurationException {
		if (properties.getProperty(INDEXDIRECTORY_PATH) != null) {

			indexDirectory = properties.getProperty(INDEXDIRECTORY_PATH);

		} else {
			throw new ConfigurationException("IndexDirectory does not set!");
		}
	}

	@Override
	public Collection<String> getSuggestions(String word,
			int maxSuggestionNumber) {
		Directory dir;
		List<String> result = new LinkedList<String>();
		try {
			dir = FSDirectory.open(new File(indexDirectory), null);

			IndexSearcher searcher = new IndexSearcher(dir, true);

			Query query = new PrefixQuery(new Term(TOPIC, word));

			TopDocs hits = searcher.search(query, maxSuggestionNumber);

			Document doc;
			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				doc = searcher.doc(scoreDoc.doc);
				if (doc != null) {
					result.add(doc.get(TOPIC));
				}
			}

		} catch (IOException e) {
			throw new CompassException(
					"Can't open the index directory for suggestion provider.",
					e);
		}
		return result;
	}

	@Override
	public void indexKnowledgeBaseForSuggestion(KnowledgeBase knowledgeBase) {
		try {
			Directory dir = FSDirectory.open(new File(indexDirectory), null);

			IndexWriter indexWriter;
			indexWriter = new IndexWriter(dir, new StandardAnalyzer(
					LUCENEVERSION), MaxFieldLength.UNLIMITED);

			Collection<Topic> topics = knowledgeBase.getTopics();

			Document doc;
			
			removeExistedKnowledgeBase(indexWriter, knowledgeBase.getKnowledgeBaseDescriptor().getId());

			for (Topic topic : topics) {



				for (TopicName topicName : topic.getNames()) {
					
					doc = new Document();
					doc.add(new Field(TOPIC, topicName.getName(), Field.Store.YES,
							Field.Index.ANALYZED_NO_NORMS));
					doc.add(new Field(KNOWLEDGE_BASE, Long.toString(knowledgeBase.getKnowledgeBaseDescriptor().getId()), Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));

					indexWriter.addDocument(doc);
				}
				
			}

			indexWriter.commit();
			indexWriter.optimize();

			indexWriter.close();
		} catch (CorruptIndexException e) {
			throw new CompassException(
					"Can't index the knowledge base with suggestion provider.",
					e);
		} catch (LockObtainFailedException e) {
			throw new CompassException(
					"Can't index the knowledge base with suggestion provider.",
					e);
		} catch (IOException e) {
			throw new CompassException(
					"Can't index the knowledge base with suggestion provider.",
					e);
		}

	}

	private void removeExistedKnowledgeBase(IndexWriter indexWriter, long id) {
		try {
			indexWriter.deleteDocuments(new Term(KNOWLEDGE_BASE, Long.toString(id)));
		} catch (CorruptIndexException e) {
			log.error("Error occured when try to remove knowlegde base from suggestion repository!");
		} catch (IOException e) {
			log.error("Error occured when try to remove knowlegde base from suggestion repository!");
		}
		
	}

}
