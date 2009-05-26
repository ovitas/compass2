package no.ovitas.compass2.dao;

import no.ovitas.compass2.model.KnowledgeBaseHolder;

public interface KBBuilderDao {

	public KnowledgeBaseHolder buildKB(String kbAccessString);
}
