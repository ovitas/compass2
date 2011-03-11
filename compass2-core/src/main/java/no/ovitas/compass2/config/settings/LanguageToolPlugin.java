package no.ovitas.compass2.config.settings;

/**
 * @author csanyi
 * 
 */
public class LanguageToolPlugin implements Plugin{

	// Attributes
	private LanguageTool languageTool;


	// Methods
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "LangageTools\n";
		toDumpOut += ind + languageTool.dumpOut(ind) + "\n";
		
		return toDumpOut;
	}

	@Override
	public PluginType getPluginType() {
		// TODO Auto-generated method stub
		return PluginType.LANGUAGE_TOOL_MAIN_PLUGIN;
	}

	/**
	 * This is a getter method for languageTool.
	 * @return the languageTool
	 */
	public LanguageTool getLanguageTool() {
		return languageTool;
	}

	/**
	 * This is a setter method for languageTool.
	 * @param languageTool the languageTool to set
	 */
	public void setLanguageTool(LanguageTool languageTool) {
		this.languageTool = languageTool;
	}
	
	
}
