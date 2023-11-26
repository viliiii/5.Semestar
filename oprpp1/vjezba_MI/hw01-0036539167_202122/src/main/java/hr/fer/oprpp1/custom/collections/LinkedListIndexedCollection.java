package hr.fer.oprpp1.custom.collections;


/**
 * Resizeable, dynamic, double LinkedList implementation. Each Node is represented
 * with the ListNode class and has pointers to previous and next node.
 */
public class LinkedListIndexedCollection extends Collection{

    private int size;
    private ListNode first;
    private ListNode last;

    /**
     * Nested class that represents every node in the list.
     */
    private static class ListNode{

        ListNode previous;
        ListNode next;
        Object value = null;    //Ako je value = null, ListNode je prazan.

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
     * Returns the number of nodes in this list.
     * @return the number of nodes in this list.
     */
    int size(){
        return this.size;
    }

    /**
     * Adds new node with given value to the end of the list.
     * @param value the value to be added.
     * @throws NullPointerException if the value is null.
     */
    void add(Object value){
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
    }

    /**
     * Returns the value of the node at the given index.
     * @param index the index of the node whose value is to be returned.
     * @return the value of the node at the given index.
     * @throws IndexOutOfBoundsException if the index is bigger than or equal to
     * the size of the list or negative.
     */
    Object get(int index){
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
    void clear(){
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
    }

    /**
     * Inserts the specified value into the specified position in the list.
     * @param value the value to be inserted.
     * @param position the position to insert the value.
     * @throws IndexOutOfBoundsException if the specified position is less than zero
     * or bigger than the size of the list.
     * @throws NullPointerException if the specified value is null.
     */
    void insert(Object value, int position){
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


    }

    /**
     * Returns the index of the first occurrence of the specified value in the list.
     * If the value is not found in the list, returns -1.
     * @param value the value to search index for.
     * @return the index of the first occurrence of the specified value or
     * -1 if the value is not found in the list.
     * @throws NullPointerException if the value is null.
     */
    int indexOf(Object value){
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
    void remove(int index){
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
    }

    /**
     * Checks if the specified value is in the list.
     * @param value the value to be checked if it is in collection.
     * @return true if the value is in the list, false otherwise.
     */
    boolean contains(Object value){
        return indexOf(value) != -1;
    }

    Object[] toArray(){

        Object[] arr = new Object[size];

        ListNode tmp = first;

        for (int i = 0; i < size; i++) {
            arr[i] = tmp.value;
            tmp = tmp.next;
        }

        return arr;
    }

    /**
     * Performs the Processor.process() on each value of the node of the list.
     * @param processor the processor to process each element.
     */
    void forEach(Processor processor){
        ListNode tmp = first;

        for (int i = 0; i < size; i++) {
            processor.process(tmp.value);
            tmp = tmp.next;
        }
    }



}
