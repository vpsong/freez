package vp.freez.cache;

/**
 * 
 * @author vp.song
 * 
 */
public interface Cache<K, V> {
	V put(K key, V value, long expires);

	boolean add(K key, V value, long expires);

	V get(K key);

	long exist(K key);

	int size();

	V remove(K key);

	void clear();
}
