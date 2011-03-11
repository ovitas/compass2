package no.ovitas.compass2.kbimport.rdf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.xml.namespace.QName;

import no.ovitas.compass2.exception.CompassErrorID;
import no.ovitas.compass2.exception.ImportException;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Import mapping between an RDF resource and the Compass2 model.
 * 
 * @author Csaba Daniel
 */
public class RDFImportMapping {

	private class NamespaceRule extends Rule {

		@Override
		public void begin(String namespace, String name, Attributes attributes)
				throws Exception {
			String prefix = attributes.getValue(ATTR_PREFIX);
			String uri = attributes.getValue(ATTR_URI);

			if (StringUtils.isEmpty(prefix) || StringUtils.isEmpty(uri))
				throw new ImportException(
						"Invalid namespace definition: prefix=" + prefix
								+ " uri=" + uri);

			if (nsPrefixes.containsKey(prefix))
				throw new ImportException(
						"Duplicated namespace prefix definition: prefix="
								+ prefix + " uri=" + uri);

			nsPrefixes.put(prefix, uri);
		}
	}

	private class NameGenElementRule extends Rule {

		private QName qname;

		@Override
		public void begin(String namespace, String name, Attributes attributes)
				throws Exception {
			String elemName = attributes.getValue(ATTR_NAME);

			if (!StringUtils.isEmpty(elemName)) {
				qname = createQName(elemName);
			}
		}

		@Override
		public void body(String namespace, String name, String text)
				throws Exception {
			if (!StringUtils.isEmpty(text) && !StringUtils.isWhitespace(text)) {
				if (qname != null)
					throw new ImportException("Duplicate element definition: "
							+ qname);

				String trimmedText = StringUtils.trim(text);

				if (StringUtils.isEmpty(trimmedText))
					throw new ImportException(
							"Invalid element definition: name=" + text);

				qname = createQName(trimmedText);
			}
		}

		@Override
		public void end(String namespace, String name) throws Exception {
			if (qname == null)
				throw new ImportException("Invalid element definition");

			nameGenElements.add(qname);
			qname = null;
		}
	}

	private class RelationGenElementRule extends Rule {

		private QName qname;
		private String label;

		@Override
		public void begin(String namespace, String name, Attributes attributes)
				throws Exception {
			String elemName = attributes.getValue(ATTR_NAME);
			label = attributes.getValue(ATTR_LABEL);

			if (StringUtils.isEmpty(label))
				throw new ImportException("Label is missing: name=" + elemName);

			if (!StringUtils.isEmpty(elemName)) {
				qname = createQName(elemName);
			}
		}

		@Override
		public void body(String namespace, String name, String text)
				throws Exception {
			if (!StringUtils.isEmpty(text) && !StringUtils.isWhitespace(text)) {
				if (qname != null)
					throw new ImportException("Duplicate element definition: "
							+ qname);

				String trimmedText = StringUtils.trim(text);

				if (StringUtils.isEmpty(trimmedText))
					throw new ImportException(
							"Invalid element definition: name=" + text);

				qname = createQName(trimmedText);
			}
		}

		@Override
		public void end(String namespace, String name) throws Exception {
			if (qname == null)
				throw new ImportException("Invalid element definition");

			LabelledQName lQName = new LabelledQName(qname, label);
			relationGenElements.put(qname, lQName);

			qname = null;
			label = null;
		}
	}

	private static final String XP_NAMESPACE = "compass2-rdf/namespace";
	private static final String XP_NAMEGENELEMENT = "compass2-rdf/name-generator-element";
	private static final String XP_RELATIONGENELEMENT = "compass2-rdf/relation-generator-element";

	private static final String ATTR_PREFIX = "prefix";
	private static final String ATTR_URI = "uri";
	private static final String ATTR_NAME = "name";
	private static final String ATTR_LABEL = "label";

	private Map<String, String> nsPrefixes = new HashMap<String, String>();
	private Collection<QName> nameGenElements = new HashSet<QName>();
	private Map<QName, LabelledQName> relationGenElements = new HashMap<QName, LabelledQName>();

	/**
	 * Construct a new mapping.
	 */
	public RDFImportMapping() {
	}

