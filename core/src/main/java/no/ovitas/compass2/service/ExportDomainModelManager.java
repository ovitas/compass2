/**
 * 
 */
package no.ovitas.compass2.service;

import no.ovitas.compass2.exception.ConfigurationException;

/**
 * @author magyar
 *
 */
public interface ExportDomainModelManager {

	
	public String exportModel();
	public void init() throws ConfigurationException;
	
}
