package vp.freez.web.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vp.freez.log.Logger;
import vp.freez.web.Setup;
import vp.freez.web.config.AnnotationSetup;
import vp.freez.web.config.FreezConfig;

/**
 * 
 * @author vpsong
 * 
 */
public class FreezServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(FreezServlet.class);

	public FreezServlet() {
	}

	@Override
	public void init() throws ServletException {
		super.init();
		ServletConfig servletConfig = getServletConfig();
		String setupType = servletConfig.getInitParameter("setup");
		Setup setup = null;
		if ("annotation".equals(setupType)) {
			setup = new AnnotationSetup();
		}
		if (setup == null) {
			logger.error("unsupport setup type");
		}
		String controllerPackage = servletConfig.getInitParameter("controllerPackage");
		String classPath = servletConfig.getServletContext().getRealPath("/WEB-INF/classes/");
		FreezConfig freezConfig = new FreezConfig(setupType, controllerPackage, classPath);
		setup.init(freezConfig);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.service(request, response);
	}

}
