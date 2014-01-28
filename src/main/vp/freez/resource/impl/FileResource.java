package vp.freez.resource.impl;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import vp.freez.exception.ResourceException;
import vp.freez.resource.Resource;
import vp.freez.util.StringUtil;
import vp.freez.web.annotation.Action;
import vp.freez.web.annotation.AnnotationInfo;
import vp.freez.web.annotation.Cache;
import vp.freez.web.annotation.Namespace;
import vp.freez.web.annotation.Service;
import vp.freez.web.annotation.Views;
import vp.freez.web.annotation.impl.ClassAnnotationInfo;
import vp.freez.web.annotation.impl.MethodAnnotationInfo;
import vp.freez.web.config.UrlMapping;
import vp.freez.web.ioc.IocManager;

/**
 * 
 * @author vpsong
 * 
 */
public class FileResource extends Resource {

	public FileResource(File file, String packagePath) {
		super(file, packagePath);
		if (!file.exists() || !file.isFile()) {
			throw new ResourceException("FileResource must be a file");
		}
	}

	public Set<AnnotationInfo> scan() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Set<AnnotationInfo> set = new HashSet<AnnotationInfo>();
		Class<?> cls = Class.forName(getClassName());
		Map<Class<?>, String> namespaceMap = UrlMapping.getNamespaceMap();
		Map<String, Object> iocContainer = IocManager.getInstance().getIocContainer();
		Namespace namespace = cls.getAnnotation(Namespace.class);
		if (namespace != null) {
			namespaceMap.put(cls, namespace.value());
		}
		Service service = cls.getAnnotation(Service.class);
		if(service != null) {
			String name = StringUtil.isBlank(service.value()) ? cls.getSimpleName() : service.value();
			iocContainer.put(name, cls.newInstance());
		}

		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			MethodAnnotationInfo ai = new MethodAnnotationInfo();
			ai.setMethod(method);
			ai.setAction(method.getAnnotation(Action.class));
			ai.setViews(method.getAnnotation(Views.class));
			ai.setCache(method.getAnnotation(Cache.class));
			set.add(ai);
		}
		return set;
	}

	public String getClassName() {
		String path = getPackagePath();
		return path.substring(0, path.length() - 6);
	}

}