	/**
	 * Add a new name generator element to the mapping.
	 * 
	 * @param qname
	 *            the qualified name of the element to add
	 */
	public void addNameGenElement(QName qname) {
		nameGenElements.add(qname);
	}

	/**
	 * Add a new relation generator element to the mapping.
	 * 
	 * @param qname
	 *            the qualified name of the element to add
	 * @param label
	 *            the label of the element
	 */
	public void addRelationGenElement(QName qname, String label) {
		LabelledQName lQName = new LabelledQName(qname, label);
		relationGenElements.put(qname, lQName);
	}

	/**
	 * Check if a <code>QName</code> exist in the mapping as a name generator
	 * element.
	 * 
	 * @param qname
	 *            the <code>QName</code> to check
	 * @return <code>true</code> if the <code>QName</code> exist in the mapping,
	 *         <code>false</code> otherwise
	 */
	public boolean hasNameGenElement(QName qname) {
		return nameGenElements.contains(qname);
	}

	/**
	 * Check if a <code>QName</code> exist in the mapping as a relation
	 * generator element.
	 * 
	 * @param qname
	 *            the <code>QName</code> to check
	 * @return <code>true</code> if the <code>QName</code> exist in the mapping,
	 *         <code>false</code> otherwise
	 */
	public boolean hasRelationGenElement(QName qname) {
		return relationGenElements.containsKey(qname);
	}

	/**
	 * Get the label of a relation generator element.
	 * 
	 * @param qname
	 *            the <code>QName</code> of the relation generator element
	 * @return the label of the element or <code>null</code> if the element does
	 *         not exist in the mapping
	 */
	public String getRelationGenElementLabel(QName qname) {
		LabelledQName lQName = relationGenElements.get(qname);

		if (lQName == null)
			return null;

		String label = lQName.getLabel();
		return label;
	}

	/**
	 * Load the mapping from a mapping file.
	 * <p>
	 * <i>Note: the current mapping is being reset before loading.</i>
	 * </p>
	 * 
	 * @param in
	 *            the InputStream to read the mapping from
	 * @throws ImportException
	 *             in case of any issue during reading or setting the mapping
	 */
	public void load(InputStream in) throws ImportException {
		// Reset the current mapping
		nsPrefixes = new HashMap<String, String>();
		nameGenElements = new HashSet<QName>();
		relationGenElements = new HashMap<QName, LabelledQName>();
		// Set up the digester
		Digester digester = new Digester();

		digester.addRule(XP_NAMESPACE, new NamespaceRule());
		digester.addRule(XP_NAMEGENELEMENT, new NameGenElementRule());
		digester.addRule(XP_RELATIONGENELEMENT, new RelationGenElementRule());
		// Load mapping
		try {
			digester.parse(in);
		} catch (IOException e) {
			throw new ImportException(CompassErrorID.IMP_IO_ERROR, e
					.getMessage(), e);
		} catch (SAXException e) {
			throw new ImportException(CompassErrorID.IMP_INVALID_MAP_FORMAT, e
					.getMessage(), e);
		}
	}

	private QName createQName(String name) throws ImportException {
		int separatorPos = name.indexOf(":");

		if (separatorPos == -1)
			return new QName(name);

		if (separatorPos == 0)
			throw new ImportException(CompassErrorID.IMP_INVALID_MAP_FORMAT,
					"Prefix is missing: name=" + name);

		if (separatorPos == name.length() - 1)
			throw new ImportException(CompassErrorID.IMP_INVALID_MAP_FORMAT,
					"Local part is missing: name=" + name);

		String prefix = name.substring(0, separatorPos);
		String localPart = name.substring(separatorPos + 1);

		String uri = nsPrefixes.get(prefix);

		if (StringUtils.isEmpty(uri))
			throw new ImportException(CompassErrorID.IMP_INVALID_MAP_FORMAT,
					"Unregistered prefix: prefix=" + prefix + " name=" + name);

		if (StringUtils.isEmpty(localPart))
			throw new ImportException(CompassErrorID.IMP_INVALID_MAP_FORMAT,
					"Missing local name: name=" + name);

		QName qname = new QName(uri, localPart, prefix);
		return qname;
	}
}
