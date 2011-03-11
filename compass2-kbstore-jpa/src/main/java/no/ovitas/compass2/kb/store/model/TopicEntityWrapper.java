package no.ovitas.compass2.kb.store.model;

import java.util.ArrayList;
import java.util.List;

import no.ovitas.compass2.model.knowledgebase.Topic;

import org.springframework.core.style.ToStringCreator;

/**
 * <code>TopicEntity</code> wrapper class used for the alternative transitive
 * path algorithm.
 * 
 * @author Csaba Daniel
 */
public class TopicEntityWrapper {

	private TopicEntity topic;
	private double bestPathWeight = 0;
	private List<DirectRelationEntity> bestPath = new ArrayList<DirectRelationEntity>();
	private boolean complete = false;
	private int hopCount = 0;

	/**
	 * Construct a new wrapper instance with the specified underlying topic.
	 * 
	 * @param topic
	 *            the underlying topic
	 */
	public TopicEntityWrapper(TopicEntity topic) {
		this.topic = topic;
	}

	/**
	 * Get the underlying topic.
	 * 
	 * @return the underlying topic
	 */
	public TopicEntity getTopic() {
		return topic;
	}

	/**
	 * Get the weight of the best path (highest product) from the current topic
	 * to this topic.
	 * 
	 * @return the weight of the best path
	 */
	public double getBestPathWeight() {
		return bestPathWeight;
	}

	/**
	 * Set the weight of the best path (highest product) from the base topic to
	 * this topic.
	 * 
	 * @param bestPathWeight
	 *            the best path weight to set
	 */
	public void setBestPathWeight(double bestPathWeight) {
		this.bestPathWeight = bestPathWeight;
	}

	/**
	 * Get the list of the <code>DirectRelationEntity</code>-s those form the
	 * best path from the base topic to this topic.
	 * 
	 * @return the list of <code>Relation</code>s
	 */
	public List<DirectRelationEntity> getBestPath() {
		return bestPath;
	}

	/**
	 * Set the list of the <code>DirectRelationEntity</code>-s those form the
	 * best path from the base topic to this topic.
	 * 
	 * @param bestPath
	 *            the list of <code>Relation</code>s to set
	 */
	public void setBestPath(List<DirectRelationEntity> bestPath) {
		this.bestPath = bestPath;
	}

	/**
	 * Get if all path calculations to this topic is complete.
	 * 
	 * @return <code>true</code> if all path calculations to this topic is
	 *         complete, <code>false</code> otherwise
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
	 * Set if all path calculations to this topic is complete.
	 * 
	 * @param complete
	 *            the state of completeness to set
	 */
	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	/**
	 * Get the hop count in which we reached this topic from the base in the
	 * current best path.
	 * 
	 * @return the maximum hop count
	 */
	public int getHopCount() {
		return hopCount;
	}

	/**
	 * Set the hop count in which we reached this topic from the base in the
	 * current best path.
	 * 
	 * @param data.getHopCount()
	 *            the maximum hop count to set
	 */
	public void setHopCount(int maxHopCount) {
		this.hopCount = maxHopCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof TopicEntityWrapper))
			return false;

		TopicEntityWrapper wrapper = (TopicEntityWrapper) obj;
		Topic wrappedTopic = wrapper.getTopic();
		if (wrappedTopic == null || topic == null)
			return false;

		if (wrappedTopic.getId() == topic.getId())
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		if (topic == null)
			return 0;

		Long topicId = topic.getId();
		return topicId.hashCode();
	}

	@Override
	public String toString() {
		ToStringCreator creator = new ToStringCreator(this);

		creator.append("topic.externalId", topic.getExternalId());
		creator.append("bestPathWeight", bestPathWeight);

		StringBuilder builder = new StringBuilder();
		for (DirectRelationEntity relation : bestPath) {
			if (builder.length() > 0)
				builder.append(", ");

			builder.append("(");
			builder.append(relation.getStartTopic().getExternalId());
			builder.append(" -> ");
			builder.append(relation.getEndTopic().getExternalId());
			builder.append(")");
		}

		creator.append("bestPath", builder.toString());
		creator.append("hopCount", hopCount);
		creator.append("complete", complete);

		return creator.toString();
	}
}
