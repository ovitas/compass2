package no.ovitas.compass2.service;

import java.util.List;
import java.util.Set;

import no.ovitas.compass2.model.KnowledgeBaseHolder;
import no.ovitas.compass2.model.Topic;
import no.ovitas.compass2.model.TopicTreeNode;

/**
 * @author magyar
 * @version 1.0
 */
public interface KnowledgeBaseManager {

	
	public void setConfiguration(ConfigurationManager manager);
	public void setExpansionThreshold(double expansionThreshold);
	public void setMaxTopicNumberToExpand(int maxTopicNumberToExpand);
	/**
	 * 
	 * @param fuzzyMatch
	 * @param prefixMatching
	 * @param hopCount
	 * @param thresholdWeight
	 * @param search
	 */
	public Set<TopicTreeNode> getExpansion(boolean fuzzyMatch, boolean prefixMatching, int hopCount, double thresholdWeight, String search, Integer maxTopicNumberToExpand);

	/**
	 * 
	 * @param fuzzyMatch
	 * @param prefixMatching
	 * @param hopCount
	 * @param thresholdWeight
	 * @param search
	 */
	public List<Set<TopicTreeNode>> getExpansion(boolean fuzzyMatch, boolean prefixMatching, int hopCount, double thresholdWeight, List<String> search, Integer maxTopicNumberToExpand);

	/**
	 * 
	 * @param kb
	 */
	public void importKB(String kb);

	/**
	 * 
	 * @param kb
	 */
	public void updateKB(String kb);
	
	public KnowledgeBaseHolder getKbModel();

}