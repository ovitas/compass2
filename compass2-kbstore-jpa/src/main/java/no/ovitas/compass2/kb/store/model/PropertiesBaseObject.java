/**
 * 
 */
package no.ovitas.compass2.kb.store.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Transient;

import no.ovitas.compass2.model.Properties;

/**
 * @class PropertiesBaseObject
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.09.15.
 * 
 */
public abstract class PropertiesBaseObject extends BaseObject implements Properties, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8022379682888959692L;
	
	protected Map<String, Object> properties = null;

	private long importId;

	@Transient
	public void setProperty(String key, Object value) {
		if (properties == null) {
			properties = new HashMap<String, Object>(1);
		}
		properties.put(key, value);
	}

	@Transient
	public Object getProperty(String key) {
		if (properties == null)
			return null;

		return properties.get(key);
	}

	@Transient
	public Set<String> keySet() {
		return properties.keySet();
	}
	
	@Transient
	public long getImportId() {
		return importId;
	}

	@Transient
	public void setImportId(long id) {
		this.importId = id;
		
	}
		
}
