package no.ovitas.compass2.config.settings;

public class Referral extends SubPlugin {
	
	@Override
	public String dumpOut(String indent) {
		String ind = indent + " ";
		StringBuffer buffer = new StringBuffer();
		buffer.append(ind);
		buffer.append("Referral: \n");
		buffer.append(ind);
		buffer.append(super.dumpOut(ind));
		buffer.append("\n");
		return buffer.toString();
	}

	@Override
	public PluginType getPluginType() {
		return PluginType.REFERRAL_PLUGIN;
	}

}
