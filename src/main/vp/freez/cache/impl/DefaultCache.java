package vp.freez.cache.impl;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import vp.freez.cache.Cache;
import vp.freez.cache.CacheOversizedException;

/**
 * 
 * @author vp.song
 * 
 */
public class DefaultCache<K, V> implements Cache<K, V> {

	private int maxSize;
	private static final float RELEASE_RATE = 0.8F;
	private long hitTimes;
	private long missTimes;

	private Map<K, CacheObject<K, V>> cacheMap;
	private PriorityQueue<CacheObject<K, V>> expiresQueue;

	public DefaultCache(int initSize) {
		this.maxSize = initSize;
		cacheMap = new ConcurrentHashMap<K, CacheObject<K, V>>(initSize);
		expiresQueue = new PriorityQueue<CacheObject<K, V>>(initSize,
				new Comparator<CacheObject<K, V>>() {
					public int compare(CacheObject<K, V> o1,
							CacheObject<K, V> o2) {
						if (o1.expires > o2.expires) {
							return 1;
						} else if (o1.expires < o2.expires) {
							return -1;
						} else {
							return 0;
						}
					}
				});
	}

	public V put(K key, V value, long age) {
		if(this.size() >= maxSize * RELEASE_RATE) {
			deleteExpired();
			if(this.size() >= maxSize) {
				throw new CacheOversizedException();
			}
		}
		long expires = System.currentTimeMillis() + age * 1000;
		CacheObject<K, V> obj = new CacheObject<K, V>(key, value, expires);
		cacheMap.put(key, obj);
		expiresQueue.add(obj);
		return value;
	}

	public boolean add(K key, V value, long expires) {
		if(this.size() >= maxSize * RELEASE_RATE) {
			deleteExpired();
			if(this.size() >= maxSize) {
				throw new CacheOversizedException();
			}
		}
		long exist = exist(key);
		if (exist > 0) {
			return false;
		}
		put(key, value, expires);
		return true;
	}

	public V get(K key) {
		CacheObject<K, V> cobj = cacheMap.get(key);
		if (cobj == null) {
			++missTimes;
			return null;
		}
		if(cobj.expires <= System.currentTimeMillis()) {
			cacheMap.remove(cobj.key);
			expiresQueue.remove(cobj);
			++missTimes;
			return null;
		}
		++hitTimes;
		return cobj.object;
	}

	public long exist(K key) {
		CacheObject<K, V> cobj = cacheMap.get(key);
		if (cobj == null) {
			return -1L;
		}
		if(cobj.expires <= System.currentTimeMillis()) {
			cacheMap.remove(cobj.key);
			expiresQueue.remove(cobj);
			return -1L;
		}
		return cobj.expires;
	}

	public int size() {
		return cacheMap.size();
	}

	public V remove(K key) {
		CacheObject<K, V> cobj = cacheMap.remove(key);
		if (cobj == null) {
			return null;
		}
		expiresQueue.remove(cobj);
		return cobj.object;
	}

	public int deleteExpired() {
		int count = 0;
		CacheObject<K, V> cobj = expiresQueue.peek();
		while (cobj != null && cobj.expires <= System.currentTimeMillis()) {
			cacheMap.remove(cobj.key);
			expiresQueue.poll();
			++count;
			cobj = expiresQueue.peek();
		}
		return count;
	}

	public synchronized void clear() {
		cacheMap.clear();
		expiresQueue.clear();
	}

	public long getHitTimes() {
		return hitTimes;
	}

	public long getMissTimes() {
		return missTimes;
	}

	protected class CacheObject<K, V> {
		public K key;
		public V object;
		public long expires;

		public CacheObject(K key, V object, long expires) {
			this.key = key;
			this.object = object;
			this.expires = expires;
		}
	}

}
