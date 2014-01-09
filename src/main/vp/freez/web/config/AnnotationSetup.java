package vp.freez.web.config;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import vp.freez.log.Logger;
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

	private Logger logger = Logger.getLogger(AnnotationSetup.class);

	public void init(FreezConfig config) {
		String controllerPackage = config.getControllerPackage();
		String path = config.getClassPath() + "/"
				+ controllerPackage.replaceAll("\\.", "/");
		Set<FileResource> frSet = Resource.getResource(new File(path),
				controllerPackage).findClassResource();
		if (frSet == null) {
			logger.error("controllerPackage does not exist");
			return;
		}
		Set<AnnotationInfo> aiSet = new HashSet<AnnotationInfo>();
		try {
			for (FileResource fr : frSet) {
				aiSet.addAll(fr.scan());
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		for(AnnotationInfo ai : aiSet) {
			ai.affect();
		}
		Map<Pattern, Method> map = UrlMapping.getUrlMap();
		for(Entry en : map.entrySet()) {
			System.out.println(en.getKey());
		}
	}

	public void destroy(FreezConfig config) {
		// TODO Auto-generated method stub
	}

}
