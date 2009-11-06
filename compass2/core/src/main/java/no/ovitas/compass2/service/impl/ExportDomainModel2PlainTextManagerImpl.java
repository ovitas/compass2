/**
 * 
 */
package no.ovitas.compass2.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;



import no.ovitas.compass2.model.KnowledgeBaseHolder;
import no.ovitas.compass2.model.Relation;
import no.ovitas.compass2.model.Topic;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.ExportDomainModelManager;
import no.ovitas.compass2.service.factory.KBFactory;

/**
 * @author magyar
 *
 */
public class ExportDomainModel2PlainTextManagerImpl implements
		ExportDomainModelManager {

	protected ConfigurationManager configManager;
    protected KnowledgeBaseHolder kbHolder=null;

	
	public void setConfiguration(ConfigurationManager manager){
		configManager = manager;
	}

	/* (non-Javadoc)
	 * @see no.ovitas.compass2.service.ExportDomainModelManager#exportModel()
	 */
	@Override
	public String exportModel() {
		File tempFile = null;
		try {
		    kbHolder = KBFactory.getInstance().getKBImplementation().getKbModel();
			tempFile = File.createTempFile("Compass2", ".txt");
			BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile),"UTF-8"));
			if(kbHolder!=null){
				
			 for(Topic src : kbHolder.getTopics().values()){
				 dumpTopic(fw,src);
			 }
			}
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return tempFile.getAbsolutePath();
	}
	
	protected void dumpTopic(BufferedWriter fw, Topic topic) throws IOException{
		fw.write(topic.getName()+"\n");
		for(Relation rel : topic.getRelations()){
			if(rel.getSource().getName().equals(topic.getName())){
				fw.write(" - ");
				fw.write(rel.getRelationType().getRelationName()+":"+rel.getTarget().getName()+"\n");
			}
		}
		
		
	}

}