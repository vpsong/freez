package vp.freez.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author vpsong
 *
 */
public abstract class Controller {
	protected final static String SUCCESS="success";
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
}
