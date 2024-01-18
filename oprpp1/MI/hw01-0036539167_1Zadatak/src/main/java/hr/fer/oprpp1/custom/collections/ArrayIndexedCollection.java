package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;

/**
 * This class is a resizeable array-like collection of objects.
 * It is a concrete implementation of class Collection with storing capabilities
 * and methods to modify its contents.
 */
public class ArrayIndexedCollection extends Collection{
    private int size;
    private Object[] elements;

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
     * Returns the number of elements in this collection.
     * @return the number of elements in this collection.
     */
    int size() {
        return this.size;
    }

    /**
     * Adds the given value to this collection.
     * If the new value exceeds the capacity of this collection, the capacity is doubled.
     * @param value the value to be added.
     * @throws NullPointerException if the value is null.
     */
    void add(Object value){
        if(value == null) throw new NullPointerException("New value can not be null.");
        if(size == elements.length) {
            elements = Arrays.copyOf(elements, 2 * size);

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
    Object get(int index){
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        return elements[index];
    }

    /**
     * Removes all elements from the collection.
     */
    void clear(){
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }

        size = 0;
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
    void insert(Object value, int position){
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
    }

    /**
     * Returns the first occurrence of the specified value in the collection
     * or -1 if there is no such occurrence or if the value is null.
     * @param value the value to search the index in the collection.
     * @return the index of the first occurrence of the specified value.
     */
    int indexOf(Object value){
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
    void remove(int index){
        if(index < 0 || index >= size) throw new IndexOutOfBoundsException();

        for(int i=index; i<(size-1); i++){
            elements[i] = elements[i+1];
        }

        size--;
    }

    /**
     * Removes the first occurrence of the specified element from the collection.
     * @param value the value to be removed.
     * @return true if the element was in the collection and removed successfully, false otherwise.
     */
    boolean remove(Object value){

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
    boolean contains(Object value){
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
    Object[] toArray(){
        return Arrays.copyOf(elements, size);
    }

    /**
     * Performs the Processor.process() on each element of the collection.
     * @param processor the processor to process each element.
     */
    void forEach(Processor processor){
        for(int i=0; i<size;i++){
            processor.process(elements[i]);
        }
    }


}
