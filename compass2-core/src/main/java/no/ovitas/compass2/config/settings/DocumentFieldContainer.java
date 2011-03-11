/**
 * 
 */
package no.ovitas.compass2.config.settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import org.apache.log4j.Logger;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author gyalai
 *
 */
public class DocumentFieldContainer implements Setting, PostProcessable{
	
	private Map<String, Field> fields;
	private List<Field> tmp;
	private Logger logger = Logger.getLogger(this.getClass());
	
	@SuppressWarnings("unchecked")
	public DocumentFieldContainer() {
		// TODO Auto-generated constructor stub
		tmp = new ArrayList<Field>();
		fields = Collections.synchronizedSortedMap(new TreeMap<String, Param>());
	}

	public void postProcess(){
		for(Field p : tmp){
			this.fields.put(p.getName(), p);
		}
	}
	
	public String dumpOut(String indent) {
		// TODO Auto-generated method stub
		String ind = indent + " ";
		String toDumpOut = ind + "DocumentFieldContainer\n";
		
		if(fields != null && fields.size() > 0){
			for(Field p : fields.values()){
				toDumpOut += ind + p.dumpOut(ind);//"name: " + p.name + " value: " + p.value+"\n";
			}
			
		}
		
		return toDumpOut;
	}

	public void dumpOut(){
		String toDumpOut = "";
		if(fields != null && fields.size() > 0){
			for(Field p : fields.values()){
			toDumpOut += ".[field.name].="+p.getName()+"\n";
			toDumpOut += ".[field.indexed].="+p.getIndexed()+"\n";
			toDumpOut += ".[field.storeded].="+p.getStored()+"\n";
			}
			
		}
		logger.debug(toDumpOut); 
	}
	
	/**
	 * Add field.
	 */
	public void addField(Field f) {
		tmp.add(f);
	}

	public Collection<Field> getFields() {
		return fields.values();
	}
	
	public Field getField(String name) {
		if (name != null && fields.containsKey(name)) {
			return fields.get(name);
		} else {
			logger.error("Field: " + name + " is not exist!");
			return null;
		}
	}
	
	
	
}
