package no.ovitas.compass2.webapp.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collection;

import javax.swing.tree.TreeNode;

import org.apache.commons.digester.SetRootRule;
import org.springframework.beans.factory.ListableBeanFactory;

import no.ovitas.compass2.exception.ConfigurationException;
import no.ovitas.compass2.model.Hit;
import no.ovitas.compass2.model.KnowledgeBaseHolder;
import no.ovitas.compass2.model.RelationType;
import no.ovitas.compass2.model.ResultObject;
import no.ovitas.compass2.model.Topic;
import no.ovitas.compass2.model.Relation;
import no.ovitas.compass2.model.TopicTreeNode;
import no.ovitas.compass2.service.CompassManager;
import no.ovitas.compass2.service.ConfigurationManager;
import no.ovitas.compass2.service.factory.CompassManagerFactory;
import no.ovitas.compass2.service.factory.KBFactory;
import no.ovitas.compass2.service.impl.ConfigurationManagerImpl;
import no.ovitas.compass2.Constants;
import no.ovitas.compass2.service.impl.ConfigurationManagerImpl;
import com.opensymphony.xwork2.Preparable;

public class UpdateWeightAction extends BaseAction implements Preparable {

	private static final long serialVersionUID = 2394809088238874832L;

	/*
	 * FIELDS
	 */

	private String modifiedRelationtypeId;
	private String modifiedWeightValue;
	private String modifiedGenWeightValue;
	
	private KnowledgeBaseHolder kbHolder;

	//FIELDS END
	
	/*
	 * Getters & Setters 
	 */	
	
	public String getModifiedRelationtypeId() {
		return modifiedRelationtypeId;
	}

	public void setModifiedRelationtypeId(String modifiedRelationtypeId) {
		this.modifiedRelationtypeId = modifiedRelationtypeId;
	}

	public String getModifiedWeightValue() {
		return modifiedWeightValue;
	}

	public void setModifiedWeightValue(String modifiedWeightValue) {
		this.modifiedWeightValue = modifiedWeightValue;
	}
	
	public String getModifiedGenWeightValue() {
		return modifiedGenWeightValue;
	}

	public void setModifiedGenWeightValue(String modifiedGenWeightValue) {
		this.modifiedGenWeightValue = modifiedGenWeightValue;
	}
	
	public KnowledgeBaseHolder getKbHolder() {
		return kbHolder;
	}

	public void setKbHolder(KnowledgeBaseHolder kbHolder) {
		this.kbHolder = kbHolder;
	}

	//Getters & Setters END

	
	/*
	 * ACTIONS
	 */
	
	public void prepare() throws Exception {
		log.info("updateWeightAction.prepare()");
		// Get knowledge base holder
		kbHolder = KBFactory.getInstance().getKBImplementation().getKbModel();
	}
	
	public String execute() {
		log.info("updateWeightAction.execute()");
		
		// Modify weight number of the specified relationtype id
		String modRelId = getModifiedRelationtypeId();
		String modWeight = getModifiedWeightValue();
		String modGenWeight = getModifiedGenWeightValue();
		
		if (modRelId != null && modWeight != null && modGenWeight != null) {
			updateRelationTypeWeights(modRelId, modWeight, modGenWeight);
		}
		
		return NONE;
	}
	
	/**
	 * Update weight of the specified relationtype id
	 */
	private void updateRelationTypeWeights(String modRelId, String modWeight, String modGenWeight) {
		log.info("updateWeightAction.updateRelationTypeWeights");
		
		Map<String, RelationType> relationTypes = kbHolder.getRelationTypes();
		
		for(String key : relationTypes.keySet()){
			RelationType relType = relationTypes.get(key);
			if (relType.getId().equals(modRelId)) {
				relType.setWeight(Double.parseDouble(modWeight));
				relType.setGeneralizationWeight(Double.parseDouble(modGenWeight));
			}
		}
		
	}

}
