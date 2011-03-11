package no.ovitas.compass2.config.settings;


/**
 * @author csanyi
 * 
 */
public class LanguageTool extends SubPlugin{

	// Methods
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "LanguageToolsImplementation:\n";
		toDumpOut += super.dumpOut(ind);
		
		return toDumpOut;
	}

	@Override
	public PluginType getPluginType() {
		// TODO Auto-generated method stub
		return PluginType.LANGUAGE_TOOL_PLUGIN;
	}
}
