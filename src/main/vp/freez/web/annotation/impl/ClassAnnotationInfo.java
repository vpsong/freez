package vp.freez.web.annotation.impl;

import vp.freez.web.annotation.AnnotationInfo;
import vp.freez.web.annotation.Namespace;
import vp.freez.web.annotation.Service;

/**
 * 类级别注解信息
 * @author vpsong
 * 
 */
public class ClassAnnotationInfo implements AnnotationInfo {

	private Namespace namespace;
	private Service service;
	private Class<?> clazz;

	public Namespace getNamespace() {
		return namespace;
	}

	public void setNamespace(Namespace namespace) {
		this.namespace = namespace;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public void affect() {
		// TODO Auto-generated method stub
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

}
