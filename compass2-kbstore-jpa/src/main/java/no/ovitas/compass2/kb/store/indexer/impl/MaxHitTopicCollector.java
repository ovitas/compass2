/**
 * 
 */
package no.ovitas.compass2.kb.store.indexer.impl;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.kb.store.util.CollectorStoreModel;
import no.ovitas.compass2.model.MatchTopicResult;
import no.ovitas.compass2.util.XPair;
import no.ovitas.compass2.util.XPairComparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Scorer;

/**
 * @author gyalai
 * 
 */
public class MaxHitTopicCollector extends Collector {

	private int docBase;
	private Scorer scorer;
	private final int maxTopicNumberToExpand;
	private PriorityQueue<CollectorStoreModel> queue;
	private int queueSize = 0;

	private Log log = LogFactory.getLog(getClass());
	private String term;

	public MaxHitTopicCollector(int maxTopicNumberToExpand) {
		this.maxTopicNumberToExpand = maxTopicNumberToExpand;
		queue = new PriorityQueue<CollectorStoreModel>(10,
				new Comparator<CollectorStoreModel>() {

					public int compare(CollectorStoreModel arg0,
							CollectorStoreModel arg1) {
						Double o1Value = arg0.getScore();
						Double o2Value = arg1.getScore();
						return o2Value.compareTo(o1Value);
					}
				});
	}

	public void setTerm(String term) {
		this.term = term;
	}

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;
	}

	@Override
	public void collect(int doc) throws IOException {
		double score = scorer.score();

		if (queueSize < maxTopicNumberToExpand) {
			queue.add(new CollectorStoreModel(term, score, doc + docBase));
		}
	}

	@Override
	public void setNextReader(IndexReader reader, int docBase)
			throws IOException {
		this.docBase = docBase;
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return false;
	}

	public MatchTopicResult getResult(IndexSearcher searcher)
			throws CorruptIndexException, IOException {

		MatchTopicResult resultObject = new MatchTopicResult();
		CollectorStoreModel pair;
		Document doc;
		long topicId;
		String topicIdStr;
		long topicNameID = 0;
		String topicNameStr;
		String term;
		int actSize;
		while (!queue.isEmpty() && resultObject.getSize() < maxTopicNumberToExpand) {
			pair = queue.poll();

			doc = searcher.doc(pair.getDocumentID());
			term = pair.getTerm();

			topicIdStr = doc.get(Constants.TOPIC_ID);
			if (topicIdStr != null) {
				try {
					topicId = Long.parseLong(topicIdStr);
					resultObject.addTopicIDToTerm(term, topicId);
				} catch (NumberFormatException e) {
					log.error("Can not parse to long: " + topicIdStr, e);
					continue;
				}
			}
		}

		return resultObject;
	}

}
