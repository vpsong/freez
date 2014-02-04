package vp.freez.cache;

/**
 * 
 * @author vp.song
 *
 */
public class CacheOversizedException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public CacheOversizedException(String msg) {
		super(msg);
	}
	
}
