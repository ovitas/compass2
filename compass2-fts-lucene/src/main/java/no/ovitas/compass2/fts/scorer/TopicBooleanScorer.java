/**
 * 
 */
package no.ovitas.compass2.fts.scorer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import no.ovitas.compass2.exception.CompassErrorID;
import no.ovitas.compass2.exception.CompassException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Similarity;

/**
 * @author gyalai
 * 
 */
public class TopicBooleanScorer extends Scorer {

	private static final class TopicScorerCollector extends Collector {
		private BucketTable bucketTable;
		private Scorer scorer;

		public TopicScorerCollector(BucketTable bucketTable) {
			this.bucketTable = bucketTable;
		}

		@Override
		public final void collect(final int doc) throws IOException {
			final BucketTable table = bucketTable;
			final int i = doc & BucketTable.MASK;
			Bucket bucket = table.buckets[i];

			if (bucket == null)
				table.buckets[i] = bucket = new Bucket();

			if (bucket.doc != doc) { // invalid bucket
				bucket.doc = doc; // set doc
				bucket.score = scorer.score(); // initialize score
				bucket.coord = 1; // initialize coord
				bucket.next = null;

				if (table.first == null) { // not first element
					table.first = bucket;
					table.firstIndex = i;
				} else if (table.firstIndex > i) { // if first element id is greater then current
					bucket.next = table.first;
					table.first = bucket;
					table.firstIndex = i;
				} else {
					Bucket tmp;
					Bucket akt;
					for (int j = i - 1; j >= table.firstIndex; j-- ) {
						akt = table.buckets[j];
						if (akt != null) {
							tmp = akt.next;
							akt.next = bucket;
							bucket.next = tmp;
							break;
						}
					}
					
				}

			} else { // valid bucket
				bucket.score += scorer.score(); // increment score
				bucket.coord++; // increment coord
			}
		}

		@Override
		public void setNextReader(IndexReader reader, int docBase) {
			// not needed by this implementation
		}

		@Override
		public void setScorer(Scorer scorer) throws IOException {
			this.scorer = scorer;
		}

		@Override
		public boolean acceptsDocsOutOfOrder() {
			return true;
		}

	}

	// An internal class which is used in score(Collector, int) for setting the
	// current score. This is required since Collector exposes a setScorer
	// method
	// and implementations that need the score will call scorer.score().
	// Therefore the only methods that are implemented are score() and doc().
	private static final class BucketScorer extends Scorer {

		float score;
		int doc = NO_MORE_DOCS;

		public BucketScorer() {
			super(null);
		}

		@Override
		public int advance(int target) throws IOException {
			return NO_MORE_DOCS;
		}

		@Override
		public int docID() {
			return doc;
		}

		@Override
		public int nextDoc() throws IOException {
			return NO_MORE_DOCS;
		}

		@Override
		public float score() throws IOException {
			return score;
		}

	}

	static final class Bucket {
		int doc = -1; // tells if bucket is valid
		float score; // incremental score
		int coord; // count of terms in score
		Bucket next; // next valid bucket
	}

	/** A simple hash table of document scores within a range. */
	static final class BucketTable {
		public static final int SIZE = 1 << 11;
		public static final int MASK = SIZE - 1;

		final Bucket[] buckets = new Bucket[SIZE];
		Bucket first = null; // head of valid list
		int firstIndex = -1; // head of valid list
		Bucket tail = null;
		int tailIndex = -1; // tail of valid list

		public BucketTable() {
		}

		public Collector newCollector() {
			return new TopicScorerCollector(this);
		}

		public final int size() {
			return SIZE;
		}
	}

	static final class SubScorer {
		public Scorer scorer;
		public Collector collector;
		public SubScorer next;

		public SubScorer(Scorer scorer, Collector collector, SubScorer next)
				throws IOException {
			this.scorer = scorer;

			this.collector = collector;
			this.next = next;
		}
	}

	private SubScorer scorers = null;
	private BucketTable bucketTable = new BucketTable();
	private int maxCoord = 1;
	private final float[] coordFactors;
	private int end;
	private Bucket current;
	private int doc = -1;;

