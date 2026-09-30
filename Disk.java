package cache;

import java.util.Map;

/**
 * Simulates a disk-backed storage layer — the final fallback in the cache hierarchy.
 *
 * <p>In a production system, this would persist data to disk or a database.
 * This implementation logs operations for demonstration purposes.</p>
 *
 * <h3>Thread Safety</h3>
 * <p>Methods are {@code synchronized} for safe concurrent access.
 * In production, disk I/O would use its own concurrency model.</p>
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class Disk<K, V> implements Cache<K, V> {

    @Override
    public synchronized V get(K key) {
        System.out.println("[Disk] GET key=" + key + " → miss (not persisted in demo)");
        return null;
    }

    @Override
    public synchronized void put(K key, V value) {
        System.out.println("[Disk] PUT key=" + key + ", value=" + value);
    }

    @Override
    public synchronized Map.Entry<K, V> evict() {
        System.out.println("[Disk] EVICT requested (no-op for disk)");
        return null;
    }
}

