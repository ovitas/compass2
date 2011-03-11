/**
 * 
 */
package no.ovitas.compass2.kb.store.model;

import java.io.Serializable;
import java.util.Properties;

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

import no.ovitas.compass2.model.knowledgebase.Scope;

/**
 * @class ScopeEntity
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.30.
 * 
 */
@Entity(name = "ScopeEntity")
@Table(name = "scope", uniqueConstraints = @UniqueConstraint(columnNames = {
		"DISPLAY_NAME", "KNOWLEDGE_BASE_ID" }))
@NamedQueries({
		@NamedQuery(name = "ScopeEntity.getAllFromKnowledgeBase", query = "SELECT s FROM ScopeEntity s WHERE s.knowledgeBase.id = :kb_id"),
		@NamedQuery(name = "ScopeEntity.deleteAllInKnowledgeBase", query = "DELETE FROM ScopeEntity se WHERE se.knowledgeBase IN (SELECT id FROM KnowledgeBaseEntity kb WHERE kb.id = :kb_id)") })
public class ScopeEntity extends PropertiesBaseObject implements Scope,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2762431755916749078L;

	private long id;

	private String displayName;

	private String externalId;

	private KnowledgeBaseEntity knowledgeBase;

	@Override
	public boolean equals(Object o) {

		if (o instanceof ScopeEntity) {
			if (displayName != null
					&& displayName.equals(((ScopeEntity) o).displayName)
					&& knowledgeBase != null
					&& knowledgeBase.equals(((ScopeEntity) o)
							.getKnowledgeBase())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return displayName.hashCode() ^ externalId.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ScopeEntity: displayName: " + displayName + ", externalID: "
				+ externalId;
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
	 * This is a setter method for displayName.
	 * 
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * This is a getter method for displayName.
	 * 
	 * @return the displayName
	 */
	@Column(name = "DISPLAY_NAME", nullable = false, length = 255)
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * This is a setter method for externalId.
	 * 
	 * @param externalId
	 *            the externalId to set
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * This is a getter method for externalId.
	 * 
	 * @return the externalId
	 */
	@Column(name = "EXTERNAL_ID", nullable = false)
	public String getExternalId() {
		return externalId;
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
	@ManyToOne
	@JoinColumn(name = "KNOWLEDGE_BASE_ID", referencedColumnName = "ID")
	public KnowledgeBaseEntity getKnowledgeBase() {
		return knowledgeBase;
	}

	@Transient
	public void addDisplayName(String displayName) {
		if (this.displayName != null)
			this.displayName += ";" + displayName;
		else
			this.displayName = displayName;
	}

}
