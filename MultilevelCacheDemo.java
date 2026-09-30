package cache;

import cache.strategy.LfuStrategy;
import cache.strategy.LruStrategy;

/**
 * Demonstration of the Multilevel Cache System.
 *
 * <p>Architecture:
 * <pre>
 *   Client → CacheController → L1 (LRU, cap=3) → L2 (LFU, cap=3) → null
 * </pre>
 *
 * <p>Key behaviors demonstrated:
 * <ul>
 *   <li><b>Eviction cascade</b>: When L1 is full, evicted entries flow to L2.</li>
 *   <li><b>Read promotion</b>:  L1 fetches cache misses from L2 and stores them locally.</li>
 *   <li><b>Strategy independence</b>: Each level can use a different eviction policy.</li>
 * </ul>
 */
public class MultilevelCacheDemo {

    public static void main(String[] args) {
        // Build the cache hierarchy: L1 (LRU) → L2 (LFU) → null
        LSeriesCache<Integer, String> l2Cache = new LSeriesCache<>(null, new LfuStrategy<>(3));
        L1Cache<Integer, String> l1Cache = new L1Cache<>(l2Cache, new LruStrategy<>(3));
        CacheController<Integer, String> cache = new CacheController<>(l1Cache);

        System.out.println("=== Phase 1: Initial inserts (keys 1-5) ===");
        cache.put(1, "apple");
        cache.put(2, "banana");
        cache.put(3, "cherry");
        cache.put(4, "date");
        cache.put(5, "elderberry");

        System.out.println("\n--- Reading all keys ---");
        for (int i = 1; i <= 5; i++) {
            System.out.printf("  get(%d) = %s%n", i, cache.get(i));
        }

        System.out.println("\n=== Phase 2: More inserts (keys 6-10) ===");
        cache.put(6, "fig");
        cache.put(7, "grape");
        cache.put(8, "honeydew");
        cache.put(9, "kiwi");
        cache.put(10, "lemon");

        System.out.println("\n--- Reading all keys ---");
        for (int i = 1; i <= 10; i++) {
            System.out.printf("  get(%d) = %s%n", i, cache.get(i));
        }

        System.out.println("\n=== Phase 3: Final inserts (keys 11-15) ===");
        cache.put(11, "mango");
        cache.put(12, "nectarine");
        cache.put(13, "orange");
        cache.put(14, "papaya");
        cache.put(15, "quince");

        System.out.println("\n--- Reading all keys ---");
        for (int i = 1; i <= 15; i++) {
            System.out.printf("  get(%d) = %s%n", i, cache.get(i));
        }
    }
}
