/**
 * 
 */
package no.ovitas.compass2.kb.store.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import no.ovitas.compass2.model.knowledgebase.Relation;
import no.ovitas.compass2.model.knowledgebase.Topic;
import no.ovitas.compass2.model.knowledgebase.TopicName;

import org.springframework.core.style.ToStringCreator;

/**
 * @class TopicEntity
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.03.
 * 
 */
@Entity(name = "TopicEntity")
@Table(name = "topic", uniqueConstraints = @UniqueConstraint(columnNames = {
		"EXTERNAL_ID", "KNOWLEDGE_BASE_ID" }))
@NamedQueries({
		@NamedQuery(name = "TopicEntity.findAllTopicsInKnowledgeBase", query = "SELECT t FROM TopicEntity t WHERE t.knowledgeBase.id = :kb_id"),
		@NamedQuery(name = "TopicEntity.getAllWithKnowledgeBase", query = "SELECT t FROM TopicEntity t WHERE t.knowledgeBase.id = :kb_id"),
		@NamedQuery(name = "TopicEntity.deleteAllInKnowledgeBase", query = "DELETE FROM TopicEntity te WHERE te.knowledgeBase IN (SELECT id FROM KnowledgeBaseEntity kb WHERE kb.id = :kb_id)") })
public class TopicEntity extends PropertiesBaseObject implements Topic, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8935230190841236008L;

	private long id;

	private String externalId;

	private KnowledgeBaseEntity knowledgeBase;

	private Set<TopicNameEntity> names;

	private String displayName;

	private Set<Relation> relationsInterface;

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o instanceof TopicEntity) {
			if (externalId != null && o != null
					&& externalId.equals(((TopicEntity) o).getExternalId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return externalId.hashCode();
	}

	@Override
	public String toString() {
		ToStringCreator creator = new ToStringCreator(this);

		creator.append("id", id);
		creator.append("importId", getImportId());
		creator.append("externalId", externalId);
		creator.append("displayName", displayName);

		return creator.toString();
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
	 * This is a setter method for names.
	 * 
	 * @param names
	 *            the names to set
	 */
	public void setNamesEntity(Set<TopicNameEntity> names) {
		this.names = names;
	}

	/**
	 * This is a getter method for names.
	 * 
	 * @return the names
	 */
	@OneToMany(mappedBy = "topic", fetch = FetchType.EAGER)
	public Set<TopicNameEntity> getNamesEntity() {
		return names;
	}

	/**
	 * This is a setter method for knowledgeBase.
	 * 
	 * @param knowledgeBase
	 *            the knowledgeBase to set
	 */
	public void setKnowledgeBase(KnowledgeBaseEntity knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
	}

	/**
	 * This is a getter method for knowledgeBase.
	 * 
	 * @return the knowledgeBase
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "KNOWLEDGE_BASE_ID", referencedColumnName = "ID")
	public KnowledgeBaseEntity getKnowledgeBase() {
		return knowledgeBase;
	}

	/**
	 * This is a setter method for fileID.
	 * 
	 * @param fileID
	 *            the fileID to set
	 */
	public void setExternalId(String externalID) {
		this.externalId = externalID;
	}

	/**
	 * This is a getter method for fileID.
	 * 
	 * @return the fileID
	 */
	@Column(name = "EXTERNAL_ID", nullable = false)
	public String getExternalId() {
		return externalId;
	}

	@Transient
	public String getDisplayName() {
		if (displayName == null || "".equals(displayName)) {
			displayName = "";
			for (TopicNameEntity name : names) {

				displayName += ";" + name.getName();
				break;

			}
		}

		return displayName.substring(1);
	}

	@Transient
	public Set<Relation> getRelations() {
		if (relationsInterface == null) {
			return new HashSet<Relation>();
		}
		return relationsInterface;
	}

	@Transient
	public Set<TopicName> getNames() {
		if (names == null) {
			return new HashSet<TopicName>(0);
		}

		return (Set<TopicName>) (Set) names;
	}

	@Transient
	public void setDisplayName(String displayName) {
		// TODO Auto-generated method stub

	}

	@Transient
	public void addTopicName(TopicName topicName) {
		if (names == null) {
			names = new HashSet<TopicNameEntity>();
		}
		if (topicName instanceof TopicNameEntity) {
			names.add((TopicNameEntity) topicName);
		}

	}

	@Transient
	public void addRelation(Relation relation) {
		if (relationsInterface == null) {
			relationsInterface = new HashSet<Relation>();
		}
		relationsInterface.add(relation);

	}

}
