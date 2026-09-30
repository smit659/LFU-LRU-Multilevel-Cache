# Multilevel Cache System (LRU + LFU)

A **generic, extensible multilevel cache** built in Java from scratch — no external libraries.

Each cache level can be independently configured with a pluggable eviction strategy (LRU, LFU, or custom), and evicted entries automatically cascade to the next level.

## Architecture

```
Client
  │
  ▼
┌──────────────────┐
│  CacheController  │   ← Simple get/put API
└────────┬─────────┘
         ▼
┌──────────────────┐
│   L1Cache (LRU)   │   ← Fastest level, promotes on read
└────────┬─────────┘
         │  evict ↓  promote ↑
         ▼
┌──────────────────┐
│ LSeriesCache (LFU)│   ← L2/L3 levels, no promotion
└────────┬─────────┘
         │  evict ↓
         ▼
┌──────────────────┐
│      Disk         │   ← Persistent storage (optional)
└──────────────────┘
```

## Design Patterns

| Pattern | Where | Purpose |
|---------|-------|---------|
| **Strategy** | `CacheStrategy` interface + `LruStrategy`, `LfuStrategy` | Pluggable eviction policies per cache level |
| **Template Method** | `AbstractCache.put()` | Common evict-then-insert flow shared across all levels |
| **Chain of Responsibility** | `L1Cache → LSeriesCache → Disk` | Each level handles or delegates to the next |

## Key Features

- **O(1) operations** — Both LRU and LFU achieve constant-time get, put, and evict
- **Generic types** — `Cache<K, V>` works with any key-value types
- **Write-back on eviction** — Evicted entries cascade to the next cache level
- **Read promotion (L1)** — Cache misses fetch from lower levels and store locally
- **Fully extensible** — Add new eviction strategies by implementing `CacheStrategy<K, V>`

## Data Structures

### LRU Strategy
- `HashMap<K, Node<K,V>>` for O(1) key lookup
- **Doubly-linked list** for O(1) reordering (most recent → head, evict from tail)

### LFU Strategy
- `HashMap<K, LfuEntry<K,V>>` for O(1) key lookup
- `HashMap<Integer, LinkedHashSet<LfuEntry>>` — frequency buckets for O(1) eviction
- Tracks `minFrequency` to always evict the least frequently used entry

## Project Structure

```
src/
└── cache/
    ├── Cache.java              # Core interface
    ├── AbstractCache.java      # Template base class
    ├── L1Cache.java            # Level-1 cache (promotes on read)
    ├── LSeriesCache.java       # Level-2+ cache (no promotion)
    ├── Disk.java               # Disk storage simulator
    ├── CacheController.java    # Client-facing API
    ├── MultilevelCacheDemo.java # Demo / entry point
    └── strategy/
        ├── CacheStrategy.java  # Strategy interface
        ├── LruStrategy.java    # LRU implementation
        ├── LfuStrategy.java    # LFU implementation
        ├── Node.java           # Doubly-linked list node (LRU)
        └── LfuEntry.java       # Frequency-tracked entry (LFU)
```

## How to Run

```bash
# Compile
javac -d out src/cache/*.java src/cache/strategy/*.java

# Run
java -cp out cache.MultilevelCacheDemo
```

## Time & Space Complexity

| Operation | LRU | LFU |
|-----------|-----|-----|
| `get`     | O(1) | O(1) |
| `insert`  | O(1) | O(1) |
| `evict`   | O(1) | O(1) amortized |
| **Space** | O(n) | O(n) |

## Extending the System

To add a new eviction strategy (e.g., FIFO, MRU):

```java
public class FifoStrategy<K, V> implements CacheStrategy<K, V> {
    // Implement insert, get, evict, isFull
}

// Use it:
L1Cache<String, Integer> cache = new L1Cache<>(null, new FifoStrategy<>(100));
```

## License

MIT
