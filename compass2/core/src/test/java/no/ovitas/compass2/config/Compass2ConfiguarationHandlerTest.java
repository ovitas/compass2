package no.ovitas.compass2.config;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import junit.framework.TestCase;

public class Compass2ConfiguarationHandlerTest extends TestCase {

	private final Log log = LogFactory.getLog(this.getClass());
    public String configFile;
    public ResourceBundle rb;
    
	public Compass2ConfiguarationHandlerTest(){
		
	}

	public Compass2ConfiguarationHandlerTest(String name){
		super(name);
	}

	public void setUp() {
        rb = ResourceBundle.getBundle("compass2");
        configFile = rb.getString("config.file.location");
    }
	@Test
	public void testConfigLoad(){
        log.info("Loading config file from " +configFile);
        Compass2ConfigurationHandler handler = new Compass2ConfigurationHandler();
		handler.initialize(configFile);
		Compass2Configuration config = handler.getConfig();
		assertNotNull(config);
		
	}
	
}
