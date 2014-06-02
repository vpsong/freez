package vp.freez.web.view;

import java.util.HashMap;
import java.util.Map;

/**
 * 视图
 * 
 * @author vpsong
 * 
 */
public class View {

	private static Map<String, String> viewMap = new HashMap<String, String>();

	public static Map<String, String> getViewMap() {
		return viewMap;
	}

	public static String getViewKey(String clsName, String mtdName,
			String viewName) {
		return new StringBuilder(clsName).append("@").append(mtdName)
				.append("@").append(viewName).toString();
	}
}
