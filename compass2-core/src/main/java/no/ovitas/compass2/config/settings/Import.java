package no.ovitas.compass2.config.settings;

/**
 * Configuration container for element <code>import-implementation</code>.
 * 
 * @author Csaba Daniel
 */
public class Import extends SubPlugin{

	private String type;

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
		buffer.append("ImportImplementation:");
		buffer.append("\n");
		buffer.append(ind);
		buffer.append("Type: ");
		buffer.append(type);
		buffer.append("\n");
		

		buffer.append(super.dumpOut(ind));
		buffer.append("\n");
		buffer.append(ind);

		return buffer.toString();
	}

	@Override
	public PluginType getPluginType() {
		// TODO Auto-generated method stub
		return PluginType.IMPORT_PLUGIN;
	}

	/**
	 * This is a getter method for type.
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * This is a setter method for type.
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	
}
