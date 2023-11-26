package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * This class is a resizeable array-like collection of objects.
 * It is a concrete implementation of class Collection with storing capabilities
 * and methods to modify its contents.
 */
public class ArrayIndexedCollection implements List{
    private int size;
    private Object[] elements;
    private long modificationCount = 0;

    /**
     *  Constructs a new ArrayIndexedCollection with initial capacity of 16 Objects.
     */
    public ArrayIndexedCollection() {
        elements = new Object[16];
        size = 0;
    }

    /**
     * Constructs a new ArrayIndexedCollection with given initial capacity.
     *
     * @param initialCapacity the initial number of elements in the collection.
     * @throws IllegalArgumentException if the given initial capacity is zero or negative.
     */
    public ArrayIndexedCollection(int initialCapacity) {
        if(initialCapacity < 1) throw new IllegalArgumentException("Size should be 1 or more.");

        elements = new Object[initialCapacity];
        size = 0;
    }

    /**
     * Constructs a new ArrayIndexedCollection with the given initial capacity
     * and copies the elements of given Collection into this collection.
     * If the initial capacity is smaller than the number of elements
     * in the other collection to be copied, new collection is created
     * with the size of the given one.
     * @param other Collection to be copied into this collection.
     * @param initialCapacity the initial number of elements in the collection.
     * @throws NullPointerException if other collection is null.
     */
    public ArrayIndexedCollection(Collection other, int initialCapacity) {
        if(other == null) throw new NullPointerException("Other Collection is null");


        int newsize = Math.max(initialCapacity, other.size());
        elements = new Object[newsize];

        addAll(other);
    }

    /**
     * Constructs a new ArrayIndexedCollection and copies the elements
     * of the given one into it. New collection is the size of the given one.
     * @param other Collection to be copied into constructed one.
     */
    public ArrayIndexedCollection(Collection other) {
        this(other, 16);
    }








    /**
     * Private class, implementation of ElementsGetter interface.
     * It is used to access elements of ArrayIndexedCollection in order
     * from index 0 to last index.
     */
    private static class ElementsGetterArrIndColl implements ElementsGetter{
        int indToGet = 0;
        ArrayIndexedCollection coll;
        private long savedModificationCount;

        /**
         * Constructor. Creates new instance of ElementsGetterArrIndColl
         * with reference to its outer class which stores elements that 'Getter
         * needs to be returning.
         * @param collection collections whose elements are to be returned
         */
        public ElementsGetterArrIndColl(ArrayIndexedCollection collection) {
            coll =  collection;
            savedModificationCount = collection.modificationCount;
        }

        /**
         * Checks if there is next possible element to be returned.
         * @return true if there is element to be returned, false otherwise
         */
        @Override
        public boolean hasNextElement(){
            if(savedModificationCount != coll.modificationCount){
                throw new ConcurrentModificationException();
            }
            return indToGet< coll.size();
        }

        /**
         * Returns the element at the current position of ElementsGetter.
         * If it's called for the first time, returns first element, otherwise,
         * everytime returns next element.
         * @return element next to the last returned element, or first element
         * when called first time
         * @throws NoSuchElementException if there are no elements to be returned
         */
        @Override
        public Object getNextElement(){
            if(savedModificationCount != coll.modificationCount){
                throw new ConcurrentModificationException();
            }
            if(hasNextElement()){
                return coll.elements[indToGet++];
            }else{
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * Creates a new instance of ElementsGetter for ArrayIndexedCollection.
     * @return a new instance of ElementsGetterArrIndColl
     */
    @Override
    public ElementsGetterArrIndColl createElementsGetter(){

        return new ElementsGetterArrIndColl(this);
    }

    /**
     * Returns the number of elements in this collection.
     * @return the number of elements in this collection.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Adds the given value to this collection.
     * If the new value exceeds the capacity of this collection, the capacity is doubled.
     * @param value the value to be added.
     * @throws NullPointerException if the value is null.
     */
    @Override
    public void add(Object value){
        if(value == null) throw new NullPointerException("New value can not be null.");
        if(size == elements.length) {
            elements = Arrays.copyOf(elements, 2 * size);
            modificationCount++;
        }
        elements[size] = value;
        size++;
    }

    /**
     * Returns the element at the specified index in the collection.
     * @param index the index of the element to be returned.
     * @return  the element at the specified index in the collection.
     * @throws IndexOutOfBoundsException if the index is less than zero or
     * bigger than the size of the collection.
     */
    @Override
    public Object get(int index){
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        return elements[index];
    }

    /**
     * Removes all elements from the collection.
     */
    @Override
    public void clear(){
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }

        size = 0;
        modificationCount++;
    }

    /**
     * Inserts the given element into the collection at the specified position.
     * Elements starting from the specified position are shifted one position to the right.
     * @param value the value to insert.
     * @param position the position to insert.
     * @throws NullPointerException if the value is null.
     * @throws IndexOutOfBoundsException if the position is less than zero
     * or bigger than the size of the collection.
     */
    @Override
    public void insert(Object value, int position){
        if(value == null) throw new NullPointerException("New value is null.");
        if(position<0 || position> size) throw new IndexOutOfBoundsException();
        if(size == elements.length) {
            elements = Arrays.copyOf(elements, 2 * size);
        }


        for(int i=size; i>position; i--){
            elements[i] = elements[i-1];
        }

        elements[position] = value;
        size++;
        modificationCount++;
    }

    /**
     * Returns the first occurrence of the specified value in the collection
     * or -1 if there is no such occurrence or if the value is null.
     * @param value the value to search the index in the collection.
     * @return the index of the first occurrence of the specified value.
     */
    @Override
    public int indexOf(Object value){
        if (value == null) return -1;
        for(int i=0; i<size; i++){
            if (elements[i].equals(value)) return i;
        }
        return -1;
    }

    /**
     * Removes the element at the specified index in the collection.
     * Elements starting from the specified index (after the removed one)
     * are shifted one place left.
     * @param index the index of the ellement to be removed.
     * @throws IndexOutOfBoundsException if the index is less than zero
     * or bigger or equal to the size of the collection.
     */
    @Override
    public void remove(int index){
        if(index < 0 || index >= size) throw new IndexOutOfBoundsException();

        for(int i=index; i<(size-1); i++){
            elements[i] = elements[i+1];
        }

        size--;
        modificationCount++;
    }

    /**
     * Removes the first occurrence of the specified element from the collection.
     * @param value the value to be removed.
     * @return true if the element was in the collection and removed successfully, false otherwise.
     */
    @Override
    public boolean remove(Object value){

        if(!this.contains(value)) return false;

        this.remove(this.indexOf(value));
        return true;
    }

    /**
     * Checks if the collection contains the specified element.
     * @param value the value to be checked if it is in collection.
     * @return true if the collection contains the specified element, false
     * if not or if the value is null.
     */
    @Override
    public boolean contains(Object value){
        if (value == null) return false;
        for (int i = 0; i < size; i++) {
            if(elements[i].equals(value)) return true;
        }
        return false;
    }

    /**
     * Creates a new Array with the elements of this collection.
     * @return array of Objects that are in this collection.
     */
    @Override
    public Object[] toArray(){
        return Arrays.copyOf(elements, size);
    }

}
