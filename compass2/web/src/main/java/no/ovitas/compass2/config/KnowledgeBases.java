package no.ovitas.compass2.config;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class KnowledgeBases {

	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());
	private ArrayList<KnowledgeBase> knowledgeBases;

	// Getter / setter methods

	public ArrayList<KnowledgeBase> getKnowledgeBases() {
		return knowledgeBases;
	}

	public void setKnowledgeBases(ArrayList<KnowledgeBase> knowledgeBases) {
		this.knowledgeBases = knowledgeBases;
	}

	// Constructors

	public KnowledgeBases() {}

	// Methods
}
