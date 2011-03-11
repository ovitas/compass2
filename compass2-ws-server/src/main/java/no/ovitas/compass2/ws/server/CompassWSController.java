package no.ovitas.compass2.ws.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import no.ovitas.compass2.config.CompassManager;
import no.ovitas.compass2.config.factory.CompassManagerFactory;
import no.ovitas.compass2.config.settings.SearchField;
import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.Scope;
import no.ovitas.compass2.search.FullTextFieldCriteria;
import no.ovitas.compass2.search.FullTextQuery;
import no.ovitas.compass2.search.FullTextQueryResult;
import no.ovitas.compass2.search.KnowledgeBaseResult;
import no.ovitas.compass2.search.KnowledgeBaseTreeResult;
import no.ovitas.compass2.search.QueryResult;
import no.ovitas.compass2.search.TopicListQueryResult;
import no.ovitas.compass2.search.TopicQuery;
import no.ovitas.compass2.search.TopicResult;
import no.ovitas.compass2.search.TopicResultNode;
import no.ovitas.compass2.search.TopicTreeQueryResult;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring MVC controller that provides REST type web service interface for the
 * query operations of <code>no.ovitas.compass2.config.CompassManager</code>.
 * 
 * @author Csaba Daniel
 */
@Controller
public class CompassWSController {

	private static final String VIEW_NAME = "compassWSView";
	private static final String RESULT_NAME = "compassWSResult";

	/**
	 * Get the collection of knwowledge base descriptors of the available
	 * knowledge bases in the current Compass instance.
	 * <p>
	 * URL mapping: <code>/kbs</code>
	 * </p>
	 * 
	 * @return the <code>ModelAndView</code> object with the result
	 */
	@RequestMapping("/kbs")
	public ModelAndView listKnowledgeBases() {
		CompassManagerFactory factory = CompassManagerFactory.getInstance();
		CompassManager manager = factory.getCompassManager();

		Collection<KnowledgeBaseDescriptor> kbds = manager.listKnowledgeBases();

		ModelAndView mav = new ModelAndView(VIEW_NAME, RESULT_NAME, kbds);
		return mav;
	}

	/**
	 * Get the knwowledge base descriptor of the specified knowledge base in the
	 * current Compass instance.
	 * <p>
	 * URL mapping: <code>/kbs/{knowledgeBaseId}</code>
	 * </p>
	 * 
	 * @param knowledgeBaseId
	 *            the id of the knowledge base to get the descriptor for
	 * @return the <code>ModelAndView</code> object with the result
	 */
	@RequestMapping("/kbs/{knowledgeBaseId}")
	public ModelAndView listKnowledgeBases(@PathVariable long knowledgeBaseId) {
		CompassManagerFactory factory = CompassManagerFactory.getInstance();
		CompassManager manager = factory.getCompassManager();

		KnowledgeBaseDescriptor kbd = manager.getKnowledgeBase(knowledgeBaseId);

		ModelAndView mav = new ModelAndView(VIEW_NAME, RESULT_NAME, kbd);
		return mav;
	}

	/**
	 * Get the relation types available in the specified knowledge base.
	 * <p>
	 * URL mapping: <code>/kbs/{knowledgeBaseId}/relationTypes</code>
	 * </p>
	 * 
	 * @param knowledgeBaseId
	 *            the id of the knowledge base
	 * @return the <code>ModelAndView</code> object with the result
	 */
	@RequestMapping("/kbs/{knowledgeBaseId}/relationTypes")
	public ModelAndView getRelationTypes(@PathVariable long knowledgeBaseId) {
		CompassManagerFactory factory = CompassManagerFactory.getInstance();
		CompassManager manager = factory.getCompassManager();

		KnowledgeBaseDescriptor kb = manager.getKnowledgeBase(knowledgeBaseId);
		Collection<RelationType> relTypes = manager.getRelationTypes(kb);

		ModelAndView mav = new ModelAndView(VIEW_NAME, RESULT_NAME, relTypes);
		return mav;
	}

