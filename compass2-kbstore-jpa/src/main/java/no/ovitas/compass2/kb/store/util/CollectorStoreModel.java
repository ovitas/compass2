/**
 * 
 */
package no.ovitas.compass2.kb.store.util;

/**
 * @author gyalai
 *
 */
public class CollectorStoreModel {
	
	private final String term;
	private final Double score;
	private final Integer documentID;

	public CollectorStoreModel(String term, Double score, Integer documentID) {
		this.term = term;
		this.score = score;
		this.documentID = documentID;
		
	}

	public String getTerm() {
		return term;
	}

	public Double getScore() {
		return score;
	}

	public Integer getDocumentID() {
		return documentID;
	}
	
	

}
