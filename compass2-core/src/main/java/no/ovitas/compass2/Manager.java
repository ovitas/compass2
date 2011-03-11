package no.ovitas.compass2;

import java.util.Properties;

import no.ovitas.compass2.config.ConfigurationManager;
import no.ovitas.compass2.exception.ConfigurationException;

public interface Manager {

	/**
	 * Set configuration manager.
	 * 
	 * @param manager
	 *            the configuration manager
	 * @throws ConfigurationException
	 */
	public void setConfiguration(ConfigurationManager manager)
			throws ConfigurationException;

	/**
	 * Initialize the manager. In the properties parameter contains all
	 * parameters form configuration XML.
	 * 
	 * @param properties
	 *            parameters from configuration XML
	 * @throws ConfigurationException
	 */
	public void init(Properties properties) throws ConfigurationException;

}