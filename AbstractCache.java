package cache;

import cache.strategy.CacheStrategy;

import java.util.Map;

/**
 * Abstract base class for all cache levels.
 * Implements common put and evict logic, delegating to a {@link CacheStrategy}
 * for storage and eviction decisions.
 *
 * <p>When the cache is full, the evicted entry is automatically pushed
 * down to the next cache level (write-back on eviction).</p>
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {

    protected final Cache<K, V> nextCache;
    protected final CacheStrategy<K, V> cacheStrategy;

    /**
     * @param nextCache     the next level cache to propagate evicted entries to (may be {@code null})
     * @param cacheStrategy the eviction strategy for this cache level
     */
    public AbstractCache(Cache<K, V> nextCache, CacheStrategy<K, V> cacheStrategy) {
        this.nextCache = nextCache;
        this.cacheStrategy = cacheStrategy;
    }

    /**
     * Inserts a key-value pair. If the cache is full, evicts the least
     * priority entry and pushes it to the next cache level.
     */
    @Override
    public void put(K key, V value) {
        if (cacheStrategy.isFull()) {
            Map.Entry<K, V> evicted = cacheStrategy.evict();
            if (evicted != null && nextCache != null) {
                nextCache.put(evicted.getKey(), evicted.getValue());
            }
        }
        cacheStrategy.insert(key, value);
    }

    @Override
    public Map.Entry<K, V> evict() {
        return cacheStrategy.evict();
    }
}
