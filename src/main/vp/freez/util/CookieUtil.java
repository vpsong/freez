package vp.freez.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie工具
 * 
 * @author vpsong
 * 
 */
public class CookieUtil {

	public static final String VALUE_ENCODE = "utf-8";
	public static final String DEFAULT_PATH = "/";
	public static String DOMAIN;
	/**
	 * 默认最大有效时间
	 */
	public static final int DEFAULT_MAX_AGE = 60 * 60 * 24 * 365;
	/**
	 * 默认有效时间
	 */
	public static final int DEFAULT_AGE = -1;

	public static final void addCookie(final HttpServletResponse response,
			final String key, final String value, final String domain,
			final String path, final int maxAge) {
		String encodedValue;
		try {
			encodedValue = value == null ? "" : URLEncoder.encode(value,
					VALUE_ENCODE);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		Cookie c = new Cookie(key, encodedValue);
		if (domain != null) {
			c.setDomain(domain);
		} else {
			// c.setDomain(DOMAIN);
		}
		c.setPath(path);
		if (maxAge != 0) {
			c.setMaxAge(maxAge);
		}
		response.addCookie(c);
	}

	public static final void addCookie(final HttpServletResponse response,
			final String key, final String value, final String path,
			final int maxAge) {
		addCookie(response, key, value, null, path, maxAge);
	}

	public static final void addCookie(final HttpServletResponse response,
			final String key, final String value, final int maxAge) {
		addCookie(response, key, value, null, DEFAULT_PATH, maxAge);
	}

	public static final void addCookie(final HttpServletResponse response,
			final String key, final String value) {
		addCookie(response, key, value, null, DEFAULT_PATH, DEFAULT_AGE);
	}

	public static final void removeCookie(final HttpServletResponse response,
			final String key, final String domain, final String path) {
		addCookie(response, key, "", domain, path, 0);
	}

	public static final void removeCookie(final HttpServletResponse response,
			final String key, final String path) {
		addCookie(response, key, "", null, path, 0);
	}

	public static final void removeCookie(final HttpServletResponse response,
			final String key) {
		addCookie(response, key, "", null, DEFAULT_PATH, 0);
	}

	public static final String getCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie c : cookies) {
			if (StringUtil.equals(key, c.getName())) {
				try {
					return URLDecoder.decode(c.getValue(), VALUE_ENCODE);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}

	public static final Map<String, String> getCookieMap(
			HttpServletRequest request) {
		Map<String, String> cookieMap = new HashMap<String, String>();
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return cookieMap;
		}
		try {
			for (Cookie c : cookies) {
				cookieMap.put(c.getName(),
						URLDecoder.decode(c.getValue(), VALUE_ENCODE));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return cookieMap;
	}
}
