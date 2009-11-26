/**
 * 
 */
package no.ovitas.compass2.util.lucene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ovitas.compass2.util.XPair;
import no.ovitas.compass2.util.XPairComparator;

import org.apache.lucene.search.HitCollector;

/**
 * @author magyar
 *
 */
public class PagerHitCollector extends HitCollector {

	protected List<Integer> docIds;
	protected int hitCounter;
	protected double resultThreshold;
	protected Map<Integer, Float> scores;
	protected int maxNumberOfHits;
	protected List<XPair<Float,Integer>> scoreList;
	
	public float getScore(int docId){
		Float f = scores.get(docId);
		if(f!=null) return f.floatValue();
		return -1.0F;
		
	}
	
	public PagerHitCollector(int maxNumberOfHits){
		docIds = new ArrayList<Integer>();
		scores = new HashMap<Integer,Float>();
		hitCounter = 0;
		this.maxNumberOfHits = maxNumberOfHits; 
		resultThreshold = 0.0;
		scoreList = new ArrayList<XPair<Float,Integer>>();
	}
	
	public void collect(int id, float score) {
      hitCounter++;
      if(resultThreshold <=score){
    	  scoreList.add(new XPair<Float,Integer>(new Float(score), new Integer(id)));
      }
     /* if((docIds.size() <=maxNumberOfHits) && resultThreshold <=score ){
    	  docIds.add(id);
    	  scores.put(id, score);
      }*/
      /*
      if(docIds.size()>maxNumberOfHits ){
    	  throw new RuntimeException("Pagination finished");
      }*/
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

}
