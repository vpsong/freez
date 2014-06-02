package vp.freez.web.context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import vp.freez.db.ConnectionPool;
import vp.freez.web.controller.Controller;
import vp.freez.web.exception.RenderException;
import vp.freez.web.interceptor.Interceptor;
import vp.freez.web.interceptor.InterceptorManager;

/**
 * 
 * @author vp.song
 * 
 */
public class ActionInvocation {

	private ActionContext actionContext;
	/**
	 * 目标controller
	 */
	private Controller controller;
	/**
	 * 调用的方法
	 */
	private Method invokeMethod;
	private int interceptIndex;

	public ActionInvocation(ActionContext actionContext, Controller controller,
			Method invokeMethod) {
		this.actionContext = actionContext;
		this.controller = controller;
		this.invokeMethod = invokeMethod;
	}

	public void invoke() {
		List<Interceptor> interceptors = InterceptorManager.getInstance()
				.getInvokeMap().get(invokeMethod);
		if (interceptors != null && interceptIndex < interceptors.size()) {
			Interceptor i = interceptors.get(interceptIndex++);
			i.intercept(this);
			return;
		}
		try {
			invokeMethod.invoke(controller);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof RenderException) {
				// 用exception方式强制返回
				ConnectionPool.getPool().commitOrRollback();
			} else {
				e.printStackTrace();
			}
		}
	}

}
