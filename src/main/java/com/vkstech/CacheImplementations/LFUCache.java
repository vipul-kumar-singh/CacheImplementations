package com.vkstech.CacheImplementations;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class LFUCache<K, V> {

    private int minFrequency = 0;

    private final int capacity;
    private final Map<K, V> cache;
    private final Map<K, Integer> frequencies;
    private final Map<Integer, LinkedHashSet<K>> frequencyList;


    public LFUCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>();
        frequencies = new HashMap<>();
        frequencyList = new HashMap<>();
    }

    private void increaseFrequency(K key) {
        int frequency = frequencies.get(key);
        frequencies.put(key, frequency + 1);

        frequencyList.get(frequency).remove(key);

        if (frequency == minFrequency && frequencyList.get(frequency).isEmpty()) {
            minFrequency++;
        }

        frequencyList.computeIfAbsent(frequency + 1, k -> new LinkedHashSet<>())
                .add(key);
    }

    public V get(K key) {
        if (!cache.containsKey(key))
            return null;

        increaseFrequency(key);
        return cache.get(key);
    }

    public void put(K key, V value) {
        if (capacity <= 0)
            return;

        if (cache.containsKey(key)) {
            cache.put(key, value);
            increaseFrequency(key);
            return;
        }

        if (cache.size() >= capacity)
            removeLFU();

        cache.put(key, value);
        frequencies.put(key, 1);
        minFrequency = 1;
        frequencyList.computeIfAbsent(minFrequency, k -> new LinkedHashSet<>())
                .add(key);
    }

    private void removeLFU() {
        K lfuKey = frequencyList.get(minFrequency).iterator().next();
        frequencyList.get(minFrequency).remove(lfuKey);

        if (frequencyList.get(minFrequency).isEmpty()) {
            frequencyList.remove(minFrequency);
        }

        cache.remove(lfuKey);
        frequencies.remove(lfuKey);
    }

    public static void main(String[] args) {
        LFUCache<Integer, String> cache = new LFUCache<>(3);

        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");
        System.out.println(cache.get(1));

        cache.put(4, "four");
        System.out.println(cache.get(2));
        System.out.println(cache.get(1));
    }
}
