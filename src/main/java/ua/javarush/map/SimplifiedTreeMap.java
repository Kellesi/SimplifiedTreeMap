package ua.javarush.map;

import java.util.Comparator;

public class SimplifiedTreeMap<K, V> {
    private final Comparator<? super K> comparator;
    private Entry<K, V> root;
    private int size;

    public SimplifiedTreeMap() {
        comparator = null;
    }

    public SimplifiedTreeMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    public boolean add(K key, V value) {
        if (size == 0) {
            root = new Entry<>(key, value, null);
            size++;
            return true;
        }

        Entry<K, V> temp = root;
        Entry<K, V> parent;
        int diff;

        do {
            parent = temp;
            diff = compare(key, temp.key);
            if (diff < 0) {
                temp = temp.left;
            } else if (diff > 0) {
                temp = temp.right;
            } else {
                temp.value = value;
                return false;
            }
        } while (temp != null);

        Entry<K, V> newEntry = new Entry<>(key, value, parent);

        if (diff > 0) {
            parent.right = newEntry;
        } else {
            parent.left = newEntry;
        }

        size++;
        return true;
    }

    public Entry<K, V> getEntry(K key) {
        Entry<K, V> temp = root;

        do {
            int diff = compare(key, temp.key);
            if (diff < 0) {
                temp = temp.left;
            } else if (diff > 0) {
                temp = temp.right;
            } else {
                return temp;
            }
        } while (temp != null);
        return null;
    }

    public boolean remove(K key) {
        Entry<K, V> forRemove = getEntry(key);
        if (forRemove == null) {
            return false;
        }

        size--;

        if (size == 1) {
            if (root.key == key) {
                root = null;
            }
            return true;
        }

        if (forRemove.left == null && forRemove.right == null) {
            return removeLeaf(forRemove);
        }

        if (forRemove.right != null && forRemove.left == null) {
            if (compare(forRemove.parent.key, forRemove.key) > 0) {
                forRemove.parent.left = forRemove.right;
            } else {
                forRemove.parent.right = forRemove.right;
            }
            return true;
        } else if (forRemove.right == null && forRemove.left != null) {
            if (compare(forRemove.parent.key, forRemove.key) > 0) {
                forRemove.parent.left = forRemove.left;
            } else {
                forRemove.parent.right = forRemove.left;
            }
            return true;
        }

        removeWithTwoChildren(forRemove);
        return true;
    }

    private void removeWithTwoChildren(Entry<K, V> forRemove) {
        Entry<K, V> lowestLeftChild;
        Entry<K, V> temp = forRemove.right;

        do {
            lowestLeftChild = temp;
            temp = temp.left;
        } while (temp != null);

        lowestLeftChild.right = forRemove.right;
        lowestLeftChild.left = forRemove.left;

        if (compare((forRemove.parent).key, forRemove.key) > 0) {
            (forRemove.parent).left = lowestLeftChild;
        } else {
            (forRemove.parent).right = lowestLeftChild;
        }
    }

    private boolean removeLeaf(Entry<K, V> forRemove) {
        if (compare(forRemove.parent.key, forRemove.key) > 0) {
            forRemove.parent.left = null;
        } else {
            forRemove.parent.right = null;
        }
        return true;
    }

    public int size() {
        return size;
    }

    public boolean contains(K key) {
        return getEntry(key) != null;
    }

    private int compare(K k1, K k2) {
        return comparator == null ? ((Comparable<? super K>) k1).compareTo(k2)
                : comparator.compare(k1, k2);
    }

    private class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> left;
        private Entry<K, V> right;
        private Entry<K, V> parent;

        private Entry(K key, V value, Entry<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return "key = " + key + ", value = " + value;
        }
    }
}
