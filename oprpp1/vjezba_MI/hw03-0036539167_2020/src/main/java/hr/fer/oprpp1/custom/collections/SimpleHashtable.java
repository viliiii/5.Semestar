package hr.fer.oprpp1.custom.collections;



import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class representing hashing table.
 * It is a collection of TableEntry<K, V> elements stored in
 * an array of references to "heads" of lists. In other words,
 * every index of the array table is a slot for potential list of TableEntry
 * elements (table[x] is null or head of the list).
 * Which slots are to be filled is ordered with Object.hashCode().
 * @param <K> keys
 * @param <V> values
 */
public class SimpleHashtable<K, V>
    implements Iterable<SimpleHashtable.TableEntry<K, V>>{

    private TableEntry<K, V>[] table;
    private int size;
    private int modificationCount;

    private boolean reallocating = false;

    /**
     * Each of entries described in SimpleHashTable javadoc.
     * It has reference to next TableEntry if there is one.
     * @param <K> key
     * @param <V> value
     */
    public static class TableEntry<K,V>{
        private K key;
        private V value;
        TableEntry<K, V> next;

        /**
         * Constructor.
         * @param key key
         * @param value value
         * @throws NullPointerException if key is null
         */
        public TableEntry(K key, V value) {
            if(key == null){
                throw new NullPointerException("Key can not be null.");
            }
            this.key = key;
            this.value = value;
            next = null;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    /**
     * Default constructor.
     */
    @SuppressWarnings("unchecked")
    public SimpleHashtable(){
        table = (TableEntry<K, V>[]) new TableEntry[16];
        size = 0;
        modificationCount = 0;
    }

    /**
     * Constructor with initial capacity.
     * Initial capacity will be first bigger power of number 2
     * than the given initialCapacity int.
     * @param initialCapacity initialCapacity (not always the
     *                        capacity that will be used, as described)
     */
    @SuppressWarnings("unchecked")
    public SimpleHashtable(int initialCapacity){
        if(initialCapacity<1) throw new IllegalArgumentException("Capacity must be at least 1.");
        int tableSize = 1;
        while (tableSize < initialCapacity){
            tableSize*=2;
        }
        table = (TableEntry<K, V>[]) new TableEntry[tableSize];
        size = 0;
    }

    /**
     * Puts the specified TableEntry<K, V> pair into the hash table.
     * If the key already exists, updates the corresponding value.
     * If the key does not exist, adds a new entry.
     * For choosing a slot, |key.hashCode()| % (table.length)   is used.
     *
     * @param key   the key
     * @param value the value
     * @return the previous value associated with the key, or null if the key did not exist
     * @throws NullPointerException if the key is null
     */
    public V put(K key, V value){
        if(key == null) throw new NullPointerException("Key can not be null.");

        tryReallocate();
        int slot = Math.abs(key.hashCode()) % table.length;
        TableEntry<K, V> tmp = table[slot];
        TableEntry<K, V> tmpNew = new TableEntry<>(key, value);
        if(tmp == null) {
            table[slot] = tmpNew;
            if(!reallocating){ this.size++;this.modificationCount++;}
            return null;
        }
        if(tmp.next == null){
            if(tmp.getKey().equals(key)){
                V tmpValue = tmp.getValue();
                tmp.setValue(value);
                return tmpValue;
            }
            tmp.next = tmpNew;
            if(!reallocating) { this.size++;this.modificationCount++;}
            return null;
        }
        while (tmp.next != null){
            if(tmp.getKey().equals(key)){
                V tmpValue = tmp.getValue();
                tmp.setValue(value);
                return tmpValue;
            }
            tmp = tmp.next;
        }
        tmp.next = tmpNew;

        if(!reallocating) { this.size++;this.modificationCount++;}
        return null;
    }

    /**
     * Private helper method for doubling the length of the table
     * if it is overfilled by some standards. For optimisation.
     */
    @SuppressWarnings("unchecked")
    private void tryReallocate(){
        if((double)size/table.length >= 0.75){
            TableEntry<K,V>[] tmpArr = this.toArray();
            int newLength = table.length*2;
            this.table = (TableEntry<K, V>[]) new TableEntry[newLength];

            for(TableEntry<K,V> entry:tmpArr){
                reallocating = true;
                this.put(entry.getKey(), entry.getValue());
                reallocating = false;
            }
        }
    }

    /**
     * Gets the value associated with the specified key in the hash table.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped,
     * or null if this map contains no mapping for the key
     */
    public V get(Object key){
        int slot = Math.abs(key.hashCode()) % table.length;
        TableEntry<K, V> tmp = table[slot];

        while (tmp != null){
            if(tmp.getKey().equals(key)){
                return tmp.getValue();
            }
            tmp = tmp.next;
        }

        return null;
    }

    /**
     * Returns the size of the table.
     * @return the number of different keys in the table
     */
    public int size(){
        return this.size;
    }

    /**
     * Checks whether the hash table contains the specified key.
     *
     * @param key the key to search for
     * @return true if the hash table contains the specified key, false otherwise
     */
    public boolean containsKey(Object key){
        if(key == null) return false;
        int slot = Math.abs(key.hashCode()) % table.length;
        TableEntry<K, V> tmp = table[slot];

        while (tmp != null){
            if(tmp.getKey().equals(key)){
                return true;
            }
            tmp = tmp.next;
        }
        return false;
    }

    /**
     * Checks whether the hash table contains the specified value.
     *
     * @param value the value to search for
     * @return true if the hash table contains the specified value, false otherwise
     */
    public boolean containsValue(Object value){
        for(TableEntry<K,V> entry: table){
            for(;entry != null; entry= entry.next){
                if(entry.getValue().equals(value)) return true;
            }
        }
        return false;
    }

    /**
     * Removes the key-value pair with the specified key from the hash table.
     *
     * @param key the key whose mapping is to be removed from the hash table
     * @return the previous value associated with the key, or null if the key did not exist
     */
    public V remove(Object key){
        if(key == null || !containsKey(key)) return null;

        int slot = Math.abs(key.hashCode()) % table.length;
        TableEntry<K, V> tmp = table[slot];
        TableEntry<K, V> tmpNext = tmp.next;

        if(tmp.getKey().equals(key)){
            table[slot] = tmpNext;
            this.size--;
            this.modificationCount++;
        }

        while (tmpNext != null){
            if(tmpNext.getKey().equals(key)){
                tmp.next = tmpNext.next;
                V tmpValue = tmpNext.getValue();
                tmpNext = null;
                this.size--;
                this.modificationCount++;
                return tmpValue;
            }
            tmpNext = tmpNext.next;
            tmp = tmp.next;

        }
        return null;
    }

    /**
     * Checks whether the hash table is empty.
     *
     * @return true if the hash table contains no key-value pairs, false otherwise
     */
    public boolean isEmpty(){
        return size == 0;
    }

    /**
     * Returns a string representation of the hash table.
     * [key1=value1, key2=value2, key3=value3]  and so on.
     *
     * @return a string representation of the hash table
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(TableEntry<K,V> entry: table){
            for(;entry != null; entry= entry.next){
                sb.append(entry.toString()).append(", ");
            }
        }
        sb.deleteCharAt(sb.toString().lastIndexOf(","));
        sb.deleteCharAt(sb.toString().lastIndexOf(" "));
        sb.append("]");
        return sb.toString();
    }

    /**
     * Returns a new TableEntry<K,V>[] array with all the elements
     * stored in table without changing table. Order is from index 0 to last and
     * from head to last list element.
     * @return a new TableEntry<K,V>[] array
     */
    @SuppressWarnings("unchecked")
    public TableEntry<K,V>[] toArray(){
        TableEntry<K,V>[] arr = (TableEntry<K, V>[]) new TableEntry[this.size];
        int i=0;
        for(TableEntry<K,V> entry: table){
            for(;entry != null; entry= entry.next){
                arr[i++] = entry;
            }
        }
        return arr;
    }

    /**
     * Clears all key-value pairs from the hash table.
     * The table size is set to 0.
     */
    public void clear(){
        for(TableEntry<K,V> entry: table){
            entry = null;
        }
        size = 0;
    }

    /**
     * Implementation of Iterator interface for this class.
     */
    private class SimpleHashTableIterator
        implements Iterator<TableEntry<K, V>>{
        TableEntry<K, V> next;        // next entry to return
        TableEntry<K,V> current;     // current entry
        int expectedModCount;
        int index;             // current slot

        SimpleHashTableIterator(){
            current = null;
            next = null;
            index = 0;
            expectedModCount = modificationCount;
            for(;index< table.length; index++){
                if(table[index]!=null){
                    next=table[index];
                    break;
                }
            }
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         * @throws ConcurrentModificationException if something was changed in
         * the structure of the table during the iteration process
         */
        @Override
        public boolean hasNext() {
            if(modificationCount!=expectedModCount){
                throw new ConcurrentModificationException();
            }
            return next != null;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         * @throws ConcurrentModificationException if something was changed in
         * the structure of the table during the iteration process
         */
        @Override
        public TableEntry<K, V> next() {
            if(modificationCount!=expectedModCount){
                throw new ConcurrentModificationException();
            }
            if(next == null){
                throw new NoSuchElementException("No more elements.");
            }
            TableEntry<K, V> tmp = next;
            current = tmp;

            if(next.next != null){
                next = next.next;
            }else{
                next = null;
                if(index + 1 < table.length){
                    for(;++index< table.length; index++) {
                        if (table[index] != null) {
                            next = table[index];
                            break;
                        }
                    }
                }else {
                    next = null;
                }
            }
            return tmp;
        }

        /**
         * Removes from the underlying collection the last element returned
         * by this iterator (optional operation).  This method can be called
         * only once per call to {@link #next}.
         * <p>
         * The behavior of an iterator is unspecified if the underlying collection
         * is modified while the iteration is in progress in any way other than by
         * calling this method, unless an overriding class has specified a
         * concurrent modification policy.
         * <p>
         * The behavior of an iterator is unspecified if this method is called
         * after a call to the {@link #forEachRemaining forEachRemaining} method.
         *
         * @throws ConcurrentModificationException if something was changed in
         * the structure of the table during the iteration process
         * @throws UnsupportedOperationException if the {@code remove}
         *                                       operation is not supported by this iterator
         * @throws IllegalStateException         if the {@code next} method has not
         *                                       yet been called, or the {@code remove} method has already
         *                                       been called after the last call to the {@code next}
         *                                       method
         * @implSpec The default implementation throws an instance of
         * {@link UnsupportedOperationException} and performs no other action.
         */
        @Override
        public void remove() {
            if(modificationCount!=expectedModCount){
                throw new ConcurrentModificationException();
            }
            if(current==null){
                throw new IllegalStateException("Remove is called before first call of method next() or two times in a row.");
            }

            /*if(next==null){
                throw new NoSuchElementException("No element to be removed.");
            }*/
            SimpleHashtable.this.remove(current.getKey());
            current = null;
            expectedModCount = modificationCount;
        }
    }

    /**
     * Returns an iterator over elements of this HashingTable.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<SimpleHashtable.TableEntry<K, V>> iterator() {
        return new SimpleHashTableIterator();
    }
}
