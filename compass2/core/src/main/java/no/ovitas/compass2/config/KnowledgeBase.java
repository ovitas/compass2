package no.ovitas.compass2.config;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class KnowledgeBase extends BaseConfigItem {

	// Attributes

	private Logger logger = Logger.getLogger(this.getClass());
	private KnowledgeBaseImplementation knowledgeBaseImplementation;
	private Expansion expansion;

	// Getter / setter methods

	public KnowledgeBaseImplementation getKnowledgeBaseImplementation() {
		return knowledgeBaseImplementation;
	}

	public void setKnowledgeBaseImplementation(
			KnowledgeBaseImplementation knowledgeBaseImplementation) {
		this.knowledgeBaseImplementation = knowledgeBaseImplementation;
	}

	public Expansion getExpansion() {
		return expansion;
	}

	public void setExpansion(Expansion expansion) {
		this.expansion = expansion;
	}

	// Constructors

	public KnowledgeBase() {
		super();
	}

	// Methods
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "KnowledgeBase\n";
		toDumpOut += ind + knowledgeBaseImplementation.dumpOut(" ") + "\n";
		toDumpOut += ind + expansion.dumpOut(ind) + "\n";
		
		return toDumpOut;
	}
}
