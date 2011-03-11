/**
 * 
 */
package no.ovitas.compass2.util;

import java.util.Comparator;

/**
 * @author magyar
 *
 */
public class XPairComparator implements Comparator<XPair<Float,Integer>> {

	/**
	 * 
	 */
	public XPairComparator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(XPair<Float,Integer> o1, XPair<Float,Integer> o2) {
		Float o1Value = o1.getKey();
		Float o2Value = o2.getKey();
		return o2Value.compareTo(o1Value);
	}

}
