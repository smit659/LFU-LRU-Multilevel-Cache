package cache;

/**
 * Entry point for client code to interact with the multilevel cache system.
 *
 * <p>The controller provides a simple {@code get/put} API and hides the
 * complexity of the multilevel hierarchy behind a single interface.</p>
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class CacheController<K, V> {

    private final L1Cache<K, V> l1Cache;

    public CacheController(L1Cache<K, V> l1Cache) {
        this.l1Cache = l1Cache;
    }

    /**
     * Retrieves a value by key. Transparently traverses the cache hierarchy.
     *
     * @param key the key to look up
     * @return the value, or {@code null} if not found at any level
     */
    public V get(K key) {
        return l1Cache.get(key);
    }

    /**
     * Stores a key-value pair. Inserted at L1; evictions cascade downward.
     *
     * @param key   the key
     * @param value the value
     */
    public void put(K key, V value) {
        l1Cache.put(key, value);
    }
}
