/**
 * 
 */
package no.ovitas.compass2.fts;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.exception.CompassException;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.fts.factory.FTSContentHandlerFactory;
import no.ovitas.compass2.fts.factory.FTSIndexerFactory;
import no.ovitas.compass2.fts.util.AddConfiguration;
import no.ovitas.compass2.fts.util.DeleteConfiguration;
import no.ovitas.compass2.fts.util.DocumentSetting;
import no.ovitas.compass2.fts.util.IndexerConfiguration;
import no.ovitas.compass2.fts.util.IndexerConfigurationHandler;
import no.ovitas.compass2.fts.util.ReplaceConfiguration;
import no.ovitas.compass2.fts.util.ReplaceSetting;
import no.ovitas.compass2.fts.util.Scope;
import no.ovitas.compass2.fts.util.UpdateConfiguration;
import no.ovitas.compass2.model.Content;
import no.ovitas.compass2.model.DocumentDetails;
import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.model.FullTextSearchConfig;
import no.ovitas.compass2.search.FullTextQuery;
import no.ovitas.compass2.search.FullTextQueryResult;
import no.ovitas.compass2.search.FullTextTopicQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @class FTSManagerImpl
 * @project compass2-fts-lucene
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version 1.0
 * @date 2010.07.28.
 * 
 */
public class FTSManagerImpl implements FullTextSearchManager {

	private String indexDirectory;

	protected int allHitNumber;

	// private FullTextSearchImplementation ftsImpl;
	protected ConfigurationManager configManager;
	private Log log = LogFactory.getLog(getClass());

	public Set<String> listAllContentHandlerType()
			throws ConfigurationException {
		return FTSContentHandlerFactory.getInstance()
				.listAllContentHandlerType();
	}

	/**
	 * Index the document and save to indexer repository.
	 */
	public void indexDocument(boolean reindex, int depth, String dir,
			String contentHandlerType) {
		log.info("Index directory: " + dir);
		File f = new File(dir);

		FTSIndexer indexer = FTSIndexerFactory.getInstance()
				.getIndexerImplementation();
		FTSContentHandler handler = FTSContentHandlerFactory.getInstance()
				.getHandlerImplementation(contentHandlerType);
		indexer.openIndexer(reindex);
		try {
			if (f.isFile()) {

				try {
					Content content = handler.getContent(f);

					indexer.index(content);

				} catch (CorruptIndexException e) {
					log.error("Error occured when indexing!", e);
				} catch (IOException e) {
					log.error("Error occured when indexing!", e);
				} catch (SAXParseException e) {
					log.error("Error occured when indexing!", e);
				} catch (SAXException e) {
					log.error("Error occured when indexing!", e);
				} catch (ParserConfigurationException e) {
					log.error("Error occured when indexing!", e);
				} catch (CompassException e) {
					log.error("Error occured when indexing!", e);
				}
			} else {
				addDocumentsFromDirectory(f, depth, indexer, handler);
			}

		} catch (Exception e) {
			log.error("Error occured when indexing!", e);
		} finally {
			indexer.closeIndexer();
		}
	}

	/**
	 * Index all files in a directory $depth$ depth.
	 * 
	 * @param f
	 * @param depth
	 * @param indexer
	 */
	private void addDocumentsFromDirectory(File directory, int depth,
			FTSIndexer indexer, FTSContentHandler handler) {

		File ff[] = directory.listFiles();

		if (ff != null && ff.length > 0) {
			for (File file : ff) {
				try {
					if (!file.isDirectory()) {
						log.info("Processing file: " + file.getCanonicalPath());

						String name = file.getAbsolutePath();

						if (!name.toLowerCase().endsWith("jpg")
								&& !name.toLowerCase().endsWith("ico")
								&& !name.toLowerCase().endsWith("png")
								&& !name.toLowerCase().contains(".axd")) {
							indexer.index(handler.getContent(file));
						}
					} else if (depth > 0 && !"..".equals(file.getName())
							&& !".".equals(file.getName())) {
						addDocumentsFromDirectory(file, depth - 1, indexer,
								handler);
					}
				} catch (IOException e) {
					log.error("Error occured when processing file: " + file, e);
				} catch (ConfigurationException e) {
					log.error("Error occured when processing file: " + file, e);
				} catch (SAXParseException e) {
					log.error("Error occured when processing file: " + file, e);
				} catch (SAXException e) {
					log.error("Error occured when processing file: " + file, e);
				} catch (ParserConfigurationException e) {
					log.error("Error occured when processing file: " + file, e);
				} catch (CompassException e) {
					log.error("Error occured when processing file: " + file, e);
				}
			}
		}

	}

