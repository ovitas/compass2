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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.NotImplementedException;

import no.ovitas.compass2.model.knowledgebase.RelationType;

/**
 * @class DirectRelationTypeEntity
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.03.
 * 
 */
@Entity
@Table(name = "direct_relation_type", uniqueConstraints = @UniqueConstraint(columnNames = { "DISPLAY_NAME", "KNOWLEDGE_BASE_ID" }))
@NamedQueries( { 
	@NamedQuery(name = "DirectRelationTypeEntity.getDirectRelationTypeEntity", query = "SELECT drte FROM DirectRelationTypeEntity drte WHERE drte.externalId = :id and drte.knowledgeBaseEntity.id = :kb_id"), 
	@NamedQuery(name = "DirectRelationTypeEntity.getAllFromKnowledgeBase", query = "SELECT drte FROM DirectRelationTypeEntity drte WHERE drte.knowledgeBaseEntity.id = :kb_id"),
	@NamedQuery(name = "DirectRelationTypeEntity.deleteAllInKnowledgeBase", query = "DELETE FROM DirectRelationTypeEntity drt WHERE drt.knowledgeBaseEntity IN (SELECT id FROM KnowledgeBaseEntity kb WHERE kb.id = :kb_id)")
})
public class DirectRelationTypeEntity extends PropertiesBaseObject implements RelationType, Serializable{

	private static final long serialVersionUID = 3182890844487123290L;

	private long id;
	
	private String externalId;

	private String displayName;

	private double weight;

	private double generalizationWeight;
	
	private int occurrence;
	
	private boolean active;

	private KnowledgeBaseEntity knowledgeBaseEntity;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * no.ovitas.compass2.kb.store.model.BaseObject#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof RelationType) {
			if (externalId != null && o != null
					&& externalId.equals(((RelationType) o).getExternalId())) {
				return true;
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
		return externalId.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.ovitas.compass2.kb.store.model.BaseObject#toString()
	 */
	@Override
	public String toString() {
		return "DirectRelationTypeEntity: name: " + displayName + ", weight: "
				+ weight + ", generalizationWeight: " + generalizationWeight;
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

	/**
	 * This is a setter method for name.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setDisplayName(String name) {
		this.displayName = name;
	}

	/**
	 * This is a getter method for name.
	 * 
	 * @return the name
	 */
	@Column(name = "DISPLAY_NAME", length = 400, nullable = false)
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * This is a setter method for weight.
	 * 
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * This is a getter method for weight.
	 * 
	 * @return the weight
	 */
	@Column(name = "WEIGHT", nullable = false)
	public double getWeight() {
		return weight;
	}

	/**
	 * This is a setter method for generalizationWeight.
	 * 
	 * @param generalizationWeight
	 *            the generalizationWeight to set
	 */
	public void setGeneralizationWeight(double generalizationWeight) {
		this.generalizationWeight = generalizationWeight;
	}

	/**
	 * This is a getter method for generalizationWeight.
	 * 
	 * @return the generalizationWeight
	 */
	@Column(name = "GENERALIZATION_WEIGHT", nullable = false)
	public double getGeneralizationWeight() {
		return generalizationWeight;
	}

	/**
	 * This is a setter method for knowledgeBaseEntity.
	 * 
	 * @param knowledgeBaseEntity
	 *            the knowledgeBaseEntity to set
	 */
	public void setKnowledgeBaseEntity(KnowledgeBaseEntity knowledgeBaseEntity) {
		this.knowledgeBaseEntity = knowledgeBaseEntity;
	}

	/**
	 * This is a getter method for knowledgeBaseEntity.
	 * 
	 * @return the knowledgeBaseEntity
	 */

	@ManyToOne(optional = false)
	@JoinColumn(name = "KNOWLEDGE_BASE_ID", referencedColumnName = "ID")
	public KnowledgeBaseEntity getKnowledgeBaseEntity() {
		return knowledgeBaseEntity;
	}

	/**
	 * This is a setter method for occurence.
	 * @param occurence the occurence to set
	 */
	public void setOccurrence(int occurence) {
		this.occurrence = occurence;
	}

	/**
	 * This is a getter method for occurence.
	 * @return the occurence
	 */
	@Column(name = "OCCURRENCE")
	public int getOccurrence() {
		return occurrence;
	}

	/**
	 * This is a setter method for active.
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * This is a getter method for active.
	 * @return the active
	 */
	@Column(name = "ACTIVE")
	public boolean isActive() {
		return active;
	}

	/**
	 * This is a setter method for externalId.
	 * @param externalId the externalId to set
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * This is a getter method for externalId.
	 * @return the externalId
	 */
	@Column(name = "EXTERNAL_ID", nullable = false)
	public String getExternalId() {
		return externalId;
	}

	@Transient
	public void makeUnique() {
		throw new NotImplementedException("makeUnique not implemented in DirectRealtionTypeEntity class.");
		
	}

	@Transient
	public void increaseOccurence() {
		occurrence++;		
	}
	
	public void decreaseOccurrence() {
		occurrence--;
	}

	@Transient
	public void clearOccurence() {
		occurrence = 0;		
	}

	@Transient
	public void addDisplayName(String displayName) {
		if (this.displayName != null && !this.displayName.contains(displayName))
			this.displayName += ";" + displayName;
		else
			this.displayName = displayName;
	}

}
