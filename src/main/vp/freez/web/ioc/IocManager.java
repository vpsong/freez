package vp.freez.web.ioc;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author vpsong
 * 
 */
public class IocManager {

	private Map<String, Object> iocContainer = new HashMap<String, Object>();
	private static IocManager instance = new IocManager();

	private IocManager() {
	}

	public static IocManager getInstance() {
		return instance;
	}

	public Map<String, Object> getIocContainer() {
		return iocContainer;
	}

}
