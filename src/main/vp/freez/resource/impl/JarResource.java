package vp.freez.resource.impl;

import java.io.File;
import java.util.Set;

import vp.freez.resource.Resource;

/**
 * 
 * @author vpsong
 *
 */
public class JarResource extends Resource {

	public JarResource(File file, String packagePath) {
		super(file, packagePath);
		// TODO Auto-generated constructor stub
	}

	public Set<FileResource> findClassResource() {
		// TODO Auto-generated method stub
		return null;
	}

}
