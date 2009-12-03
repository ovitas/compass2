package no.ovitas.compass2.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import no.ovitas.compass2.model.Relation;
import no.ovitas.compass2.model.Topic;
import no.ovitas.compass2.model.TopicTreeNode;

public class TopicUtil {
	private static class TopicNode{
		Topic parent;
		Topic topic;
		double distance;
		
		public TopicNode(Topic topic, Topic parent, double distance) {
			this.topic = topic;
			this.parent = parent;
			this.distance = distance;
		}
	}
	
	private static class TopicLinkNode {
		Topic topic;
		double cost;
		double boost;
		
		public TopicLinkNode(Topic topic, double distance, double boost) {
			this.topic = topic;
			this.cost  = distance;
			this.boost = boost;
		}
		
		public TopicLinkNode(Topic topic, double boost) {
			this.topic = topic;
			this.boost = boost;
		}
	}
	
	private static abstract class TopicExpander {
		List<TopicLinkNode> expand(Topic topic) {
			List<TopicLinkNode> ret = new ArrayList<TopicLinkNode>();
			List<Relation> relations = topic.getRelations();
			if (relations != null) {
				for (Relation relation : relations) {
					Topic otherTopic = relation.getSource();
					if (otherTopic.equals(topic)) {
						otherTopic = relation.getTarget();
						ret.add(new TopicLinkNode(otherTopic, getDistance(relation, true), getBoost(relation, true)));
					}
				}
			}
			return ret;
		}
		
		List<TopicLinkNode> contract(Topic topic) {
			List<TopicLinkNode> ret = new ArrayList<TopicLinkNode>();
			List<Relation> relations = topic.getRelations();
			if (relations != null) {
				for (Relation relation : relations) {
					Topic otherTopic = relation.getTarget();
					if (otherTopic.equals(topic)) {
						otherTopic = relation.getSource();
						ret.add(new TopicLinkNode(otherTopic, getDistance(relation, false), getBoost(relation, false)));
					}
				}
			}
			return ret;
		}
		
		protected abstract double getDistance(Relation relation, boolean ahead);
		protected abstract double getBoost(Relation relation, boolean ahead);
	}
	
	private static class TopicExpanderForMinHopCount extends TopicExpander {
		protected double getDistance(Relation relation, boolean ahead) {
			return 1;
		}
		protected double getBoost(Relation relation, boolean ahead) {
			if (ahead)
				return relation.getRelationType().getWeight();
			else
				return relation.getRelationType().getGeneralizationWeight();
		}
	}
	
	private static class TopicExpanderForMaxWeight extends TopicExpander {
		protected double getDistance(Relation relation, boolean ahead) {
			if (ahead)
				return -Math.log(relation.getRelationType().getWeight());
			else
				return -Math.log(relation.getRelationType().getGeneralizationWeight());
		}
		protected double getBoost(Relation relation, boolean ahead) {
			if (ahead)
				return relation.getRelationType().getWeight();
			else
				return relation.getRelationType().getGeneralizationWeight();
		}
	}
	
	private static final Comparator<TopicNode> comparator = new Comparator<TopicNode>() {
        public int compare(TopicNode left, TopicNode right) {
        	double ret = left.distance - right.distance;
            return ret < 0 ? -1 : ret > 0 ? 1 : 0;
        }
    };
	
