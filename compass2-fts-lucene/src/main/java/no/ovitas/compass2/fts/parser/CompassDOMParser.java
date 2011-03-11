package no.ovitas.compass2.fts.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import no.ovitas.compass2.Constants;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CompassDOMParser {
	
	private String mainHeading = "";
	private String subHeading = "";
	private String pageKeywords = "";
	private String pageContent = "";
	private boolean hasPageContent = false;
	private Document document;
	private Log logger = LogFactory.getLog(getClass());
	
	public HashMap<String, String> parse(File xmlFile) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
		DocumentBuilder docBuilder = null;
		String content = readFileContent(xmlFile);
		
		// Check if the given file has contents or not
		if (!content.isEmpty()) {
			//logger.error("Content: " + content);
			String replacedContent = replaceSpecifiedStrings(content);
			//logger.error("Replaced content: " + replacedContent);
	
				// Create a DocumentBuilderFactory
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	
				// Set DocumentBuilderFactory properties
				docBuilderFactory.setIgnoringElementContentWhitespace(true);
	
				// Create a DocumentBuilder
				docBuilder = docBuilderFactory.newDocumentBuilder();
	
				// Create and set an ErrorHandler
				CompassDOMErrorHandler errorHandler = new CompassDOMErrorHandler();
				docBuilder.setErrorHandler(errorHandler);
			
				document = docBuilder.parse(new ByteArrayInputStream(replacedContent.getBytes(CompassDOMConstants.ENCODING)));
				return start();
	
		} else {
			logger.error("The " + xmlFile + " file is empty!");
			throw new IOException("The " + xmlFile + " file is empty!");
		}
		
	}
	
	public CompassDOMParser() {


	}
	
	/**
	 * This method visits all the nodes in a DOM tree
	 * 
	 * @param node
	 * @param level
	 */
	private void visitNode(Node node, int level) {
		int lastInd = -1;
		int firstInd = 0;
		// Process node
		if (node != null) {
			// If there are any children, visit each one
			NodeList list = node.getChildNodes();
			if (list != null){
				for (int i = 0; i < list.getLength(); i++) {
					// Get child node
					Node childNode = list.item(i);
		
					// TITLE
					if (childNode.getNodeName().equals(CompassDOMConstants.TITLE)) {
						
						// ------ MAINHEADING ------
						Node titleNode = childNode;
						String fullHeading = titleNode.getTextContent();
						
						lastInd = fullHeading.length();
						
						// Remove the same part of the main heading
						if (fullHeading.lastIndexOf(CompassDOMConstants.NO_NORWAY_TRAVEL_GUIDE) != -1) {
							lastInd = fullHeading.lastIndexOf(CompassDOMConstants.NO_NORWAY_TRAVEL_GUIDE);
						} else if (fullHeading.lastIndexOf(CompassDOMConstants.EN_NORWAY_TRAVEL_GUIDE) != -1) {
							lastInd = fullHeading.lastIndexOf(CompassDOMConstants.EN_NORWAY_TRAVEL_GUIDE);
						}
						
						if (fullHeading.indexOf(CompassDOMConstants.NO_INTEREST) != -1) {
							firstInd = fullHeading.indexOf(CompassDOMConstants.NO_INTEREST) + CompassDOMConstants.NO_INTEREST.length();
						} else if (fullHeading.indexOf(CompassDOMConstants.EN_INTEREST) != -1) {
							firstInd = fullHeading.indexOf(CompassDOMConstants.EN_INTEREST) + CompassDOMConstants.EN_INTEREST.length();
						}
						
						mainHeading = fullHeading.substring(firstInd, lastInd-2).trim();
					}
					
					// META
					if (childNode.getNodeName().equals(CompassDOMConstants.META)) {
						Node metaNode = childNode;
						if (metaNode.hasAttributes()) {
							for (int j = 0; j < metaNode.getAttributes().getLength()-1; j++) {
		
								// One of the attributes of span
								Node metaNodeFirstAttribute = metaNode.getAttributes().item(j+1);
								Node metaNodeNextAttribute = metaNode.getAttributes().item(j);
								//logger.error(metaNodeFirstAttribute.getNodeName() + ":" + metaNodeFirstAttribute.getNodeValue() + " " + metaNodeNextAttribute.getNodeName() + ":" + metaNodeNextAttribute.getNodeValue());
								
								// ------ SUBHEADING ------
		
								// <meta name="description" content="...">
								if (metaNodeFirstAttribute.getNodeName().equals(CompassDOMConstants.NAME) &&
										metaNodeFirstAttribute.getNodeValue().equals(CompassDOMConstants.DESCRIPTION) &&
										metaNodeNextAttribute.getNodeName().equals(CompassDOMConstants.CONTENT)) {
									subHeading = metaNodeNextAttribute.getNodeValue().trim();
								}
								
								// ------ PAGEKEYWORDS ------
								// <meta name="keywords" content="...">
								if (metaNodeFirstAttribute.getNodeName().equals(CompassDOMConstants.NAME) &&
										metaNodeFirstAttribute.getNodeValue().equals(CompassDOMConstants.KEYWORDS) &&
										metaNodeNextAttribute.getNodeName().equals(CompassDOMConstants.CONTENT)) {
									pageKeywords = removeCommas(metaNodeNextAttribute.getNodeValue());
								}
							}
						}
					}
	
					// DIV
					if (childNode.getNodeName().equals(CompassDOMConstants.DIV)) {
						Node divNode = childNode;
						if (divNode.hasAttributes()) {
							for (int j = 0; j < divNode.getAttributes().getLength(); j++) {
		
								// One of the attributes of div
								Node divNodeAttribute = divNode.getAttributes().item(j);
		
								// ------ PAGECONTENT ------
		
								// <div class="normaltext">
//								if (divNodeAttribute.getNodeName().equals(CompassDOMConstants.CLASS) &&
//									divNodeAttribute.getNodeValue().equals(CompassDOMConstants.CLASS_NORMALTEXT) &&
//									!hasHiddenParentDiv(divNode)) {
//									pageContent = divNode.getTextContent().trim();
//									//pageContent = getSeparatedContent(divNode);
//									lastInd = pageContent.indexOf(CompassDOMConstants.LAST_UPDATED);
//									if (lastInd != -1)
//										pageContent = pageContent.substring(0, lastInd).trim();
//		
//									hasPageContent = true;
//								}
		
								// <div class="c_productheader"> concatenate
								if (divNodeAttribute.getNodeName().equals(CompassDOMConstants.CLASS) &&
									divNodeAttribute.getNodeValue().equals(CompassDOMConstants.CLASS_C_PRODUCTHEADER)) {
									
									pageContent += " " + divNode.getTextContent().trim();
								}
								
								// <div class="c_productmaincontent"> concatenate
								if (divNodeAttribute.getNodeName().equals(CompassDOMConstants.CLASS) &&
									divNodeAttribute.getNodeValue().equals(CompassDOMConstants.CLASS_C_PRODUCTMAINCONTENT)) {
								
									pageContent += " " + divNode.getTextContent().trim();
								}
							}
						}
					}
		
					// Visit child node
					visitNode(childNode, level + 1);
				}
			}// if (list!= null)
		}// if (node != null) 
	}

	/**
	 * Remove commas from str 
	 * @param str
	 * @return
	 */
	private String removeCommas(String str) {
		String result = "";
		String[] keywords;
		
		keywords = str.split(",");
		
		for (String kword : keywords) {
			result += kword.trim() + " ";
		}
		
		return result.trim();
	}

	/**
	 * Check if divNode has a parent which is hidden
	 * @param divNode
	 * @return divNode is hidden or not
	 */
	private boolean hasHiddenParentDiv(Node divNode) {
		Boolean hiddenDiv = false;
		
		Node divNodeParent = divNode.getParentNode();
		
		while (divNodeParent != null) {
			if (divNodeParent.hasAttributes()) {
				for (int i = 0; i < divNodeParent.getAttributes().getLength(); i++) {
					// Check if the div is hidden or not
					// <div style="display: none">
					if (divNodeParent.getAttributes().item(i).getNodeName().equals(CompassDOMConstants.STYLE) &&
							divNodeParent.getAttributes().item(i).getNodeValue().contains(CompassDOMConstants.DISPLAY_NONE)){
						hiddenDiv = true;
					}
				}
			}
			divNodeParent = divNodeParent.getParentNode();
		}
		
		return hiddenDiv;
	}

	/**
	 * Replace specified string in xml content
	 * @param content
	 * @return
	 */
	private String replaceSpecifiedStrings(String content) {
		String replacedContent = content;
		
		// Relace & between strings
		replacedContent = replacedContent.replaceAll(" &amp; ", " and ");
		
		// Replace &quot; to '
		replacedContent = replacedContent.replaceAll("&quot;", "\'");
		
		// Convert html characters to real characters
		replacedContent = StringEscapeUtils.unescapeHtml(replacedContent);
		
		// Replace & from the url
		replacedContent = replacedContent.replaceAll("&", "_");
		
		// Replace ~ from the url
		replacedContent = replacedContent.replaceAll("~", "_");
		
		// Correct the comma's place
		replacedContent = replacedContent.replaceAll(" , ", ", "); 
		
		// Separate the text in tags
		replacedContent = replacedContent.replaceAll("</", " </");
		replacedContent = replacedContent.replaceAll("/>", "/> ");
		
		// Remove dtd definition because we skip the validation
		replacedContent = replacedContent.replaceAll("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", "");
		replacedContent = replacedContent.replaceAll("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", "");
		
		//logger.error(replacedContent);
		
		return replacedContent;
	}

	/**
	 * Read xml file content with UTF-8 encodng
	 * @param xmlFile
	 * @return
	 */
	private String readFileContent(File xmlFile) {
		String content = "";
		try {
			String line;
			BufferedReader in = new BufferedReader(new FileReader(xmlFile));
			while( (line = in.readLine()) != null) {		
				content += new String(line.getBytes(), CompassDOMConstants.ENCODING) + " ";
			}
		}
		catch (UnsupportedEncodingException ue) {
			logger.error("Not supported encoding : " + ue.getMessage());
		}
		catch (IOException e) {
			logger.error("IOException: " + e.getMessage());

		}
		return content;
	}
	
	/**
	 * Start parsing the document
	 */
	protected HashMap<String, String> start(){
		HashMap<String, String> result = new HashMap<String, String>();

		visitNode(document, 0);
		
		result.put(Constants.TITLE_INDEX, mainHeading.replaceAll("\\s+", " "));
		result.put(Constants.SUBHEADING_INDEX, subHeading.replaceAll("\\s+", " "));
		result.put(Constants.DATA_INDEX, (pageKeywords + pageContent).replaceAll("\\s+", " "));
		result.put(Constants.KEYWORD_INDEX, pageKeywords.replaceAll("\\s+", " "));
		
		return result;
	}
}
