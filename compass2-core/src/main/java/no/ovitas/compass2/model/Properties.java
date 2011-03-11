/**
 * 
 */
package no.ovitas.compass2.model;

import java.util.Set;

/**
 * @class Properties
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.09.15.
 * 
 */
public interface Properties {

	/**
	 * Create a property in the object.
	 * 
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, Object value);
	
	/**
	 * Get value of the property.
	 * @param key
	 * @return
	 */
	public Object getProperty(String key);
	
	
	/**
	 * Return with the stored keys.
	 * 
	 * @return
	 */
	public Set<String> keySet();
	
	/**
	 * Get the import internal id;
	 * 
	 * @return
	 */
	public long getImportId();
	
	/**
	 * Set the import internal id.
	 * 
	 * @param id
	 */
	public void setImportId(long id);
	
}
