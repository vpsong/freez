package vp.freez.web.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author vp.song
 *
 */
public class InterceptorManager {
	
	private Map<Method, List<Interceptor>> invokeMap = new HashMap<Method, List<Interceptor>>();
	private Map<String, Interceptor> interceptorMap = new HashMap<String, Interceptor>();
	private static InterceptorManager instance = new InterceptorManager();

	private InterceptorManager() {}

	public static InterceptorManager getInstance() {
		return instance;
	}

	public Map<Method, List<Interceptor>> getInvokeMap() {
		return invokeMap;
	}

	public Map<String, Interceptor> getInterceptorMap() {
		return interceptorMap;
	}
	
}
