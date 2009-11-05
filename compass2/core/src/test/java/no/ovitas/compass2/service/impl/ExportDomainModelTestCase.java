/**
 * 
 */
package no.ovitas.compass2.service.impl;

import no.ovitas.compass2.service.ExportDomainModelManager;

/**
 * @author magyar
 *
 */
public class ExportDomainModelTestCase extends BaseManagerTestCase {

	
	protected ExportDomainModelManager exportDomainModelManager;
	
	public void setExportDomainModelManager(
			ExportDomainModelManager exportDomainModelManager) {
		this.exportDomainModelManager = exportDomainModelManager;
	}

	public void testExport() throws Exception {
		super.logger.info("File name: "+this.exportDomainModelManager.exportModel());
		
	}
}
