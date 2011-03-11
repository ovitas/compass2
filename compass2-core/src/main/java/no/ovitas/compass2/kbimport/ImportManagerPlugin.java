package no.ovitas.compass2.kbimport;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import no.ovitas.compass2.Manager;
import no.ovitas.compass2.exception.ImportException;
import no.ovitas.compass2.kb.KnowledgeBase;
import no.ovitas.compass2.model.ImportResult;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.RelationTypeSetting;
import no.ovitas.compass2.model.knowledgebase.Scope;

/**
 * Main interface that give access to the the features of the knowledge base
 * import plugins.
 * 
 * @author Csaba Daniel
 */
public interface ImportManagerPlugin extends Manager {

	/**
	 * Import a knowledge base from the specified <code>InputStream</code>.
	 * 
	 * @param in
	 *            the <code>InputStream</code> from which the knowledge base
	 *            definition can be read.
	 * @return the result of the import
	 * @throws ImportException
	 *             if an exception occurs that blocks the import of the
	 *             knowledge base definition
	 */
	public void importKB(InputStream in, KnowledgeBase entityCreatorFactory) throws ImportException;

	/**
	 * Import a knowledge base from the specified <code>File</code>.
	 * @param knowledgeBaseDescriptor 
	 * 
	 * @param in
	 *            the <code>File</code> from which the knowledge base definition
	 *            can be read.
	 * @return the result of the import
	 * @throws ImportException
	 *             if an exception occurs that blocks the import of the
	 *             knowledge base definition
	 */
	public void importKB(File file, KnowledgeBase entityCreatorFactory) throws ImportException;

	/**
	 * Import a knowledge base from the specified <code>URL</code>.
	 * @param knowledgeBaseDescriptor 
	 * 
	 * @param in
	 *            the <code>URL</code> from which the knowledge base definition
	 *            can be read.
	 * @return the result of the import
	 * @throws ImportException
	 *             if an exception occurs that blocks the import of the
	 *             knowledge base definition
	 */
	public void importKB(URL url, KnowledgeBase entityCreatorFactory) throws ImportException;

	/**
	 * Get all available scopes from knowledge base which is under importing.
	 * 
	 * @return the available scopes.
	 * @throws ImportException
	 */
	public Collection<Scope> getScopes();

	/**
	 * Filter knowledge base is under importing. This is the second step of
	 * knowledge base storing process.
	 * 
	 * @param scopesImportID
	 *            the import id for that scopes which want to import. The other
	 *            scopes will be dropped
	 * @throws ImportException
	 */
	public void filterImportedKnowledgeBase(Collection<Long> scopes);
	
	/**
	 * Clean up after import process.
	 */
	public void cleanUp();

	/**
	 * Get relation types from knowledge base is under importing.
	 * 
	 * @return the relation types.
	 * @throws ImportException
	 */
	public Collection<RelationType> getRelationTypes();

	/**
	 * Get the imported knowledge base model.
	 * 
	 * @return the imported model
	 */
	public ImportResult getResultModel();

	/**
	 * Get {@link KnowledgeBaseDescriptor} of the imported knowledge base.
	 * 
	 * @return imported knowledge base descriptor.
	 */
	public KnowledgeBaseDescriptor getKnowledgeBaseDescriptor();

	/**
	 * Update the imported relation types with the specified settings.
	 * 
	 * @param relationTypeSettings
	 *            the settings to apply to the imported relation types
	 */
	public void updateRelationTypes(
			Collection<RelationTypeSetting> relationTypeSettings);
}
