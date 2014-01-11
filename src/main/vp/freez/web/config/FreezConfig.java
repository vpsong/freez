package vp.freez.web.config;

/**
 * 
 * @author vpsong
 * 
 */
public class FreezConfig {

	private String setupType;
	private String controllerPackage;
	private String classPath;

	public FreezConfig(String setupType, String controllerPackage,
			String classPath) {
		this.setupType = setupType;
		this.controllerPackage = controllerPackage;
		this.classPath = classPath;
	}

	public String getControllerPackage() {
		return controllerPackage;
	}

	public String getSetupType() {
		return setupType;
	}

	public String getClassPath() {
		return classPath;
	}

}
