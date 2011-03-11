/**
 * 
 */
package no.ovitas.compass2.kb.store.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseType;

/**
 * @class KnowledgeBaseEntity
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.02.
 * 
 */
@Entity(name = "KnowledgeBaseEntity")
@Table(name = "knowledge_base", uniqueConstraints = { @UniqueConstraint(columnNames = { "DISPLAY_NAME" }) })
@NamedQueries({
	@NamedQuery(name = "KnowledgeBaseEntity.getKnowledgeBase", query = "SELECT k FROM KnowledgeBaseEntity k WHERE k.displayName = :kb_name")
})
public class KnowledgeBaseEntity extends BaseObject implements KnowledgeBaseDescriptor, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3596384758519596829L;

	private long id;

	private String displayName;

	private String description;
	
	private boolean active;
	
	private KnowledgeBaseType type;

	/**
	 * Default constructor.
	 */
	public KnowledgeBaseEntity() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * no.ovitas.compass2.kb.store.model.BaseObject#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o instanceof KnowledgeBaseEntity) {
			if (displayName != null && o != null
					&& displayName.equals(((KnowledgeBaseEntity) o).getDisplayName())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {

		return displayName.hashCode();
	}

	@Override
	public String toString() {
		return "KnowledgeBaseEntity: Name: " + displayName + ", description: "
				+ description;
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
	@Column(name = "DISPLAY_NAME", nullable = false, length = 100)
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * This is a setter method for description.
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * This is a getter method for description.
	 * 
	 * @return the description
	 */
	@Column(name = "DESCRIPTION", nullable = true)
	public String getDescription() {
		return description;
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
	 * This is a getter method for active.
	 * @return the active
	 */
	@Column(name = "ACTIVE")
	public boolean isActive() {
		return active;
	}

	/**
	 * This is a setter method for active.
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	public KnowledgeBaseType getType() {
		return type;
	}

	public void setType(KnowledgeBaseType type) {
		this.type = type;
	}
}
