package no.ovitas.compass2.model;

import java.util.Map;
import java.util.Set;
import no.ovitas.compass2.config.settings.Field;
import no.ovitas.compass2.model.FieldType;

/**
 * @class Content
 * @project compass2-fts-lucene
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 1.0
 * @date 2010.07.28.
 * 
 */
public class Content {
	
	private Map<String, Object> contents;
	private final Map<String, Field> fields;
	
	public Content(Map<String, Object> cont, Map<String, Field> fields) {
		this.fields = fields;
		contents = cont;
	}
	
	public Set<String> getContentStoredFieldNames() {
		return contents.keySet();
	}

	public Object getValue(String fieldName) {
		return contents.get(fieldName);
	}
	
	public void setValue(String fieldName, Object value) {
		contents.put(fieldName, value);
	}

	public String getStored(String fieldName) {
		// TODO Auto-generated method stub
		return fields.get(fieldName).getStored().toUpperCase();
	}

	public String getIndexed(String fieldName) {
		// TODO Auto-generated method stub
		return fields.get(fieldName).getIndexed().toUpperCase();
	}
	
	public FieldType getType(String fieldName) {
		return  fields.get(fieldName).getFieldType();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder stream = new StringBuilder("Content:");
		for (String fName : contents.keySet()) {
			stream.append("\t Field name: " + fName + " value: " + contents.get(fName) + "\n");
		}
		
		return stream.toString();
	}

}
