package hr.fer.oprpp1.custom.collections;

/**
 * Implementation of the dynamic Stack that stores Object instances
 * and methods for manipulating with its contents.
 */
public class ObjectStack {
    private ArrayIndexedCollection stack;

    /**
     * Default constructor. Constructs a new instance of the ObjectStack.
     */
    public ObjectStack() {
        stack = new ArrayIndexedCollection();
    }

    /**
     * Checks if the stack contains any elements.
     * @return true if the stack contains any elements, false otherwise.
     */
    public boolean isEmpty(){
        return stack.isEmpty();
    }
    /**
     * Returns the number of elements in the stack.
     * @return the number of elements in the stack.
     */
    public int size(){
        return stack.size();
    }

    /**
     * Adds an element to the top of the stack.
     * @param value the value to be added.
     */
    public void push(Object value){
        stack.add(value);
    }


    /**
     * Removes the element from the top of the stack and returns it.
     * @return value from the top of the stack.
     * @throws EmptyStackException if there are no elements in the stack.
     */
    public Object pop(){
       if(stack.isEmpty()){
           throw new EmptyStackException("You can not pop from an empty stack.");
       }
       Object popped =  stack.get(stack.size()-1);
       stack.remove(stack.size()-1);
       return popped;
    }

    /**
     * Returns the element at the top of the stack.
     * @return the element at the top of the stack.
     * @throws EmptyStackException if the stack is empty.
     */
    public Object peek(){
        if(stack.isEmpty()){
            throw new EmptyStackException("You can not pop from an empty stack.");
        }
        return stack.get(stack.size()-1);
    }

    /**
     * Returns all the elements from the stack.
     */
    public void clear(){
        stack.clear();
    }



}
