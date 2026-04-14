/**
 * Gabriel Cardenas
 * Course: ADV Java
 * Date: 04/10/2026
 *
 * This is an example implementation of a HashMap, which is a data structure that stores data in key value pairs
 * mapping items through a hash function.
 */
import java.util.Iterator;
import java.util.LinkedList;

/** 
 * Represents a generic hashmap implementation that allows for collisions through chaining (linked lists.)
 * Supports operations such as put, delete, and get.
 */
public class MyHashMap<K, V> {

    // ── Inner entry class ─────────────────────────────────────────────────
    /**
    * Defines the entry class which stores key value pairs.
    */
    static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key   = key;
            this.value = value;
        }
    }

    // ── Fields ────────────────────────────────────────────────────────────
    private LinkedList<Entry<K, V>>[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 11;

    // ── Constructor ───────────────────────────────────────────────────────\
    /**
     * Creates a generic hashmap, (Default size of 11) using buckets as linked list to avoid collisions.
     */
    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new LinkedList[DEFAULT_CAPACITY];
        size  = 0;
    }

    // ── Hash helper ───────────────────────────────────────────────────────
    /**
     * Returns hashcode key.
     * 
     * @param key the key to hash
     * @return the hashcode index for the table
     */
    private int hash(K key) {
        // Keys must not be null
        return Math.abs(key.hashCode()) % table.length;
    }

    // ── put ───────────────────────────────────────────────────────────────

    // Put method compares keys with .equals() instead of == because == compares objects in memory location, whereas
    // .equals() compares the actual object data. For example, for a string, the string values are compared, rather than
    // memory location.

    // Remove additionally uses Iterator instead of a for-each loop because modifying a linked-list inside of a for-each
    // loop causes a concurrent modification error, which is an issue fixed by the functionality of an iterator object.

    /**
     * Puts a value in a bucket (with collision functionality) based off of a key and value.
     * Replaces old value if existing.
     * 
     * @param key the key to insert or update
     * @param value the value to associate with the key
     * @return The previous value that was associated with the key, or null if no value exists.
     */
    public V put(K key, V value) {
        if (key == null) {
            return null;
        }

        int index = hash(key);
        if( table[index] == null) {
            table[index] = new LinkedList<Entry<K,V>>();
        }
        
        for (Entry<K,V> entry : table[index]) {
            if (entry.key.equals(key)) {
                V oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
        }
        size++;
        table[index].addFirst(new Entry<>(key, value));
        return null;
    }

    // ── get ───────────────────────────────────────────────────────────────
    /**
     * Gets a value in a bucket based off of a provided key.
     * 
     * @param key the key you are looking for
     * @return The value that is associated with the key, or null if no value exists.
     */
    public V get(K key) {
        if (key == null) {
            return null;
        }
        int index = hash(key);
        if (table[index] == null) {
            return null;
        }
        //   -- if an entry with a matching key is found, return its value
        for (Entry<K,V> entry : table[index]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null; 
    }
    // ── containsKey ───────────────────────────────────────────────────────/
    /**
     * Checks if a value is in a bucket based off of a provided key
     * 
     * @param key the key you are checking if it exists
     * @return true if the key has an associated value, false if not.
     * 
     */
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }
        int index = hash(key);
        for (Entry<K,V> entry : table[index]) {
            if (entry.key.equals(key)) {
                return true;
            }
        }

        return false;
    }

    // ── remove ────────────────────────────────────────────────────────────
    /**
     * Removes a value in a bucket based off of a key.
     * Returns null if no such key exists.
     * 
     * @param key the key to remove.
     * @return The previous value that was associated with the key, or null if no value exists.
     */
    public V remove(K key) {
        if (key == null) {
            return null;
        }
        int index = hash(key);
        if (table[index] == null) {
            return null;
        }
        Iterator<Entry<K,V>> it = table[index].iterator();
        while (it.hasNext()) {
            Entry<K,V> entry = it.next();
            if (entry.key.equals(key)) {
                V oldValue = entry.value;
                size--;
                it.remove();
                return oldValue;
            }
        }
        return null; 
    }

    // ── size ──────────────────────────────────────────────────────────────
    /** 
     * Returns the size of the hashmap
     * @return the size of the hashmap
     */
    public int size() {
        return size;
    }

    // ── isEmpty ───────────────────────────────────────────────────────────
    /** 
     * Returns true or false depending on the emptiness of the map
     * @return true if hashmap is empty, false if not.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    // ── Test driver ───────────────────────────────────────────────────────
    /** 
     * Tests the functionality of the hashmap using storage retrieval and update functions.
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        // put -- basic insertions
        map.put("cat", 1);
        map.put("dog", 2);
        map.put("rat", 3);
        map.put("bat", 4);
        map.put("ant", 5);
        System.out.println("Size after 5 insertions: " + map.size());   // 5

        // get -- existing keys
        System.out.println("get(cat): " + map.get("cat"));              // 1
        System.out.println("get(bat): " + map.get("bat"));              // 4

        // get -- missing key
        System.out.println("get(owl): " + map.get("owl"));              // null

        // put -- duplicate key (update)
        map.put("cat", 99);
        System.out.println("get(cat) after update: " + map.get("cat")); // 99
        System.out.println("Size after update: " + map.size());         // 5

        // containsKey
        System.out.println("containsKey(dog): " + map.containsKey("dog")); // true
        System.out.println("containsKey(elk): " + map.containsKey("elk")); // false

        // remove -- existing key
        map.remove("dog");
        System.out.println("get(dog) after remove: " + map.get("dog")); // null
        System.out.println("Size after remove: " + map.size());         // 4

        // remove -- missing key
        map.remove("owl");
        System.out.println("Size after removing missing key: " + map.size()); // 4

        // null value -- a key can legitimately map to null
        map.put("fox", null);
        System.out.println("get(fox): " + map.get("fox"));              // null
        System.out.println("containsKey(fox): " + map.containsKey("fox")); // true
        System.out.println("Size with null value: " + map.size());      // 5

        // forced collision -- "AaAaAa" and "BBBaaa" have the same hashCode in Java
        MyHashMap<String, Integer> collisionMap = new MyHashMap<>();
        collisionMap.put("AaAaAa", 1);
        collisionMap.put("BBBaaa", 2);
        System.out.println("Collision get(AaAaAa): " + collisionMap.get("AaAaAa")); // 1
        System.out.println("Collision get(BBBaaa): " + collisionMap.get("BBBaaa")); // 2
        collisionMap.remove("AaAaAa");
        System.out.println("After remove, get(BBBaaa): " + collisionMap.get("BBBaaa")); // 2
        System.out.println("After remove, size: " + collisionMap.size()); // 1
    }
}
