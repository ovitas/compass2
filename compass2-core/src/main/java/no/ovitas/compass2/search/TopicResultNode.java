package no.ovitas.compass2.search;

import java.util.Collection;

import no.ovitas.compass2.model.knowledgebase.RelationDirection;

/**
 * Extension of the <code>TopicResult</code> to allow the representation of a
 * result tree.
 * 
 * @author Csaba Daniel
 */
public interface TopicResultNode extends TopicResult {

	/**
	 * Relation name for base nodes.
	 */
	public static final String NULL_RELATION = "";

	/**
	 * Get the relation type name of the direct relation the current topic has
	 * with it's parent. In case of base topics this method should return
	 * {@link TopicResultNode#NULL_RELATION}.
	 * 
	 * @return relation type name
	 */
	public String getRelationTypeName();

	/**
	 * Return the id of relation that connects the parent of this node with this
	 * node.
	 * 
	 * @return the relation id
	 */
	public long getRelationId();

	/**
	 * Get the children nodes of this node.
	 * 
	 * @return collection of children nodes
	 */
	public Collection<TopicResultNode> getChildren();

	/**
	 * Get the direction of the relation from the base topic to this topic.
	 * 
	 * @return the relation direction
	 */
	public RelationDirection getDirection();
}
