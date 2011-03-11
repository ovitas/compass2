package no.ovitas.compass2.lt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.exception.CompassException;
import no.ovitas.compass2.exception.ConfigParameterMissingException;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.config.settings.LanguageTool;
import no.ovitas.compass2.lt.LanguageToolsManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.spell.JaroWinklerDistance;
import org.apache.lucene.search.spell.LevensteinDistance;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.NGramDistance;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * @author gyalai
 * @version 1.0
 * @created 27-jul.-2010 13:03:40
 */
public class LuceneLTManagerImpl implements LanguageToolsManager {

	protected ConfigurationManager configManager;
	private Log log = LogFactory.getLog(getClass());
	private LanguageTool ltImpl;
	private String spellCheckDir;
	private String indexDir;
	private String indexField;
	private String suggestionNum;
	private String language;

	/**
	 * Setter. Sets the configuration manager. Called by the LT factory.
	 * 
	 * @param manager
	 *            the configuration manager
	 */
	public void setConfiguration(ConfigurationManager manager) {
		configManager = manager;
	}

	public LuceneLTManagerImpl() {

	}

	/**
	 * Initialize the spell checker. Called by the factory.
	 * 
	 * @param properties
	 */
	public void initSpellchecker(Properties properties)
			throws ConfigParameterMissingException, ConfigurationException,
			IOException {

		spellCheckDir = properties
				.getProperty(Constants.LUCENE_SPELLCHECKER_DIRECTORY);
		indexDir = properties
				.getProperty(Constants.LUCENE_SPELLCHECKER_INDEX_DIRECTORY);
		indexField = properties
				.getProperty(Constants.LUCENE_SPELLCHECKER_FIELD);
		suggestionNum = properties
				.getProperty(Constants.LUCENE_SPELLCHECK_MAX_SUGGESTION_NUM);
		language = properties.getProperty(Constants.SNOWBALL_LANGUAGE);

		if (spellCheckDir == null) {
			new ConfigParameterMissingException("Paremeter: "
					+ Constants.LUCENE_SPELLCHECKER_DIRECTORY
					+ " is missing from config!");
		}
		if (indexDir == null) {
			new ConfigParameterMissingException("Paremeter: "
					+ Constants.LUCENE_SPELLCHECKER_INDEX_DIRECTORY
					+ " is missing from config!");
		}
		if (indexField == null) {
			new ConfigParameterMissingException("Paremeter: "
					+ Constants.LUCENE_SPELLCHECKER_FIELD
					+ " is missing from config!");
		}
		java.io.File scd = new java.io.File(spellCheckDir);
		if (!scd.isDirectory()) {
			new ConfigurationException(spellCheckDir + " is not a directory!");
		}
		Directory dir = FSDirectory.open(scd, null);
		SpellChecker spell = new SpellChecker(dir);

		java.io.File scid = new File(indexDir);

		if (!scid.isDirectory()) {
			new ConfigurationException(indexDir + " is not a directory!");
		}

		IndexReader r = IndexReader.open(FSDirectory.open(scid, null)); // #2
		try {
			spell.indexDictionary(new LuceneDictionary(r, indexField)); // #3
		} finally {
			r.close();
		}
		dir.close();
	}

	/**
	 * Spell checker.
	 * 
	 * @param userSearch
	 *            the given search word need to be spell checked
	 * @return only the best matching word is returned.
	 */
	public String getSpellingSuggestion(String userSearch) {

		if (spellCheckDir == null) {
			new ConfigParameterMissingException("Paremeter: "
					+ Constants.LUCENE_SPELLCHECKER_DIRECTORY
					+ " is missing from config!");
		}
		java.io.File scd = new java.io.File(spellCheckDir);
		if (!scd.isDirectory()) {
			new ConfigurationException(spellCheckDir + " is not a directory!");
		}
		Directory dir;
		try {
			dir = FSDirectory.open(scd, null);
			SpellChecker spell = new SpellChecker(dir);
//			spell.indexDictionary(new PlainTextDictionary(new File("/media/work/product/compass/data/fulldictionary00.txt")));
			spell.setStringDistance(new JaroWinklerDistance());
			String[] suggestions = spell.suggestSimilar(userSearch, 1);
			if (suggestions != null && suggestions.length > 0) {
				return suggestions[0];
			}
		} catch (IOException e) {
			throw new CompassException("Spell checker directory not available!", e);
		}

		return null;
	}

	/**
	 * Spell checker
	 * 
	 * @param userSearch
	 *            the given search word need to be spell checked
	 * @return spelling suggestions. Max number of suggestions is controlled by
	 *         lucene.spellchecker.max.suggestion.num parameter in the config
	 *         file.
	 */
	public List<String> getSpellingSuggestions(String userSearch) {

		if (spellCheckDir == null) {
			new ConfigParameterMissingException("Paremeter: "
					+ Constants.LUCENE_SPELLCHECKER_DIRECTORY
					+ " is missing from config!");
		}
		java.io.File scd = new java.io.File(spellCheckDir);
		if (!scd.isDirectory()) {
			new ConfigurationException(spellCheckDir + " is not a directory!");
		}
		Directory dir;
		try {
			dir = FSDirectory.open(scd, null);
		SpellChecker spell = new SpellChecker(dir);
		spell.setStringDistance(new LevensteinDistance());

		int sn = 5;
		if (suggestionNum != null) {
			try {
				sn = Integer.parseInt(suggestionNum);
			} catch (Exception ex) {
				log.error("Invalid integer value: " + suggestionNum);
			}
		}
		String[] suggestions = spell.suggestSimilar(userSearch, sn);
		if (suggestions != null && suggestions.length > 0) {
			List<String> s = new ArrayList<String>();
			for (String suggestion : suggestions) {
				s.add(suggestion);
			}
			return s;
		}
		} catch (IOException e) {
			throw new CompassException("Spell checker directory not available!", e);
		}
		return null;
	}

	/**
	 * Get stem by given word.
	 * 
	 * @param word
	 * @return the stem
	 */
	public String getStem(String word) {

		Analyzer a = new SnowballAnalyzer(Version.LUCENE_30, language);
		try {
			QueryParser qp = new QueryParser(Version.LUCENE_30, "", a);
			Query stemmed = qp.parse(word); // Throws ParseException
			return stemmed.toString();
		} catch (ParseException pe) {
			log.error(pe.getMessage());
			return null;
		}
	}

	/**
	 * @param tokens
	 *            list of tokens needed to process
	 * @return list of stem words
	 */
	public Collection<String> getStems(Collection<String> tokens) {
		List<String> stems = new ArrayList<String>();

		Analyzer a = new SnowballAnalyzer(Version.LUCENE_30, language);
		try {
			for (String token : tokens) {
				QueryParser qp = new QueryParser(Version.LUCENE_30, "", a);
				Query stemmed = qp.parse(token); // Throws ParseException
				stems.add(stemmed.toString());
			}
			return stems;
		} catch (ParseException pe) {
			log.error(pe.getMessage());
			return null;
		}
	}

	@Override
	public void init(Properties properties) throws ConfigurationException {
		try {
			initSpellchecker(properties);
		} catch (ConfigParameterMissingException e) {

			throw new ConfigurationException(e.getMessage(), e);
		} catch (IOException e) {

			throw new ConfigurationException(e.getMessage(), e);
		}

	}

}