/**
 * 
 */
package no.ovitas.compass2.config.settings;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @class SearchBases
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.07.29.
 * 
 */
public class SearchFields implements Setting{

	private Logger logger = Logger.getLogger(this.getClass());


	private List<SearchField> fields;

	public SearchFields() {
		fields = new LinkedList<SearchField>();
	}

	public List<SearchField> getFields() {
		return fields;
	}

		
	public void addField(SearchField field){
		fields.add(field);
	}


	
	public void dumpOut(){
		String toDumpOut = "";
		if(fields != null && fields.size() > 0){
			for(SearchField p : fields){
			toDumpOut += ".[field.index].="+p.getIndexField()+"\n";
			toDumpOut += ".[field.search].="+p.getSearchField()+"\n";
			toDumpOut += ".[field.type].="+p.getType()+"\n";
			}
			
		}
		logger.debug(toDumpOut); 
	}
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "SearchFields\n";
		
		if(fields != null && fields.size() > 0){
			for(SearchField p : fields){
				toDumpOut += ind + p.dumpOut(ind);//"name: " + p.name + " value: " + p.value+"\n";
			}
			
		}
		
		return toDumpOut; 
	}
	
}
