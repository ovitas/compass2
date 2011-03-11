package no.ovitas.compass2.fts;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import no.ovitas.compass2.Manager;
import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.config.settings.Field;
import no.ovitas.compass2.exception.ConfigurationException;

public abstract class AbstractFTSContentHandlerImpl  implements FTSContentHandler, Manager{

	protected ConfigurationManager configuration;
	
	protected Map<String, Field> fields = new HashMap<String, Field>();
	
	public AbstractFTSContentHandlerImpl() {
		super();
	}

	@Override
	public void setConfiguration(ConfigurationManager manager)
			throws ConfigurationException {
		configuration = manager;
	}
	
	@Override
	public void init(Properties properties) throws ConfigurationException {
		
		
	}
	
	@Override
	public void setFields(Collection<Field> fields) {
		for (Field field : fields) {
			this.fields.put(field.getName(), field);
		}
		
	}

	protected Map<String,Object> filterContent(Map<String,Object> contents) {
		// TODO Auto-generated method stub
		
		Map<String, Object> result = new TreeMap<String, Object>();
		
		for ( String fieldName : contents.keySet()) {
			
			if (fields.containsKey(fieldName) || fields.isEmpty()) {
				result.put(fieldName, contents.get(fieldName));
			}
		}
		
		return result;
	
	}

}