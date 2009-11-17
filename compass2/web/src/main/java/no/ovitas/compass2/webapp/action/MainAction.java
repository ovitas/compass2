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

public class MainAction extends BaseAction implements Preparable {

	private static final long serialVersionUID = 2394809088238874832L;

	/*
	 * FIELDS
	 */
    private static int counter = 0;
    private String data;

	//FIELDS END
	
	/*
	 * Getters & Setters 
	 */	

    public long getServerTime() {
        return System.currentTimeMillis();
    }

    public int getCount() {
        return ++counter;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

	//Getters & Setters END

	
	/*
	 * ACTIONS
	 */
	
	public void prepare() throws Exception {
		log.info("mainAction.prepare()");
	}
	
	public String execute() {
		log.info("mainAction.execute()");
		
		return NONE;
	}
	
}
