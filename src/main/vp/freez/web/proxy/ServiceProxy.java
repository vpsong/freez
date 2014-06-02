package vp.freez.web.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import vp.freez.web.annotation.Cache;
import vp.freez.web.cache.WebCache;

public class ServiceProxy implements InvocationHandler {

	private Object targetObject;

	public Object newProxyInstance(Object targetObject) {
		this.targetObject = targetObject;
		return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),
				targetObject.getClass().getInterfaces(), this);
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Cache cacheAnnotation = method.getAnnotation(Cache.class);
		if (cacheAnnotation == null) {
			return method.invoke(targetObject, args);
		}
		// action缓存
		WebCache cache = WebCache.getInstance();
		String cacheKey = WebCache.methodCacheKey(method, args);
		Object ret = cache.get(cacheKey);
		if (ret != null) {
			return ret;
		}
		try {
			ret = method.invoke(targetObject, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int age = cacheAnnotation.expire();
		cache.put(cacheKey, ret, age);
		return ret;
	}

}
