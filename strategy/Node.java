package cache.strategy;

/**
 * Doubly-linked list node used internally by {@link LruStrategy}.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
class Node<K, V> {

    Node<K, V> prev;
    Node<K, V> next;
    K key;
    V value;

    Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
