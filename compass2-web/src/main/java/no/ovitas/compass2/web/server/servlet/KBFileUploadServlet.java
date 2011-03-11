/**
 * Created on 2010.08.25.
 *
 * Copyright (C) 2010 Thot-Soft 2002 Ltd. All Rights Reserved.
 *
 * This software is the proprietary information of ThotSoft 2002 Ltd.
 * Use is subject to license terms.
 *
 * $Id:$
 */
package no.ovitas.compass2.web.server.servlet;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.ovitas.compass2.config.CompassManager;
import no.ovitas.compass2.config.factory.CompassManagerFactory;
import no.ovitas.compass2.exception.ImportException;
import no.ovitas.compass2.kb.KnowledgeBase;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseType;
import no.ovitas.compass2.model.knowledgebase.RelationType;
import no.ovitas.compass2.model.knowledgebase.RelationTypeSetting;
import no.ovitas.compass2.model.knowledgebase.Scope;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author nyari
 * 
 */
@Controller
public class KBFileUploadServlet extends HttpServlet {

	private static final String ACTION_UPDATE = "update";

	private static final String ACTION_REPLACE = "replace";

	private static final String ACTION_PARAM = "action";

	public static final String UPLOADED_DOCUMENT_ATTRIBUTE = "compass2.uploadedDocument";

	private static final String URL_PARAM = "url";

	private static final String FILE_PARAM = "file";

	private static final String TYPE_PARAM = "type";

	private Log logger = LogFactory.getLog(getClass());

	@RequestMapping(value = "/import", method = RequestMethod.GET)
	public void importKnowledgeBase(
			@RequestParam("type") String type,
			@RequestParam("file") String fileName,
			@RequestParam("scopes") String scopes,
			@RequestParam("name") String kbName,
			@RequestParam("way") boolean way,
			@RequestParam(required = false, value = "description") String description,
			@RequestParam(required = false, value = "kb_id") long kbId,
			HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		PrintStream output = new PrintStream(resp.getOutputStream());

		logger.info("Automatic file upload requested.");
		output.println("Automatic file upload requested.");

		CompassManager compassManager = CompassManagerFactory.getInstance()
				.getCompassManager();
		Collection<String> uploadTypes = compassManager
				.getAvailableUploadTypes();

		Set<String> uploadTypesWithOutSpace = new HashSet<String>();
		String replace;
		for (String uploadType : uploadTypes) {
			replace = uploadType;

			replace = replace.replaceAll(" ", "");

			replace = replace.replaceAll("\\.", "");

			replace = replace.replaceAll("\\:", "");

			replace = replace.replaceAll("\\?", "");

			replace = replace.replaceAll("\\&", "");
			uploadTypesWithOutSpace.add(replace);
		}

		if (!(uploadTypes.contains(type) || uploadTypesWithOutSpace
				.contains(type))) {
			output.println("Error occured!");
			output.println("Type parameter is not set perfect!");

			logger.debug("Type parameter is not set perfect!");

			output.println("The usable types are:");
			for (String uploadType : uploadTypes) {
				output.println("\t" + uploadType);
			}
			for (String uploadType : uploadTypesWithOutSpace) {
				output.println("\t" + uploadType);
			}

			output.println(usage());

			return;
		}

		KnowledgeBase newKnowledgeBase = compassManager
				.newInstanceKnowledgeBase((!way) ? KnowledgeBaseType.SPECGEN
						: KnowledgeBaseType.TWOWAY);

		if (kbId != 0) {
			newKnowledgeBase.loadDefaultKnowledgeBase(kbId);
		} else {
			newKnowledgeBase.createDefaultKnowledgeBase(kbName, description);
		}

		logger.info("Start reading the file");
		try {
			compassManager
					.importKnowledgeBase(fileName, type, newKnowledgeBase);

			Collection<Scope> importedScopes;
			importedScopes = compassManager.getImportedScopes();

			List<Long> selectedScopes = new ArrayList<Long>();

			List<String> splitedScope = Arrays.asList(scopes.split(";"));

			for (Scope scope : importedScopes) {
				if (splitedScope.contains(scope.getExternalId())) {
					selectedScopes.add(scope.getImportId());
				}
			}

			logger.info("Import the selected scopes");
			compassManager.filterImportedKnowledgeBase(selectedScopes);

			Collection<RelationType> relationTypesInImportedKnowledgeBase = compassManager
					.getImportedRelationTypes();

			List<RelationTypeSetting> selectedRelType = new ArrayList<RelationTypeSetting>();

			RelationTypeSetting relSetting;
			for (RelationType relationType : relationTypesInImportedKnowledgeBase) {
				relSetting = new RelationTypeSetting();

				relSetting.setImportId(relationType.getImportId());
				relSetting.setExternalId(relationType.getExternalId());
				relSetting.setWeight(relationType.getWeight());
				relSetting.setGeneralizationWeight(relationType
						.getGeneralizationWeight());

				selectedRelType.add(relSetting);
			}

			logger.info("Start store the imported knowledgeBase");
			compassManager.storeImportedKnowledgeBase(selectedRelType);

		} catch (ImportException e) {
			output.println("Error occured!");
			output.println("When try to upload file!");

			logger.debug("When try to upload file!");

			output.println(usage());
			return;
		}

	}

	private String usage() {
		// TODO Auto-generated method stub
		return null;
	}

}
