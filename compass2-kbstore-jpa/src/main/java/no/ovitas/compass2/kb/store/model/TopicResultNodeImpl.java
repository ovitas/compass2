package no.ovitas.compass2.kb.store.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import no.ovitas.compass2.model.knowledgebase.RelationDirection;
import no.ovitas.compass2.model.knowledgebase.Topic;
import no.ovitas.compass2.search.TopicResult;
import no.ovitas.compass2.search.TopicResultNode;
import no.ovitas.compass2.search.TopicWeightResultCollector;

import org.springframework.core.style.ToStringCreator;

/**
 * POJO implementation of the <code>TopicResultNode</code> interface.
 * 
 * @author Csaba Daniel
 */
public class TopicResultNodeImpl implements TopicResultNode {

	private Topic topic;
	private long baseTopicId = -1;
	private int hops = 0;
	private boolean complete = false;
	private double weight = 0;
	private Collection<String> names = new ArrayList<String>();
	private TopicResultNodeImpl parent;
	private Collection<TopicResultNodeImpl> children = new ArrayList<TopicResultNodeImpl>();
	private String relationTypeName = TopicResultNode.NULL_RELATION;
	private int baseIndex = 0;
	private RelationDirection direction;
	private long relationId = -1;

	/**
	 * Create a new instance with the specified underlying topic.
	 * 
	 * @param topic
	 *            the underlying topic
	 */
	public TopicResultNodeImpl(Topic topic) {
		this.topic = topic;
	}

	/**
	 * Get the underlying topic.
	 * 
	 * @return the underlying topic
	 */
	public Topic getTopic() {
		return topic;
	}

	public long getId() {
		return topic.getId();
	}

	/**
	 * Get the import id of the underlying topic.
	 * 
	 * @return the topic import id
	 */
	public long getImportId() {
		return topic.getImportId();
	}

	public long getBaseTopicId() {
		return baseTopicId;
	}

	/**
	 * Set the id of the base topic the current topic was expanded from.
	 * 
	 * @param baseTopicId
	 *            the base topic id to set
	 */
	public void setBaseTopicId(long baseTopicId) {
		this.baseTopicId = baseTopicId;
	}

	/**
	 * Get the hop count from a base topic to this topic.
	 * 
	 * @return the hop count
	 */
	public int getHops() {
		return hops;
	}

	/**
	 * Set the hop count from a base topic to this topic.
	 * 
	 * @param hops
	 *            the hop count to set
	 */
	public void setHops(int hopCount) {
		this.hops = hopCount;
	}

	/**
	 * Get if the processing of the current topic is complete.
	 * 
	 * @return <code>true</code> if processing is complete, <code>false</code>
	 *         otherwise
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
	 * Set if the processing of the current topic is complete.
	 * 
	 * @param complete
	 *            the completeness of this topic
	 */
	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public Collection<String> getNames() {
		return names;
	}

	/**
	 * Set the names of this topic
	 * 
	 * @param names
	 *            the collection of names to set
	 */
	public void setNames(Collection<String> names) {
		this.names = names;
	}

	public double getWeight() {
		return weight;
	}

	/**
	 * Assign a weight to this topic via the relations from one of the base
	 * topics.
	 * 
	 * @param weight
	 *            the weight to assign
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * Get the parent of this topic result.
	 * 
	 * @return the parent topic result
	 */
	public TopicResultNodeImpl getParent() {
		return parent;
	}

	private void setParent(TopicResultNodeImpl parent) {
		this.parent = parent;
	}

	@SuppressWarnings("unchecked")
	public Collection<TopicResultNode> getChildren() {
		return Collections.unmodifiableCollection((Collection) children);
	}

	/**
	 * Set the implementations of the child nodes of this topic.
	 * 
	 * @param children
	 *            collection of children node implementations
	 */
	public void setChildrenImpl(Collection<TopicResultNodeImpl> children) {
		this.children = children;
	}

	/**
	 * Get the implementations of the child nodes of this topic.
	 * 
	 * @return collection of children node implementations
	 */
	public Collection<TopicResultNodeImpl> getChildrenImpl() {
		return children;
	}

	/**
	 * Add a child node to this topic result.
	 * 
	 * @param resultNode
	 *            the result node to add
	 */
	public void addChild(TopicResultNodeImpl resultNode) {
		resultNode.setParent(this);
		children.add(resultNode);
	}

	/**
	 * Remove a child node from this topic result.
	 * 
	 * @param resultNode
	 *            the result node to remove
	 * @return <code>true</code> if the child was removed
	 */
	public boolean removeChild(TopicResultNodeImpl resultNode) {
		boolean removed = children.remove(resultNode);

		if (removed)
			resultNode.setParent(null);

		return removed;

	}

	public String getRelationTypeName() {
		return relationTypeName;
	}

	/**
	 * Set the relation type name of the direct relation the current topic has
	 * with it's parent.
	 * 
	 * @param relationTypeName
	 *            the relation type name to set
	 */
	public void setRelationTypeName(String relationTypeName) {
		this.relationTypeName = relationTypeName;
	}

	public long getRelationId() {
		return relationId;
	}

	/**
	 * Set the id of relation that connects the parent of this node with this
	 * node.
	 * 
	 * @param relationId
	 *            the relation id to set
	 */
	public void setRelationId(long relationId) {
		this.relationId = relationId;
	}

	/**
	 * Get the index of the base topic. This is applicable only for base topics,
	 * the value is 0 for other topics.
	 * 
	 * @return the index of the base topic
	 */
	public int getBaseIndex() {
		return baseIndex;
	}

	/**
	 * Set the index of the base topic. This should be set only for base topics.
	 * 
	 * @param baseIndex
	 *            the index to set
	 */
	public void setBaseIndex(int baseIndex) {
		this.baseIndex = baseIndex;
	}

	public RelationDirection getDirection() {
		return direction;
	}

	/**
	 * Set the direction of the relation from the base topic to this topic.
	 * 
	 * @param direction
	 *            the relation direction
	 */
	public void setDirection(RelationDirection direction) {
		this.direction = direction;
	}

	/**
	 * Get if the current topic is a root topic node in a tree
	 * 
	 * @return <code>true</code> if this ia a root topic node in the tree,
	 *         <code>false</code> otherwise
	 */
	public boolean isRoot() {
		return baseTopicId == topic.getId();
	}

	public boolean equals(Object obj) {
		if (obj instanceof TopicResult) {
			if (obj != null) {
				TopicResult res = (TopicResult) obj;

				if (getId() == res.getId())
					return true;
			}
		}

		return false;
	}

	public int hashCode() {
		int hashCode = ((Long) getId()).hashCode();
		return hashCode;
	}

	public String toString() {
		ToStringCreator creator = new ToStringCreator(this);

		creator.append("id", getId());
		creator.append("baseTopicId", baseTopicId);
		creator.append("hops", hops);
		creator.append("complete", complete);
		creator.append("weight", weight);
		creator.append("relationTypeName", relationTypeName);
		creator.append("baseIndex", baseIndex);

		return creator.toString();
	}

	public void collectTopicWeight(TopicWeightResultCollector topicWeightResultCollector) {
		
		boolean collect = topicWeightResultCollector.collect(this);
		
		if (children != null && collect) {
			for (TopicResultNodeImpl child : children) {
				child.collectTopicWeight(topicWeightResultCollector);
			}
		}
		
	}
}
