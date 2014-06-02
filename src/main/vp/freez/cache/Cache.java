package vp.freez.cache;

/**
 * 
 * @author vp.song
 * 
 */
public interface Cache<K, V> {
	
	V put(K key, V value, long expires);

	/**
	 * 已经存在的不会再覆盖
	 * @param key
	 * @param value
	 * @param expires
	 * @return false if already exist
	 */
	boolean add(K key, V value, long expires);

	V get(K key);

	long exist(K key);

	int size();

	V remove(K key);

	void clear();

}
