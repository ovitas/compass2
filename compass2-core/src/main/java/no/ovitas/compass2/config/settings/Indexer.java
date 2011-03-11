/**
 * 
 */
package no.ovitas.compass2.config.settings;

/**
 * @author gyalai
 *
 */
public class Indexer extends SubPlugin {
			
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind;
		toDumpOut += "IndexerImplementation:\n";
		toDumpOut += super.dumpOut(ind);
		return toDumpOut;
	}

	@Override
	public PluginType getPluginType() {
		// TODO Auto-generated method stub
		return PluginType.INDEXER_PLUGIN;
	}
	
}