	@Override
	public void indexDocuments(String configurationFilePath,
			String contentHandlerType) {
		IndexerConfigurationHandler configurationHandler = IndexerConfigurationHandler
				.getInstance();
		configurationHandler.loadConfigFile(configurationFilePath);
		IndexerConfiguration indexerConfiguration = configurationHandler
				.getIndexerConfiguration();

		FTSContentHandler handler;
		handler = FTSContentHandlerFactory.getInstance()
				.getHandlerImplementation(contentHandlerType);

		FTSIndexer indexer = FTSIndexerFactory.getInstance()
				.getIndexerImplementation();

		indexer.openIndexer(indexerConfiguration.isReIndex());

		try {
			addDocumentsToIndex(indexerConfiguration, handler, indexer);

			updateDocumentsToIndex(indexerConfiguration, handler, indexer);

			deleteDocumentsFromIndex(indexerConfiguration, handler, indexer);

		} catch (Exception e) {
			log.error("Error occured when indexing!", e);
		} finally {
			indexer.closeIndexer();
		}
	}

	private void deleteDocumentsFromIndex(
			IndexerConfiguration indexerConfiguration,
			FTSContentHandler handler, FTSIndexer indexer) {

		DeleteConfiguration deleteConfiguration = indexerConfiguration
				.getDeleteConfiguration();

		List<DocumentSetting> documents;

		if (deleteConfiguration != null) {
			String baseDir = indexerConfiguration.getBaseDir();

			List<ReplaceSetting> replaceSettings = replaceConfiguration(
					indexerConfiguration, Scope.DELETE);

			documents = deleteConfiguration.getDocuments();

			List<Content> contents = new ArrayList<Content>(0);
			contents = getContents(indexerConfiguration, handler, documents,
					baseDir, replaceSettings, contents);

			for (Content content2 : contents) {
				try {
					indexer.deleteDocument((String) content2
							.getValue(Constants.URI_INDEX));
				} catch (CompassException e) {
					log.error("Error occured when processing content: "
							+ content2.getValue(Constants.URI_INDEX), e);
				}
			}

		}
	}

	private String replace(String uri, List<ReplaceSetting> replaceSettings) {
		for (ReplaceSetting replaceSetting : replaceSettings) {
			uri = uri.replace(replaceSetting.getValue(),
					replaceSetting.getWith());
		}

		return uri;
	}

	private void updateDocumentsToIndex(
			IndexerConfiguration indexerConfiguration,
			FTSContentHandler handler, FTSIndexer indexer) {
		UpdateConfiguration updateConfiguration = indexerConfiguration
				.getUpdateConfiguration();

		List<DocumentSetting> documents;

		if (updateConfiguration != null) {
			String baseDir = indexerConfiguration.getBaseDir();
			List<ReplaceSetting> replaceSettings = replaceConfiguration(
					indexerConfiguration, Scope.UPDATE);

			documents = updateConfiguration.getDocuments();

			List<Content> contents = new ArrayList<Content>(0);
			contents = getContents(indexerConfiguration, handler, documents,
					baseDir, replaceSettings, contents);

			for (Content content2 : contents) {
				try {
					indexer.updateIndex(content2);
				} catch (CompassException e) {
					log.error("Error occured when processing content: "
							+ content2.getValue(Constants.URI_INDEX), e);
				}
			}
		}

	}

	private void addDocumentsToIndex(IndexerConfiguration indexerConfiguration,
			FTSContentHandler handler, FTSIndexer indexer) {

		AddConfiguration addConfiguration = indexerConfiguration
				.getAddConfiguration();

		List<DocumentSetting> documents;

		if (addConfiguration != null) {
			String baseDir = indexerConfiguration.getBaseDir();
			List<ReplaceSetting> replaceSettings = replaceConfiguration(
					indexerConfiguration, Scope.ADD);

			documents = addConfiguration.getDocuments();

			List<Content> contents = new ArrayList<Content>(0);
			contents = getContents(indexerConfiguration, handler, documents,
					baseDir, replaceSettings, contents);

			for (Content content2 : contents) {
				try {
					indexer.index(content2);
				} catch (CompassException e) {
					log.error("Error occured when processing content: "
							+ content2.getValue(Constants.URI_INDEX), e);
				}
			}

		}

	}

