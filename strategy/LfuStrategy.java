package cache.strategy;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Least Frequently Used (LFU) eviction strategy.
 *
 * <p>Uses a <b>HashMap + Frequency Buckets (LinkedHashSet)</b> to achieve
 * O(1) get, insert, and evict. Entries with the lowest access frequency
 * are evicted first. Ties are broken by insertion order (FIFO) within
 * the same frequency bucket.</p>
 *
 * <h3>Time Complexity</h3>
 * <ul>
 *   <li>{@code get}    — O(1)</li>
 *   <li>{@code insert} — O(1)</li>
 *   <li>{@code evict}  — O(1) amortized</li>
 * </ul>
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class LfuStrategy<K, V> implements CacheStrategy<K, V> {

    private final Map<K, LfuEntry<K, V>> map;
    private final Map<Integer, LinkedHashSet<LfuEntry<K, V>>> frequencyBuckets;
    private final int capacity;
    private int minFrequency;

    public LfuStrategy(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.frequencyBuckets = new HashMap<>();
        this.minFrequency = 1;
    }

    @Override
    public boolean insert(K key, V value) {
        if (map.containsKey(key)) {
            LfuEntry<K, V> entry = map.get(key);
            entry.value = value;
            incrementFrequency(entry);
            return true;
        }

        LfuEntry<K, V> entry = new LfuEntry<>(key, value);
        map.put(key, entry);
        frequencyBuckets.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(entry);
        minFrequency = 1;
        return true;
    }

    @Override
    public V get(K key) {
        if (!map.containsKey(key)) {
            return null;
        }

        LfuEntry<K, V> entry = map.get(key);
        incrementFrequency(entry);
        return entry.value;
    }

    @Override
    public Map.Entry<K, V> evict() {
        LinkedHashSet<LfuEntry<K, V>> bucket = frequencyBuckets.get(minFrequency);
        if (bucket == null || bucket.isEmpty()) {
            return null;
        }

        LfuEntry<K, V> evicted = bucket.iterator().next();
        bucket.remove(evicted);

        if (bucket.isEmpty()) {
            frequencyBuckets.remove(minFrequency);
        }

        map.remove(evicted.key);
        return Map.entry(evicted.key, evicted.value);
    }

    @Override
    public boolean isFull() {
        return map.size() == capacity;
    }

    /**
     * Moves an entry from its current frequency bucket to the next one.
     * Updates {@code minFrequency} if the old bucket becomes empty.
     */
    private void incrementFrequency(LfuEntry<K, V> entry) {
        LinkedHashSet<LfuEntry<K, V>> oldBucket = frequencyBuckets.get(entry.frequency);
        if (oldBucket != null) {
            oldBucket.remove(entry);
            if (oldBucket.isEmpty()) {
                frequencyBuckets.remove(entry.frequency);
                if (minFrequency == entry.frequency) {
                    minFrequency = entry.frequency + 1;
                }
            }
        }

        entry.frequency++;
        frequencyBuckets.computeIfAbsent(entry.frequency, k -> new LinkedHashSet<>()).add(entry);
    }
}
