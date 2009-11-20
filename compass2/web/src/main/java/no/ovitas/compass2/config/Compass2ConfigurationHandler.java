package no.ovitas.compass2.config;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;

/**
 * @author csanyi
 * 
 */
public class Compass2ConfigurationHandler {
	
	// Attributes
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	// Getter / setter methods
	
	// Constructors

	public Compass2ConfigurationHandler() {}

	// Methods
	
	/**
	 * Setup digester
	 */
	protected void setupDigester(Digester digester){
		
		String prefix = ConfigConstants.COPMASS2_CONFIG;
		
		// compass2-config
		digester.addObjectCreate(prefix, Compass2Configuration.class);
		
		digester.addObjectCreate(prefix + "/" + ConfigConstants.FULL_TEXT_SEARCH, FullTextSearch.class);
	}

}
