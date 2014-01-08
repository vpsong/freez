package vp.freez.web;

import vp.freez.web.config.FreezConfig;

/**
 * 
 * @author vpsong
 *
 */
public interface Setup {

	void init(FreezConfig config);
	
	void destroy(FreezConfig config);
}
