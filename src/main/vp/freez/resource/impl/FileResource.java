package vp.freez.resource.impl;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import vp.freez.resource.Resource;
import vp.freez.util.StringUtil;
import vp.freez.web.annotation.Action;
import vp.freez.web.annotation.AnnotationInfo;
import vp.freez.web.annotation.Cache;
import vp.freez.web.annotation.InterceptorName;
import vp.freez.web.annotation.Interceptors;
import vp.freez.web.annotation.Namespace;
import vp.freez.web.annotation.Service;
import vp.freez.web.annotation.Views;
import vp.freez.web.annotation.impl.MethodAnnotationInfo;
import vp.freez.web.config.UrlMapping;
import vp.freez.web.exception.ResourceException;
import vp.freez.web.interceptor.Interceptor;
import vp.freez.web.interceptor.InterceptorManager;
import vp.freez.web.ioc.IocManager;
import vp.freez.web.proxy.ServiceProxy;

/**
 * 
 * @author vp.song
 * 
 */
public class FileResource extends Resource {

	public FileResource(File file, String packagePath) {
		super(file, packagePath);
		if (!file.exists() || !file.isFile()) {
			throw new ResourceException("FileResource must be a file");
		}
	}

	public Set<AnnotationInfo> scan() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		Set<AnnotationInfo> set = new HashSet<AnnotationInfo>();
		Class<?> cls = Class.forName(getClassName());
		Map<Class<?>, String> namespaceMap = UrlMapping.getNamespaceMap();
		Map<String, Object> iocContainer = IocManager.getInstance()
				.getIocContainer();
		Map<String, Interceptor> interceptorMap = InterceptorManager
				.getInstance().getInterceptorMap();
		Namespace namespace = cls.getAnnotation(Namespace.class);
		if (namespace != null && !StringUtil.isBlank(namespace.value())) {
			namespaceMap.put(cls, namespace.value());
		}
		Service service = cls.getAnnotation(Service.class);
		if (service != null) {
			String name = StringUtil.isBlank(service.value()) ? cls
					.getSimpleName() : service.value();
			ServiceProxy proxy = new ServiceProxy();
			iocContainer.put(name, proxy.newProxyInstance(cls.newInstance()));
		}
		InterceptorName interceptor = cls.getAnnotation(InterceptorName.class);
		if (interceptor != null && !StringUtil.isBlank(interceptor.value())) {
			interceptorMap.put(interceptor.value(),
					(Interceptor) cls.newInstance());
		}

		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			MethodAnnotationInfo ai = new MethodAnnotationInfo();
			ai.setMethod(method);
			ai.setAction(method.getAnnotation(Action.class));
			ai.setViews(method.getAnnotation(Views.class));
			ai.setCache(method.getAnnotation(Cache.class));
			ai.setInterceptors(method.getAnnotation(Interceptors.class));
			set.add(ai);
		}
		return set;
	}

	public String getClassName() {
		String path = getPackagePath();
		return path.substring(0, path.length() - 6);
	}

}
