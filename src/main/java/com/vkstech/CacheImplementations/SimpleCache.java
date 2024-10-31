package com.vkstech.CacheImplementations;

import java.util.HashMap;
import java.util.Map;

public class SimpleCache<K, V> {

    private final Map<K, CacheEntry<V>> cache = new HashMap<>();
    private final long ttl;

    public SimpleCache(long ttl) {
        this.ttl = ttl;
    }

    private static class CacheEntry<V> {
        private final V value;
        private final long expiryTime;

        public CacheEntry(V value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    public void put(K key, V value) {
        CacheEntry<V> entry = new CacheEntry<>(value, System.currentTimeMillis() + ttl);
        cache.put(key, entry);
    }

    public void remove(K key) {
        cache.remove(key);
    }

    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);

        if (entry != null && !entry.isExpired()) {
            return entry.value;
        }

        cache.remove(key);
        return null;
    }

    // Periodic cleanup method
    public void cleanup() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleCache<String, String> cache = new SimpleCache<>(2000);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        System.out.println(cache.get("key1"));
        System.out.println(cache.get("key2"));
        Thread.sleep(3000);
        System.out.println(cache.get("key1"));
        cache.cleanup();
    }

}

