package no.ovitas.compass2.search.impl;

import java.util.Collections;
import java.util.Map;

import no.ovitas.compass2.search.TermTopicWeightResultCollector;

abstract class TopicQueryResultImpl {

	protected final Map<String, String> termWithStemPair;

	public TopicQueryResultImpl(Map<String, String> termWithStemPair) {
		super();
		this.termWithStemPair = termWithStemPair;
	}
	
	public Map<String, String> getTermWithStemPair() {
		return termWithStemPair;
	}

	protected void setupCollector(TermTopicWeightResultCollector collector) {
		collector.addTermWithStemPair(Collections.unmodifiableMap(termWithStemPair));
	}
	
}