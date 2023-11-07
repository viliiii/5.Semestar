package hr.fer.oprpp1.custom.collections;


import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Resizeable, dynamic, double LinkedList implementation. Each Node is represented
 * with the ListNode class and has pointers to previous and next node.
 */
public class LinkedListIndexedCollection implements List{

    private int size;
    private ListNode first;
    private ListNode last;

    private long modificationCount = 0;

    /**
     * Nested class that represents every node in the list.
     */
    private static class ListNode{

        ListNode previous;
        ListNode next;
        Object value = null;    //value = null  ==> ListNode is empty.

        /**
         * Default constructor. Constructs a new ListNode.
         */
        public ListNode() {
            previous = null;
            next = null;
        }

        /**
         * Constructs a new ListNode with the pointers to the previous and next
         * ListNode objects and value.
         * @param previous pointer to the previous ListNode object.
         * @param next pointer to the next ListNode object.
         * @param value value of created ListNode.
         */
        public ListNode(ListNode previous, ListNode next, Object value) {
            this.previous = previous;
            this.next = next;
            this.value = value;
        }

        /**
         * Constructs a new ListNode with the given value.
         * @param value value of created ListNode.
         */
        public ListNode(Object value) {
            this(null, null, value);
        }
    }


    /**
     * Default constructor.
     * Constructs an empty list.
     */
    public LinkedListIndexedCollection() {
        first = new ListNode();
        first.next = null;
        first.previous = null;

        last = new ListNode();
        last.next = null;
        last.previous = null;

        size = 0;
    }

    /**
     * Constructs a list containing the elements of the given Collection other.
     * @param other Collection whose elements are to be added into list.
     */
    public LinkedListIndexedCollection(Collection other) {
        first = new ListNode();
        first.next = null;
        first.previous = null;

        last = new ListNode();
        last.next = null;
        last.previous = null;

        size = 0;

        addAll(other);
    }
    /**
     * Private class, implementation of ElementsGetter interface.
     * It is used to access elements of ListIndexedCollection in order
     * from index 0 to last index.
     */
    private static class ElementsGetterListInd implements ElementsGetter{

        ListNode current;
        LinkedListIndexedCollection coll;
        private long savedModificationCount;

        /**
         * Constructor. Creates new instance of ElementsGetterListInd
         * with reference to its outer class which stores elements that 'Getter
         * needs to be returning.
         * @param collection collection whose elements are to be returned
         */
        public ElementsGetterListInd(LinkedListIndexedCollection collection) {
            coll = collection;
            current = collection.first;
            savedModificationCount = collection.modificationCount;
        }

