/**
 * 
 */
package no.ovitas.compass2.config.settings;

/**
 * @author gyalai
 *
 */
public class TopicNameIndexerSetting extends SubPlugin {

	@Override
	public PluginType getPluginType() {
		return PluginType.TOPIC_NAME_INDEXER_PLUGIN;
	}
	
	@Override
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "TopicNameIndexerSetting\n";
		toDumpOut += super.dumpOut(ind);
		
		return toDumpOut;
	}

}
