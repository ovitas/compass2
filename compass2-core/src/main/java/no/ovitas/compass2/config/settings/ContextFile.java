/**
 * 
 */
package no.ovitas.compass2.config.settings;

/**
 * @class ContextFile
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.08.04.
 * 
 */
public class ContextFile implements Setting{
	
	private String file;

	/**
	 * This is a setter method for file.
	 * @param file the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * This is a getter method for file.
	 * @return the file
	 */
	public String getFile() {
		return file;
	}

	
	public String dumpOut(String indent) {
	
		return indent + " Context-File: file: " + file + "\n";
	}
}
