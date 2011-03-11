package no.ovitas.compass2.fts.util;

import java.util.ArrayList;
import java.util.List;

public class DeleteConfiguration {
	private List<DocumentSetting> documents = new ArrayList<DocumentSetting>(0);

	public List<DocumentSetting> getDocuments() {
		return documents;
	}
	
	public void addDocumentSetting(DocumentSetting documentSetting) {
		documents.add(documentSetting);
	}
}
