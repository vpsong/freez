package vp.freez.web.context;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author vpsong
 *
 */
public class FreezRequest extends HttpServletRequestWrapper {

	public FreezRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public HttpSession getSession() {
		return (HttpSession) getAttribute("freez_session");
	}

	@Override
	public HttpSession getSession(boolean create) {
		return getSession();
	}
	
	
}
