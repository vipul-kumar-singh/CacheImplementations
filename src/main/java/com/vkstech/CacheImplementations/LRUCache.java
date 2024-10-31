package com.vkstech.CacheImplementations;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LRUCache<K, V> {

    private final int capacity;
    private final Map<K, V> cache;
    private final Deque<K> order;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.order = new LinkedList<>();
    }

    public void put(K key, V value) {
        if (cache.containsKey(key)) {
            cache.put(key, value);
            order.remove(key);
            order.addFirst(key);
            return;
        }

        if (cache.size() >= capacity) {
            K lruKey = order.removeLast();
            cache.remove(lruKey);
        }

        cache.put(key, value);
        order.addFirst(key);
    }

    public V get(K key) {
        if (!cache.containsKey(key))
            return null;

        order.remove(key);
        order.addFirst(key);

        return cache.get(key);
    }

    public void remove(K key) {
        cache.remove(key);
        order.remove(key);
    }

    @Override
    public String toString() {
        return "LRUCache{" +
                "cache=" + cache +
                ", order=" + order +
                '}';
    }

    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>(3);

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
