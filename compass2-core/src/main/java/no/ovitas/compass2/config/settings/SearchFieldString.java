/**
 * 
 */
package no.ovitas.compass2.config.settings;

import no.ovitas.compass2.model.FittingType;
import no.ovitas.compass2.model.MatchingType;

/**
 * @class SearchFieldString
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.10.
 * 
 */
public class SearchFieldString extends SearchField {

	private float weight;

	private String match;

	private String fit;

	/**
	 * This is a setter method for weight.
	 * 
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * This is a getter method for weight.
	 * 
	 * @return the weight
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * This is a setter method for match.
	 * 
	 * @param match
	 *            the match to set
	 */
	public void setMatch(String match) {
		this.match = match;
	}

	/**
	 * This is a getter method for match.
	 * 
	 * @return the match
	 */
	public MatchingType getMatchType() {
		return MatchingType.valueOf(match.toUpperCase());
	}

	/**
	 * This is a getter method for match.
	 * 
	 * @return the match
	 */
	public String getMatch() {
		return match;
	}

	/**
	 * This is a getter method for fit.
	 * 
	 * @return the fit
	 */
	public String getFit() {
		return fit;
	}

	/**
	 * This is a setter method for fit.
	 * 
	 * @param fit
	 *            the fit to set
	 */
	public void setFit(String fit) {
		this.fit = fit;
	}

	/**
	 * This is a getter method for fit.
	 * 
	 * @return the fit
	 */
	public FittingType getFitType() {
		return FittingType.valueOf(fit.toUpperCase());
	}

	@Override
	public String dumpOut(String ind) {
		// TODO Auto-generated method stub
		return ind + " SearchFieldString: index-field: "
				+ indexField + ", search-field: " + searchField + ", type: "
				+ type + ", default: " + defaultIndex + ", weight: " + weight
				+ ", match: " + match + ", fit: " + fit + "\n";
	}
}
