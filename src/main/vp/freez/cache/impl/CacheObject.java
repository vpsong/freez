package vp.freez.cache.impl;

public class CacheObject<K, V> {
	
	public K key;
	public V object;
	/**
	 * 过期时间
	 */
	public long expires;

	public CacheObject(K key, V object, long expires) {
		this.key = key;
		this.object = object;
		this.expires = expires;
	}
}

