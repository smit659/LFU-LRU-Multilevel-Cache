package cache;

import cache.strategy.CacheStrategy;

/**
 * Generic lower-level cache (L2, L3, etc.).
 *
 * <p>Unlike {@link L1Cache}, this level does <b>not</b> promote entries on read.
 * It simply checks its own storage first, then delegates to the next level.</p>
 *
 * <h3>Thread Safety</h3>
 * <p>The local strategy is accessed under this level's lock. The lock is
 * released before delegating downstream.</p>
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class LSeriesCache<K, V> extends AbstractCache<K, V> {

    public LSeriesCache(Cache<K, V> nextCache, CacheStrategy<K, V> cacheStrategy) {
        super(nextCache, cacheStrategy);
    }

    /**
     * Retrieves the value for the given key.
     * Checks local storage first (under lock), then delegates downstream without promotion.
     */
    @Override
    public V get(K key) {
        lock.lock();
        try {
            V value = cacheStrategy.get(key);
            if (value != null) {
                return value;
            }
        } finally {
            lock.unlock();
        }
        return nextCache != null ? nextCache.get(key) : null;
    }
}

