package hr.fer.oprpp1.custom.collections;

import java.util.NoSuchElementException;

/**
 * Interface for ElementGetters, the classes that are used for
 * accessing the elements of parent collection one by one.
 */
public interface ElementsGetter<T> {
    /**
     * Checks if there is next possible element to be returned.
     * @return true if there is element to be returned, false otherwise
     */
    boolean hasNextElement();

    /**
     * Returns the element at the current position of ElementsGetter.
     * If it's called for the first time, returns first element, otherwise,
     * everytime returns next element.
     * @return element next to the last returned element, or first element
     * when called first time
     * @throws NoSuchElementException if there are no elements to be returned
     */
    T getNextElement();

    /**
     * Does processor.process() on every element of collection that haven't been returned
     * with getNextElement().
     * @param p processor to process the elements.
     */
    default void processRemaining(Processor p){
        while (hasNextElement()){
            p.process(getNextElement());
        }
    }

}
