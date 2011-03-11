package no.ovitas.compass2.config.settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnowldegeBaseSetting implements Setting {

	private Options options;
	
	private List<ScopeSetting> scopes = new ArrayList<ScopeSetting>(0);
	
	private String name;
	
	@Override
	public String dumpOut(String indent) {
		String ind = indent + " ";
		StringBuffer buffer = new StringBuffer(ind);
		
		buffer.append("KnowledgeBaseSetting: \n");
		
		buffer.append(ind);
		buffer.append(name);
		buffer.append("\n");
		
		buffer.append(ind);
		buffer.append(options.dumpOut(ind));
		buffer.append("\n");
		
		for (ScopeSetting scope : scopes) {
			buffer.append(ind);
			buffer.append(scope.dumpOut(ind));
			buffer.append("\n");
		}
		
		return buffer.toString();
	}

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

	public Collection<ScopeSetting> getScopes() {
		return scopes;
	}
	
	public void addScope(ScopeSetting scope) {
		scopes.add(scope);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
