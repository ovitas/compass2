/**
 * 
 */
package no.ovitas.compass2.config.settings;

import java.util.HashSet;
import java.util.Set;

/**
 * @class ContentHandlerPlugin
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.09.14.
 * 
 */
public class ContentHandlerPlugin implements Plugin {

	private Set<ContentHandler> contentHanders = new HashSet<ContentHandler>();
	
	
	
	@Override
	public String dumpOut(String indent) {
		String ind = indent + " ";
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(ind + "ContentHandlerPlugin:\n");
		for (ContentHandler ch : contentHanders) {
			sb.append(ch.dumpOut(ind) + "\n");
		}
		
		return sb.toString();
	}

	
	@Override
	public PluginType getPluginType() {
		// TODO Auto-generated method stub
		return PluginType.CONTENT_HANDLER_MAIN_PLUGIN;
	}


	/**
	 * This is a getter method for contentHanders.
	 * @return the contentHanders
	 */
	public Set<ContentHandler> getContentHanders() {
		return contentHanders;
	}


	/**
	 * This is a setter method for contentHanders.
	 * @param contentHanders the contentHanders to set
	 */
	public void setContentHanders(Set<ContentHandler> contentHanders) {
		this.contentHanders = contentHanders;
	}
	
	public void addContentHandler(ContentHandler contentHandler) {
		contentHanders.add(contentHandler);
	}


	public ContentHandler getSpecifiedContentHandler(String type) {
		for (ContentHandler ch : contentHanders) {
			if (ch.getType() != null && ch.getType().equals(type)) {
				return ch;
			}
		}
		return null;
	}

}
