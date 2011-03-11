/**
 * 
 */
package no.ovitas.compass2.search;

import java.util.Collection;

import no.ovitas.compass2.model.Hit;

/**
 * @author gyalai
 *
 */
public interface FullTextQueryResult {

	/**
	 * Get hits results.
	 * 
	 * @return
	 */
	public Collection<Hit> getHits();
}
