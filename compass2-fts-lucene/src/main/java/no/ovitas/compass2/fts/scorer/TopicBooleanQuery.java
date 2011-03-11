/**
 * 
 */
package no.ovitas.compass2.fts.scorer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import no.ovitas.compass2.exception.CompassErrorID;
import no.ovitas.compass2.exception.CompassException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ComplexExplanation;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.Weight;

/**
 * @class TopicBooleanQuery
 * @project compass2-fts-lucene
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.09.07.
 * 
 */
public class TopicBooleanQuery extends Query {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BooleanQuery booleanQuery;

	public TopicBooleanQuery() {
		booleanQuery = new BooleanQuery();
	}

	public void add(Query query) {
		booleanQuery.add(query, Occur.SHOULD);
	}

	@Override
	public Weight createWeight(Searcher searcher) throws IOException {
		return new TopicBooleanWeight(searcher);
	}

	@Override
	public Query rewrite(IndexReader reader) throws IOException {
		Query rewrite = booleanQuery.rewrite(reader);
		TopicBooleanQuery clone = null;
		if (rewrite instanceof BooleanQuery) {
			if (rewrite != booleanQuery) {
				clone = (TopicBooleanQuery) this.clone();
				clone.booleanQuery = (BooleanQuery) rewrite;
			}
		} else {
			return rewrite;
		}
		if (clone != null) {
			return clone;
		}
		return this;
	}

	protected class TopicBooleanWeight extends Weight {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected Similarity similarity;
		private final Searcher searcher;
		private List<Weight> weights;
		private List<BooleanClause> clauses;

		public TopicBooleanWeight(Searcher searcher) {
			this.searcher = searcher;
			this.similarity = getSimilarity(searcher);
			weights = new ArrayList<Weight>();

			clauses = Arrays.asList(booleanQuery.getClauses());

			try {
				Query query;
				for (BooleanClause booleanClause : clauses) {
					query = booleanClause.getQuery();
					weights.add(query.createWeight(searcher));
				}
			} catch (IOException e) {
				throw new CompassException(CompassErrorID.FTS_QUERY_ERROR, "Exception occurred when create query weight!", e);
			}
		}

		@Override
		public Explanation explain(IndexReader reader, int doc)
				throws IOException {
			ComplexExplanation sumExpl = new ComplexExplanation();
			sumExpl.setDescription("sum of:");
			int coord = 0;
			int maxCoord = 0;
			float sum = 0.0f;
			boolean fail = false;
			int shouldMatchCount = 0;
			Iterator<BooleanClause> cIter = clauses.iterator();
			for (Iterator<Weight> wIter = weights.iterator(); wIter.hasNext();) {
				Weight w = wIter.next();
				BooleanClause c = cIter.next();
				if (w.scorer(reader, true, true) == null) {
					continue;
				}
				Explanation e = w.explain(reader, doc);
				if (!c.isProhibited())
					maxCoord++;
				if (e.isMatch()) {
					if (!c.isProhibited()) {
						sumExpl.addDetail(e);
						sum += e.getValue();
						coord++;
					} else {
						Explanation r = new Explanation(0.0f,
								"match on prohibited clause ("
										+ c.getQuery().toString() + ")");
						r.addDetail(e);
						sumExpl.addDetail(r);
						fail = true;
					}
					if (c.getOccur() == Occur.SHOULD)
						shouldMatchCount++;
				} else if (c.isRequired()) {
					Explanation r = new Explanation(0.0f,
							"no match on required clause ("
									+ c.getQuery().toString() + ")");
					r.addDetail(e);
					sumExpl.addDetail(r);
					fail = true;
				}
			}
			if (fail) {
				sumExpl.setMatch(Boolean.FALSE);
				sumExpl.setValue(0.0f);
				sumExpl.setDescription("Failure to meet condition(s) of required/prohibited clause(s)");
				return sumExpl;
			}

			sumExpl.setMatch(0 < coord ? Boolean.TRUE : Boolean.FALSE);
			sumExpl.setValue(sum);

			float coordFactor = similarity.coord(coord, maxCoord);
			if (coordFactor == 1.0f) // coord is no-op
				return sumExpl; // eliminate wrapper

			ComplexExplanation result = new ComplexExplanation(
					sumExpl.isMatch(), sum * coordFactor, "product of:");
			result.addDetail(sumExpl);
			result.addDetail(new Explanation(coordFactor, "coord(" + coord
					+ "/" + maxCoord + ")"));
			return result;
		}

		@Override
		public Query getQuery() {
			return TopicBooleanQuery.this;
		}

		@Override
		public float getValue() {
			return getBoost();
		}

		@Override
		public void normalize(float norm) {
			norm *= getBoost();

			for (Weight weight : weights) {
				weight.normalize(norm);
			}
		}

		@Override
		public Scorer scorer(IndexReader reader, boolean scoreDocsInOrder,
				boolean topScorer) throws IOException {

			List<Scorer> scorers = new ArrayList<Scorer>();
			Scorer scorer;
			for (Weight weight : weights) {
				scorer = weight.scorer(reader, true, false);
				scorers.add(scorer);
			}

			return new TopicBooleanScorer(similarity, scorers);

		}

		@Override
		public float sumOfSquaredWeights() throws IOException {
			float sum = 0.0f;
			for (Weight weight : weights) {

				sum += weight.sumOfSquaredWeights();
			}

			sum *= getBoost() * getBoost(); // boost each sub-weight

			return sum;
		}

	}

	@Override
	public String toString(String field) {

		return field + ":" + booleanQuery.toString();
	}

}
