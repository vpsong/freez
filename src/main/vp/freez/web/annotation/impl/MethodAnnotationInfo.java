package vp.freez.web.annotation.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import vp.freez.util.StringUtil;
import vp.freez.web.annotation.Action;
import vp.freez.web.annotation.AnnotationInfo;
import vp.freez.web.annotation.Cache;
import vp.freez.web.annotation.Interceptors;
import vp.freez.web.annotation.JSP;
import vp.freez.web.annotation.Views;
import vp.freez.web.cache.WebCache;
import vp.freez.web.config.UrlMapping;
import vp.freez.web.interceptor.Interceptor;
import vp.freez.web.interceptor.InterceptorManager;
import vp.freez.web.view.View;

/**
 * 
 * @author vpsong
 * 
 */
public class MethodAnnotationInfo implements AnnotationInfo {
	
	private static Logger logger = Logger.getLogger("MethodAnnotationInfo");

	private Method method;
	private Action action;
	private Views views;
	private Cache cache;
	private Interceptors interceptors;

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public Interceptors getInterceptors() {
		return interceptors;
	}

	public void setInterceptors(Interceptors interceptors) {
		this.interceptors = interceptors;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Views getViews() {
		return views;
	}

	public void setViews(Views views) {
		this.views = views;
	}

	public void affect() {
		if (action != null && action.value() != null) {
			Map<Pattern, Method> map = UrlMapping.getUrlMap();
			Map<Class<?>, String> namespaceMap = UrlMapping.getNamespaceMap();
			for (String url : action.value()) {
				String namespace = namespaceMap.get(method.getDeclaringClass());
				if (!StringUtil.isBlank(namespace)) {
					url = new StringBuilder("/").append(namespace).append("/")
							.append(url).append("$").toString();
				}
				map.put(Pattern.compile(url), method);
			}
		}
		if (views != null && views.value() != null) {
			String clsName = method.getDeclaringClass().getName();
			String methodName = method.getName();
			Map<String, String> viewMap = View.getViewMap();
			JSP[] jsps = views.value();
			for (JSP jsp : jsps) {
				viewMap.put(View.getViewKey(clsName, methodName, jsp.name()),
						jsp.path());
			}
		}
		if(interceptors != null && interceptors.value() != null && interceptors.value().length > 0) {
			String[] values = interceptors.value();
			Map<String, Interceptor> interceptorMap = InterceptorManager.getInstance().getInterceptorMap();
			List<Interceptor> list = new ArrayList<Interceptor>();
			for(String value : values) {
				if(interceptorMap.get(value) != null) {
					list.add(interceptorMap.get(value));
				} else {
					logger.log(Level.WARNING, "no interceptor named " + value);
				}
			}
			InterceptorManager.getInstance().getInvokeMap().put(method, list);
		}
	}
}