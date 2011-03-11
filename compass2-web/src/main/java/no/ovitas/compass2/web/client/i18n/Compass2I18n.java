/**
 * Created on 2010.07.16.
 *
 * Copyright (C) 2010 Thot-Soft 2002 Ltd. All Rights Reserved.
 *
 * This software is the proprietary information of ThotSoft 2002 Ltd.
 * Use is subject to license terms.
 *
 * $Id:$
 */
package no.ovitas.compass2.web.client.i18n;

import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * @author nyari
 *
 */
public interface Compass2I18n extends ConstantsWithLookup {
	
	String buttonSearch();
	String buttonSearchAgain();
	String buttonReset();
	String buttonSave();
	String buttonAdvancedSearch();
	String buttonSimpleSearch();
	String buttonUpload();
	String buttonNext();
	String buttonCancel();
	String buttonFinish();
	
	String titleSearch();
	String titleRelationTypes();
	String titleResult();
	String titleTopicExpansion();
	String titleError();
	String titleLoadError();
	String titleSimple();
	String titleAdvanced();
	String titleBaseSettings();
	String titleAdvancedSettings();
	String titleKBUpload();
	String titleEditScopes();
	String titleEditRelationTipes();
	String titleResultsFounded();
	String titleKBGroup();
	String titleAheadTree();
	String titleBackTree();
	String titleAllTree();
	
	String labelSearch();
	String labelHopCount();
	String labelMaxTopicNumberToExpand();
	String labelExpansionThreshold();
	String labelResultThreshold();
	String labelMaxNumberOfHits();
	String labelTopicPrefixMatch();
	String labelFuzzyMatch();
	String labelStartDate();
	String labelEndDate();
	String labelKBFile();
	String labelKBFileType();
	String labelKB();
	String labelKBDescription();
	String labelTreeSearch();
	String labelKBName();
	String labelKBScopeName();
	String labelAheadTree();
	String labelBackTree();
	String labelAllTree();
	String labelKBType();
	String labelSpecGenType();
	String labelTwoWayType();
	String labelAddSuggestion();
	
	String columnId();
	String columnRelationName();
	String columnRef();
	String columnWeightAhead();
	String columnWeightAback();
	String columnTitle();
	String columnScore();
	String columnName();
	String columnScope();
	
	String messageUpload();
	String messageSearch();
	String messageSave();
	String messageLoadError();
	String messageUploadFinished();
	
	String error_30();
	String error_31();
	String error_32();
	String error_33();
	String error_34();
	String error_35();
	String error_10();
	String error_11();
	String error_12();
	String error_13();
	String error_15();
	String error_16();
	String error_17();
	String error_18();
	String error_20();
	String error_21();
	String error_101();
	String error_1();
}
