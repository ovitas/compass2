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
	protected int from;
	protected int to;
	protected int hitCounter;
	protected Map<Integer, Float> scores;
	
	public float getScore(int docId){
		Float f = scores.get(docId);
		if(f!=null) return f.floatValue();
		return -1.0F;
		
	}
	
	public PagerHitCollector(int from, int to){
		docIds = new ArrayList<Integer>();
		scores = new HashMap<Integer,Float>();
		this.from = from;
		this.to = to;
		hitCounter = 0;
	}
	
	public void collect(int id, float score) {
      hitCounter++;
      if(hitCounter>=from && hitCounter <=to){
    	  docIds.add(id);
    	  scores.put(id, score);
      }
      if(hitCounter>to){
    	  throw new RuntimeException("Pagination finished");
      }
	}

	public List<Integer> getDocIds() {
		return docIds;
	}

}
