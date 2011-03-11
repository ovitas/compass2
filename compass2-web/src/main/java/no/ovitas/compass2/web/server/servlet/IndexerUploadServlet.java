/**
 * 
 */
package no.ovitas.compass2.web.server.servlet;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.ovitas.compass2.config.CompassManager;
import no.ovitas.compass2.config.factory.CompassManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @class IndexerUploadServlet
 * @project compass2-web
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.09.14.
 * 
 */
@Controller
public class IndexerUploadServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8798855046201601684L;


	private Log logger = LogFactory.getLog(getClass());

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public void index(@RequestParam("type") String type,
			@RequestParam("dir") String dir,
			@RequestParam("reindex") boolean reindex,
			@RequestParam("depth") int depth, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		PrintStream output = new PrintStream(resp.getOutputStream());

		if (type == null || dir == null || depth < 0) {
			output.println("Error occured!");
			logger.debug("Dir, type or depth param doesn't be good!");
			output.println(usage());
			return;
		}

		CompassManagerFactory compassManagerFactory = CompassManagerFactory
				.getInstance();

		if (compassManagerFactory == null) {
			output.println("CompassManagerFactory not available!");
			logger.debug("CompassManagerFactory not available!");
			return;
		}

		CompassManager compassManager = compassManagerFactory
				.getCompassManager();

		if (compassManager == null) {
			output.println("CompassManager not available!");
			logger.debug("CompassManager not available!");
			return;
		}

		try {
			compassManager.indexDocument(reindex, depth, dir, type);
		} catch (Exception e) {
			output.println("Error occured while index process running!");
			logger.error("Error occured while index process running!", e);
		}
	}
	
	@RequestMapping(value = "/config-index", method = RequestMethod.GET)
	public void configIndex(@RequestParam("type") String type,
			@RequestParam("configPath") String configPath,
			HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		PrintStream output = new PrintStream(resp.getOutputStream());

		if (type == null || configPath == null) {
			output.println("Error occured!");
			logger.debug("Dir, type or depth param doesn't be good!");
			output.println(usage());
			return;
		}

		CompassManagerFactory compassManagerFactory = CompassManagerFactory
				.getInstance();

		if (compassManagerFactory == null) {
			output.println("CompassManagerFactory not available!");
			logger.debug("CompassManagerFactory not available!");
			return;
		}

		CompassManager compassManager = compassManagerFactory
				.getCompassManager();

		if (compassManager == null) {
			output.println("CompassManager not available!");
			logger.debug("CompassManager not available!");
			return;
		}

		try {
			compassManager.indexDocuments(configPath, type);
		} catch (Exception e) {
			output.println("Error occured while index process running!");
			logger.error("Error occured while index process running!", e);
		}
	}

	
	// @Override
	// protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	// throws ServletException, IOException {
	//
	// PrintStream output = new PrintStream(resp.getOutputStream());
	//
	// logger.info("Automatic file index requested.");
	// output.println("Automatic file index requested.");
	//
	// String fileParam = req.getParameter(DIR_PARAM);
	//
	// if (fileParam == null) {
	// output.println("Error occured!");
	// output.println("Dir parameter is required!");
	//
	// logger.debug("Dir parameter is required!");
	//
	// output.println(usage());
	//
	// return;
	// }
	//
	// // String reIndex = req.getParameter(REINDEX)
	//
	// }

	private char[] usage() {
		// TODO Auto-generated method stub
		return null;
	}
}
