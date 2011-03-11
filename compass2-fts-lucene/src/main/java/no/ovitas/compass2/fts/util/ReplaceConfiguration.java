/**
 * 
 */
package no.ovitas.compass2.fts.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gyalai
 *
 */
public class ReplaceConfiguration {

	private List<ReplaceSetting> replaces = new ArrayList<ReplaceSetting>(0);
	private List<ReplaceSetting> addReplaces = new ArrayList<ReplaceSetting>(0);
	private List<ReplaceSetting> updateReplaces = new ArrayList<ReplaceSetting>(0);
	private List<ReplaceSetting> deleteReplaces = new ArrayList<ReplaceSetting>(0);

	public void addReplaceSetting(ReplaceSetting setting) {
		replaces.add(setting);
	}
	
	public List<ReplaceSetting> getReplaces() {
		return replaces;
	}
	
	public void postProcess() {
		for (ReplaceSetting replace : replaces) {
			switch (replace.getScopeEnum()) {
			case ADD:
				addReplaces.add(replace);
				break;
			case DELETE:
				deleteReplaces.add(replace);
				break;
			case UPDATE:
				updateReplaces.add(replace);
				break;
			}
		}
	}

	public List<ReplaceSetting> getAddReplaces() {
		return addReplaces;
	}

	public List<ReplaceSetting> getUpdateReplaces() {
		return updateReplaces;
	}

	public List<ReplaceSetting> getDeleteReplaces() {
		return deleteReplaces;
	}
	
	public List<ReplaceSetting> getReplaceSettings(Scope scope) {
		switch (scope) {
		case ADD:
			return addReplaces;
		case DELETE:
			return deleteReplaces;
		case UPDATE:
			return updateReplaces;
		}
		
		return new ArrayList<ReplaceSetting>(0);
	}
		
}
