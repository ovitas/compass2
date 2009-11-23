/**
 * 
 */
package no.ovitas.compass2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author magyar
 *
 */
public class Topic implements Serializable, Comparable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String id;
	private List<String> alternativeNames = new ArrayList<String>();
	private List<Relation> relations = new ArrayList<Relation>();

	public Topic() {
	}
	
	public Topic(Topic src) {
		name = src.name;
		alternativeNames = src.alternativeNames;
		relations = src.relations;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if(name!=null){
		 this.name = name.trim().replaceAll("\"", "");
		}else{
			this.name=null;
		}
	}
	
	public List<String> getAlternativeNames() {
		return alternativeNames;
	}
	
	public void setAlternativeNames(List<String> alternativeNames) {
		this.alternativeNames = alternativeNames;
	}
	
	public List<Relation> getRelations() {
		return relations;
	}
	
	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}
	 
	public void addRelation(Relation relation){
		this.relations.add(relation);
	}
	
	public void addAlternativeName(String altName){
		if(altName!=null){
		 this.alternativeNames.add(altName.trim().replaceAll("\"", ""));
		}
	}

	public int compareTo(Object o) {
		if (o instanceof Topic){
			return name.compareTo(((Topic)o).getName());
		}else{
			return name.compareTo(o.toString());
		}
	}
	
	public boolean equals(Object o){
		if (o instanceof Topic){
			if(((Topic)o).getName().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public int hashCode() {
		return name.hashCode();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
