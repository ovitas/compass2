/**
 * 
 */
package no.ovitas.compass2.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author magyar
 *
 */
public class ResultObject implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected List<Hit> hits;
	protected List<Set<TopicTreeNode>> expansions;
	
	public List<Hit> getHits() {
		return hits;
	}
	
	public void setHits(List<Hit> hits) {
		this.hits = hits;
	}
	
	public ResultObject(List<Set<TopicTreeNode>> expansions, List<Hit> hits) {
		super();
		this.expansions = expansions;
		this.hits = hits;
	}
	
	public List<Set<TopicTreeNode>> getExpansions() {
		return expansions;
	}
	
	public void setExpansions(List<Set<TopicTreeNode>> expansions) {
		this.expansions = expansions;
	}
	
	/**
	 * @param hits
	 */
	public ResultObject(List<Hit> hits) {
		super();
		this.hits = hits;
	}
	
	/**
	 * 
	 */
	public ResultObject() {
		super();
		// TODO Auto-generated constructor stub
	}
}
