package no.ovitas.compass2.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathResult;

import com.sun.org.apache.xpath.internal.domapi.XPathEvaluatorImpl;

public class XmlUtil {
	public static Map<String, String> parseXPath(File f, Map<String, String> fields) throws Exception {
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = documentBuilder.parse(f);
		Node rootNode = document.getDocumentElement();
		XPathEvaluator xPathEvaluator = new XPathEvaluatorImpl();
		
		Map<String, String> ret = new HashMap<String, String>();
		for (Map.Entry<String, String> item : fields.entrySet()) {
			XPathResult value = (XPathResult) xPathEvaluator.evaluate(item.getValue(), rootNode, null, XPathResult.ANY_TYPE, null);
			Node node;
			while ((node = value.iterateNext()) != null) {
				ret.put(item.getKey(), node.getTextContent());
			}
		}
		return ret;
	}
}
