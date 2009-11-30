/**
 * 
 */
package no.ovitas.compass2.model;

import java.io.Serializable;

/**
 * @author magyar
 *
 */
public class Relation implements Serializable {

	
	private Topic source;
	private Topic target;
	private RelationType relationType;
	
	
	public Topic getSource() {
		return source;
	}
	public void setSource(Topic source) {
		this.source = source;
	}
	public Topic getTarget() {
		return target;
	}
	public void setTarget(Topic target) {
		this.target = target;
	}
	public RelationType getRelationType() {
		return relationType;
	}
	public void setRelationType(RelationType relationType) {
		this.relationType = relationType;
	}
}
