/**
 * 
 */
package no.ovitas.compass2.config.settings;

/**
 * @class ImplementationSpring
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.09.09.
 * 
 */
public class ImplementationSpring implements Implementation {
	
	private String beanName;
	
	private ContextFileContainer contextFiles;

	/* (non-Javadoc)
	 * @see no.ovitas.compass2.config.settings.Setting#dumpOut(java.lang.String)
	 */
	@Override
	public String dumpOut(String indent) {
		// TODO Auto-generated method stub
		return indent + " ImpelentationSpring: beanName: " + beanName + "\n" + contextFiles.dumpOut(indent + " ");
	}

	/**
	 * This is a getter method for beanName.
	 * @return the beanName
	 */
	public String getBeanName() {
		return beanName;
	}

	/**
	 * This is a setter method for beanName.
	 * @param beanName the beanName to set
	 */
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	/**
	 * This is a getter method for contextFiles.
	 * @return the contextFiles
	 */
	public ContextFileContainer getContextFiles() {
		return contextFiles;
	}

	/**
	 * This is a setter method for contextFiles.
	 * @param contextFiles the contextFiles to set
	 */
	public void setContextFiles(ContextFileContainer contextFiles) {
		this.contextFiles = contextFiles;
	}

	@Override
	public ImplementationType getImplementationType() {
		// TODO Auto-generated method stub
		return ImplementationType.SPRING;
	}
	
	

}
