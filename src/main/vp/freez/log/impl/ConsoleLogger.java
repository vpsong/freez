package vp.freez.log.impl;

import vp.freez.log.Logger;

/**
 * 
 * @author vpsong
 *
 */
public class ConsoleLogger extends Logger {

	public ConsoleLogger(Class<?> type) {
		super(type);
	}

	public void info(String msg) {
		System.out.println(this.time() + "--" + this.getName() + "::" + msg);
	}

	public void error(String msg) {
		System.err.println(this.time() + "--" + this.getName() + "::" + msg);
	}
	
}
