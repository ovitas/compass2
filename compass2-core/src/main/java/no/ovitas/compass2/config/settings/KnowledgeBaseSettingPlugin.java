package no.ovitas.compass2.config.settings;


/**
 * @author csanyi
 * 
 */
public class KnowledgeBaseSettingPlugin extends SubPlugin {

	// Attributes
	private String id;
	
	private TopicNameIndexerSetting topicNameIndexerSetting;
	
	private SearchSettings searchSettings;
	
	// Methods
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "KnowledgeBaseImplementation\n";
		toDumpOut += ind + "ID: " + id + "\n";
		toDumpOut += ind + topicNameIndexerSetting.dumpOut(ind) + "\n";
		if (searchSettings != null)
			toDumpOut += ind + searchSettings.dumpOut(ind) + "\n";
		toDumpOut += super.dumpOut(ind);
		
		return toDumpOut;
	}

	@Override
	public PluginType getPluginType() {
		// TODO Auto-generated method stub
		return PluginType.KNOWLEDGE_BASE_PLUGIN;
	}

	/**
	 * This is a getter method for id.
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * This is a setter method for id.
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public TopicNameIndexerSetting getTopicNameIndexerSetting() {
		return topicNameIndexerSetting;
	}

	public void setTopicNameIndexerSetting(
			TopicNameIndexerSetting topicNameIndexerSetting) {
		this.topicNameIndexerSetting = topicNameIndexerSetting;
	}

	public SearchSettings getSearchSettings() {
		return searchSettings;
	}

	public void setSearchSettings(SearchSettings searchSettings) {
		this.searchSettings = searchSettings;
	}
	
}
