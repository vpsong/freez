package vp.freez.web.config;

/**
 * 
 * @author vpsong
 * 
 */
public class FreezConfig {

	/**
	 * 初始化类型，annotation或xml
	 */
	private String setupType;
	/**
	 * 注解所在的包
	 */
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
