package no.ovitas.compass2.config.settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration container for element <code>import</code>.
 * 
 * @author Csaba Daniel
 */
public class ImportPlugin implements Plugin {

	private List<Import> imports = new ArrayList<Import>();

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
		buffer.append("Import: \n");
		
		for (Import importPlugin : imports) {
			
			buffer.append(ind + importPlugin.dumpOut(ind) + "\n");
		}

		return buffer.toString();
	}

	@Override
	public PluginType getPluginType() {
		// TODO Auto-generated method stub
		return PluginType.IMPORT_MAIN_PLUGIN;
	}
	
	public void addImport(Import importP) {
		imports.add(importP);
	}

	/**
	 * This is a getter method for imports.
	 * @return the imports
	 */
	public List<Import> getImports() {
		return imports;
	}

	/**
	 * This is a setter method for imports.
	 * @param imports the imports to set
	 */
	public void setImports(List<Import> imports) {
		this.imports = imports;
	}
	
	
}
