package vp.freez.web.annotation.impl;

import vp.freez.web.annotation.AnnotationInfo;
import vp.freez.web.annotation.Namespace;

/**
 * 
 * @author vpsong
 * 
 */
public class ClassAnnotationInfo implements AnnotationInfo {

	private Namespace namespace;
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

}
