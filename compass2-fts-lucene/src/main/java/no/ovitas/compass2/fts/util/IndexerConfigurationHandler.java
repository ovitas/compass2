/**
 * 
 */
package no.ovitas.compass2.fts.util;

import java.io.File;

import no.ovitas.compass2.exception.CompassErrorID;
import no.ovitas.compass2.exception.CompassException;
import no.ovitas.compass2.exception.ConfigurationException;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.StackAction;
import org.apache.log4j.Logger;

/**
 * @author gyalai
 *
 */
public class IndexerConfigurationHandler {
	private static Logger logger = Logger.getLogger(IndexerConfigurationHandler.class);
	private static IndexerConfigurationHandler instance = null;
	private IndexerConfiguration indexerConfiguration;
	
	private IndexerConfigurationHandler() {
	}
	
	public static IndexerConfigurationHandler getInstance() {
		if ( instance == null) {
			instance = new IndexerConfigurationHandler();
		}
		
		return instance;
	}
	
	public void loadConfigFile(String configPath) throws ConfigurationException {
		Digester digester = new Digester();
		indexerConfiguration = null;
		setupDigester(digester);
		digester.setValidating(true);
		digester.setStackAction(new StackAction() {
			
			@Override
			public Object onPush(Digester d, String stackName, Object o) {
				return o;
			}
			
			@Override
			public Object onPop(Digester d, String stackName, Object o) {
				if (o instanceof ReplaceConfiguration) {
					((ReplaceConfiguration) o).postProcess();
				}
				
				return o;
			}
		});
		
		File cfile = new File( configPath);
		if(cfile!=null && cfile.canRead() && cfile.isFile()){
			try {
				indexerConfiguration = (IndexerConfiguration)digester.parse(cfile);
			} catch (Exception e) {
				throw new CompassException(CompassErrorID.FTS_INVALID_INDEX_CONFIGURATION_FILE_ERROR, "Exception occured while loading and processing the configuration: " + e.getMessage(),e);
			}
		}
	}
	
	public IndexerConfiguration getIndexerConfiguration() {
		return indexerConfiguration;
	}

	private void setupDigester(Digester digester) {
		
		//Indexer-Config
		digester.addObjectCreate(ConfigConstants.TAG_ROOT, IndexerConfiguration.class);
		digester.addSetProperties(ConfigConstants.TAG_ROOT, ConfigConstants.ATTR_BASE_DIR, "baseDir");
		digester.addSetProperties(ConfigConstants.TAG_ROOT, ConfigConstants.ATTR_DEPTH, "depth");
		digester.addSetProperties(ConfigConstants.TAG_ROOT, ConfigConstants.ATTR_RE_INDEX, "reIndex");
		
		//Replace in url
		digester.addObjectCreate(ConfigConstants.TAG_REPLACE_IN_URL, ReplaceConfiguration.class);
		digester.addSetNext(ConfigConstants.TAG_REPLACE_IN_URL, "setReplaceConfiguration");
		
		//Replace
		digester.addObjectCreate(ConfigConstants.TAG_REPLACE, ReplaceSetting.class);
		digester.addSetNext(ConfigConstants.TAG_REPLACE, "addReplaceSetting");
		digester.addSetProperties(ConfigConstants.TAG_REPLACE, ConfigConstants.ATTR_SCOPE, "scope");
		digester.addSetProperties(ConfigConstants.TAG_REPLACE, ConfigConstants.ATTR_VALUE, "value");
		digester.addSetProperties(ConfigConstants.TAG_REPLACE, ConfigConstants.ATTR_WITH, "with");
		
		//Add
		digester.addObjectCreate(ConfigConstants.TAG_ADD, AddConfiguration.class);
		digester.addSetNext(ConfigConstants.TAG_ADD, "setAddConfiguration");
		
		//Update
		digester.addObjectCreate(ConfigConstants.TAG_UPDATE, UpdateConfiguration.class);
		digester.addSetNext(ConfigConstants.TAG_UPDATE, "setUpdateConfiguration");
		
		//Delete
		digester.addObjectCreate(ConfigConstants.TAG_DELETE, DeleteConfiguration.class);
		digester.addSetNext(ConfigConstants.TAG_DELETE, "setDeleteConfiguration");
		
		//DocumentSetting
		digester.addObjectCreate(ConfigConstants.TAG_DOCUMENT, DocumentSetting.class);
		digester.addSetNext(ConfigConstants.TAG_DOCUMENT, "addDocumentSetting");
		digester.addSetProperties(ConfigConstants.TAG_DOCUMENT, ConfigConstants.ATTR_URL, "url");
	}
}
