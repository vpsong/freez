package vp.freez.resource.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

import vp.freez.exception.ResourceException;
import vp.freez.resource.Resource;

/**
 * 
 * @author vpsong
 * 
 */
public class DirResource extends Resource {

	public DirResource(File file, String packagePath) {
		super(file, packagePath);
		if (!file.exists() || !file.isDirectory()) {
			throw new ResourceException("DirResource must be a dir");
		}
	}

	public Set<FileResource> findClassResource() {
		Set<FileResource> set = new HashSet<FileResource>();
		File[] files = getFile().listFiles(new ClassFileFilter());
		for (File file : files) {
			Resource resource = Resource.getResource(file, getPackagePath() + "." + file.getName());
			if (resource instanceof FileResource) {
				set.add((FileResource) resource);
			} else if (resource instanceof DirResource) {
				set.addAll(resource.findClassResource());
			}
		}
		return set;
	}

	class ClassFileFilter implements FileFilter {
		public boolean accept(File file) {
			return file.isDirectory() || (file.getName().endsWith(".class"));
		}
	}
}
