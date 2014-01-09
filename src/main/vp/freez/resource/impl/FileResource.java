package vp.freez.resource.impl;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import vp.freez.exception.ResourceException;
import vp.freez.resource.Resource;
import vp.freez.web.annotation.Action;
import vp.freez.web.annotation.AnnotationInfo;
import vp.freez.web.annotation.Namespace;
import vp.freez.web.annotation.impl.ClassAnnotationInfo;
import vp.freez.web.annotation.impl.MethodAnnotationInfo;
import vp.freez.web.config.UrlMapping;

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

	public Set<AnnotationInfo> scan() throws ClassNotFoundException {
		Set<AnnotationInfo> set = new HashSet<AnnotationInfo>();
		Class<?> cls = Class.forName(getClassName());
		Annotation[] annotations = cls.getDeclaredAnnotations();
		Map<Class<?>, String> namespaceMap = UrlMapping.getNamespaceMap();
		for (Annotation an : annotations) {
			if (an instanceof Namespace) {
				namespaceMap.put(cls, ((Namespace) an).value());
			}
		}
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			Annotation[] ans = method.getAnnotations();
			for (Annotation an : ans) {
				MethodAnnotationInfo ai = new MethodAnnotationInfo();
				if (an instanceof Action) {
					ai.setAction((Action) an);
				}
				ai.setMethod(method);
				set.add(ai);
			}
		}
		return set;
	}

	public String getClassName() {
		String path = getPackagePath();
		return path.substring(0, path.length() - 6);
	}

}
