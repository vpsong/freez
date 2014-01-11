package vp.freez.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vp.freez.web.Setup;
import vp.freez.web.config.AnnotationSetup;
import vp.freez.web.config.FreezConfig;
import vp.freez.web.config.UrlMapping;
import vp.freez.web.controller.Controller;

/**
 * 
 * @author vpsong
 * 
 */
public class FreezServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = Logger.getLogger("FreezServlet");

	@Override
	public void init() throws ServletException {
		ServletConfig servletConfig = getServletConfig();
		String setupType = servletConfig.getInitParameter("setup");
		Setup setup = null;
		if ("annotation".equals(setupType)) {
			setup = new AnnotationSetup();
		}
		if (setup == null) {
			logger.log(Level.SEVERE, "unsupport setup type");
		}
		String controllerPackage = servletConfig.getInitParameter("controllerPackage");
		String classPath = servletConfig.getServletContext().getRealPath("/WEB-INF/classes/");
		FreezConfig freezConfig = new FreezConfig(setupType, controllerPackage, classPath);
		setup.init(freezConfig);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		for(Entry<Pattern, Method> en: UrlMapping.getUrlMap().entrySet()) {
			if(en.getKey().matcher(request.getRequestURI()).find()) {
				Method method = en.getValue();
				try {
					Controller ctrl = (Controller)method.getDeclaringClass().newInstance();
					ctrl.setRequest(request);
					ctrl.setResponse(response);
					method.invoke(ctrl);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
