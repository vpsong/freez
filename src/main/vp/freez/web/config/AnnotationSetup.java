package vp.freez.web.config;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import vp.freez.resource.Resource;
import vp.freez.resource.impl.FileResource;
import vp.freez.web.Setup;
import vp.freez.web.annotation.AnnotationInfo;

/**
 * 
 * @author vpsong
 * 
 */
public class AnnotationSetup implements Setup {

	private static Logger logger = Logger.getLogger("AnnotationSetup");

	public void init(FreezConfig config) {
		String controllerPackage = config.getControllerPackage();
		String path = config.getClassPath() + "/"
				+ controllerPackage.replaceAll("\\.", "/");
		Set<FileResource> frSet = Resource.getResource(new File(path),
				controllerPackage).findClassResource();
		if (frSet == null) {
			logger.log(Level.WARNING, "controllerPackage does not exist");
			return;
		}
		Set<AnnotationInfo> aiSet = new HashSet<AnnotationInfo>();
		try {
			for (FileResource fr : frSet) {
				aiSet.addAll(fr.scan());
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		for(AnnotationInfo ai : aiSet) {
			ai.affect();
		}
	}

	public void destroy(FreezConfig config) {
		// TODO Auto-generated method stub
	}

}
