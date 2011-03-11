/**
 * 
 */
package no.ovitas.compass2.search.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.search.FullTextQueryResult;

/**
 * @author gyalai
 *
 */
public class FullTextQueryResultImpl implements FullTextQueryResult {

	private Collection<Hit> hits;
	
	@Override
	public Collection<Hit> getHits() {
		
		if (hits == null) {
			return Collections.unmodifiableCollection(new ArrayList<Hit>());
		}
		
		return Collections.unmodifiableCollection(hits);
	}
	
	public void setHits(Collection<Hit> hits) {
		this.hits = hits;
	}

}
