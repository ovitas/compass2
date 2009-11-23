package no.ovitas.compass2.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author csanyi
 * 
 */
public class KnowledgeBases {

	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());
	private Map<String, KnowledgeBase> knowledgeBases;
	private List<KnowledgeBase> temp;

	// Getter / setter methods

	public Map<String, KnowledgeBase> getKnowledgeBases() {
		return knowledgeBases;
	}

	public void addKnowledgeBase(KnowledgeBase kb){
		temp.add(kb);
	}

	// Constructors

	public KnowledgeBases() {
		temp = new ArrayList<KnowledgeBase>();
		this.knowledgeBases = Collections.synchronizedSortedMap(new TreeMap<String, KnowledgeBase>());
		
	}
	
	public void postProcess(){
		for(KnowledgeBase kb : temp){
			this.knowledgeBases.put(kb.getName(), kb);
		}
	}

	// Methods
}
