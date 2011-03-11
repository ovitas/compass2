/**
 * 
 */
package no.ovitas.compass2.config.settings;

/**
 * @author gyalai
 *
 */
public class ReferralPlugin implements Plugin {

	private Referral referral;
	
	@Override
	public String dumpOut(String indent) {
		String ind = indent + " ";
		StringBuffer buffer = new StringBuffer(ind);
		buffer.append("ReferralPlugin: \n");
		buffer.append(ind);
		if (referral != null) {
			buffer.append(referral.dumpOut(ind));
		}
		buffer.append("\n");
		return buffer.toString();
	}
	
	public Referral getReferral() {
		return referral;
	}

	public void setReferral(Referral referral) {
		this.referral = referral;
	}

	@Override
	public PluginType getPluginType() {
		return PluginType.REFERRAL_MAIN_PLUGIN;
	}

}
