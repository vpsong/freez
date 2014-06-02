package vp.freez.web.context;

import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import vp.freez.util.CookieUtil;

/**
 * 取代服务端的session，将session信息保存到cookie
 * 
 * @author vpsong
 * 
 */
public class FreezSession implements HttpSession {

	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private String id;
	private Map<String, String> cookieMap;
	private long creationTime;
	private long lastAccessedTime;
	private int maxInactiveInterval;
	private boolean valid = true;
	
	public FreezSession(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response =  response;
		creationTime = System.currentTimeMillis();
		lastAccessedTime = creationTime;
		cookieMap = CookieUtil.getCookieMap(request);
	}

	public String getAttribute(String key) {
		return cookieMap.get(key);
	}

	public Enumeration<String> getAttributeNames() {
		return new Vector(cookieMap.keySet()).elements();
	}

	public long getCreationTime() {
		return creationTime;
	}

	public String getId() {
		return id;
	}

	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public ServletContext getServletContext() {
		return request.getServletContext();
	}

	
	public HttpSessionContext getSessionContext() {
		return null;
	}

	public String getValue(String key) {
		return getAttribute(key);
	}

	public String[] getValueNames() {
		String[] names = new String[cookieMap.size()];
		return cookieMap.keySet().toArray(names);
	}

	public void invalidate() {
		this.valid = false;
	}

	public boolean isNew() {
		return false;
	}

	public void putValue(String key, Object value) {
		setAttribute(key, value);
	}

	public void removeAttribute(String key) {
		CookieUtil.addCookie(response, key, "", 1);
	}

	public void removeValue(String key) {
		removeAttribute(key);
	}

	public void setAttribute(String key, Object value) {
		String strValue = String.valueOf(value);
		CookieUtil.addCookie(response, key, strValue);
		cookieMap.put(key, String.valueOf(strValue));
	}

	public void setMaxInactiveInterval(int interval) {
		this.maxInactiveInterval = interval;
	}
	
	public void refresh() {
		lastAccessedTime = System.currentTimeMillis();
	}
	
}
