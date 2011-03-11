package no.ovitas.compass2.kb.store;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.kb.KnowledgeBase;
import no.ovitas.compass2.kb.TopicNameIndexer;
import no.ovitas.compass2.model.MatchTopicResult;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;

public class MockTopicNameIndexer implements TopicNameIndexer {

	@Override
	public void init(Properties properties) throws ConfigurationException {
	}

	@Override
	public void setConfiguration(ConfigurationManager manager)
			throws ConfigurationException {
	}

	@Override
	public void removeKnowledgeBase(KnowledgeBaseDescriptor descriptor)
			throws IOException {
	}

	@Override
	public void saveKnowledgeBase(KnowledgeBase knowledgeBase)
			throws IOException {
	}

	@Override
	public MatchTopicResult getRelatedResults(Collection<String> terms,
			long knowledgeBaseID, int maxTopicNumberToExpand,
			boolean perfixMatch) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
