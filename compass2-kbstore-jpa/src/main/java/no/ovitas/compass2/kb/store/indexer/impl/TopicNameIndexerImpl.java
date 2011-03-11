/**
 * 
 */
package no.ovitas.compass2.kb.store.indexer.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.exception.CompassException;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.kb.KnowledgeBase;
import no.ovitas.compass2.kb.TopicNameIndexer;
import no.ovitas.compass2.model.MatchTopicResult;
import no.ovitas.compass2.model.FullTextSearchConfig;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseConfig;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.Relation;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.model.knowledgebase.Topic;
import no.ovitas.compass2.model.knowledgebase.TopicName;

/**
 * @author gyalai
 * 
 */
public class TopicNameIndexerImpl implements TopicNameIndexer {

	private ConfigurationManager manager;
	private static final String INDEXDIRECTORY_PATH = "indexdirectory-path";
	private static final Version LUCENEVERSION = Version.LUCENE_30;
	private String indexDirectory;

	private Log log = LogFactory.getLog(getClass());

	public void setConfiguration(ConfigurationManager manager)
			throws ConfigurationException {
		this.manager = manager;
	}

	public void init(Properties properties) throws ConfigurationException {
		if (properties.getProperty(INDEXDIRECTORY_PATH) != null) {

			indexDirectory = properties.getProperty(INDEXDIRECTORY_PATH);

		} else {
			throw new ConfigurationException("IndexDirectory does not set!");
		}
	}

	public void saveKnowledgeBase(KnowledgeBase knowledgeBase)
			throws IOException {
		Directory dir = FSDirectory.open(new File(indexDirectory), null);

		IndexWriter indexWriter = new IndexWriter(dir, new StandardAnalyzer(
				LUCENEVERSION), MaxFieldLength.UNLIMITED);

		Collection<Topic> topics = knowledgeBase.getTopics();

		long knowledgeBaseID = knowledgeBase.getKnowledgeBaseDescriptor()
				.getId();

		Document doc;

		for (Topic topic : topics) {
			for (TopicName topicName : topic.getNames()) {
				doc = new Document();
				doc.add(new Field(Constants.TOPIC_ID, Long.toString(topic
						.getId()), Field.Store.YES,
						Field.Index.NOT_ANALYZED_NO_NORMS));
				doc.add(new Field(Constants.TOPIC_NAME, topicName.getName(),
						Field.Store.YES, Field.Index.ANALYZED));
				doc.add(new Field(Constants.KNOWLEDGE_BASE_ID, Long
						.toString(knowledgeBaseID), Field.Store.YES,
						Field.Index.NOT_ANALYZED_NO_NORMS));

				indexWriter.addDocument(doc);
			}
		}

		indexWriter.commit();
		indexWriter.optimize();

		indexWriter.close();
		dir.close();

	}

	public MatchTopicResult getRelatedResults(Collection<String> terms,
			long knowledgeBaseID, int maxTopicNumberToExpand,
			boolean perfixMatch) throws IOException {
		Directory dir = FSDirectory.open(new File(indexDirectory), null);

		IndexSearcher searcher = new IndexSearcher(dir, true);

		MaxHitTopicCollector collector = new MaxHitTopicCollector(
				maxTopicNumberToExpand);

		Query query;
		for (String term : terms) {
			collector.setTerm(term);
			query = createQuery(term, perfixMatch, knowledgeBaseID, searcher.getIndexReader());

			searcher.search(query, collector);
		}

		return collector.getResult(searcher);
	}

	private Query createQuery(String term, boolean perfixMatch, long kbID, IndexReader indexReader) {

		BooleanQuery rootQuery = new BooleanQuery();
		Query termQuery;

		String[] split = term.split(" ");

		LinkedList<Term> terms = new LinkedList<Term>();

		for (String termString : split) {
			terms.add(new Term(Constants.TOPIC_NAME, termString));
		}

		if (terms.size() == 1) {

			if (perfixMatch) {
				termQuery = new PrefixQuery(terms.getFirst());
			} else {
					termQuery = new TermQuery(terms.getFirst());
			}
		} else {
			MultiPhraseQuery mp = new MultiPhraseQuery();
			
			Iterator<Term> iterator = terms.iterator();
			Term termValue;
			
			ArrayList<Term> subTerms = new ArrayList<Term>();
			
			while (iterator.hasNext()) {
				termValue = iterator.next();
				
				if (perfixMatch && !iterator.hasNext()) {
					try {
						TermEnum termEnum = indexReader.terms(termValue);
						do {							
							if (termEnum.term() == null) {
								break;
							}
							subTerms.add(termEnum.term());
						} while(!termEnum.next());
						
						mp.add(subTerms.toArray(new Term[subTerms.size()]));
					} catch (IOException e) {
						throw new CompassException("Index reader can't read the repository!", e);
					}
				} else {
					mp.add(termValue);
				}
				
				
			}
			
			termQuery = mp;
			
		}
		rootQuery.add(termQuery, Occur.MUST);

		Query kbIdQuery = new TermQuery(new Term(Constants.KNOWLEDGE_BASE_ID,
				Long.toString(kbID)));

		rootQuery.add(kbIdQuery, Occur.MUST);

		return rootQuery;
	}

	public void removeKnowledgeBase(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		Directory dir;
		try {
			dir = FSDirectory.open(new File(indexDirectory), null);

			IndexSearcher searcher = new IndexSearcher(dir, false);

			IndexReader indexReader = searcher.getIndexReader();
			int deleteDocuments = indexReader.deleteDocuments(new Term(
					Constants.KNOWLEDGE_BASE_ID, Long
							.toString(knowledgeBaseDescriptor.getId())));

			indexReader.close();
			dir.close();
			log.info("Deleted documents: " + deleteDocuments);
		} catch (IOException e) {
			log.error("Error occured when remove indexed elements", e);
		}
	}

}