	private List<Content> getContents(
			IndexerConfiguration indexerConfiguration,
			FTSContentHandler handler, List<DocumentSetting> documents,
			String baseDir, List<ReplaceSetting> replaceSettings,
			List<Content> contents) {
		File file;
		Content content;
		for (DocumentSetting documentSetting : documents) {
			file = new File(baseDir + documentSetting.getUrl());
			if (!file.isDirectory()) {
				try {
					content = handler.getContent(file);
					replaceURL(content, replaceSettings);
					contents.add(content);
				} catch (IOException e) {
					log.error("Error occured when processing file: " + file, e);
				} catch (ConfigurationException e) {
					log.error("Error occured when processing file: " + file, e);
				} catch (SAXParseException e) {
					log.error("Error occured when processing file: " + file, e);
				} catch (SAXException e) {
					log.error("Error occured when processing file: " + file, e);
				} catch (ParserConfigurationException e) {
					log.error("Error occured when processing file: " + file, e);
				} catch (CompassException e) {
					log.error("Error occured when processing file: " + file, e);
				}
			} else {
				contents = getContentsFromDir(file,
						indexerConfiguration.getDepth(), handler,
						replaceSettings);

			}
		}
		return contents;
	}

	private List<ReplaceSetting> replaceConfiguration(
			IndexerConfiguration indexerConfiguration, Scope scope) {
		List<ReplaceSetting> replaceSettings;
		ReplaceConfiguration replaceConfiguration = indexerConfiguration
				.getReplaceConfiguration();
		if (replaceConfiguration != null) {
			replaceSettings = replaceConfiguration.getReplaceSettings(scope);
		} else {
			replaceSettings = new ArrayList<ReplaceSetting>();
		}
		return replaceSettings;
	}

	private List<Content> getContentsFromDir(File directory, int depth,
			FTSContentHandler handler, List<ReplaceSetting> replaceSettings) {

		List<Content> result = new ArrayList<Content>(0);

		if (depth < 1) {
			return result;
		}
		Content content;
		File ff[] = directory.listFiles();

		if (ff != null && ff.length > 0) {
			for (File file : ff) {
				try {
					if (!file.isDirectory()) {
						log.debug("Processing file: " + file.getCanonicalPath());

						content = handler.getContent(file);
						replaceURL(content, replaceSettings);
						result.add(content);

					} else if (depth > 0 && !"..".equals(file.getName())
							&& !".".equals(file.getName())) {
						result.addAll(getContentsFromDir(file, depth - 1,
								handler, replaceSettings));
					}
				} catch (IOException e) {
					log.error("Error occured when processing file: " + file, e);
				} catch (ConfigurationException e) {
					log.error("Error occured when processing file: " + file, e);
				} catch (SAXParseException e) {
					log.error("Error occured when processing file: " + file, e);
				} catch (SAXException e) {
					log.error("Error occured when processing file: " + file, e);
				} catch (ParserConfigurationException e) {
					log.error("Error occured when processing file: " + file, e);
				} catch (CompassException e) {
					log.error("Error occured when processing file: " + file, e);
				}
			}
		}

		return result;
	}

	private void replaceURL(Content content,
			List<ReplaceSetting> replaceSettings) {
		String value = (String) content.getValue(Constants.URI_INDEX);

		for (ReplaceSetting replaceSetting : replaceSettings) {
			value = value.replace(replaceSetting.getValue(),
					replaceSetting.getWith());
		}

		content.setValue(Constants.URI_INDEX, value);
	}

	@Override
	public FullTextQueryResult doSearch(FullTextQuery fullTextQuery,
			FullTextTopicQuery fullTextTopicQuery) {
		FTSIndexer indexer = FTSIndexerFactory.getInstance()
				.getIndexerImplementation();

		return indexer.doSearch(fullTextQuery, fullTextTopicQuery);
	}

	/**
	 * Return with the searched document.
	 * 
	 * @throws ConfigurationException
	 */
	public DocumentDetails getDocument(String id) {
		FTSIndexer indexer = FTSIndexerFactory.getInstance()
				.getIndexerImplementation();

		DocumentDetails doc = indexer.getDocument(id);

		indexer.closeIndexer();

		return doc;
	}

	/**
	 * Initialize FTSManager object.
	 */
	public void init(Properties properties) throws ConfigurationException {

	}

	public void setConfiguration(ConfigurationManager manager) {
		configManager = manager;
	}

}
