/**
 * 
 */
package no.ovitas.compass2.config.settings;

/**
 * @class SuggestionProvider
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.09.20.
 * 
 */
public class SuggestionProvider extends SubPlugin {
	
	@Override
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDump = ind + "SuggestionProvider:\n";
		toDump += ind + super.dumpOut(ind) + "\n";
		
		return toDump;
	}

	/* (non-Javadoc)
	 * @see no.ovitas.compass2.config.settings.Plugin#getPluginType()
	 */
	@Override
	public PluginType getPluginType() {
		return PluginType.SUGGESTION_PROVIDER;
	}

}
