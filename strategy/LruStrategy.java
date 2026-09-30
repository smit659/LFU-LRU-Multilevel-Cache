package cache.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Least Recently Used (LRU) eviction strategy.
 *
 * <p>Uses a <b>HashMap + Doubly-Linked List</b> for O(1) get, insert, and evict.
 * The most recently accessed entry is at the head; the least recently used
 * entry is at the tail and gets evicted first.</p>
 *
 * <h3>Time Complexity</h3>
 * <ul>
 *   <li>{@code get}    — O(1)</li>
 *   <li>{@code insert} — O(1)</li>
 *   <li>{@code evict}  — O(1)</li>
 * </ul>
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class LruStrategy<K, V> implements CacheStrategy<K, V> {

    private final Map<K, Node<K, V>> map;
    private final int capacity;
    private Node<K, V> head;
    private Node<K, V> tail;

    public LruStrategy(int capacity) {
        this.map = new HashMap<>();
        this.capacity = capacity;
    }

    @Override
    public boolean insert(K key, V value) {
        if (head == null) {
            head = new Node<>(key, value);
            tail = head;
            map.put(key, head);
            return true;
        }

        if (map.containsKey(key)) {
            Node<K, V> node = map.get(key);
            node.value = value;
            moveToHead(node);
            return true;
        }

        Node<K, V> oldHead = head;
        head = new Node<>(key, value);
        head.next = oldHead;
        oldHead.prev = head;
        map.put(key, head);
        return true;
    }

    @Override
    public V get(K key) {
        if (!map.containsKey(key)) {
            return null;
        }

        Node<K, V> node = map.get(key);
        moveToHead(node);
        return node.value;
    }

    @Override
    public Map.Entry<K, V> evict() {
        if (tail == null) {
            return null;
        }

        Node<K, V> evicted = tail;
        if (tail.prev != null) {
            tail = tail.prev;
            tail.next = null;
        } else {
            head = null;
            tail = null;
        }

        map.remove(evicted.key);
        return Map.entry(evicted.key, evicted.value);
    }

    @Override
    public boolean isFull() {
        return map.size() == capacity;
    }

    /**
     * Moves the given node to the head of the doubly-linked list,
     * marking it as the most recently used.
     */
    private void moveToHead(Node<K, V> node) {
        if (node == head) {
            return;
        }

        // Update tail if needed
        if (node == tail) {
            tail = tail.prev;
        }

        // Unlink from current position
        if (node.prev != null) {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }

        // Move to head
        Node<K, V> oldHead = head;
        head = node;
        head.prev = null;
        head.next = oldHead;
        oldHead.prev = head;
    }
}
