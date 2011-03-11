package no.ovitas.compass2.kbimport.rdf;

import java.io.Serializable;

import javax.xml.namespace.QName;

/**
 * This class represents a <code>javax.xml.namespace.QName</code> with an
 * additional text label.
 * 
 * @author Csaba Daniel
 */
public class LabelledQName implements Serializable {

	private static final long serialVersionUID = 1L;

	private QName qname;
	private String label;

	/**
	 * <code>QName</code> constructor specifying the the <code>QName</code> and
	 * label.
	 * 
	 * @param qname
	 *            the <code>QName</code>
	 * @param label
	 *            the label of the <code>QName</code>
	 */
	public LabelledQName(QName qname, String label) {
		this.qname = qname;
		this.label = label;
	}

	/**
	 * <code>QName</code> constructor specifying the the Namespace URI, local
	 * part, prefix and label.
	 * <p>
	 * If the Namespace URI is <code>null</code>, it is set to <code>""</code>
	 * </p>
	 * <p>
	 * If the local part is <code>null</code> or <code>.equals("")</code>, an
	 * <code>IllegalArgumentException</code> is thrown.
	 * </p>
	 * <p>
	 * If the prefix is <code>null</code>, an IllegalArgumentException is
	 * thrown. Use <code>""</code> to explicitly indicate that no prefix is
	 * present or the prefix is not relevant.
	 * </p>
	 * 
	 * @param namespaceURI
	 *            Namespace URI of the <code>QName</code>
	 * @param localPart
	 *            local part of the <code>QName</code>
	 * @param prefix
	 *            prefix of the <code>QName</code>
	 * @param label
	 *            label of the <code>QName</code>
	 */
	public LabelledQName(String namespaceURI, String localPart, String prefix,
			String label) {
		qname = new QName(namespaceURI, localPart, prefix);
		this.label = label;
	}

	/**
	 * <code>QName</code> constructor specifying the the Namespace URI, local
	 * part and prefix.
	 * <p>
	 * If the Namespace URI is <code>null</code>, it is set to <code>""</code>
	 * </p>
	 * <p>
	 * If the local part is <code>null</code> or <code>.equals("")</code>, an
	 * <code>IllegalArgumentException</code> is thrown.
	 * </p>
	 * <p>
	 * If the prefix is <code>null</code>, an IllegalArgumentException is
	 * thrown. Use <code>""</code> to explicitly indicate that no prefix is
	 * present or the prefix is not relevant.
	 * </p>
	 * <p>
	 * When using this constructor, the prefix and the label is set to
	 * <code>""</code>.
	 * </p>
	 * 
	 * @param namespaceURI
	 *            Namespace URI of the <code>QName</code>
	 * @param localPart
	 *            local part of the <code>QName</code>
	 * @param prefix
	 *            prefix of the <code>QName</code>
	 */
	public LabelledQName(String namespaceURI, String localPart, String prefix) {
		qname = new QName(namespaceURI, localPart, prefix);
		label = "";
	}

	/**
	 * <code>QName</code> constructor specifying the the Namespace URI and local
	 * part.
	 * <p>
	 * If the Namespace URI is <code>null</code>, it is set to <code>""</code>
	 * </p>
	 * <p>
	 * If the local part is <code>null</code> or <code>.equals("")</code>, an
	 * <code>IllegalArgumentException</code> is thrown.
	 * </p>
	 * <p>
	 * When using this constructor, the prefix and the label are all set to
	 * <code>""</code>.
	 * </p>
	 * 
	 * @param namespaceURI
	 *            Namespace URI of the <code>QName</code>
	 * @param localPart
	 *            local part of the <code>QName</code>
	 */
	public LabelledQName(String namespaceURI, String localPart) {
		qname = new QName(namespaceURI, localPart);
		label = "";
	}

	/**
	 * <code>QName</code> constructor specifying the local part.
	 * <p>
	 * If the local part is <code>null</code> or <code>.equals("")</code>, an
	 * <code>IllegalArgumentException</code> is thrown.
	 * </p>
	 * <p>
	 * When using this constructor, the Namespace URI, the prefix and the label
	 * are all set to <code>""</code>.
	 * </p>
	 * 
	 * @param localPart
	 *            local part of the <code>QName</code>
	 */
	public LabelledQName(String localPart) {
		qname = new QName(localPart);
		label = "";
	}

	/**
	 * Get the label of the <code>QName</code>.
	 * 
	 * @return the label of the <code>QName</code>
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Get the underlying <code>QName</code>.
	 * 
	 * @return the underlying <code>QName</code>
	 */
	public QName getQname() {
		return qname;
	}

}
