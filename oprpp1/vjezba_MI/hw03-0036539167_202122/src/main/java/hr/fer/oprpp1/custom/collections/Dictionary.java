package hr.fer.oprpp1.custom.collections;


/**
 * Dictionary storing Entry<K, V> elements similar as Map.
 * It is actually wrapper for ArrayIndexedCollection which is used
 * for actual storing of Entries.
 * @param <K> key, similar as Map key
 * @param <V> value associated to key
 */
public class Dictionary<K, V> {

    private ArrayIndexedCollection<Entry<K, V>> entries;

    /**
     * Single element of Dictionary. Key can not be null.
     * If the key is tried to be set as null, NullPointerException
     * will be thrown.
     * @param <K> key, similar as Map key
     * @param <V> value associated to key
     */
    private static class Entry<K, V>{
        private K key;
        private V value;

        /**
         * Constructor.
         * @param key key
         * @param value value
         * @throws NullPointerException if key is null
         */
        public Entry(K key, V value) {
            if(key == null){
                throw new NullPointerException("Key can not be null.");
            }
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            if(key == null){
                throw new NullPointerException("Key can not be null.");
            }
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    /**
     * Default constructor.
     */
    public Dictionary(){
        entries = new ArrayIndexedCollection<>();
    }

    /**
     * Checks if the number of entries is equal to zero.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty(){
        return entries.isEmpty();
    }

    /**
     * Returns the size of the Dictionary.
     * @return the number of Entries in Dictionary
     */
    public int size(){
        return entries.size();
    }

    /**
     * Removes all the entries from the Dictionary.
     */
    public void clear(){
        entries.clear();
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return old value or null if there was no such entry
     */
    public V put(K key, V value){
        for (int i=0; i< entries.size(); i++){
            if(entries.get(i).getKey().equals(key)){
                V tmp = entries.get(i).getValue();
                entries.get(i).setValue(value);
                return tmp;
            }
        }
        entries.add(new Entry<>(key, value));
        return null;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or null if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     * null if this map contains no mapping for the key
     * @throws NullPointerException if the specified key is null
     */
    public V get(Object key){
        if(key == null){
            throw new NullPointerException("Key can not be null.");
        }
        for (int i=0; i< entries.size(); i++){
            if(entries.get(i).getKey().equals(key)){
                return entries.get(i).getValue();
            }
        }
        return null;
    }

    /**
     * Removes the Entry with the specified key from the Dictionary.
     * @param key key of the Entry to be removed
     * @return value of the removed Entry
     * @throws NullPointerException if the key is null
     */
    public V remove(K key){
        if(key == null){
            throw new NullPointerException("Key can not be null.");
        }
        for (int i=0; i< entries.size(); i++){
            if(entries.get(i).getKey().equals(key)){
                V tmp = entries.get(i).getValue();
                entries.remove(i);
                return tmp;
            }
        }

        return null;
    }

}
