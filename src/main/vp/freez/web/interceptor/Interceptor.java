package vp.freez.web.interceptor;

import vp.freez.web.context.ActionInvocation;

/**
 * 
 * @author vp.song
 *
 */
public interface Interceptor {
	
	void intercept(ActionInvocation invocation);
	
}
