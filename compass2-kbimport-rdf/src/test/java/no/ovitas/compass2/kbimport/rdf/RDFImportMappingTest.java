package no.ovitas.compass2.kbimport.rdf;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.namespace.QName;

import org.junit.Assert;
import org.junit.Test;

public class RDFImportMappingTest {

	private static final String MAPPING_FILE_PATH = "src/test/resources/NorwayMuseums-mapping.xml";
	private static final String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
	private static final String SKOS = "http://www.w3.org/2004/02/skos/core#";
	private static final QName RDFS_LABEL = new QName(RDFS, "label");
	private static final QName SKOS_PREF_LABEL = new QName(SKOS, "prefLabel");
	private static final QName RDFS_NAME = new QName(RDFS, "name");
	private static final QName SKOS_SUBJECT = new QName(SKOS, "subject");
	private static final QName SKOS_BROADER = new QName(SKOS, "broader");
	private static final QName SKOS_RELATION = new QName(SKOS, "relation");
	private static final String SUBJECT_LABEL = "subject";
	private static final String BROADER_LABEL = "broader";

	@Test
	public void testLoad() throws Exception {
		RDFImportMapping mapping = new RDFImportMapping();

		File file = new File(MAPPING_FILE_PATH);
		FileInputStream in = new FileInputStream(file);
		mapping.load(in);

		Assert.assertEquals(true, mapping.hasNameGenElement(RDFS_LABEL));
		Assert.assertEquals(true, mapping.hasNameGenElement(SKOS_PREF_LABEL));
		Assert.assertEquals(false, mapping.hasNameGenElement(RDFS_NAME));

		Assert.assertEquals(true, mapping.hasRelationGenElement(SKOS_SUBJECT));
		Assert.assertEquals(true, mapping.hasRelationGenElement(SKOS_BROADER));
		Assert
				.assertEquals(false, mapping
						.hasRelationGenElement(SKOS_RELATION));

		Assert.assertEquals(SUBJECT_LABEL, mapping
				.getRelationGenElementLabel(SKOS_SUBJECT));
		Assert.assertEquals(BROADER_LABEL, mapping
				.getRelationGenElementLabel(SKOS_BROADER));
		Assert.assertNull(mapping.getRelationGenElementLabel(SKOS_RELATION));
	}
}