        /**
         * Checks if there is next possible element to be returned.
         * @return true if there is element to be returned, false otherwise
         */
        @Override
        public boolean hasNextElement() {
            if(savedModificationCount != coll.modificationCount){
                throw new ConcurrentModificationException();
            }
            return current != null;
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
        public Object getNextElement() {
            if(savedModificationCount != coll.modificationCount){
                throw new ConcurrentModificationException();
            }
            if(hasNextElement()){
                Object tmp = current.value;
                current = current.next;
                return tmp;
            }else {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * Creates a new instance of ElementsGetter for ListIndexedCollection.
     * @return a new instance of ElementsGetterListInd
     */
    @Override
    public ElementsGetter createElementsGetter() {
        return new ElementsGetterListInd(this);
    }

    /**
     * Returns the number of nodes in this list.
     * @return the number of nodes in this list.
     */
    @Override
    public int size(){
        return this.size;
    }

    /**
     * Adds new node with given value to the end of the list.
     * @param value the value to be added.
     * @throws NullPointerException if the value is null.
     */
    @Override
    public void add(Object value){
        if(value == null){
            throw new NullPointerException("The value can not be null.");
        }

        if(size == 0){
            first.value = value;
            last.value = value;
            size++;
        }else if(size == 1){
            last.value = value;
            first.next = last;
            last.previous = first;
            size++;
        }else{
            ListNode newValue = new ListNode(value);
            last.next = newValue;
            newValue.previous = last;
            last = newValue;
            size++;
        }
        modificationCount++;
    }

    /**
     * Returns the value of the node at the given index.
     * @param index the index of the node whose value is to be returned.
     * @return the value of the node at the given index.
     * @throws IndexOutOfBoundsException if the index is bigger than or equal to
     * the size of the list or negative.
     */
    @Override
    public Object get(int index){
        if(index <0 || index>= size) {
            throw new IndexOutOfBoundsException("Invalid index.");
        }
        ListNode tmp = first;

        for (int i = 0; i < index; i++) {
            tmp = tmp.next;
        }
        return tmp.value;
    }

    /**
     * Removes all nodes from the list.
     */
    @Override
    public void clear(){
        ListNode tmp = first;

        while(tmp != null){
            ListNode tmpNext = tmp.next;
            tmp.previous = null;
            tmp.next = null;
            tmp = tmp.next;
        }
        first = null;
        last = null;
        size = 0;
        modificationCount++;
    }

    /**
     * Inserts the specified value into the specified position in the list.
     * @param value the value to be inserted.
     * @param position the position to insert the value.
     * @throws IndexOutOfBoundsException if the specified position is less than zero
     * or bigger than the size of the list.
     * @throws NullPointerException if the specified value is null.
     */
    @Override
    public void insert(Object value, int position){
        if(position < 0 || position >size){
            throw new IndexOutOfBoundsException();
        }
        if(value == null){
            throw new NullPointerException("The value can not be null.");
        }

        ListNode newValue = new ListNode(value);

        if(position == 0){
            newValue.next = first;
            first.previous = newValue;
            first = newValue;
        }else if(position == size){
            newValue.previous = last;
            last.next = newValue;
            last = newValue;
        }else{
            ListNode tmp = first;
            for(int i=0; i<position; i++){
                tmp = tmp.next;
            }
            newValue.next = tmp;
            tmp.previous.next = newValue;
            tmp.previous = newValue;

        }
        size++;
        modificationCount++;

    }

    /**
     * Returns the index of the first occurrence of the specified value in the list.
     * If the value is not found in the list, returns -1.
     * @param value the value to search index for.
     * @return the index of the first occurrence of the specified value or
     * -1 if the value is not found in the list.
     * @throws NullPointerException if the value is null.
     */
    @Override
    public int indexOf(Object value){
        if(value == null) {
            throw new NullPointerException();
        }
        ListNode tmp = first;

        for(int i=0; i<size; i++){
            if(tmp.value.equals(value)) return i;
            tmp = tmp.next;
        }
        return -1;
    }

    /**
     * Removes the node at the specified index from the list.
     * @param index the index of the node to be removed.
     * @throws IndexOutOfBoundsException if the index is bigger than or equal to
     * the size of the list or negative.
     */
    @Override
    public void remove(int index){
        if(index < 0 || index >=size){
            throw new IndexOutOfBoundsException();
        }

        if(size == 1){
            first = null;
            last = null;
        }else if(index == 0){
            first = first.next;
        }else{
            ListNode tmp = first;
            for(int i=0; i<index; i++){
                tmp = tmp.next;
            }
            tmp.previous.next = tmp.next;
            tmp.next.previous = tmp.previous;
        }

        size--;
        modificationCount++;
    }

    /**
     * Checks if the specified value is in the list.
     * @param value the value to be checked if it is in collection.
     * @return true if the value is in the list, false otherwise.
     */
    @Override
    public boolean contains(Object value){
        return indexOf(value) != -1;
    }

    /**
     * Removes the specified value from the List.
     * @param value the value to be removed.
     * @return true if successfully removed, false otherwise
     */
    @Override
    public boolean remove(Object value) {
        int index = indexOf(value);
        if(index == -1) return false;

        remove(index);
        return true;
    }

    /**
     * Returns new Array consisting all the elements from the List.
     * @return Array consisting all the elements from the List.
     */
    @Override
    public Object[] toArray(){

        Object[] arr = new Object[size];

        ListNode tmp = first;

        for (int i = 0; i < size; i++) {
            arr[i] = tmp.value;
            tmp = tmp.next;
        }

        return arr;
    }


}
