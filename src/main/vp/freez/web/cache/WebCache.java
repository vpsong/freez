package vp.freez.web.cache;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import vp.freez.cache.impl.PriorityCache;

/**
 * 
 * @author vp.song
 * 
 */
public class WebCache extends PriorityCache<String, Object> {

	private static Logger logger = Logger.getLogger("WebCache");
	private long DEFAULT_AGE = 10L;

	private static WebCache instance = new WebCache();

	private WebCache() {
		super(30);
	}

	public Object put(String key, Object value) {
		logger.info("cache " + key);
		return super.put(key, value, DEFAULT_AGE * 60);
	}

	@Override
	public Object put(String key, Object value, long age) {
		logger.info("cache " + key);
		if(age <= 0) {
			age = DEFAULT_AGE;
		}
		return super.put(key, value, age * 60);
	}

	public static WebCache getInstance() {
		return instance;
	}

	public static String methodCacheKey(Method method, Object[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append(method.getDeclaringClass().getName()).append("@@")
				.append(method.getName());
		if (args != null && args.length > 0) {
			for (Object arg : args) {
				sb.append("@@").append(String.valueOf(arg));
			}
		}
		return sb.toString();
	}
}
