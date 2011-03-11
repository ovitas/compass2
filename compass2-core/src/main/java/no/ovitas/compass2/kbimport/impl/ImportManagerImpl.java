/**
 * 
 */
package no.ovitas.compass2.kbimport.impl;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.config.settings.Import;
import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.exception.ImportException;
import no.ovitas.compass2.kb.KnowledgeBase;
import no.ovitas.compass2.kbimport.ImportManager;
import no.ovitas.compass2.kbimport.ImportManagerPlugin;
import no.ovitas.compass2.kbimport.factory.ImportManagerPluginFactory;
import no.ovitas.compass2.model.ImportResult;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.RelationTypeSetting;
import no.ovitas.compass2.model.knowledgebase.Scope;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @class ImportManagerImpl
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.23.
 * 
 */
public class ImportManagerImpl implements ImportManager {

	private ConfigurationManager configurationManager;

	private Map<String, ImportManagerPlugin> plugins;
	
	private ImportManagerPlugin activePlugin;

	private Log log = LogFactory.getLog(getClass());

	@Override
	public void init(Properties properties) throws ConfigurationException {

		plugins = new HashMap<String, ImportManagerPlugin>();
		String type;
		ImportManagerPlugin importManager;

		if (configurationManager.getImportPlugin() != null) {

			List<Import> implementations = configurationManager
					.getImportPlugin().getImports();
			for (Import importImplementation : implementations) {
				type = importImplementation.getType();
				importManager = ImportManagerPluginFactory.getInstance()
						.getImportManager(type);

				plugins.put(type, importManager);
			}

		}
	}

	@Override
	public void setConfiguration(ConfigurationManager manager)
			throws ConfigurationException {
		this.configurationManager = manager;
	}

	@Override
	public Set<String> listAvailableImportPlugins() {
		Set<String> keySet = plugins.keySet();
		if (keySet == null) {
			keySet = new HashSet<String>();
		}

		return keySet;
	}

	@Override
	public void importKB(InputStream in, String type,
			KnowledgeBase entityCreatorFactory) throws ImportException {

		if (!plugins.containsKey(type)) {
			log.debug("The type is not supported type:" + type);
			throw new ImportException("Not supported type. Type:" + type);
		}

		activePlugin = plugins.get(type);

		activePlugin.importKB(in, entityCreatorFactory);
	}

	@Override
	public void importKB(File file, String type,
			KnowledgeBase entityCreatorFactory) throws ImportException {

		if (!plugins.containsKey(type)) {
			log.debug("The type is not supported type:" + type);
			throw new ImportException("Not supported type. Type:" + type);
		}

		activePlugin = plugins.get(type);

		activePlugin.importKB(file, entityCreatorFactory);
	}

	@Override
	public void importKB(URL url, String type,
			KnowledgeBase entityCreatorFactory) throws ImportException {

		if (!plugins.containsKey(type)) {
			log.debug("The type is not supported type:" + type);
			throw new ImportException("Not supported type. Type:" + type);
		}
		
		if (activePlugin != null) {
			log.debug("Has active import!");
			throw new ImportException("Has active import!");
		}

		activePlugin = plugins.get(type);

		activePlugin.importKB(url, entityCreatorFactory);
	}

	@Override
	public Collection<Scope> getScopes() throws ImportException {

		if (activePlugin == null) {
			throw new ImportException(
					"Doesn't have active import manager plugin!");
		}

		return activePlugin.getScopes();
	}

	@Override
	public void filterImportedKnowledgeBase(Collection<Long> scopes) throws ImportException {

		if (activePlugin == null) {
			throw new ImportException(
					"Doesn't have active import manager plugin!");
		}

		activePlugin.filterImportedKnowledgeBase(scopes);
		activePlugin.cleanUp();

	}

	@Override
	public ImportResult getResultModel() {

		ImportResult resultModel = activePlugin.getResultModel();

		activePlugin = null;
		resultModel.getEntityFactory().finishImport();
		return resultModel;
	}

	@Override
	public Collection<RelationType> getRelationTypes() throws ImportException {
		if (activePlugin == null) {
			throw new ImportException(
					"Doesn't have active import manager plugin!");
		}
		
		return activePlugin.getRelationTypes();

	}

	@Override
	public KnowledgeBaseDescriptor getKnowledgeBaseDescriptor() {
		return activePlugin.getKnowledgeBaseDescriptor();
	}

	@Override
	public void updateRelationTypes(
			Collection<RelationTypeSetting> relationTypeSettings) {
		activePlugin.updateRelationTypes(relationTypeSettings);
	}
}