	/**
	 * Get the scopes available in the specified knowledge base.
	 * <p>
	 * URL mapping: <code>/kbs/{knowledgeBaseId}/scopes</code>
	 * </p>
	 * 
	 * @param knowledgeBaseId
	 *            the id of the knowledge base
	 * @return the <code>ModelAndView</code> object with the result
	 */
	@RequestMapping("/kbs/{knowledgeBaseId}/scopes")
	public ModelAndView getScopes(@PathVariable long knowledgeBaseId) {
		CompassManagerFactory factory = CompassManagerFactory.getInstance();
		CompassManager manager = factory.getCompassManager();

		KnowledgeBaseDescriptor kb = manager.getKnowledgeBase(knowledgeBaseId);
		Collection<Scope> scopes = manager.getScopes(kb);

		ModelAndView mav = new ModelAndView(VIEW_NAME, RESULT_NAME, scopes);
		return mav;
	}

	/**
	 * Perform a topic list search and retrieve the results in topic list
	 * format.
	 * <p>
	 * <i>Note: the search is performed with the default search configuration of
	 * the knowledge</i>
	 * </p>
	 * <p>
	 * URL mapping: <code>/kbs/{knowledgeBaseId}/topicList/{term}</code>
	 * </p>
	 * 
	 * @param knowledgeBaseId
	 *            the knowledge base to perform the search in
	 * @param term
	 *            the term to search for
	 * @return the <code>ModelAndView</code> object with the result
	 */
	@RequestMapping("/kbs/{knowledgeBaseId}/topicList/{term}")
	public ModelAndView topicList(@PathVariable long knowledgeBaseId,
			@PathVariable String term) {
		Collection<TopicResult> tResults = new ArrayList<TopicResult>();

		CompassManagerFactory factory = CompassManagerFactory.getInstance();
		CompassManager manager = factory.getCompassManager();

		KnowledgeBaseDescriptor kbd = manager.getKnowledgeBase(knowledgeBaseId);

		TopicQuery tq = manager.createTopicQuery();
		tq.addTerm(term);
		tq.createTopicCriteria(kbd);

		TopicListQueryResult listResult = manager.search(tq);
		Collection<KnowledgeBaseResult> kbResults = listResult.getResult();
		if (!kbResults.isEmpty()) {
			KnowledgeBaseResult kbResult = kbResults.iterator().next();
			tResults = kbResult.getTermResult(term);
		}

		ModelAndView mav = new ModelAndView(VIEW_NAME, RESULT_NAME, tResults);
		return mav;
	}

	/**
	 * Perform a topic list search based on the provided XML-based query and
	 * retrieve the results in topic list format.
	 * <p>
	 * URL mapping: <code>/topicList</code>
	 * </p>
	 * 
	 * @param topicQuery
	 *            the XML-based topic query, for element description see <a
	 *            href="../../../../../compass-request.html">XSD
	 *            documentation</a>
	 * @return the <code>ModelAndView</code> object with the result
	 */
	@RequestMapping("/topicList")
	public ModelAndView topicList(
			@RequestParam(required = true) String topicQuery) {
		CompassManagerFactory factory = CompassManagerFactory.getInstance();
		CompassManager manager = factory.getCompassManager();

		CompassWSUnmarshaller um = new CompassWSUnmarshaller(manager);
		TopicQuery tq = um.unmarshal(topicQuery, TopicQuery.class);
		TopicListQueryResult listResult = manager.search(tq);

		Collection<KnowledgeBaseResult> kbResults = listResult.getResult();

		ModelAndView mav = new ModelAndView(VIEW_NAME, RESULT_NAME, kbResults);
		return mav;
	}

