/**
 * 
 */
package no.ovitas.compass2.config.settings;

/**
 * @class SubPlugin
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.09.09.
 * 
 */
public abstract class SubPlugin implements Plugin {

	protected Implementation implementation;

	protected ParamContainer params;

	@Override
	public String dumpOut(String indent) {
		String ind = indent + " ";
		StringBuilder builder = new StringBuilder();

		builder.append(ind + implementation.dumpOut(ind) + "\n");

		if (params != null) {
			builder.append(ind + params.dumpOut(ind) + "\n");
		}

		return builder.toString();
	}

	/**
	 * This is a getter method for implementation.
	 * 
	 * @return the implementation
	 */
	public Implementation getImplementation() {
		return implementation;
	}

	/**
	 * This is a setter method for implementation.
	 * 
	 * @param implementation
	 *            the implementation to set
	 */
	public void setImplementation(Implementation implementation) {
		this.implementation = implementation;
	}

	/**
	 * This is a getter method for params.
	 * 
	 * @return the params
	 */
	public ParamContainer getParams() {
		if (params == null) {
			params = new ParamContainer();
		}
		return params;
	}

	/**
	 * This is a setter method for params.
	 * 
	 * @param params
	 *            the params to set
	 */
	public void setParams(ParamContainer params) {
		this.params = params;
	}

}
