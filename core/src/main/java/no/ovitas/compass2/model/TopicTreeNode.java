package no.ovitas.compass2.model;

import java.util.Collection;
import java.util.HashMap;

public class TopicTreeNode extends Topic {
	private static final long serialVersionUID = 1L;
	
	private TopicTreeNode parent;
	private double boost;
	private HashMap<Topic, TopicTreeNode> children = new HashMap<Topic, TopicTreeNode>(0);
	private boolean flag;
	
	public TopicTreeNode(Topic src, double boost) {
		super(src);
		this.boost = boost;
		this.flag = false;
	}

	public void setParent(TopicTreeNode parent) {
		this.parent = parent;
		if (parent != null) {
			parent.children.put(this, this);
		}
	}

	public void intersect(TopicTreeNode other) {
		HashMap<Topic, TopicTreeNode> newNodes = new HashMap<Topic, TopicTreeNode>();
		for (TopicTreeNode node : children.values()) {
			TopicTreeNode otherNode = other.children.get(node);
			if (otherNode != null) {
				newNodes.put(node, node);
				node.intersect(otherNode);
			}
		}
		children = newNodes;
	}
	
	/**
	 * Remove not flagged node of the current node
	 */
	public void truncate() {
		
		// If this node is flagged
		if (this.isFlag())
		{
			// Iterate over the node and remove not flagged nodes
			for (TopicTreeNode child : children.values()) {
				child.truncate();
			}
		} else {
			// Remove this from it parent children collection
			parent.removeChildNode(this);
		}
		
	}
	
	/**
	 * Remove child node from children collection 
	 * @param child
	 */
	public void removeChildNode(TopicTreeNode child) {
		HashMap<Topic, TopicTreeNode> newNodes = new HashMap<Topic, TopicTreeNode>();
		for (TopicTreeNode oneChild : children.values()) {
			
			if (oneChild != child) {
				newNodes.put(oneChild, oneChild);
			}
		}
		children = newNodes;
	}
		
	public TopicTreeNode getParent() {
		return parent;
	}
	
	public Collection<TopicTreeNode> getChildren() {
		return children.values();
	}

	public double getBoost() {
		return boost;
	}

	public void setBoost(double boost) {
		this.boost = boost;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}
