/**
 * 
 */
package no.ovitas.compass2.config;

import java.util.ArrayList;
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
	
	public Param getParam(String name){
		if (name != null && params.containsKey(name)){
			return params.get(name);
		} else {
			logger.error("Parameter: " + name + " is not exist!");
			return null;
		}
	}
	
	public Collection<Param> getParams(){
		return params.values();
	}
	
	// Constructor

	public ParamContainer() {
		temp = new ArrayList<Param>();
		this.params = Collections.synchronizedSortedMap(new TreeMap<String, Param>());
	}

	// Methods
	
	public void addParam(Param param){
		temp.add(param);
	}
	
	public void postProcess(){
		for(Param p : temp){
			this.params.put(p.getName(), p);
		}
	}
	
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
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "ParamContainer\n";
		
		if(params != null && params.size() > 0){
			for(Param p : params.values()){
				toDumpOut += ind + p.dumpOut(ind);//"name: " + p.name + " value: " + p.value+"\n";
			}
			
		}
		return toDumpOut; 
	}
	

}
