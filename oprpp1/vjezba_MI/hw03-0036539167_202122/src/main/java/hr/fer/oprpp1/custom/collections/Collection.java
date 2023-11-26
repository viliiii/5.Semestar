package hr.fer.oprpp1.custom.collections;


import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Interface for implementing a collection.
 * Interface provides methods for manipulating with collections
 * such as adding, removing, modifying etc.
 * Concrete collections will implement this interface and
 * override methods.
 */
public interface Collection<T> {


    /**
     * Checks if the collection is empty.
     * @return true if the collection is empty, false otherwise.
     */
    default boolean isEmpty(){

        return this.size() == 0;
    }

    /**
     * Returns the size of the collection.
     * @return the size of the collection.
     */
    int size();

    /**
     * Adds the given value to the collection.
     * @param value the value to be added.
     */
    void add(T value);

    /**
     * Checks if the collection contains the given value.
     * @param value the value to be checked if it is in collection.
     * @return true if the collection contains the given value, false otherwise.
     */
    boolean contains(Object value);

    /**
     * Removes the given value from the collection.
     * @param value the value to be removed.
     * @return true if the value was found and removed, false otherwise.
     */
    boolean remove(Object value);

    /**
     * Transforms the collection into an array of objects.
     * @return an array containing all the elements of the collection.
     */
    Object[] toArray();

    /**
     * Performs the Processor.process() on each element of the collection.
     * @param processor the processor to process each element.
     */
    default void forEach(Processor<? super T> processor){
        ElementsGetter<T> getter = this.createElementsGetter();
        while (getter.hasNextElement()){
            processor.process(getter.getNextElement());
        }
    }

    /**
     * Adds all the elements of the given collection to this collection.
     * @param other the Collection to be added to this collection.
     */
    default void addAll(Collection<? extends T> other){
        class LocalProcessor implements Processor<T>{
            public void process(T value){
                add(value);
            }
        }

        LocalProcessor localProcessor = new LocalProcessor();
        other.forEach(localProcessor);
    }

    /**
     * Removes all the elements from the collection.
     */
    void clear();

    /**
     * Creates a new instance of ElementsGetter for parent Collection.
     * @return a new instance of ElementsGetter(parentCollection)
     */
    ElementsGetter<T> createElementsGetter();

    /**
     * Adds all the objects from Collection col that pass tester.test() to this Collection.
     * @param col Collection which elements are to be tested and added
     * @param tester tester for testing elements from Collection col
     */
    default void addAllSatisfying(Collection<? extends T> col, Tester<? super T> tester){
        ElementsGetter<? extends T> colGetter = col.createElementsGetter();
        while (colGetter.hasNextElement()){
            T tmpElement = colGetter.getNextElement();
            if(tester.test(tmpElement)){
                this.add(tmpElement);
            }
        }
    }

    default <U> void addModified(Collection<? extends U> other, Function<? super U, ? extends T> function){
        other.forEach(new Processor<U>() {
            @Override
            public void process(U value) {
                add(function.apply(value));
            }
        });
    }

    default <U> void copyTransformedIntoIfAllowed(Collection<? super T> other, Function<? super T, ? extends U> function, Predicate<? super U> predicate) {

        this.forEach(new Processor<T>() {
            @Override
            public void process(T value) {
                U transformed = function.apply(value);

                if(predicate.test(transformed)) other.add(value);
            }
        });
    }





}
