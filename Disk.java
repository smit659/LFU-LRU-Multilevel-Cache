package cache;

import java.util.Map;

/**
 * Simulates a disk-backed storage layer — the final fallback in the cache hierarchy.
 *
 * <p>In a production system, this would persist data to disk or a database.
 * This implementation logs operations for demonstration purposes.</p>
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class Disk<K, V> implements Cache<K, V> {

    @Override
    public V get(K key) {
        System.out.println("[Disk] GET key=" + key + " → miss (not persisted in demo)");
        return null;
    }

    @Override
    public void put(K key, V value) {
        System.out.println("[Disk] PUT key=" + key + ", value=" + value);
    }

    @Override
    public Map.Entry<K, V> evict() {
        System.out.println("[Disk] EVICT requested (no-op for disk)");
        return null;
    }
}
