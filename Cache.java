package cache;

import java.util.Map;

/**
 * Core interface defining the contract for all cache levels.
 * Each cache level must support get, put, and evict operations.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
public interface Cache<K, V> {

    /**
     * Retrieves the value associated with the given key.
     *
     * @param key the key whose value is to be retrieved
     * @return the value associated with the key, or {@code null} if not found
     */
    V get(K key);

    /**
     * Inserts or updates a key-value pair in the cache.
     * May trigger eviction if the cache is full.
     *
     * @param key   the key to insert
     * @param value the value to associate with the key
     */
    void put(K key, V value);

    /**
     * Evicts an entry from the cache based on the underlying strategy.
     *
     * @return the evicted key-value pair, or {@code null} if nothing to evict
     */
    Map.Entry<K, V> evict();
}
