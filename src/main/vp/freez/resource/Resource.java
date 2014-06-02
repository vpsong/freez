package vp.freez.resource;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

import vp.freez.resource.impl.DirResource;
import vp.freez.resource.impl.FileResource;

/**
 * 资源
 * @author vpsong
 * 
 */
public abstract class Resource {

	/**
	 * 文件
	 */
	private File file;
	/**
	 * 所在包路径
	 */
	private String packagePath;

	public Resource(File file, String packagePath) {
		this.file = file;
		this.packagePath = packagePath;
	}

	public static Resource getResource(File file, String packagePath) {
		if (!file.exists()) {
			return null;
		} else if (file.isFile()) {
			// 文件资源
			return new FileResource(file, packagePath);
		} else if (file.isDirectory()) {
			// 目录资源
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
