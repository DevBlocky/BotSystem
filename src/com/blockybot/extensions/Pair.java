package com.blockybot.extensions;

/**
 * Pair classed used as a simple pair class
 * 
 * @author BlockBa5her
 *
 * @param <K>
 *            Generic Key Type
 * @param <V>
 *            Generic Value Type
 */
public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
