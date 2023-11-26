package hr.fer.oprpp1.custom.collections;

/**
 * Exception made for the class ObjectStack.
 * This exception is thrown when the pop() or peek() method is called but the stack is empty.
 */
public class EmptyStackException extends RuntimeException{
    /**
     * Default constructor.
     */
    public EmptyStackException(){
        super();
    }

    /**
     * Constructor with message option.
     * @param message the exception message.
     */
    public EmptyStackException(String message){
        super(message);
    }

}
