/**
 * 
 */
package no.ovitas.compass2.web.client.model;

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;

/**
 * @author gyalai
 * 
 */
public class KnowledgeBaseTreeModel extends SelectedTreeModel implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public int hashCode() {
		return (int) (getId() % 1000);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof KnowledgeBaseTreeModel) {
			if (getId() != null && obj != null && getId().equals(((KnowledgeBaseTreeModel) obj).getId())) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void add(ModelData child) {
		if (child instanceof ScopeTreeModel) {
			super.add(child);
		}
	}
	
	public Long getId() {
		return get(Compass2Constans.ID);
	}
	
	public void setId(Long id) {
		set(Compass2Constans.ID, id);
	}

	public String getDescription() {
		return get(Compass2Constans.DESCRIPTION);
	}
	
	public void setDescription(String description) {
		set(Compass2Constans.DESCRIPTION, description);
	}	
	
	public String getLabel() {
		return get(Compass2Constans.LABEL);
	}
	
	public void setLabel(String label) {
		set(Compass2Constans.LABEL, label);
	}
	
	public String getValue() {
		return get(Compass2Constans.VALUE);
	}
	
	public void setValue(String value) {
		set(Compass2Constans.VALUE, value);
	}
		
	public Boolean hasLoadedChild() {
		return get(Compass2Constans.HAS_LOADED_CHILD);
	}	
	
	public void setLoadedChild(Boolean loadedChild) {
		set(Compass2Constans.HAS_LOADED_CHILD, loadedChild);
	}
	
	public Boolean hasAheadTree() {
		return get(Compass2Constans.HAS_AHEAD_TREE);
	}
	
	public void setHasAheadTree(Boolean tree) {
		set(Compass2Constans.HAS_AHEAD_TREE, tree);
	}
	
	public Boolean isAheadTree() {
		return get(Compass2Constans.IS_AHEAD_TREE);
	}
	
	public void setAheadTree(Boolean tree) {
		set(Compass2Constans.IS_AHEAD_TREE, tree);
	}
	
	public Boolean hasBackTree() {
		return get(Compass2Constans.HAS_BACK_TREE);
	}
	
	public void setHasBackTree(Boolean tree) {
		set(Compass2Constans.HAS_BACK_TREE, tree);
	}
	
	public Boolean isBackTree() {
		return get(Compass2Constans.IS_BACK_TREE);
	}
	
	public void setBackTree(Boolean tree) {
		set(Compass2Constans.IS_BACK_TREE, tree);
	}
	
	public Boolean hasAllTree() {
		return get(Compass2Constans.HAS_ALL_TREE);
	}
	
	public void setHasAllTree(Boolean tree) {
		set(Compass2Constans.HAS_ALL_TREE, tree);
	}
	
	public Boolean isAllTree() {
		return get(Compass2Constans.IS_ALL_TREE);
	}
	
	public void setAllTree(Boolean tree) {
		set(Compass2Constans.IS_ALL_TREE, tree);
	}
}
