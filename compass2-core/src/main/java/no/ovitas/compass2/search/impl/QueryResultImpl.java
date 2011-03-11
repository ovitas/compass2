/**
 * 
 */
package no.ovitas.compass2.search.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import no.ovitas.compass2.search.FullTextQueryResult;
import no.ovitas.compass2.search.QueryResult;
import no.ovitas.compass2.search.TopicListQueryResult;
import no.ovitas.compass2.search.TopicTreeQueryResult;

/**
 * @author gyalai
 * 
 */
public class QueryResultImpl implements QueryResult {

	private FullTextQueryResult fullTextQueryResult;

	private TopicListQueryResult topicListQueryResult;

	private TopicTreeQueryResult topicTreeQueryResult;

	@Override
	public FullTextQueryResult getFullTextQueryResult() {
		return fullTextQueryResult;
	}

	public void setFullTextQueryResult(FullTextQueryResult fullTextQueryResult) {
		this.fullTextQueryResult = fullTextQueryResult;
	}

	@Override
	public TopicListQueryResult getTopicListQueryResult() {
		return topicListQueryResult;
	}

	public void setTopicListQueryResult(
			TopicListQueryResult topicListQueryResult) {
		this.topicListQueryResult = topicListQueryResult;
	}

	@Override
	public TopicTreeQueryResult getTopicTreeQueryResult() {
		return topicTreeQueryResult;
	}

	public void setTopicTreeQueryResult(
			TopicTreeQueryResult topicTreeQueryResult) {
		this.topicTreeQueryResult = topicTreeQueryResult;
	}

}
