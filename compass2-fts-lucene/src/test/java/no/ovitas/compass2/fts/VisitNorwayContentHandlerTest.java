package no.ovitas.compass2.fts;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class VisitNorwayContentHandlerTest {
	
	@Test
	public void testIndex() throws UnsupportedEncodingException, SAXException, ParserConfigurationException, IOException {
		VisitNorwayContentHandlerImpl handler = new VisitNorwayContentHandlerImpl();
		
		handler.getContent(new File("/mnt/storage/exchange/milan/index.html?pid=68311"));
	}

}
