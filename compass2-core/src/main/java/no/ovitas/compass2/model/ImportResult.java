package no.ovitas.compass2.model;

import java.util.List;

import no.ovitas.compass2.kb.KnowledgeBase;

/**
 * A class implementing this interface represents the result of a Compass2
 * knowledge base import. The result consist of the imported knowledge base
 * model and the possible warning messages raised when a minor, non-blocking
 * issue occured during the input.
 * 
 * @author Csaba Daniel
 */
public interface ImportResult {

	/**
	 * Get the factory object that was used during import to create the model
	 * elements.
	 * 
	 * @return the element creator factory
	 */
	public KnowledgeBase getEntityFactory();

	/**
	 * Get the list of the warning messages raised during the import.
	 * 
	 * @return the warning message list
	 */
	public List<String> getWarningMessages();
}
