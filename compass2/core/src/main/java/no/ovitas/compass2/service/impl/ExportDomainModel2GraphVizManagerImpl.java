/**
 * 
 */
package no.ovitas.compass2.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;



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
public class ExportDomainModel2GraphVizManagerImpl implements
		ExportDomainModelManager {

    protected static String standardNodePart ="[ style = \"filled\" penwidth = 1 fillcolor = \"white\" fontname = \"Courier New\" shape = \"Mrecord\" label =";
    protected static String standardEdgePart ="[ penwidth = 5 fontsize = 28 fontcolor = \"black\" label =";
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
			tempFile = File.createTempFile("Compass2", ".gv");
			BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile),"8859_1"));
			writeHeader(fw);
			writeNodes(fw);
			writeEdges(fw);
			writeEnd(fw);
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return tempFile.getAbsolutePath();
	}
	
	protected void writeHeader(BufferedWriter fw) throws IOException{
	 fw.write("digraph g {\n");
	 fw.write("graph [fontsize=30 labelloc=\"t\" label=\"\" splines=true overlap=false rankdir = \"LR\"];\n");
	 fw.write("ratio = auto;\n");
	}
	
	protected void writeNodes(BufferedWriter fw)throws IOException{
	 Map<String, Topic> topics = kbHolder.getTopics();
	 for(Topic t : topics.values()){
		 //Base64 b64 = new Base64(true);
		 String label = Base64.encodeBase64URLSafeString(t.getName().getBytes("UTF-8"));
		 fw.write("\""+label+"\"");
		 fw.write(standardNodePart);
		 fw.write("\""+t.getName().replaceAll("\"", "'")+"\"];\n");
	 }
	}
	
	protected void writeEdges(BufferedWriter fw)throws IOException{
		 Map<String, Topic> topics = kbHolder.getTopics();
		 for(Topic t : topics.values()){
			 List<Relation> relations = t.getRelations();
			 for(Relation r: relations){
				 if(r.getSource().getName().equals(t.getName())){
					 Topic target = r.getTarget();
					 String label = r.getRelationType().getId()+": "+r.getRelationType().getWeight();
					 String slabel = Base64.encodeBase64URLSafeString(t.getName().getBytes("UTF-8"));
					 String tlabel = Base64.encodeBase64URLSafeString(target.getName().getBytes("UTF-8"));

					 fw.write(slabel+"->");
					 fw.write(tlabel);
					 fw.write(standardEdgePart);
					 fw.write("\""+label+"\" ];\n");
				 }
			 }
		 }
		
	}

	protected void writeEnd(BufferedWriter fw)throws IOException{
		fw.write("}");
	}
}
