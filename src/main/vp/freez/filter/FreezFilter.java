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

import vp.freez.db.ConnectionPool;
import vp.freez.util.StringUtil;
import vp.freez.web.Setup;
import vp.freez.web.annotation.Ioc;
import vp.freez.web.config.AnnotationSetup;
import vp.freez.web.config.FreezConfig;
import vp.freez.web.config.UrlMapping;
import vp.freez.web.context.ActionContext;
import vp.freez.web.context.ActionInvocation;
import vp.freez.web.context.FreezRequest;
import vp.freez.web.context.FreezSession;
import vp.freez.web.controller.Controller;
import vp.freez.web.ioc.IocManager;

/**
 * 框架入口
 * @author vpsong
 * 
 */
public class FreezFilter implements Filter {

	private static Logger logger = Logger.getLogger("FreezFilter");
	/**
	 * 不拦截静态资源
	 */
	private static Pattern ignorePtn = Pattern
			.compile("^(.+[.])(jsp|png|gif|jpg|js|css|jspx|jpeg)$");

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		// request wrapper
		FreezRequest request = new FreezRequest((HttpServletRequest) req);
		HttpServletResponse response = (HttpServletResponse) resp;
		// 与play!类似的cookie式session
		FreezSession session = new FreezSession(request, response);
		request.setAttribute("freez_session", session);

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
					Map<String, String[]> paramMap = request.getParameterMap();
					Map<String, Object> iocContainer = IocManager.getInstance()
							.getIocContainer();
					Field[] fields = ctrl.getClass().getDeclaredFields();
					for (Field f : fields) {
						Ioc ioc = f.getAnnotation(Ioc.class);
						if (ioc != null) {
							String name = StringUtil.isBlank(ioc.value()) ? f
									.getName() : ioc.value();
							Object value = iocContainer.get(name);
							f.setAccessible(true);
							f.set(ctrl, value);
							f.setAccessible(false);
						} else {
							String[] values = paramMap.get(f.getName());
							if (values != null && values.length > 0) {
								f.setAccessible(true);
								f.set(ctrl, values[0]);
								f.setAccessible(false);
							}
						}
					}
					ActionContext actionContext = new ActionContext(request,
							response);
					ActionInvocation invocation = new ActionInvocation(
							actionContext, ctrl, method);
					invocation.invoke();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		if (!isMatch) {
			// 404
			request.getRequestDispatcher("/404.jsp").forward(request, response);
		}

	}

	public void init(FilterConfig fConfig) throws ServletException {
		// 启动类型目前只支持annotation方式
		String setupType = fConfig.getInitParameter("setup");
		Setup setup = null;
		if ("annotation".equals(setupType)) {
			setup = new AnnotationSetup();
		}
		if (setup == null) {
			logger.log(Level.SEVERE, "unsupport setup type");
		}
		// 自动扫描该包下的类
		String controllerPackage = fConfig
				.getInitParameter("controllerPackage");
		String classPath = fConfig.getServletContext().getRealPath(
				"/WEB-INF/classes/");
		FreezConfig freezConfig = new FreezConfig(setupType, controllerPackage,
				classPath);
		setup.init(freezConfig);
	}

	public void destroy() {
		// 销毁连接池
		ConnectionPool.getPool().destroy();
	}

}
