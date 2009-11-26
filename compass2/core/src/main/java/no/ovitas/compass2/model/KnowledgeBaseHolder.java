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
	protected Map<String, List<Topic>> topics;
	protected List<Topic> topicsList;
	protected List<Relation> relations;
	protected Double defaultPrefixBoost = new Double(0.5);
	
	public KnowledgeBaseHolder(){
		relationTypes = new TreeMap<String,RelationType>();
		topics = new TreeMap<String,List<Topic>>();
		topicsList = new ArrayList<Topic>();
		relations = new ArrayList<Relation>();
	}

	public Map<String, RelationType> getRelationTypes() {
		return relationTypes;
	}

	public Map<String, List<Topic>> getTopics() {
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
				if(name.toLowerCase().startsWith(pref) &&  !name.toLowerCase().equals(pref)) {
					List<Topic> tm = topics.get(name);
					for(Topic topic : tm){
						topic.setDefaultBoost(this.defaultPrefixBoost);
					 matches.add(topic);
					}
				}
				if(name.toLowerCase().startsWith(pref) &&  name.toLowerCase().equals(pref)) {
					List<Topic> tm = topics.get(name);
					for(Topic topic : tm){
						topic.setDefaultBoost(new Double(1));
					 matches.add(topic);
					}
				}
			}
		}
		return matches;
		
	}
	
	public List<Topic> findTopic(String name){
		return topics.get(name);
	}
	
	public Topic findFirstTopic(String name){
		List<Topic> tl = topics.get(name);
		return (tl == null ? null : tl.get(0));
	}

	public List<Topic> findTopicCaseInSensitive(String topicName){
		List<Topic> matches = new ArrayList<Topic>();
		for(String name : topics.keySet()){
			if(name.trim().equalsIgnoreCase(topicName.trim())) {
				List<Topic> tm = topics.get(name);
				for(Topic topic : tm){
					topic.setDefaultBoost(new Double(1));
				 matches.add(topic);
				}
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
		List<Topic> actList = topics.get(t.getName());
		if(actList==null){
			actList = new ArrayList<Topic>();
			topics.put(t.getName(), actList);
		}
		actList.add(t);
		this.topicsList.add(t);
	}
	
	public void addRelationType(RelationType rt){
		relationTypes.put(rt.getId(), rt);
	}

	public List<Topic> getTopicsList() {
		return topicsList;
	}
}
