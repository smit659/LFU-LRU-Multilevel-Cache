package cache.strategy;

/**
 * Internal data holder for {@link LfuStrategy}.
 * Wraps a key-value pair with an access frequency counter.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
class LfuEntry<K, V> {

    final K key;
    V value;
    int frequency;

    LfuEntry(K key, V value) {
        this.key = key;
        this.value = value;
        this.frequency = 1;
    }
}
