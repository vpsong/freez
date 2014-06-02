package vp.freez.util;

/**
 * 字符串工具
 * @author vpsong
 * 
 */
public final class StringUtil {

	/**
	 * null或空字符返回true
	 * @param str
	 * @return
	 */
	public final static boolean isBlank(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static boolean equals(String str1, String str2) {
		if (str1 == null) {
			return str2 == null ? true : false;
		}
		return str2 == null ? false : str1.equals(str2);
	}
}
