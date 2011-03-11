package no.ovitas.compass2.web.client.controllers.content;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import no.ovitas.compass2.config.CompassManager;
import no.ovitas.compass2.config.factory.CompassManagerFactory;
import no.ovitas.compass2.config.settings.SearchField;
import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.search.FullTextFieldCriteria;
import no.ovitas.compass2.search.FullTextQuery;
import no.ovitas.compass2.search.QueryResult;
import no.ovitas.compass2.search.TopicCriteria;
import no.ovitas.compass2.search.TopicQuery;
import no.ovitas.compass2.util.SearchUtil;
import no.ovitas.compass2.web.client.controllers.form.DefaultSearchConfig;
import no.ovitas.compass2.web.client.controllers.form.FormSearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/**")
public class SearchController {

	private static Logger logger = LoggerFactory
			.getLogger(SearchController.class);

	@RequestMapping(value = "/suggestionJson")
	public void searchJson(@RequestParam String term,
			HttpServletResponse response) {
		MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();
		MediaType jsonMimeType = MediaType.APPLICATION_JSON;

		CompassManager compassManager = CompassManagerFactory.getInstance()
				.getCompassManager();

		if (compassManager != null) {

			Collection<String> suggestions = compassManager.getSuggestions(
					term, 10);
			try {

				if (jsonConverter
						.canWrite(suggestions.getClass(), jsonMimeType)) {
					jsonConverter.write(suggestions, jsonMimeType,
							new ServletServerHttpResponse(response));
				}
			} catch (HttpMessageNotWritableException e) {
				logger.error("Error occured when getSuggestions", e);
			} catch (IOException e) {
				logger.error("Error occured when getSuggestions", e);
			}

		}

	}

	@RequestMapping("/")
	public String searchForm(ModelMap modelMap, HttpServletRequest request) {

		CompassManager compassManager = CompassManagerFactory.getInstance()
				.getCompassManager();

		if (compassManager != null) {

			DefaultSearchConfig configs = getDefaultSearchConfigs(compassManager);

			FormSearch formSearch = new FormSearch();

			modelMap.addAttribute("configs", configs);
			modelMap.addAttribute("knowledgeBases",
					compassManager.listKnowledgeBases());
			modelMap.addAttribute("isSearch", false);
			modelMap.addAttribute("formSearch", formSearch);

			return "/index";
		}

		return "/error";
	}

	private DefaultSearchConfig getDefaultSearchConfigs(
			CompassManager compassManager) {

		DefaultSearchConfig configs = new DefaultSearchConfig();

		configs.setSearchFields(compassManager.getDefaultSearchFields());

		configs.setSearchOptions(compassManager.getDefaultSearchConfig());

		return configs;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(@Valid FormSearch formSearch, BindingResult result,
			ModelMap modelMap, HttpServletRequest request) {

		CompassManager compassManager = CompassManagerFactory.getInstance()
				.getCompassManager();

		if (compassManager != null) {

			String term = formSearch.getSearchValue();

			TopicQuery topicQuery = null;

			FullTextQuery fullTextQuery = compassManager.createFullTextQuery();

			Collection<String> terms = setupFullTextQuery(fullTextQuery, term,
					compassManager);

			Collection<KnowledgeBaseDescriptor> knowledgeBaseDescriptors = compassManager
					.listKnowledgeBases();
			if (formSearch.getSelected() != null
					&& !formSearch.getSelected().isEmpty()) {
				topicQuery = compassManager.createTopicQuery();

				topicQuery.addTerms(terms);

				topicQuery.setTreeSearch(false);

				long select;
				for (KnowledgeBaseDescriptor knowledgeBaseDescriptor : knowledgeBaseDescriptors) {

					for (Object selected : formSearch.getSelected()) {
						try {
							select = Long.parseLong((String) selected);
						} catch (NumberFormatException e) {
							continue;
						}
						if (select == (knowledgeBaseDescriptor.getId())) {

							TopicCriteria topicCriteria = topicQuery
									.createTopicCriteria(knowledgeBaseDescriptor);
							break;
						}

					}
				}

			}

			QueryResult queryResult = compassManager.search(topicQuery,
					fullTextQuery);

			Collection<Hit> hits = queryResult.getFullTextQueryResult()
					.getHits();

			DefaultSearchConfig configs = getDefaultSearchConfigs(compassManager);

			modelMap.addAttribute("configs", configs);

			modelMap.addAttribute("knowledgeBases", knowledgeBaseDescriptors);
			modelMap.addAttribute("result", hits);
			modelMap.addAttribute("formSearch", formSearch);
			modelMap.addAttribute("isSearch", true);

			return "/index";
		}

		return "/error";
	}

	private Collection<String> setupFullTextQuery(FullTextQuery fullTextQuery,
			String term, CompassManager compassManager) {

		Set<String> terms = new HashSet<String>(0);

		Collection<String> splittedTerms = SearchUtil.splitSearchString(term);

		splittedTerms = SearchUtil.toLowerCase(splittedTerms);

		Collection<SearchField> defaultSearchFields = compassManager
				.getDefaultSearchFields();
		FullTextFieldCriteria fullTextFieldCriteria;
		for (SearchField searchField : defaultSearchFields) {
			fullTextFieldCriteria = fullTextQuery.createCriteria(searchField
					.getIndexField());
			fullTextFieldCriteria.addTerms(splittedTerms);
		}

		terms.addAll(splittedTerms);

		return terms;
	}

}
