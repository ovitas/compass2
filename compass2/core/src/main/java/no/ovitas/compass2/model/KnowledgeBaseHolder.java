package no.ovitas.compass2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class KnowledgeBaseHolder implements Serializable {

	/**
	 *Serial version id 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Map<String, RelationType> relationTypes;
	protected Map<String, Topic> topics;
	protected List<Relation> relations;
	
	public KnowledgeBaseHolder(){
		relationTypes = new TreeMap<String,RelationType>();
		topics = new TreeMap<String,Topic>();
		relations = new ArrayList<Relation>();
	}

	public Map<String, RelationType> getRelationTypes() {
		return relationTypes;
	}

	public Map<String, Topic> getTopics() {
		return topics;
	}
	
	/*public List<Topic> findTopicByPrefixMatch(String prefix){
		List<Topic> matches = new ArrayList<Topic>();
		for(String name : topics.keySet()){
			if(name.startsWith(prefix)){
				matches.add(topics.get(name));
			}
		}
		return matches;
		
	}*/
	
	public List<Topic> findTopicByPrefixMatchCaseInSensitive(String prefix){
		String pref = prefix.trim().toLowerCase();
		List<Topic> matches = new ArrayList<Topic>();
		for(String name : topics.keySet()){
			if (name.length() >= pref.length()) {
				// Get topic name prefix
				
				if(name.toLowerCase().startsWith(pref) ||  name.toLowerCase().equals(pref)) {
					matches.add(topics.get(name));
				}
			}
		}
		return matches;
		
	}
	
	public Topic findTopic(String name){
		return topics.get(name);
	}
	
	public List<Topic> findTopicCaseInSensitive(String topicName){
		List<Topic> matches = new ArrayList<Topic>();
		for(String name : topics.keySet()){
			if(name.trim().equalsIgnoreCase(topicName.trim())) {
				matches.add(topics.get(name));
			}
		}
		return matches;
	}

	public RelationType findRelationType(String id){
		return relationTypes.get(id);
	}

	public List<Relation> getRelations() {
		return relations;
	}
	
	public void addRelation(Relation rel){
		this.relations.add(rel);
	}
	
	public void addTopic(Topic t){
		topics.put(t.getName(), t);
	}
	
	public void addRelationType(RelationType rt){
		relationTypes.put(rt.getId(), rt);
	}
}
