package no.ovitas.compass2.web.client.model;

public class EntityModel extends BaseListModel {

	private static final long serialVersionUID = 1L;

	public Long getId() {
		return get(Compass2Constans.ID);
	}
	
	public void setId(Long id) {
		set(Compass2Constans.ID, id);
	}
	
}
