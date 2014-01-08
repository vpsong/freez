package vp.freez.web.config;

import java.io.File;
import java.util.Set;

import vp.freez.log.Logger;
import vp.freez.resource.Resource;
import vp.freez.resource.impl.FileResource;
import vp.freez.web.Setup;

/**
 * 
 * @author vpsong
 * 
 */
public class AnnotationSetup implements Setup {

	private Logger logger = Logger.getLogger(AnnotationSetup.class);

	public void init(FreezConfig config) {
		String controllerPackage = config.getControllerPackage();
		controllerPackage = controllerPackage.replaceAll("\\.", "/");
		String path = config.getClassPath() + "/" + controllerPackage;
		Set<FileResource> set = Resource.getResource(new File(path))
				.findClassResource();
		if (set == null) {
			logger.error("controllerPackage does not exist");
			return;
		}
		for (FileResource f : set) {
			logger.info(f.getClassName());
		}
	}

	public void destroy(FreezConfig config) {
		// TODO Auto-generated method stub
	}

}
