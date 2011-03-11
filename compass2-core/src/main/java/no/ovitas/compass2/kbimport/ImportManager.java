package no.ovitas.compass2.kbimport;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Set;

import no.ovitas.compass2.Manager;
import no.ovitas.compass2.exception.ImportException;
import no.ovitas.compass2.kb.KnowledgeBase;
import no.ovitas.compass2.model.ImportResult;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.RelationTypeSetting;
import no.ovitas.compass2.model.knowledgebase.Scope;

/**
 * @class ImportManager
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.08.23.
 * 
 */
public interface ImportManager extends Manager{
	
	/**
	 * List all available file type which has <code>ImportManagerPlugin</code>.
	 *  
	 * @return the list of file types.
	 */
	public Set<String> listAvailableImportPlugins();
	
	/**
	 * Import a knowledge base from the specified <code>InputStream</code>.
	 * 
	 * @param in
	 *            the <code>InputStream</code> from which the knowledge base
	 *            definition can be read.
	 * @param type
	 *            the file type.
	 * @return the result of the import
	 * @throws ImportException
	 *             if an exception occurs that blocks the import of the
	 *             knowledge base definition
	 */
	public void importKB(InputStream in, String type,  KnowledgeBase entityCreatorFactory) throws ImportException;

	/**
	 * Import a knowledge base from the specified <code>File</code>.
	 * 
	 * @param in
	 *            the <code>File</code> from which the knowledge base definition
	 *            can be read.
	 * @param type
	 *            the file type.
	 * @return the result of the import
	 * @throws ImportException
	 *             if an exception occurs that blocks the import of the
	 *             knowledge base definition
	 */
	public void importKB(File file, String type, KnowledgeBase entityCreatorFactory) throws ImportException;

	/**
	 * Import a knowledge base from the specified <code>URL</code>.
	 * 
	 * @param in
	 *            the <code>URL</code> from which the knowledge base definition
	 *            can be read.
	 * @param type
	 *            the file type.
	 * @return the result of the import
	 * @throws ImportException
	 *             if an exception occurs that blocks the import of the
	 *             knowledge base definition
	 */
	public void importKB(URL url, String type,  KnowledgeBase entityCreatorFactory) throws ImportException;
	
	/**
	 * Get all available scopes from knowledge base which is under importing.
	 * 
	 * @return the available scopes.
	 * @throws ImportException
	 */
	public Collection<Scope> getScopes() throws ImportException;
	
	/**
	 * Filter knowledge base is under importing. This is the second step of
	 * knowledge base storing process.
	 * 
	 * @param scopesImportID
	 *            the import id for that scopes which want to import. The other
	 *            scopes will be dropped
	 * @throws ImportException
	 */
	public void filterImportedKnowledgeBase(Collection<Long> scopes) throws ImportException;
	
	/**
	 * Get relation types from knowledge base is under importing.
	 * 
	 * @return the relation types.
	 * @throws ImportException
	 */
	public Collection<RelationType> getRelationTypes() throws ImportException;
	
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
