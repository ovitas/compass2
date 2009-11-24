package no.ovitas.compass2.util.lucene;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import no.ovitas.compass2.config.FullTextSearchImplementation;
import no.ovitas.compass2.exception.ConfigurationException;

import org.apache.lucene.index.IndexWriter;

public interface ContentIndexer {

	public abstract void close() throws IOException;

	public abstract void index(File f, Map<String, String> additionalFields)
			throws Exception;
	public void setIndexWriter(IndexWriter indexWriter);
	public void setIndexDirectory(String indexDirectory);
	public void init() throws ConfigurationException, IOException;
	public void setFTSImpl(FullTextSearchImplementation ftsImpl);

}