/**
 * 
 */
package no.ovitas.compass2.kb.store.model;

import java.io.Serializable;

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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.NotImplementedException;

import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.model.knowledgebase.TopicName;

/**
 * @class TopicNameEntity
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.03.
 * 
 */
@Entity(name = "TopicNameEntity")
@Table(name = "topic_name")
@NamedQueries( {
@NamedQuery(name = "TopicNameEntity.sameTopicName", query = "SELECT distinct (t.topic.id) FROM TopicNameEntity t WHERE t.name LIKE :tn_name and t.topic.knowledgeBase.id = :kb_id"),
@NamedQuery(name = "TopicNameEntity.equalTopicName", query = "SELECT t FROM TopicNameEntity t WHERE t.name = :tn_name and t.topic.knowledgeBase.id = :kb_id"),
@NamedQuery(name = "TopicNameEntity.deleteAllInKnowledgeBase", query = "DELETE FROM TopicNameEntity tne WHERE tne.topic IN (SELECT id FROM TopicEntity t WHERE t.knowledgeBase.id = :kb_id)")
})
public class TopicNameEntity extends PropertiesBaseObject implements TopicName, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	
	private String name;
	
	private TopicEntity topic;
	
	private ScopeEntity scopeEntity;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * no.ovitas.compass2.kb.store.model.BaseObject#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o instanceof TopicEntity) {
			if (name != null && o != null && name.equals(((TopicNameEntity)o).getName())) {
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
		// TODO Auto-generated method stub
		return name.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.ovitas.compass2.kb.store.model.BaseObject#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "TopicNameEntity: name: " + name;
	}

	/**
	 * This is a setter method for id.
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * This is a getter method for id.
	 * @return the id
	 */
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public long getId() {
		return id;
	}

	/**
	 * This is a setter method for name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * This is a getter method for name.
	 * @return the name
	 */
	@Column(name = "NAME", nullable = false, length = 255)
	public String getName() {
		return name;
	}


	/**
	 * This is a setter method for topic.
	 * @param topic the topic to set
	 */
	public void setTopic(TopicEntity topic) {
		this.topic = topic;
	}

	/**
	 * This is a getter method for topic.
	 * @return the topic
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TOPIC_ID", referencedColumnName = "ID")
	public TopicEntity getTopic() {
		return topic;
	}

	/**
	 * This is a setter method for scope.
	 * @param scope the scope to set
	 */
	public void setScopeEntity(ScopeEntity scope) {
		this.scopeEntity = scope;
	}

	/**
	 * This is a getter method for scope.
	 * @return the scope
	 */
	@ManyToOne
	@JoinColumn(name = "SCOPE_ID", referencedColumnName = "ID")
	public ScopeEntity getScopeEntity() {
		return scopeEntity;
	}

	@Transient
	public Scope getScope() {		
		return scopeEntity;
	}

}
