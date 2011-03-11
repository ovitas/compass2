/**
 * 
 */
package no.ovitas.compass2.config.settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author gyalai
 *
 */
public class SearchSettings implements Setting {

	private List<KnowldegeBaseSetting> knowledgeBaseSettings = new ArrayList<KnowldegeBaseSetting>(1);
	
	@Override
	public String dumpOut(String indent) {
		String ind = indent + " ";
		StringBuffer buffer = new StringBuffer(ind);
		buffer.append("SearchSettings: \n");
		
		for (KnowldegeBaseSetting setting  : knowledgeBaseSettings) {
			buffer.append(ind);
			buffer.append(setting.dumpOut(ind));
			buffer.append("\n");
		}
		
		return buffer.toString();
	}
	
	public void addKnowlegdeBaseSetting(KnowldegeBaseSetting knowldegeBaseSetting) {
		knowledgeBaseSettings.add(knowldegeBaseSetting);
	}
	
	public Collection<KnowldegeBaseSetting> getKnowledgeBaseSettings() {
		return knowledgeBaseSettings;
	}
	
}
