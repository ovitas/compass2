package no.ovitas.compass2.config.settings;

public class ScopeSetting implements Setting {

	private String name;

	@Override
	public String dumpOut(String indent) {
		String ind = indent + " ";
		StringBuffer buffer = new StringBuffer(ind);

		buffer.append("Scope: \n");

		buffer.append(ind);
		buffer.append(name);
		buffer.append("\n");

		return buffer.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
