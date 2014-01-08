package vp.freez.resource;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

import vp.freez.resource.impl.DirResource;
import vp.freez.resource.impl.FileResource;

/**
 * 
 * @author vpsong
 *
 */
public abstract class Resource {
	
	private File file;
	
	public Resource(File file) {
		super();
		this.file = file;
	}
	
	public static Resource getResource(File file) {
		if(!file.exists()) {
			return null;
		} else if(file.isFile()) {
			return new FileResource(file);
		} else if(file.isDirectory()) {
			return new DirResource(file);
		}
		return null;
	} 
	
	public Set<FileResource> findClassResource() {
		return null;
	}

	public File getFile() {
		return file;
	}
	
}
