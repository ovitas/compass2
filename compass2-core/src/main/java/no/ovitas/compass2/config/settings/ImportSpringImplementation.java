package no.ovitas.compass2.config.settings;

/**
 * Configuration container for element <code>import-spring-implementation</code>
 * .
 * 
 * @author Csaba Daniel
 */
public class ImportSpringImplementation {

	private String type;
	private String beanName;
	private ContextFileContainer contextFiles;
	private ParamContainer params;

	/**
	 * Get the type name of the file this importer can import.
	 * 
	 * @return the file type name
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the type name of the file this importer can import.
	 * 
	 * @param type
	 *            the file type name to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Get the name of the bean that implements this importer.
	 * 
	 * @return the implementor bean name
	 */
	public String getBeanName() {
		return beanName;
	}

	/**
	 * Set the name of the bean that implements this importer.
	 * 
	 * @param beanName
	 *            the implementor bean name to set
	 */
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	/**
	 * Get the context file settings for this importer.
	 * 
	 * @return the context file settings
	 */
	public ContextFileContainer getContextFiles() {
		return contextFiles;
	}

	/**
	 * Set the context file settings for this importer.
	 * 
	 * @param contextFiles
	 *            the context file settings to set
	 */
	public void setContextFiles(ContextFileContainer contextFiles) {
		this.contextFiles = contextFiles;
	}

	/**
	 * Get the configuration parameters for this importer.
	 * 
	 * @return the configuration parameters
	 */
	public ParamContainer getParams() {
		return params;
	}

	/**
	 * Set the configuration parameters for this importer.
	 * 
	 * @param params
	 *            the configuration parameters to set
	 */
	public void setParams(ParamContainer params) {
		this.params = params;
	}

	/**
	 * Return the indented <code>String</code> representation of this object.
	 * 
	 * @param indent
	 *            the indent to use for stringifying this object
	 * @return the <code>String</code> representation of the object
	 */
	public String dumpOut(String indent) {
		StringBuffer buffer = new StringBuffer();
		String ind = indent + " ";

		buffer.append(ind);
		buffer.append("ImportSpringImplementation:");
		buffer.append(" type: ");
		buffer.append(type);
		buffer.append("\n");
		buffer.append(ind);
		buffer.append("bean: ");
		buffer.append(beanName);
		buffer.append("\n");
		buffer.append(ind);
		if (contextFiles != null) {
			buffer.append(contextFiles.dumpOut(indent));
			buffer.append("\n");
			buffer.append(ind);
		}
		if (params != null) {
			buffer.append(params.dumpOut(indent));
		}

		return buffer.toString();
	}
}
