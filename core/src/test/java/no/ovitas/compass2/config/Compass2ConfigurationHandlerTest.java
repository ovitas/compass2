package no.ovitas.compass2.config;

import java.util.ResourceBundle;

import no.ovitas.compass2.exception.ConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import junit.framework.TestCase;

public class Compass2ConfigurationHandlerTest extends TestCase {

	private final Log log = LogFactory.getLog(this.getClass());
    public String configFile;
    public ResourceBundle rb;
    
	public Compass2ConfigurationHandlerTest(){
		
	}

	public Compass2ConfigurationHandlerTest(String name){
		super(name);
	}

	public void setUp() {
        rb = ResourceBundle.getBundle("compass2");
        configFile = rb.getString("config.file.location");
    }
	@Test
	public void testConfigLoad(){       
        try {
        	
        	log.info("Loading config file from " +configFile);
            Compass2ConfigurationHandler handler = Compass2ConfigurationHandler.getInstance();
			handler.loadConfig(configFile);
			Compass2Configuration config = handler.getConfig();
			log.info(config.dumpOut(""));
			assertNotNull(config);
			
		} catch (ConfigurationException ce) {
			log.error("Configuration exception occured!" + ce.getMessage());
		}
	}
	
}
