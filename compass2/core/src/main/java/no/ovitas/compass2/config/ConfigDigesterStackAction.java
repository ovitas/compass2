/**
 * 
 */
package no.ovitas.compass2.config;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.StackAction;

/**
 * @author magyar
 *
 */
public class ConfigDigesterStackAction implements StackAction {

	/* (non-Javadoc)
	 * @see org.apache.commons.digester.StackAction#onPop(org.apache.commons.digester.Digester, java.lang.String, java.lang.Object)
	 */
	public Object onPop(Digester d, String stackName, Object o) {
		if(o instanceof KnowledgeBases){
			((KnowledgeBases)o).postProcess();
		}
		return o;

	}

	/* (non-Javadoc)
	 * @see org.apache.commons.digester.StackAction#onPush(org.apache.commons.digester.Digester, java.lang.String, java.lang.Object)
	 */
	public Object onPush(Digester d, String stackName, Object o) {
		return o;
	}

}
