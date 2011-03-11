/**
 * Created on 2010.10.01.
 *
 * Copyright (C) 2010 Thot-Soft 2002 Ltd. All Rights Reserved.
 *
 * This software is the proprietary information of ThotSoft 2002 Ltd.
 * Use is subject to license terms.
 *
 * $Id:$
 */
package no.ovitas.compass2.web.client.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import no.ovitas.compass2.web.client.Compass2Main;
import no.ovitas.compass2.web.client.model.FolderModel;
import no.ovitas.compass2.web.client.model.SearchModel;
import no.ovitas.compass2.web.client.model.TopicExpansionModel;
import no.ovitas.compass2.web.client.model.TreeType;
import no.ovitas.compass2.web.client.rpc.Compass2ServiceAsync;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author nyari
 * 
 */
public class TopicExpansions extends LayoutContainer {

	private SearchModel searchModel;
	private List<TopicExpansionModel> topicExpansions;
	private Compass2ServiceAsync compass2Service;
	private Map<Long, TopicExpansionTree> topicExpansionTrees;
	private List<ResearchListener> listeners = new ArrayList<ResearchListener>();

	public TopicExpansions(List<TopicExpansionModel> topicExpansions,
			SearchModel searchModel, Compass2ServiceAsync compass2Service) {
		this.topicExpansions = topicExpansions;
		this.searchModel = searchModel;
		this.compass2Service = compass2Service;
		setLayout(new RowLayout(Orientation.VERTICAL));
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		buildGui();
	}

	private void buildGui() {
		topicExpansionTrees = new HashMap<Long, TopicExpansionTree>();
		for (TopicExpansionModel topicExpansion : topicExpansions) {
			add(createTopicExpansionPanel(topicExpansion), new RowData(1, -1,
					new Margins(0, 0, 10, 0)));
		}
		Button searchButton = new Button(Compass2Main.i18n.buttonSearchAgain());
		add(searchButton, new RowData(1, -1, new Margins(0, 0, 0, 0)));
		searchButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				fireSearch();
			}

		});
	}

	private Widget createTopicExpansionPanel(TopicExpansionModel topicExpansion) {
		ContentPanel topicExpansionsPanel = new ContentPanel(new RowLayout(
				Orientation.VERTICAL));
		topicExpansionsPanel.setCollapsible(true);
		topicExpansionsPanel.setHeading(Compass2Main.i18n.titleTopicExpansion()
				+ " - " + topicExpansion.getKnowledgeBaseName());
		topicExpansionsPanel.setHeight(200);

		TopicExpansionTree topicExpansionTree;
		int size = topicExpansion.getTrees().size();
		for (Entry<TreeType, Long> tree : topicExpansion.getTrees().entrySet()) {
			topicExpansionTree = new TopicExpansionTree(compass2Service,
					tree.getValue());

			switch (tree.getKey()) {
			case AHEAD_TREE:
				topicExpansionTree.setHeading(Compass2Main.i18n
						.titleAheadTree());
				break;
			case BACK_TREE:
				topicExpansionTree.setHeading(Compass2Main.i18n
						.titleBackTree());
				break;
			case TWO_WAY_TREE:
				topicExpansionTree.setHeading(Compass2Main.i18n
						.titleAllTree());
				break;
			default:
				break;
			}

			topicExpansionTrees.put(tree.getValue(), topicExpansionTree);

			topicExpansionsPanel.add(topicExpansionTree, new RowData(1,
					1.0 / size, new Margins(0, 5, 0, 0)));
		}
		Resizable resizable = new Resizable(topicExpansionsPanel);
		resizable.setDynamic(true);
		return topicExpansionsPanel;
	}

	public void addSearchListener(ResearchListener l) {
		listeners.add(l);
	}

	public void removeSearchListener(ResearchListener l) {
		listeners.remove(l);
	}

	public void fireSearch() {
		Collection<Long> unSelectedTopics = new ArrayList<Long>(0);
		Map<Long, Collection<Long>> unSelectedNotOpenedTopics = new HashMap<Long, Collection<Long>>(0);
		Map<Long, Collection<String>> unSelectedNotOpenedTerms = new HashMap<Long, Collection<String>>(0);
		for (Long key : topicExpansionTrees.keySet()) {
			TopicExpansionTree treePanel = topicExpansionTrees.get(key);
			if (treePanel != null) {
				unSelectedTopics.addAll(treePanel.getUnSelectedNodes());
				unSelectedNotOpenedTopics.put(key, treePanel.getUnSelectedNotOpenedNodes());
				unSelectedNotOpenedTerms.put(key, treePanel.getUnSelectedNotOpenedTermNodes());
			}
		}
		for (ResearchListener l : listeners) {
			l.search(searchModel, unSelectedTopics, unSelectedNotOpenedTopics, unSelectedNotOpenedTerms);
		}
	}

}
