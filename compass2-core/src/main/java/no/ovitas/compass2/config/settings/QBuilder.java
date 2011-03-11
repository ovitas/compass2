/**
 * 
 */
package no.ovitas.compass2.config.settings;

/**
 * @class QueryBuilder
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.09.25.
 * 
 */
public class QBuilder extends SubPlugin {

	@Override
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDump = ind + "QueryBuilder:\n";
		toDump += ind + super.dumpOut(ind) + "\n";
		
		return toDump;
	}
	
	/* (non-Javadoc)
	 * @see no.ovitas.compass2.config.settings.Plugin#getPluginType()
	 */
	@Override
	public PluginType getPluginType() {
		return PluginType.QUERY_BUILDER_PLUGIN;
	}

}
