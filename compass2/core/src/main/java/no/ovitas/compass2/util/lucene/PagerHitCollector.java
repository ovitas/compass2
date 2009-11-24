/**
 * 
 */
package no.ovitas.compass2.util.lucene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	}
	
	public void collect(int id, float score) {
      hitCounter++;
      if((hitCounter <=maxNumberOfHits) && resultThreshold <=score ){
    	  docIds.add(id);
    	  scores.put(id, score);
      }
      if(hitCounter>maxNumberOfHits){
    	  throw new RuntimeException("Pagination finished");
      }
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

}