	/**
	 * Perform a topic tree search and retrieve the results in topic tree
	 * format.
	 * <p>
	 * <i>Note: the search is performed with the default search configuration of
	 * the knowledge</i>
	 * </p>
	 * <p>
	 * URL mapping: <code>/kbs/{knowledgeBaseId}/topicTree/{term}</code>
	 * </p>
	 * 
	 * @param knowledgeBaseId
	 *            the knowledge base to perform the search in
	 * @param term
	 *            the term to search for
	 * @return the <code>ModelAndView</code> object with the result
	 */
	@RequestMapping("/kbs/{knowledgeBaseId}/topicTree/{term}")
	public ModelAndView topicTree(@PathVariable long knowledgeBaseId,
			@PathVariable String term) {
		Collection<TopicResultNode> tResults = new ArrayList<TopicResultNode>();

		CompassManagerFactory factory = CompassManagerFactory.getInstance();
		CompassManager manager = factory.getCompassManager();

		KnowledgeBaseDescriptor kbd = manager.getKnowledgeBase(knowledgeBaseId);

		TopicQuery tq = manager.createTopicQuery();
		tq.addTerm(term);
		tq.createTopicCriteria(kbd);

		TopicTreeQueryResult treeResult = manager.searchTree(tq);
		Collection<KnowledgeBaseTreeResult> kbResults = treeResult.getResult();
		if (!kbResults.isEmpty()) {
			KnowledgeBaseTreeResult kbResult = kbResults.iterator().next();
			tResults = kbResult.getTermResult(term);
		}

		ModelAndView mav = new ModelAndView(VIEW_NAME, RESULT_NAME, tResults);
		return mav;
	}

	/**
	 * Perform a topic tree search based on the provided XML-based query and
	 * retrieve the results in topic tree format.
	 * <p>
	 * URL mapping: <code>/topicTree</code>
	 * </p>
	 * 
	 * @param topicQuery
	 *            the XML-based topic query, for element description see <a
	 *            href="../../../../../compass-request.html">XSD
	 *            documentation</a>
	 * @return the <code>ModelAndView</code> object with the result
	 */
	@RequestMapping("/topicTree")
	public ModelAndView topicTree(
			@RequestParam(required = true) String topicQuery) {
		CompassManagerFactory factory = CompassManagerFactory.getInstance();
		CompassManager manager = factory.getCompassManager();

		CompassWSUnmarshaller um = new CompassWSUnmarshaller(manager);
		TopicQuery tq = um.unmarshal(topicQuery, TopicQuery.class);

		TopicTreeQueryResult treeResult = manager.searchTree(tq);

		Collection<KnowledgeBaseTreeResult> kbResults = treeResult.getResult();

		ModelAndView mav = new ModelAndView(VIEW_NAME, RESULT_NAME, kbResults);
		return mav;
	}

	/**
	 * Perform a complex search (topic and full text) for a single term and
	 * retrieve the results.
	 * <p>
	 * <i>Note: the search is performed with the default search configuration of
	 * the knowledge</i>
	 * </p>
	 * <p>
	 * URL mapping: <code>/kbs/{knowledgeBaseId}/search/{term}</code>
	 * </p>
	 * 
	 * @param knowledgeBaseId
	 *            the knowledge base to perform the search in
	 * @param term
	 *            the term to search for
	 * @return the <code>ModelAndView</code> object with the result
	 */
	@RequestMapping("/kbs/{knowledgeBaseId}/search/{term}")
	public ModelAndView search(@PathVariable long knowledgeBaseId,
			@PathVariable String term) {
		CompassManagerFactory factory = CompassManagerFactory.getInstance();
		CompassManager manager = factory.getCompassManager();

		KnowledgeBaseDescriptor kbd = manager.getKnowledgeBase(knowledgeBaseId);

		TopicQuery tq = manager.createTopicQuery();
		tq.addTerm(term);
		tq.createTopicCriteria(kbd);

		FullTextQuery ftq = manager.createFullTextQuery();

		Collection<SearchField> sfs = manager.getDefaultSearchFields();
		for (SearchField sf : sfs) {
			FullTextFieldCriteria ftfc = ftq.createCriteria(sf.getIndexField());
			ftfc.addTerm(term);
		}

		QueryResult result = manager.search(tq, ftq);
		FullTextQueryResult ftqResult = result.getFullTextQueryResult();
		Collection<Hit> hits = ftqResult.getHits();

		ModelAndView mav = new ModelAndView(VIEW_NAME, RESULT_NAME, hits);
		return mav;
	}