	public static TopicTreeNode dijkstra(Topic rootTopic, TopicExpander expander, double limit, int maxTopicNumberToExpand) {
		PriorityQueue<TopicNode> queue = new PriorityQueue<TopicNode>(10, comparator);
		HashMap<Topic, TopicNode> topicNodes = new HashMap<Topic, TopicNode>();
		List<TopicNode> finalTopicNodes = new LinkedList<TopicNode>();
		HashMap<TopicNode, TopicLinkNode> reachedTopicLinkNodes = new HashMap<TopicNode, TopicLinkNode>();
		
		TopicNode rootTopicNode = new TopicNode(rootTopic, null, 0);
		topicNodes.put(rootTopicNode.topic, rootTopicNode);
		queue.add(rootTopicNode);
		
		int idx = 0;
		while (queue.size() > 0 && idx++ < maxTopicNumberToExpand) {
			TopicNode topicNode = queue.poll();
			
			// Get source -> target nodes
			List<TopicLinkNode> topicLinkNodes = expander.expand(topicNode.topic);
			
			// Get target -> source nodes and merge into topicLinkNodes
			List<TopicLinkNode> topicLinkNodesBack = expander.contract(topicNode.topic);
			for(TopicLinkNode node : topicLinkNodesBack) {
				if (!topicLinkNodes.contains(node)) {
					topicLinkNodes.add(node);
				}
			}
			double boost = 1.0;
			for (TopicLinkNode topicLinkNode : topicLinkNodes) {
				Topic aTopic = topicLinkNode.topic;
				TopicNode aTopicNode = topicNodes.get(aTopic);
				double dist = topicNode.distance + topicLinkNode.cost;
				
				if (reachedTopicLinkNodes.containsKey(topicNode)){
					boost = reachedTopicLinkNodes.get(topicNode).boost * topicLinkNode.boost;
				} else {
					boost = 1.0 * topicLinkNode.boost;
					reachedTopicLinkNodes.put(topicNode, new TopicLinkNode(topicNode.topic, boost));
				}
				
				if (dist > limit) continue;
				if (aTopicNode == null) {
					aTopicNode = new TopicNode(aTopic, topicNode.topic, dist);
					topicNodes.put(aTopic, aTopicNode);
					queue.add(aTopicNode);
					
					// Add new reached nodes
					if (!reachedTopicLinkNodes.containsKey(aTopic)){
						reachedTopicLinkNodes.put(aTopicNode, new TopicLinkNode(aTopicNode.topic, boost));
					}
					
				} else if (queue.contains(aTopicNode) && aTopicNode.distance > dist) {
					queue.remove(aTopicNode);
					aTopicNode.parent = topicNode.topic;
					aTopicNode.distance = dist;
					queue.add(aTopicNode);
					
					// Add better reached nodes
					if (reachedTopicLinkNodes.containsKey(aTopicNode) ){
						double actBoost  = reachedTopicLinkNodes.get(aTopicNode).boost;
						if (boost > actBoost) {
							reachedTopicLinkNodes.remove(aTopic);
							reachedTopicLinkNodes.put(aTopicNode, new TopicLinkNode(aTopicNode.topic, boost));
						}
					}
					
				}
			}
			finalTopicNodes.add(topicNode);
		}
		
		Map<Topic, TopicTreeNode> ret = new HashMap<Topic, TopicTreeNode>();
		for (TopicNode topicNode: finalTopicNodes) {		
			if (topicNode.distance <= limit){
				
				// If not root topic the boost is from dijkstra
				if (!topicNode.topic.equals(rootTopic)) {
					ret.put(topicNode.topic, new TopicTreeNode(topicNode.topic, reachedTopicLinkNodes.get(topicNode).boost));
				// If root topic the boost is from topic
				} else {
					ret.put(topicNode.topic, new TopicTreeNode(topicNode.topic, topicNode.topic.getDefaultBoost()));
				}
			}

		}
		
		for (TopicNode topicNode: finalTopicNodes) {
			if (topicNode.distance <= limit) 
				ret.get(topicNode.topic).setParent(ret.get(topicNode.parent));
		}
		
		return ret.get(rootTopic);
	}
	
	public static TopicTreeNode expandTopicsForMinHopCount(Topic topic, int maxHopCount, int maxTopicNumberToExpand) {
		return dijkstra(topic, new TopicExpanderForMinHopCount(), maxHopCount, maxTopicNumberToExpand);
	}

	public static TopicTreeNode expandTopicsForMaxWeight(Topic topic, double minWeight, int maxTopicNumberToExpand) {
		return dijkstra(topic, new TopicExpanderForMaxWeight(), -Math.log(minWeight), maxTopicNumberToExpand);
	}
}
