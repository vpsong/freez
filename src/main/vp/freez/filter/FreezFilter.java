package vp.freez.filter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
public class FreezFilter implements Filter {

	private static Logger logger = Logger.getLogger("FreezFilter");
	private static Pattern ignorePtn = Pattern
			.compile("^(.+[.])(jsp|png|gif|jpg|js|css|jspx|jpeg)$");

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		boolean isMatch = false;
		if (ignorePtn.matcher(request.getRequestURI()).find()) {
			chain.doFilter(request, response);
			return;
		}
		for (Entry<Pattern, Method> en : UrlMapping.getUrlMap().entrySet()) {
			if (en.getKey().matcher(request.getRequestURI()).find()) {
				isMatch = true;
				Method method = en.getValue();
				try {
					Controller ctrl = (Controller) method.getDeclaringClass()
							.newInstance();
					ctrl.setRequest(request);
					ctrl.setResponse(response);
					Map<String, String[]> map = request.getParameterMap();
					for (Entry<String, String[]> entry : map.entrySet()) {
						Field field = ctrl.getClass().getDeclaredField(
								entry.getKey());
						if (field != null) {
							field.setAccessible(true);
							field.set(ctrl, entry.getValue()[0]);
							field.setAccessible(false);
						}
					}
					method.invoke(ctrl);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		if (!isMatch) {
			request.getRequestDispatcher("/404.jsp").forward(request, response);
		}

	}

	public void init(FilterConfig fConfig) throws ServletException {
		String setupType = fConfig.getInitParameter("setup");
		Setup setup = null;
		if ("annotation".equals(setupType)) {
			setup = new AnnotationSetup();
		}
		if (setup == null) {
			logger.log(Level.SEVERE, "unsupport setup type");
		}
		String controllerPackage = fConfig
				.getInitParameter("controllerPackage");
		String classPath = fConfig.getServletContext().getRealPath(
				"/WEB-INF/classes/");
		FreezConfig freezConfig = new FreezConfig(setupType, controllerPackage,
				classPath);
		setup.init(freezConfig);
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}

}
