package vp.freez.resource.impl;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import vp.freez.exception.ResourceException;
import vp.freez.resource.Resource;
import vp.freez.web.annotation.AnnotationInfo;

/**
 * 
 * @author vpsong
 *
 */
public class FileResource extends Resource {

	public FileResource(File file) {
		super(file);
		if(!file.exists() || !file.isFile()) {
			throw new ResourceException("FileResource must be a file");
		}
	}

	public Set<AnnotationInfo> scan() {
		Set<AnnotationInfo> set = new HashSet<AnnotationInfo>();
		
		return set;
	}
	
	public String getClassName() {
		String path = getFile().getAbsolutePath();
		String cp = "classes";
		int from = path.indexOf(cp);
		int to = path.indexOf(".class");
		return path.substring(from + cp.length() +1, to).replaceAll("\\\\", "\\.");
	}

}
