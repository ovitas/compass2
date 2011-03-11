package no.ovitas.compass2.config.settings;

/**
 * @author csanyi
 * 
 */
public class FullTextSearch extends SubPlugin {

	// Methods
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "FullTextSearchImplementation:\n";
		toDumpOut += super.dumpOut(ind);
		
		return toDumpOut;
	}

	@Override
	public PluginType getPluginType() {
		// TODO Auto-generated method stub
		return PluginType.FULL_TEXT_SEARCH_PLUGIN;
	}
}
