/**
 * 
 */
package no.ovitas.compass2.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import no.ovitas.compass2.search.FullTextTopicQuery;
import no.ovitas.compass2.search.TermTopicWeightResultCollector;

/**
 * @author gyalai
 *
 */
public final class SearchUtil {

	
	public static FullTextTopicQuery convert(TermTopicWeightResultCollector collector) {
		
		if (collector instanceof FullTextTopicQuery) {
			return (FullTextTopicQuery) collector;
		}
		
		throw new ClassCastException("The collector " + collector.getClass() + " can not convert to FullTextTopicQuery!");
	}

	public static Collection<String> splitSearchString(String term) {
		Set<String> splits;
		if (term.contains("\"")) {
			splits = splitQuotationMarks(term);
		} else {
			splits = new HashSet<String>(Arrays.asList(onlySplitExpression(term, " ")));
		}

		return splits;
	}
	
	private static Set<String> splitQuotationMarks(String value) {

		String[] quotations = onlySplitExpression(value, "\"");
		Set<String> set = new HashSet<String>();

		for (int i = 0; i < quotations.length; i++) {
			if (i % 2 != 1) {
				if (!quotations[i].isEmpty()) {
					set.addAll(Arrays.asList(onlySplitExpression(
							quotations[i], " ")));
				}
			} else {
				set.add(quotations[i]);
			}
		}

		return set;

	}

	private static String[] onlySplitExpression(String value, String delimiter) {
		if (value == null || value.isEmpty()) {
			return new String[0];
		}
		return value.split(delimiter);
	}

	public static Collection<String> toLowerCase(
			Collection<String> splittedTerms) {
		
		Collection<String> results = new ArrayList<String>(0);
		
		for (String term : splittedTerms) {
			results.add(term.toLowerCase());
		}
		
		return results;
	}
}
