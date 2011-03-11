package no.ovitas.compass2.model;

import java.util.ArrayList;
import java.util.List;

import no.ovitas.compass2.kb.KnowledgeBase;

/**
 * Implementation of the <code>ImportResultImpl</code> interface.
 * 
 * @author Csaba Daniel
 * 
 */
public class ImportResultImpl implements ImportResult {

	private KnowledgeBase factory;
	private List<String> warnings = new ArrayList<String>();

	@Override
	public KnowledgeBase getEntityFactory() {
		return factory;
	}

	/**
	 * Set the factory object that was used during import to create the model
	 * elements.
	 * 
	 * @param knowledgeBaseImpl
	 *            the result knowledge base to set
	 */
	public void setEntityFactory(KnowledgeBase factory) {
		this.factory = factory;
	}

	@Override
	public List<String> getWarningMessages() {
		return warnings;
	}

	/**
	 * Set the warning messages of the non-critical issues occured during
	 * importing a knowledge base definition.
	 * 
	 * @param warnings
	 *            the list of warning messages
	 */
	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	/**
	 * Add the specified warning message to the list of warning messages of this
	 * import.
	 * 
	 * @param warning
	 *            the warning message to add
	 */
	public void addWarning(String warning) {
		warnings.add(warning);
	}
}
