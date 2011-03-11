/**
 * 
 */
package no.ovitas.compass2.config.settings;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @class ContextFileContainer
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.04.
 * 
 */
public class ContextFileContainer implements Setting {

	private Logger logger = Logger.getLogger(this.getClass());
	private List<ContextFile> files;

	/**
	 * This is a setter method for files.
	 * 
	 * @param files
	 *            the files to set
	 */
	public void setFiles(List<ContextFile> files) {
		this.files = files;
	}

	/**
	 * This is a getter method for files.
	 * 
	 * @return the files
	 */
	public List<ContextFile> getFiles() {
		return files;
	}

	/**
	 * Get the list of context file names as a <code>String</code> array.
	 * 
	 * @return the <code>String</code> array containing the configured list of
	 *         context files. If no files are specified, the result is a
	 *         zero-length array.
	 */
	public String[] getFileNameArray() {
		List<String> fileNameList = new ArrayList<String>();
		if (files == null)
			return new String[0];

		for (ContextFile file : files) {
			String fileName = file.getFile();
			fileNameList.add(fileName);
		}

		String[] fileNameArray = fileNameList.toArray(new String[0]);
		return fileNameArray;
	}

	public ContextFileContainer() {
		files = new ArrayList<ContextFile>();
	}

	public void addFile(ContextFile file) {
		files.add(file);
	}

	public void dumpOut() {
		String toDumpOut = "";
		if (files != null && files.size() > 0) {
			for (ContextFile con : files) {
				toDumpOut += ".[context-file].=" + con.getFile() + "\n";
			}

		}
		logger.debug(toDumpOut);
	}

	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "ContextFileContainer\n";

		if (files != null && files.size() > 0) {
			for (ContextFile con : files) {
				toDumpOut += ind + con.dumpOut(ind);
			}

		}

		return toDumpOut;
	}

}
