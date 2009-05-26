package no.ovitas.compass2.service.impl;
import no.ovitas.compass2.Constants;
import no.ovitas.compass2.service.ConfigurationManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author magyar
 * @version 1.0
 */
public class ConfigurationManagerImpl implements ConfigurationManager {

	protected Properties properties;
	private Log log = LogFactory.getLog(getClass());

	public ConfigurationManagerImpl(){
		properties = new Properties();
		InputStream url =  getClass().getResourceAsStream(Constants.CONFIG_FILE);
		if(url!=null){
			try {
				
				properties.load(url);
				url.close();
				
			} catch (FileNotFoundException e) {
				log.error("Error while loading configuration file", e);
			} catch (IOException e) {
				// TODO: add proper exception handling
				log.error("Error while loading configuration file", e);
			}
		}

	}


	/**
	 * 
	 * @param parameterName
	 */
	public String getConfigParameter(String parameterName){
		if(properties !=null && !properties.isEmpty()){
			return properties.getProperty(parameterName);
		}
		return null;
	}

}