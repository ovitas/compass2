package no.ovitas.compass2.config.settings;


import java.util.ArrayList;
import java.util.List;


/**
 * @author csanyi
 * 
 */
public class KnowledgeBasePlugin implements Plugin {

	// Attributes
	
	private String defaultKnowledgeBaseID;
	
	private KnowledgeBaseSettingPlugin defaultKnowledgeBase;

	private List<KnowledgeBaseSettingPlugin> knowledgeBases = new ArrayList<KnowledgeBaseSettingPlugin>();
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "KnowledgeBases\n";
		toDumpOut += ind + "Default Knowledge base ID: " + defaultKnowledgeBaseID + "\n";
		toDumpOut += defaultKnowledgeBase.dumpOut(ind);
		toDumpOut += ind + "All known knowledge bases: \n";
		for (KnowledgeBaseSettingPlugin kb : knowledgeBases) {
			toDumpOut += kb.dumpOut(ind);
		}
		
		return toDumpOut;
	}

	@Override
	public PluginType getPluginType() {
		// TODO Auto-generated method stub
		return PluginType.KNOWLEDGE_BASE_MAIN_PLUGIN;
	}
	
	public void addKnowledgeBase(KnowledgeBaseSettingPlugin kb) {
		knowledgeBases.add(kb);
		
		if (kb.getId().equals(defaultKnowledgeBaseID)) {
			defaultKnowledgeBase = kb;
		}
	}

	/**
	 * This is a getter method for defaultKnowledgeBaseID.
	 * @return the defaultKnowledgeBaseID
	 */
	public String getDefaultKnowledgeBaseID() {
		return defaultKnowledgeBaseID;
	}

	/**
	 * This is a setter method for defaultKnowledgeBaseID.
	 * @param defaultKnowledgeBaseID the defaultKnowledgeBaseID to set
	 */
	public void setDefaultKnowledgeBaseID(String defaultKnowledgeBaseID) {
		this.defaultKnowledgeBaseID = defaultKnowledgeBaseID;
	}

	/**
	 * This is a getter method for defaultKnowledgeBase.
	 * @return the defaultKnowledgeBase
	 */
	public KnowledgeBaseSettingPlugin getDefaultKnowledgeBase() {
		return defaultKnowledgeBase;
	}

	/**
	 * This is a setter method for defaultKnowledgeBase.
	 * @param defaultKnowledgeBase the defaultKnowledgeBase to set
	 */
	public void setDefaultKnowledgeBase(
			KnowledgeBaseSettingPlugin defaultKnowledgeBase) {
		this.defaultKnowledgeBase = defaultKnowledgeBase;
	}

	/**
	 * This is a getter method for knowledgeBases.
	 * @return the knowledgeBases
	 */
	public List<KnowledgeBaseSettingPlugin> getKnowledgeBases() {
		return knowledgeBases;
	}

	/**
	 * This is a setter method for knowledgeBases.
	 * @param knowledgeBases the knowledgeBases to set
	 */
	public void setKnowledgeBases(List<KnowledgeBaseSettingPlugin> knowledgeBases) {
		this.knowledgeBases = knowledgeBases;
	}
			
}
