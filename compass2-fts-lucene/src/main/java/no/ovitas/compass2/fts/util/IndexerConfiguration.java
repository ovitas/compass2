/**
 * 
 */
package no.ovitas.compass2.fts.util;

/**
 * @author gyalai
 *
 */
public class IndexerConfiguration {
	
	private String baseDir = "";
	
	private int depth;
	
	private boolean reIndex;
	
	private ReplaceConfiguration replaceConfiguration;
	
	private AddConfiguration addConfiguration;
	
	private UpdateConfiguration updateConfiguration;
	
	private DeleteConfiguration deleteConfiguration;
	
	public boolean isReIndex() {
		return reIndex;
	}

	public void setReIndex(boolean reIndex) {
		this.reIndex = reIndex;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public ReplaceConfiguration getReplaceConfiguration() {
		return replaceConfiguration;
	}

	public void setReplaceConfiguration(ReplaceConfiguration replaceConfiguration) {
		this.replaceConfiguration = replaceConfiguration;
	}

	public AddConfiguration getAddConfiguration() {
		return addConfiguration;
	}

	public void setAddConfiguration(AddConfiguration addConfiguration) {
		this.addConfiguration = addConfiguration;
	}

	public UpdateConfiguration getUpdateConfiguration() {
		return updateConfiguration;
	}

	public void setUpdateConfiguration(UpdateConfiguration updateConfiguration) {
		this.updateConfiguration = updateConfiguration;
	}

	public DeleteConfiguration getDeleteConfiguration() {
		return deleteConfiguration;
	}

	public void setDeleteConfiguration(DeleteConfiguration deleteConfiguration) {
		this.deleteConfiguration = deleteConfiguration;
	}

}
