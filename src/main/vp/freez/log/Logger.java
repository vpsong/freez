package vp.freez.log;

import java.text.SimpleDateFormat;
import java.util.Date;

import vp.freez.log.impl.ConsoleLogger;

/**
 * 
 * @author vpsong
 *
 */
public abstract class Logger {
	
	private String name;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
	
	public Logger(Class<?> type) {
		this.name = type.getSimpleName();
	}
	
	public static Logger getLogger(Class<?> type) {
		return new ConsoleLogger(type);
	}
	
	public String time() {
		return dateFormat.format(new Date(System.currentTimeMillis()));
	}
	
	public abstract void info(String msg);
	
	public abstract void error(String msg);

	public String getName() {
		return name;
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}
	
}
