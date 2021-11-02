package lesson_8;

import java.util.ArrayList;
import java.util.LinkedList;

public class HashTableBucketImpl<K, V> implements HashTable<K, V> {

    static class Item<K, V> implements Entry<K, V> {
        private final K key;
        private V value;

        public Item(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public void setValue(V value) {
            this.value = value;
        }
    }

    private final ArrayList<LinkedList<Item<K, V>>> data;
    private int size;

    @SuppressWarnings("unchecked")
    public HashTableBucketImpl(int maxSize) {
        this.data = new ArrayList<LinkedList<Item<K, V>>>(maxSize);
        for (int i = 0; i < maxSize; i++) {
            data.add(null);
        }
    }

    private int hashFunc(K key) {
        return key.hashCode() % data.size();
    }

    @Override
    public boolean put(K key, V value) {
        int index = hashFunc(key);
        LinkedList<Item<K, V>> bucket;
        if (data.get(index) != null) {
            bucket = data.get(index);
        } else {
            bucket = new LinkedList<Item<K, V>>();
        }
        for (Item<K, V> item : bucket) {
            if (item.getKey().equals(key)) {
                item.setValue(value);
                return true;
            }
        }
        bucket.add(new Item<K, V>(key, value));
        data.set(index, bucket);
        size++;
        return true;
    }

    @Override
    public V get(K key) {
        int index = hashFunc(key);
        if (data.get(index) == null) return null;
        LinkedList<Item<K, V>> bucket = data.get(index);
        for (Item<K, V> item : bucket) {
            if (item.getKey().equals(key)) {
                return item.getValue();
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        V val = null;
        int index = hashFunc(key);
        if (data.get(index) == null) return null;
        LinkedList<Item<K, V>> bucket = data.get(index);
        for (Item<K, V> item : bucket) {
            if (item.getKey().equals(key)) {
                val = item.getValue();
                bucket.remove(item);
                data.set(index, bucket.size() != 0 ? bucket : null);
            }
        }
        size--;
        return val;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void display() {
        System.out.println("----------");
        for (int i = 0; i < data.size(); i++) {
            System.out.printf("%d = [%s]", i, data.get(i));
            System.out.println();
        }
        System.out.println("----------");
    }
}
