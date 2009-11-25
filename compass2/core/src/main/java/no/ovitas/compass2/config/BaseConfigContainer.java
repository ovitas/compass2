package no.ovitas.compass2.config;
/**
 * @author csanyi
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * @author csanyi
 *
 */
public class BaseConfigContainer<T extends BaseConfigItem> {
	
	protected Map<String, T > elements;
	protected ArrayList<T> temp;
	private  Logger logger = Logger.getLogger(this.getClass());

	
	public BaseConfigContainer(){
		elements = Collections.synchronizedSortedMap(new TreeMap<String, T>());
		temp = new ArrayList<T>();
	}
	
	public T getElement(String id){
		return elements.get(id);
	}
	
	public void addElement(String id, T element){
		elements.put(id, element);
	}
	
	public void addElement(T element){
		if(element!=null){
			temp.add(element);
		}
	}
	
	public void postProcess(){
		for(T t : temp ){
			addElement(t.getName(),t);
		}
	}
	
	public Collection<T> getElements(){
		return elements.values();
	}

	public void dumpOut(){
		logger.debug("Dumping elements: ");
		for(String e : elements.keySet()){
			logger.debug("id: "+e+", value: ");
			T element = elements.get(e);
			element.dumpOut();
		}
		logger.debug("Dumping elements finished ");		
	}
	
	public void dumpOut(String indent) {
		logger.debug(indent + "Container");
		
		for(String e : elements.keySet()){
			logger.debug("key: "+e+", value: ");
			T element = elements.get(e);
			element.dumpOut();
		}
	}
}
