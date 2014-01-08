package vp.freez.web.config;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 
 * @author vpsong
 * 
 */
public class UrlMapping {

	private Map<Pattern, Method> urlMap = new HashMap<Pattern, Method>();

	public Map<Pattern, Method> getUrlMap() {
		return urlMap;
	}

}