	protected TopicBooleanScorer(Similarity similarity,
			List<Scorer> scorersParam) throws IOException {
		super(similarity);

		for (Scorer scorer : scorersParam) {
			maxCoord++;
			if (scorer.nextDoc() != NO_MORE_DOCS) {
				scorers = new SubScorer(scorer, bucketTable.newCollector(),
						scorers);
			}
		}

		Similarity sim = getSimilarity();
		coordFactors = new float[maxCoord];
		for (int i = 0; i < maxCoord; i++) {
			coordFactors[i] = sim.coord(i, maxCoord - 1);
		}
	}

	@Override
	public float score() throws IOException {
		return (float) Math.sqrt(current.score / current.coord);
	}

	@Override
	public void score(Collector collector) throws IOException {

		score(collector, Integer.MAX_VALUE, nextDoc());
	}

	@Override
	protected boolean score(Collector collector, int max, int firstDocID)
			throws IOException {
		boolean more;
		Bucket tmp;
		BucketScorer bs = new BucketScorer();
		// The internal loop will set the score and doc before calling collect.
		collector.setScorer(bs);
		do {
			bucketTable.first = null;

			while (current != null) { // more queued

				if (current.doc >= max) {
					tmp = current;
					current = current.next;
					tmp.next = bucketTable.first;
					bucketTable.first = tmp;
					continue;
				}

				if (current.coord >= 0) {
					bs.score = (float) Math.sqrt(current.score / current.coord);
					bs.doc = current.doc;
					collector.collect(current.doc);
				}

				current = current.next; // pop the queue

			}

			if (bucketTable.first != null) {
				current = bucketTable.first;
				bucketTable.first = current.next;
				return true;
			}

			// refill the queue
			more = false;
			end += BucketTable.SIZE;
			for (SubScorer sub = scorers; sub != null; sub = sub.next) {
				int subScorerDocID = sub.scorer.docID();
				if (subScorerDocID != NO_MORE_DOCS) {
					Method method;
					try {
						method = Scorer.class.getMethod("score",
								Collector.class, int.class, int.class);

						Object result = method.invoke(sub.scorer,
								sub.collector, end, subScorerDocID);
						more |= (Boolean) result;
					} catch (SecurityException e) {
						throw new CompassException(
								CompassErrorID.FTS_QUERY_ERROR,
								"Exception occurred when create query score!",
								e);
					} catch (NoSuchMethodException e) {
						throw new CompassException(
								CompassErrorID.FTS_QUERY_ERROR,
								"Exception occurred when create query score!",
								e);
					} catch (IllegalArgumentException e) {
						throw new CompassException(
								CompassErrorID.FTS_QUERY_ERROR,
								"Exception occurred when create query score!",
								e);
					} catch (IllegalAccessException e) {
						throw new CompassException(
								CompassErrorID.FTS_QUERY_ERROR,
								"Exception occurred when create query score!",
								e);
					} catch (InvocationTargetException e) {
						throw new CompassException(
								CompassErrorID.FTS_QUERY_ERROR,
								"Exception occurred when create query score!",
								e);
					}
				}
			}
			current = bucketTable.first;

		} while (current != null || more);

		return false;
	}

	@Override
	public int docID() {
		System.out.println("[TQ]: " + this + " Doc: " + current.doc);
		return doc;
	}

	@Override
	public int nextDoc() throws IOException {
		boolean more;
		do {
			while (bucketTable.first != null) { // more queued

				current = bucketTable.first;
				bucketTable.first = current.next; // pop the queue

				// check prohibited & required, and minNrShouldMatch
				if (current.coord >= 0) {
					System.out.println("[TQ]: " + this + " Next: "
							+ current.doc);
					return doc = current.doc;
				}
			}

			// refill the queue
			more = false;
			end += BucketTable.SIZE;
			for (SubScorer sub = scorers; sub != null; sub = sub.next) {
				Scorer scorer = sub.scorer;
				sub.collector.setScorer(scorer);
				int doc = scorer.docID();
				while (doc < end) {
					sub.collector.collect(doc);
					doc = scorer.nextDoc();
				}
				more |= (doc != NO_MORE_DOCS);
			}
		} while (bucketTable.first != null || more);

		return doc = NO_MORE_DOCS;
	}

	@Override
	public int advance(int target) throws IOException {
		int doc;
		while ((doc = nextDoc()) < target) {
		}
		return doc;

	}

}
