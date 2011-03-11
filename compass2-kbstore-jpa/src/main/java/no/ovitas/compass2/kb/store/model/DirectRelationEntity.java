/**
 * 
 */
package no.ovitas.compass2.kb.store.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import no.ovitas.compass2.model.knowledgebase.Relation;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.Topic;

import org.springframework.core.style.ToStringCreator;


/**
 * @class DirectRelationEntity
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.03.
 * 
 */
@Entity
@Table(name = "direct_relation", uniqueConstraints = @UniqueConstraint(columnNames = {
		"start_topic_id", "end_topic_id", "direct_relation_type_id" }))
@NamedQueries({
	@NamedQuery(name = "DirectRelationEntity.getRelation", query = "SELECT drel FROM DirectRelationEntity drel WHERE drel.startTopic.externalId = :start_id and drel.endTopic.externalId = :end_id and drel.relationType.knowledgeBaseEntity.id = :kb_id"),
	@NamedQuery(name = "DirectRelationEntity.getRelationsStart", query = "SELECT drel FROM DirectRelationEntity drel WHERE drel.startTopic.id = :start_id"),
	@NamedQuery(name = "DirectRelationEntity.getRelationsEnd", query = "SELECT drel FROM DirectRelationEntity drel WHERE drel.endTopic.id = :end_id"),
	@NamedQuery(name = "DirectRelationEntity.getAllWithKnowledgeBase", query = "SELECT drel FROM DirectRelationEntity drel WHERE drel.relationType.knowledgeBaseEntity.id = :kb_id"),
	@NamedQuery(name = "DirectRelationEntity.deleteAllInKnowledgeBase", query = "DELETE FROM DirectRelationEntity dr WHERE dr.relationType IN (SELECT id FROM DirectRelationTypeEntity drti WHERE drti.knowledgeBaseEntity.id = :kb_id)")
})
public class DirectRelationEntity extends PropertiesBaseObject implements Relation, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3324511051666822410L;

	private long id;

	private TopicEntity startTopic;

	private TopicEntity endTopic;

	private DirectRelationTypeEntity relationType;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * no.ovitas.compass2.kb.store.model.BaseObject#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof DirectRelationEntity) {
			if (o != null) {
				DirectRelationEntity other = (DirectRelationEntity) o;
				if (startTopic != null && endTopic != null
						&& other.startTopic != null && other.endTopic != null && other.relationType != null
						&& other.startTopic.equals(startTopic)
						&& other.getEndTopic().equals(endTopic) && other.relationType.equals(relationType)) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.ovitas.compass2.kb.store.model.BaseObject#hashCode()
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return ((int) id + 1) ^ startTopic.hashCode() ^ relationType.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.ovitas.compass2.kb.store.model.BaseObject#toString()
	 */
	@Override
	public String toString() {
		ToStringCreator creator = new ToStringCreator(this);

		creator.append("id", id);
		creator.append("importId", getImportId());

		creator.append("startTopicId", startTopic == null ? null : startTopic
				.getId());
		creator
				.append("endTopicId", endTopic == null ? null : endTopic
						.getId());

		if (relationType != null) {
			creator.append("relationType", relationType.getId());
			creator.append("weight", relationType.getWeight());
			creator.append("genWeight", relationType.getGeneralizationWeight());
		} else {
			creator.append("relationType", relationType);
		}

		return creator.toString();
	}

	/**
	 * This is a setter method for relationType.
	 * 
	 * @param relationType
	 *            the relationType to set
	 */
	public void setRelationType(DirectRelationTypeEntity relationType) {
		this.relationType = relationType;
	}

	/**
	 * This is a getter method for relationType.
	 * 
	 * @return the relationType
	 */
	@OneToOne
	@JoinColumn(name = "DIRECT_RELATION_TYPE_ID", referencedColumnName = "ID")
	public DirectRelationTypeEntity getRelationType() {
		return relationType;
	}

	/**
	 * This is a setter method for endTopic.
	 * 
	 * @param endTopic
	 *            the endTopic to set
	 */
	public void setEndTopic(TopicEntity endTopic) {
		this.endTopic = endTopic;
	}

	/**
	 * This is a getter method for endTopic.
	 * 
	 * @return the endTopic
	 */

	@ManyToOne(optional = false)
	@JoinColumn(name = "END_TOPIC_ID", referencedColumnName = "ID")
	public TopicEntity getEndTopic() {
		return endTopic;
	}

	/**
	 * This is a setter method for startTopic.
	 * 
	 * @param startTopic
	 *            the startTopic to set
	 */
	public void setStartTopic(TopicEntity startTopic) {
		this.startTopic = startTopic;
	}

	/**
	 * This is a getter method for startTopic.
	 * 
	 * @return the startTopic
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "START_TOPIC_ID", referencedColumnName = "ID")
	public TopicEntity getStartTopic() {
		return startTopic;
	}

	/**
	 * This is a setter method for id.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * This is a getter method for id.
	 * 
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public long getId() {
		return id;
	}

	@Transient
	public Topic getSource() {
		return startTopic;
	}

	@Transient
	public Topic getTarget() {
		return endTopic;
	}

	@Transient
	public RelationType getType() {
		return relationType;
	}

}
