/**
 * 
 */
package no.ovitas.compass2.model.knowledgebase;

import java.util.Set;

/**
 * @class TermRelationType
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.09.25.
 * 
 */
public final class TermRelationType implements RelationType {
	
	private static final String TERM_LABEL = "TERM: ";
	private String termValue;
	
	public TermRelationType(String termValue) {
		this.termValue = termValue;
	}


	@Override
	public void setProperty(String key, Object value) {

	}


	@Override
	public Object getProperty(String key) {
		return null;
	}

	@Override
	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getImportId() {
		return 0;
	}

	@Override
	public void setImportId(long id) {
	}

	@Override
	public String getExternalId() {
		return termValue;
	}

	@Override
	public void setExternalId(String externalId) {
		termValue = externalId;
	}

	@Override
	public String getDisplayName() {
		return TERM_LABEL + termValue;
	}

	/* (non-Javadoc)
	 * @see no.ovitas.compass2.model.RelationType#setDisplayName(java.lang.String)
	 */
	@Override
	public void setDisplayName(String displayName) {
		termValue = displayName;
	}


	@Override
	public void addDisplayName(String displayName) {

	}

	@Override
	public double getWeight() {
		return 0;
	}


	@Override
	public void setWeight(double weight) {

	}

	@Override
	public double getGeneralizationWeight() {
		return 0;
	}

	@Override
	public void setGeneralizationWeight(double generalizationWeight) {
	}

	@Override
	public int getOccurrence() {
		return 0;
	}

	@Override
	public void makeUnique() {

	}

}
