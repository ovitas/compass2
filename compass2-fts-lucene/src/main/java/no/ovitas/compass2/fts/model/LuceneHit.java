/**
 * 
 */
package no.ovitas.compass2.fts.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;

import no.ovitas.compass2.Constants;
import no.ovitas.compass2.exception.CompassErrorID;
import no.ovitas.compass2.exception.CompassException;
import no.ovitas.compass2.model.Hit;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;

/**
 * @author magyar
 * 
 */
public class LuceneHit implements Serializable, Hit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String fileType;
	protected String title;
	protected String URI;
	protected Date lastModified;
	protected String ID;
	protected float score;

	public LuceneHit(Document doc, float score) {
		title = doc.get(Constants.TITLE_INDEX);
		fileType = doc.get(Constants.FILE_TYPE_INDEX);
		URI = doc.get(Constants.URI_INDEX);
		ID = doc.get(Constants.ID_INDEX);
		this.score = score;
		try {
			lastModified = DateTools.stringToDate(doc
					.get(Constants.LAST_MODIFIED_INDEX));
		} catch (ParseException e) {
			throw new CompassException(CompassErrorID.FTS_CONTENT_PARSE_ERROR, "Exception when parsing document!", e);
		}
	}

	protected void setFileType(String fileType) {
		this.fileType = fileType;
	}

	protected void setTitle(String title) {
		this.title = title;
	}

	public void setURI(String uri) {
		URI = uri;
	}

	protected void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getFileType() {
		return fileType;
	}

	public String getTitle() {
		return title;
	}

	public String getURI() {
		return URI;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public String getID() {
		return ID;
	}

	public void setID(String id) {
		ID = id;
	}

	@Override
	public String toString() {
		String ret = ".[ID].=" + ID + ";";
		ret += ".[URI].=" + URI + ";";
		ret += ".[title].=" + title + ";";
		ret += ".[fileType].=" + fileType + ";";
		ret += ".[lastModified].=" + lastModified + ";";
		ret += ".[score].=" + score + ";";
		return ret;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getScoreStr() {
		DecimalFormat nf = new DecimalFormat("0.###");

		return nf.format(score);
	}

}
