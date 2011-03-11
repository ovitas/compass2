package no.ovitas.compass2.model.knowledgebase;


/**
 * A class implementing this interface represents a Compass2 knowledge base
 * descriptor.
 * 
 * @author Csaba Daniel
 */
public interface KnowledgeBaseDescriptor {

	/**
	 * Get the knowledgeBase id.
	 * 
	 * @return
	 */
	public long getId();

	/**
	 * Get the display name of the knowledge base.
	 * 
	 * @return the display name of the knowledge base
	 */
	public String getDisplayName();

	/**
	 * Set the display name of the knowledge base.
	 * 
	 * @param displayName
	 */
	public void setDisplayName(String displayName);

	/**
	 * Get the description of the knowledge base.
	 * 
	 * @return the knowledge base description
	 */
	public String getDescription();

	/**
	 * Set the description of the knowledge base.
	 * 
	 * @param description
	 */
	public void setDescription(String description);

	/**
	 * Get the type of the knowledge base.
	 * 
	 * @return the type of the knowledge base
	 */
	public KnowledgeBaseType getType();

	/**
	 * Set the type of the knowledge base.
	 * 
	 * @param type
	 *            the type of the knowledge base
	 */
	public void setType(KnowledgeBaseType type);
}
