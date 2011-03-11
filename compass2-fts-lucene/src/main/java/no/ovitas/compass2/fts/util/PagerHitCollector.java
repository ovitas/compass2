/**
 * 
 */
package no.ovitas.compass2.fts.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import no.ovitas.compass2.util.XPair;
import no.ovitas.compass2.util.XPairComparator;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;

/**
 * @author magyar
 *
 */
public class PagerHitCollector extends Collector {

	protected List<Integer> docIds;
	protected int hitCounter;
	protected double resultThreshold;
	protected Map<Integer, Float> scores;
	protected int maxNumberOfHits;
	protected List<XPair<Float,Integer>> scoreList;
	private Scorer scorer;
	private int docBase;
	
	public float getScore(int docId){
		Float f = scores.get(docId);
		if(f!=null) return f.floatValue();
		return -1.0F;
		
	}
	
	public PagerHitCollector(int maxNumberOfHits){
		docIds = new LinkedList<Integer>();
		scores = new HashMap<Integer,Float>();
		hitCounter = 0;
		this.maxNumberOfHits = maxNumberOfHits; 
		resultThreshold = 0.0;
		scoreList = new ArrayList<XPair<Float,Integer>>();
	}
	
	public List<Integer> getDocIds() {
		return docIds;
	}

	public double getResultThreshold() {
		return resultThreshold;
	}

	public void setResultThreshold(double resultThreshold) {
		this.resultThreshold = resultThreshold;
	}

	public int getHitCounter() {
		return hitCounter;
	}
	
	/**
	 * Sort all of the docId-s by score value
	 */
	public void runSorting(){
		
		int limit = maxNumberOfHits;

		// Sort keys by score
		Collections.sort(scoreList, new XPairComparator());

		for (XPair<Float, Integer> pair : scoreList) {
			if (limit > 0) {
				docIds.add(pair.getValue());
				scores.put(pair.getValue(), pair.getKey());
				limit--;
			} else break;
		}

	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return false;
	}

	@Override
	public void collect(int doc) throws IOException {
		hitCounter++;
		System.out.println("Collect: " + doc);
	      double score = scorer.score();
		if(resultThreshold <=score ){
	    	  scoreList.add(new XPair<Float,Integer>(new Float(score), new Integer(doc + docBase)));
	      }
	}

	@Override
	public void setNextReader(IndexReader reader, int docBase)
			throws IOException {
				this.docBase = docBase;		
	}

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;
	}

}
