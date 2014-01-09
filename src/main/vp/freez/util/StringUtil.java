package vp.freez.util;

/**
 * 
 * @author vpsong
 * 
 */
public final class StringUtil {

	public final static boolean isBlank(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		return false;
	}
}
