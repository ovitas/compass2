/**
 * 
 */
package no.ovitas.compass2.config.settings;

/**
 * @class ImplementationReflection
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.09.09.
 * 
 */
public class ImplementationReflection implements Implementation {
	
	private String className;

	/**
	 * This is a getter method for className.
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * This is a setter method for className.
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/* (non-Javadoc)
	 * @see no.ovitas.compass2.config.settings.Setting#dumpOut(java.lang.String)
	 */
	@Override
	public String dumpOut(String indent) {
		return indent + " ImplementationReflection: className: " + className + "\n";
	}

	@Override
	public ImplementationType getImplementationType() {
		// TODO Auto-generated method stub
		return ImplementationType.REFLECTION;
	}

}
