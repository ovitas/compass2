/**
 * 
 */
package no.ovitas.compass2.model;

import java.util.Date;

/**
 * @author magyar
 *
 */
public interface Hit{

	public abstract String getURI();
	public abstract String getTitle();
	public abstract String getFileType();
	public abstract Date getLastModified();
	public abstract String getID();
	public abstract float getScore();
	public abstract String getScoreStr();
	public abstract void setURI(String uri);
	 
	
	
}
