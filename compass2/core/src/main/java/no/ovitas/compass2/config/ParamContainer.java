/**
 * 
 */
package no.ovitas.compass2.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author csanyi
 *
 */
public class ParamContainer {

	// Attributes
	
	private Logger logger = Logger.getLogger(this.getClass());
	protected Map<String, Param > params;
	protected List<Param> temp;

	// Getter / setter methods
		
	public void addParam(Param param){
		params.put(param.getName(), param);
	}
	
	public Param getParam(String name){
		return params.get(name);
	}
	
	public Collection<Param> getParams(){
		return params.values();
	}
	
	// Constructor

	public ParamContainer() {
		params = Collections.synchronizedSortedMap(new TreeMap<String, Param>());
	}

	// Methods
	
	public void dumpOut(){
		String toDumpOut = "";
		if(params != null && params.size() > 0){
			for(Param p : params.values()){
			toDumpOut += ".[param.name].="+p.name+"\n";
			toDumpOut += ".[param.value].="+p.value+"\n";
			}
			
		}
		logger.debug(toDumpOut); 
	}
	

}
