package no.ovitas.compass2.config.settings;


/**
 * @author csanyi
 * 
 */
public class ContentHandler extends SubPlugin{

	// Attributes
	private DocumentFieldContainer fields;
	
	private String type;
	
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof ContentHandler) {
			if (type != null &&  obj != null && type.equals(((ContentHandler) obj).getType())) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		if (type != null) {
			return type.hashCode();
		}
		return 0;
	}
	
	// Methods
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind;
		toDumpOut += "ContentHandlerImplementation:\n";
		toDumpOut += super.dumpOut(ind);
		toDumpOut += ind + "Type: " + type;
		toDumpOut += fields.dumpOut(ind);
		
		return toDumpOut;
	}

	@Override
	public PluginType getPluginType() {
		// TODO Auto-generated method stub
		return PluginType.CONTENT_HANDLER_PLUGIN;
	}

	/**
	 * This is a getter method for fields.
	 * @return the fields
	 */
	public DocumentFieldContainer getFields() {
		return fields;
	}

	/**
	 * This is a setter method for fields.
	 * @param fields the fields to set
	 */
	public void setFields(DocumentFieldContainer fields) {
		this.fields = fields;
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
