/**
 * 
 */
package no.ovitas.compass2.config.settings;


/**
 * @class SuggestionProviderPlugin
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.09.20.
 * 
 */
public class SuggestionProviderPlugin implements Plugin {

	private SuggestionProvider suggestionProvider;
	
	/* (non-Javadoc)
	 * @see no.ovitas.compass2.config.settings.Setting#dumpOut(java.lang.String)
	 */
	@Override
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "SuggestionProviderPlugin:\n";
		
		if (suggestionProvider != null) {
			toDumpOut += ind + suggestionProvider.dumpOut(ind) + "\n";
		}
		
		return toDumpOut;
	}

	/* (non-Javadoc)
	 * @see no.ovitas.compass2.config.settings.Plugin#getPluginType()
	 */
	@Override
	public PluginType getPluginType() {
		return PluginType.SUGGESTION_PROVIDER_MAIN_PLUGIN;
	}

	/**
	 * This is a getter method for suggestionProvider.
	 * @return the suggestionProvider
	 */
	public SuggestionProvider getSuggestionProvider() {
		return suggestionProvider;
	}

	/**
	 * This is a setter method for suggestionProvider.
	 * @param suggestionProvider the suggestionProvider to set
	 */
	public void setSuggestionProvider(SuggestionProvider suggestionProvider) {
		this.suggestionProvider = suggestionProvider;
	}

}
