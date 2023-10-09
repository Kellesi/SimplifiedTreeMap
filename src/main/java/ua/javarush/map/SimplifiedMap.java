package ua.javarush.map;

public interface SimplifiedMap<K,V> {
    boolean add(K key, V value);
    boolean remove(K key);
    boolean contains(K key);
    int size();
}
