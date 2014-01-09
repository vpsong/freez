package vp.freez.web.annotation.impl;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;
import vp.freez.util.StringUtil;
import vp.freez.web.annotation.Action;
import vp.freez.web.annotation.AnnotationInfo;
import vp.freez.web.config.UrlMapping;

/**
 * 
 * @author vpsong
 * 
 */
public class MethodAnnotationInfo implements AnnotationInfo {

	private Method method;
	private Action action;

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public void affect() {
		Map<Pattern, Method> map = UrlMapping.getUrlMap();
		Map<Class<?>, String> namespaceMap = UrlMapping.getNamespaceMap();
		for(String url : action.value()) {
			String namespace = namespaceMap.get(method.getDeclaringClass());
			if(!StringUtil.isBlank(namespace)) {
				url = "/" + namespace + "/" + url;
			}
			map.put(Pattern.compile(url), method);
		}
	}

}
