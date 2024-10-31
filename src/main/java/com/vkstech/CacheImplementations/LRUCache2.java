package com.vkstech.CacheImplementations;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache2<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public LRUCache2(int capacity) {
        super(capacity, 0.75f, true); // Access order is set to true
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity; // Remove the least recently used entry if size exceeds capacity
    }

    public static void main(String[] args) {
        LRUCache2<Integer, String> cache = new LRUCache2<>(3);

        cache.put(1, "one");
        cache.put(2, "too");
        cache.put(3, "three");
        System.out.println(cache);

        cache.get(1);
        cache.put(2, "two");
        System.out.println(cache);

        cache.put(4, "four");
        System.out.println(cache);
    }
}