	/**
	 * Perform a search (topic and/or full text) according to the provided query
	 * parameters and retrieve the results.
	 * <p>
	 * URL mapping: <code>/search</code>
	 * </p>
	 * 
	 * @param topicQuery
	 *            the XML-based topic query, for element description see <a
	 *            href="../../../../../compass-request.html">XSD
	 *            documentation</a>
	 * @param fullTextQuery
	 *            the XML-based full text query, for element description see <a
	 *            href="../../../../../compass-request.html">XSD
	 *            documentation</a>
	 * @return the <code>ModelAndView</code> object with the result
	 */
	@RequestMapping("/search")
	public ModelAndView search(
			@RequestParam(required = true) String topicQuery,
			@RequestParam(required = true) String fullTextQuery) {
		CompassManagerFactory factory = CompassManagerFactory.getInstance();
		CompassManager manager = factory.getCompassManager();

		CompassWSUnmarshaller um = new CompassWSUnmarshaller(manager);
		TopicQuery tq = um.unmarshal(topicQuery, TopicQuery.class);
		FullTextQuery ftq = um.unmarshal(fullTextQuery, FullTextQuery.class);

		QueryResult result = manager.search(tq, ftq);
		FullTextQueryResult ftqResult = result.getFullTextQueryResult();
		Collection<Hit> hits = ftqResult.getHits();

		ModelAndView mav = new ModelAndView(VIEW_NAME, RESULT_NAME, hits);
		return mav;
	}

	/**
	 * Get a single spelling suggestion for a specified term.
	 * <p>
	 * URL mapping: <code>/spelling/{term}</code>
	 * </p>
	 * 
	 * @param term
	 *            the term the suggestion is sought for
	 * @return the <code>ModelAndView</code> object with the result
	 */
	@RequestMapping("/spelling/{term}")
	public ModelAndView getSpellingSuggestion(@PathVariable String term) {
		CompassManagerFactory factory = CompassManagerFactory.getInstance();
		CompassManager manager = factory.getCompassManager();

		String spelling = manager.getSpellingSuggestion(term);
		Term spellingTerm = new Term(spelling);

		ModelAndView mav = new ModelAndView(VIEW_NAME, RESULT_NAME,
				spellingTerm);
		return mav;
	}

	/**
	 * Get the list of spelling suggestion for a specified term.
	 * <p>
	 * URL mapping: <code>/spellings/{term}</code>
	 * </p>
	 * 
	 * @param term
	 *            the term the suggestion is sought for
	 * @return the <code>ModelAndView</code> object with the result
	 */
	@RequestMapping("/spellings/{term}")
	public ModelAndView getSpellingSuggestions(@PathVariable String term) {
		CompassManagerFactory factory = CompassManagerFactory.getInstance();
		CompassManager manager = factory.getCompassManager();

		List<String> spellings = manager.getSpellingSuggestions(term);

		List<Term> spellingTerms = new ArrayList<Term>();
		for (String spelling : spellings) {
			spellingTerms.add(new Term(spelling));
		}

		ModelAndView mav = new ModelAndView(VIEW_NAME, RESULT_NAME,
				spellingTerms);
		return mav;
	}

	/**
	 * Get the stem of a specified word.
	 * <p>
	 * URL mapping: <code>/stem/{word}</code>
	 * </p>
	 * 
	 * @param word
	 *            the word to be stemmed
	 * @return the <code>ModelAndView</code> object with the result
	 */
	@RequestMapping("/stem/{word}")
	public ModelAndView getStem(@PathVariable String word) {
		CompassManagerFactory factory = CompassManagerFactory.getInstance();
		CompassManager manager = factory.getCompassManager();

		String stem = manager.getStem(word);
		Term spellingTerm = new Term(stem);

		ModelAndView mav = new ModelAndView(VIEW_NAME, RESULT_NAME,
				spellingTerm);
		return mav;
	}
}
