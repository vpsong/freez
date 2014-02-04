package vp.freez.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vp.freez.web.exception.RenderException;
import vp.freez.web.view.View;

/**
 * 
 * @author vpsong
 * 
 */
public abstract class Controller {
	protected final static String SUCCESS = "success";

	protected HttpServletRequest request;
	protected HttpServletResponse response;

	protected void renderText(String text) {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RenderException();
	}
	
	protected void renderView(String name) {
		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		String key = View.getViewKey(ste.getClassName(), ste.getMethodName(), name);
		String path = View.getViewMap().get(key);
		try {
			request.getRequestDispatcher(path).forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RenderException();
	}

	protected void renderJSON(String text) {
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RenderException();
	}

	protected boolean isAjaxRequest() {
		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			return true;
		}
		return false;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

}
