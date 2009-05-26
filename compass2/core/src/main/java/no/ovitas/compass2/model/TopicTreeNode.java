package no.ovitas.compass2.model;

import java.util.Collection;
import java.util.HashMap;

public class TopicTreeNode extends Topic {
	private static final long serialVersionUID = 1L;
	
	private TopicTreeNode parent;
	private HashMap<Topic, TopicTreeNode> children = new HashMap<Topic, TopicTreeNode>(0);
	
	public TopicTreeNode(Topic src) {
		super(src);
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
	
	public TopicTreeNode getParent() {
		return parent;
	}
	
	public Collection<TopicTreeNode> getChildren() {
		return children.values();
	}

}
