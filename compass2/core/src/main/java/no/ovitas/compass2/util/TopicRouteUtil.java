/**
 * 
 */
package no.ovitas.compass2.util;

/**
 * @author magyar
 *
 */
public class TopicRouteUtil {

	
	
	protected int hopCount;
	protected int expansionThreshold;
	protected int maxNumberOfNodes;
	
	
	public TopicRouteUtil(int hopCount, int expansionThreshold,
			int maxNumberOfNodes) {
		super();
		this.hopCount = hopCount;
		this.expansionThreshold = expansionThreshold;
		this.maxNumberOfNodes = maxNumberOfNodes;
	}
	
	

}
