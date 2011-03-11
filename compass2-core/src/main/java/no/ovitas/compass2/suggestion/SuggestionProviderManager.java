/**
 * 
 */
package no.ovitas.compass2.suggestion;

import java.util.Collection;

import no.ovitas.compass2.Manager;
import no.ovitas.compass2.kb.KnowledgeBase;

/**
 * @class SuggestionManager
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.09.21.
 * 
 */
public interface SuggestionProviderManager extends Manager {
	
	public void indexKnowledgeBaseForSuggestion(KnowledgeBase knowledgeBase);

	public Collection<String> getSuggestions(String word, int maxSuggestionNumber);
}
