package vp.freez.cache.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import vp.freez.cache.Cache;
import vp.freez.cache.CacheOversizedException;

/**
 * LRU cache
 * @author vpsong
 * 
 * @param <K>
 * @param <V>
 */
public class DefaultCache<K, V> implements Cache<K, V> {

	/**
	 * 最大容量
	 */
	private int maxSize;
	
	/**
	 * 命中次数
	 */
	private long hitTimes;
	
	/**
	 * 命中失败次数
	 */
	private long missTimes;

	private Map<K, CacheObject<K, V>> cacheMap;

	public DefaultCache(int initSize, int maxSize) {
		this.maxSize = maxSize;
		cacheMap = new LRUCacheMap();
	}

	public V put(K key, V value, long age) {
		if (this.size() >= maxSize) {
			throw new CacheOversizedException("cache over max size");
		}
		long expires = System.currentTimeMillis() + age * 1000;
		CacheObject<K, V> obj = new CacheObject<K, V>(key, value, expires);
		synchronized (cacheMap) {
			cacheMap.put(key, obj);
		}
		return value;
	}

	public boolean add(K key, V value, long expires) {
		if (this.size() >= maxSize) {
			throw new CacheOversizedException("cache over max size");
		}
		synchronized (cacheMap) {
			long exist = exist(key);
			if (exist > 0) {
				return false;
			}
			put(key, value, expires);
		}
		return true;
	}

	public V get(K key) {
		synchronized (cacheMap) {
			CacheObject<K, V> cobj = cacheMap.get(key);
			if (cobj == null) {
				++missTimes;
				return null;
			}
			++hitTimes;
			return cobj.object;
		}
	}

	public long exist(K key) {
		CacheObject<K, V> cobj = cacheMap.get(key);
		if (cobj == null) {
			return -1L;
		}
		return cobj.expires;
	}

	public int size() {
		return cacheMap.size();
	}

	public V remove(K key) {
		synchronized (cacheMap) {
			CacheObject<K, V> cobj = cacheMap.remove(key);
			if (cobj == null) {
				return null;
			}
			return cobj.object;
		}
	}

	public void clear() {
		synchronized (cacheMap) {
			cacheMap.clear();
		}
	}

	public long getHitTimes() {
		return hitTimes;
	}

	public long getMissTimes() {
		return missTimes;
	}
	
	public Map<K, CacheObject<K, V>> getMap() {
		return cacheMap;
	}

	protected class LRUCacheMap extends LinkedHashMap<K, CacheObject<K, V>> {
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(
				java.util.Map.Entry<K, CacheObject<K, V>> eldest) {
			return System.currentTimeMillis() >= eldest.getValue().expires;
		}

	}

}
