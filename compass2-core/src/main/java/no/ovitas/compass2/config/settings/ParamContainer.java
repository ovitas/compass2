/**
 * 
 */
package no.ovitas.compass2.config.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author csanyi
 *
 */
public class ParamContainer implements Setting, PostProcessable {

	// Attributes
	private List<Param> temp = new ArrayList<Param>();

	private Properties params = new Properties();
	
	
	public void addParam(Param param){
		temp.add(param);
	}

	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		StringBuilder builder = new StringBuilder(ind + "ParamContainer\n");
		
		if(params.size() > 0){
			Set<String> keys = params.stringPropertyNames();
			
			for (String key : keys) {
				builder.append(ind + " Key: " + key + ", value: " + params.getProperty(key) + "\n" );
			}
		}

		return builder.toString(); 
	}
	
	/**
	 * This is a getter method for params.
	 * @return the params
	 */
	public Properties getParams() {
		return params;
	}

	/**
	 * This is a setter method for params.
	 * @param params the params to set
	 */
	public void setParams(Properties params) {
		this.params = params;
	}


	@Override
	public void postProcess() {
		for (Param param : temp) {
			params.put(param.getName(), param.getValue());
		}		
	}
	

}
