/**
 * 
 */
package no.ovitas.compass2.fts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.exception.CompassException;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.fts.scorer.TopicBooleanQuery;
import no.ovitas.compass2.model.ConnectingType;
import no.ovitas.compass2.model.FittingType;
import no.ovitas.compass2.model.FullTextFieldType;
import no.ovitas.compass2.model.MatchingType;
import no.ovitas.compass2.model.FullTextSearchConfig;
import no.ovitas.compass2.search.FullTextFieldCriteria;
import no.ovitas.compass2.search.FullTextQuery;
import no.ovitas.compass2.search.FullTextTopicCriteria;
import no.ovitas.compass2.search.FullTextTopicQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;

/**
 * @class TopicQueryBuilder
 * @project compass2-fts-lucene
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.09.25.
 * 
 */
public class TopicQueryBuilder implements QueryBuilder {

	private BooleanQuery query;

	public TopicQueryBuilder() {

	}

	@Override
	public void startCreateQuery() {
		query = new BooleanQuery();
	}

	@Override
	public void addCriteria(FullTextFieldCriteria fullTextFieldCriteria,
			FullTextTopicQuery fullTextTopicQuery, boolean fuzzy, IndexReader indexReader) {

		BooleanQuery fieldQuery = new BooleanQuery();

		Collection<String> terms = fullTextFieldCriteria.getTerms();

		Query tmpQuery;

		FittingType fit = fullTextFieldCriteria.getFittingType();
		MatchingType match = fullTextFieldCriteria.getMatchingType();
		String field = fullTextFieldCriteria.getFieldName();
		boolean fuzzySearch = fuzzy | fullTextFieldCriteria.isFuzzySearch();

		if (fullTextTopicQuery != null) {

			if (terms != null && !terms.isEmpty()) {
				for (String term : terms) {
					tmpQuery = createQuery(field, term,
							fullTextTopicQuery.getFullTextTopicCriteria(term),
							fit, match, fuzzySearch, indexReader);

					fieldQuery.add(tmpQuery, getOccur(match));
				}
			}
		} else {
			if (terms != null && !terms.isEmpty()) {
				for (String term : terms) {
					tmpQuery = createQuery(field, term, null, fit, match,
							fuzzySearch, indexReader);

					fieldQuery.add(tmpQuery, getOccur(match));
				}
			}
		}

		fieldQuery.setBoost((float) fullTextFieldCriteria.getBoost());

		query.add(fieldQuery,
				getOccur(fullTextFieldCriteria.getConnectionType()));

	}

	private Query createQuery(String field, String term,
			FullTextTopicCriteria fullTextTopicCriteria, FittingType fit,
			MatchingType match, boolean fuzzy, IndexReader indexReader) {

		BooleanQuery termQuery = new BooleanQuery();

		termQuery.add(createQuery(field, term, fit, fuzzy, indexReader), Occur.SHOULD);

		if (fullTextTopicCriteria != null) {
			Map<String, Double> topicsWithWeight = fullTextTopicCriteria
					.getTopicsWithWeight();

			TopicBooleanQuery topicQueries = new TopicBooleanQuery();
			String topicName;
			Query topicQuery;
			int counter = 0;
			for (Entry<String, Double> topicWithWeight : topicsWithWeight
					.entrySet()) {
				topicName = topicWithWeight.getKey().toLowerCase();

				topicQuery = createQuery(field, topicName, fit, fuzzy,indexReader);

				topicQuery.setBoost(topicWithWeight.getValue().floatValue());

				topicQueries.add(topicQuery);

				counter++;
				if (counter > 1022) {
					break;
				}
			}

			termQuery.add(topicQueries, Occur.SHOULD);
		}

		return termQuery;
	}

	private Query createQuery(String field, String term, FittingType fit,
			boolean fuzzy, IndexReader indexReader) {
		Query q = null;

		String[] split = term.split(" ");

		LinkedList<Term> terms = new LinkedList<Term>();

		for (String termString : split) {
			terms.add(new Term(field, termString));
		}

		if (terms.size() == 1) {
			if (fuzzy) {
				q = new FuzzyQuery(new Term(field, term));
			} else {
				if (fit.equals(FittingType.PREFIX)) {
					q = new PrefixQuery(new Term(field, term));
				} else {
					q = new TermQuery(new Term(field, term));
				}

			}
		} else {
			MultiPhraseQuery mp = new MultiPhraseQuery();
			
			Iterator<Term> iterator = terms.iterator();
			Term termValue;
			
			ArrayList<Term> subTerms = new ArrayList<Term>();
			
			while (iterator.hasNext()) {
				termValue = iterator.next();
				
				if (fit.equals(FittingType.PREFIX) && !iterator.hasNext()) {
					try {
						TermEnum termEnum = indexReader.terms(termValue);
						do {			
							if (termEnum.term() == null) {
								break;
							}
							subTerms.add(termEnum.term());
						} while(!termEnum.next());
						
						mp.add(subTerms.toArray(new Term[subTerms.size()]));
					} catch (IOException e) {
						throw new CompassException("Index reader can't read the repository!", e);
					}
				} else {
					mp.add(termValue);
				}
			}
			
			q = mp;
		}

		return q;
	}

	@Override
	public Query getCreatedQuery() {
		return query;
	}

	@Override
	public void setConfiguration(ConfigurationManager manager)
			throws ConfigurationException {
	}

	@Override
	public void init(Properties properties) throws ConfigurationException {

	}

	private Occur getOccur(MatchingType matchingType) {
		switch (matchingType) {
		case MATCH_ALL:
			return Occur.MUST;
		case MATCH_ANY:
			return Occur.SHOULD;
		default:
			throw new AssertionError("Never happend!");
		}
	}

	private Occur getOccur(ConnectingType connection) {
		switch (connection) {
		case AND:
			return Occur.MUST;
		case OR:
			return Occur.SHOULD;
		case NOT:
			return Occur.MUST_NOT;
		default:
			throw new AssertionError("Never happend!");
		}
	}

}
