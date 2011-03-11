package no.ovitas.compass2.web.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

import no.ovitas.compass2.model.FullTextFieldType;

/**
 * @class SearchFieldModel
 * @project compass2-web
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.07.30.
 * 
 */
public class SearchFieldModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private FullTextFieldType type;

	public void setId(String id) {
		set(Compass2Constans.ID, id);
	}

	public String getId() {
		return get(Compass2Constans.ID);
	}
	
	public void setLabel(String label) {
		set(Compass2Constans.LABEL, label);
	}

	public String getLabel() {
		return get(Compass2Constans.LABEL);
	}
	
	public void setType(FullTextFieldType type) {
		set(Compass2Constans.TYPE, type);
	}

	public FullTextFieldType getType() {
		return get(Compass2Constans.TYPE);
	}
	
}
