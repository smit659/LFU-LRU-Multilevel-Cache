package cache;

import cache.strategy.CacheStrategy;

/**
 * L1 (Level-1) Cache — the fastest, closest cache to the client.
 *
 * <p>On a cache miss, L1 fetches the value from the next level and
 * <b>promotes</b> it into L1 for faster subsequent access (write-on-read).</p>
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class L1Cache<K, V> extends AbstractCache<K, V> {

    public L1Cache(Cache<K, V> nextCache, CacheStrategy<K, V> cacheStrategy) {
        super(nextCache, cacheStrategy);
    }

    /**
     * Retrieves the value for the given key.
     * <ol>
     *   <li>Checks the local cache strategy first.</li>
     *   <li>On miss, delegates to the next cache level.</li>
     *   <li>If found downstream, promotes the entry into this level.</li>
     * </ol>
     */
    @Override
    public V get(K key) {
        V value = cacheStrategy.get(key);
        if (value != null) {
            return value;
        }

        V downstream = nextCache != null ? nextCache.get(key) : null;
        if (downstream != null) {
            put(key, downstream);
            return downstream;
        }

        return null;
    }
}
