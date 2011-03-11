/**
 * 
 */
package no.ovitas.compass2.web.server;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.ovitas.compass2.config.factory.CompassManagerFactory;
import no.ovitas.compass2.exception.ConfigurationException;

/**
 * @author gyalai
 *
 */
public class InitServlet extends HttpServlet {
	
	private Log log = LogFactory.getLog(getClass());

	/**
	 * 
	 */
	private static final long serialVersionUID = 5316232948419585282L;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		
		CompassManagerFactory.getInstance().getCompassManager();
	}

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			CompassManagerFactory.getInstance().getCompassManager().reReadConfig();
		} catch (ConfigurationException e) {
			log.error("Error occured when re config the compass manager!", e);
		}
	}
}
