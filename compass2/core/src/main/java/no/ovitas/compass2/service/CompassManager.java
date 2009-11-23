
package no.ovitas.compass2.service;

import java.io.IOException;
import java.util.List;

import no.ovitas.compass2.exception.ConfigParameterMissingException;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.model.DocumentDetails;
import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.model.ResultObject;

/**
 * The Interface CompassManager.
 */
public interface CompassManager {

	public ResultObject search(String search, int hopCount, double thresholdWeight, boolean prefixMatch, boolean fuzzyMatch);
	public ResultObject search(String search, int hopCount, double thresholdWeight, boolean prefixMatch, boolean fuzzyMatch, int pageNum);
	
	public ResultObject search(String search, int hopCount, double thresholdWeight, boolean prefixMatch, boolean fuzzyMatch, Integer maxTopicNumberToExpand);
	public ResultObject search(String search, int hopCount, double thresholdWeight, boolean prefixMatch, boolean fuzzyMatch, int pageNum, Integer maxTopicNumberToExpand);

	public void setUseStemmingEnabled(boolean useStemming);
	public void setPrefixMatchingEnabled(boolean prefixMatching);
	public void setHitThreshold(int hitThreshold);
	public void setResultThreshold(double resultThreshold);
	public void setMaxExpansionNode(int maxExpansionNode) ;
	
	public boolean isUseStemmingEnabled();
	public boolean isPrefixMatchingEnabled();
	public int getHitThreshold();
	
	public void init() throws ConfigurationException;
	public void setConfigurationManager(ConfigurationManager configurationManager)throws ConfigurationException	;
	/**
	 * @param userSearch
	 */
	public String getSpellingSuggestion(String userSearch) throws ConfigParameterMissingException, ConfigurationException, IOException;

	/**
	 * @param userSearch
	 */
	public List<String> getSpellingSuggestions(String userSearch) throws ConfigParameterMissingException, ConfigurationException, IOException ;
	
    public DocumentDetails getDocument(String id);	

	
}
