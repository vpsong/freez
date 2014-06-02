package vp.freez.web.config;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * url映射
 * @author vpsong
 * 
 */
public class UrlMapping {

	private static Map<Pattern, Method> urlMap = new HashMap<Pattern, Method>();
	private static Map<Class<?>, String> namespaceMap = new HashMap<Class<?>, String>();

	public static Map<Pattern, Method> getUrlMap() {
		return urlMap;
	}

	public static Map<Class<?>, String> getNamespaceMap() {
		return namespaceMap;
	}

}
