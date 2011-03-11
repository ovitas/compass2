package no.ovitas.compass2.kb.store.model;

import java.util.Comparator;

/**
 * <code>Comparator</code> implementation for <code>TopicEntityWrapper</code>.
 * <p>
 * <i>Note: this comparator imposes orderings that are inconsistent with
 * equals.</i>
 * </p>
 * 
 * @author Csaba Daniel
 * 
 */
public class TopicEntityWrapperComparator implements
		Comparator<TopicEntityWrapper> {

	public int compare(TopicEntityWrapper o1, TopicEntityWrapper o2) {
		if ((o1.isComplete() == false) && (o2.isComplete() == true))
			return -1;
		if ((o2.isComplete() == false) && (o1.isComplete() == true))
			return 1;

		if (o1.getBestPathWeight() > o2.getBestPathWeight())
			return -1;
		if (o2.getBestPathWeight() > o1.getBestPathWeight())
			return 1;

		if (o1.getHopCount() < o2.getHopCount())
			return -1;
		if (o2.getHopCount() < o1.getHopCount())
			return 1;

		return 0;
	}
}
