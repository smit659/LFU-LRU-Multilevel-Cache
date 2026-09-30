package cache.strategy;

import java.util.Map;

/**
 * Strategy interface for cache eviction policies.
 *
 * <p>Implementations define how entries are stored, retrieved, and evicted.
 * This follows the <b>Strategy Pattern</b>, allowing cache levels to be
 * configured with interchangeable eviction policies (LRU, LFU, etc.).</p>
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public interface CacheStrategy<K, V> {

    /**
     * Inserts or updates a key-value pair in the cache.
     *
     * @param key   the key to insert
     * @param value the value to associate
     * @return {@code true} if the insertion was successful
     */
    boolean insert(K key, V value);

    /**
     * Retrieves the value for the given key and updates access metadata.
     *
     * @param key the key to look up
     * @return the value, or {@code null} if not found
     */
    V get(K key);

    /**
     * Evicts the least priority entry based on the strategy's policy.
     *
     * @return the evicted key-value pair, or {@code null} if empty
     */
    Map.Entry<K, V> evict();

    /**
     * @return {@code true} if the cache has reached its maximum capacity
     */
    boolean isFull();
}
