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
	private String packagePath;

	public Resource(File file, String packagePath) {
		this.file = file;
		this.packagePath = packagePath;
	}

	public static Resource getResource(File file, String packagePath) {
		if (!file.exists()) {
			return null;
		} else if (file.isFile()) {
			return new FileResource(file, packagePath);
		} else if (file.isDirectory()) {
			return new DirResource(file, packagePath);
		}
		return null;
	}

	public Set<FileResource> findClassResource() {
		return null;
	}

	public File getFile() {
		return file;
	}

	public String getPackagePath() {
		return packagePath;
	}

}
