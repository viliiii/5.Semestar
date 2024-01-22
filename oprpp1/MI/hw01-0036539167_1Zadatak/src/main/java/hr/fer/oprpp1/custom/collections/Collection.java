package hr.fer.oprpp1.custom.collections;


/**
 * Class that represents a collection of Objects.
 * Class provides methods for manipulating with collections
 * such as adding, removing, modifying etc.
 * Concrete collections will inherit from this class and
 * override methods.
 */
public class Collection {

    /**
     * Default constructor.
     * Constructs a new collection instance.
     */
    protected Collection() {
    }

    /**
     * Checks if the collection is empty.
     * @return true if the collection is empty, false otherwise.
     */
    boolean isEmpty(){

        return this.size() == 0;
    }

    /**
     * Returns the size of the collection.
     * @return the size of the collection.
     */
    int size(){
        return 0;
    }

    /**
     * Adds the given value to the collection.
     * @param value the value to be added.
     */
    void add(Object value){

    }

    /**
     * Checks if the collection contains the given value.
     * @param value the value to be checked if it is in collection.
     * @return true if the collection contains the given value, false otherwise.
     */
    boolean contains(Object value){

        return false;
    }

    /**
     * Removes the given value from the collection.
     * @param value the value to be removed.
     * @return true if the value was found and removed, false otherwise.
     */
    boolean remove(Object value){

        return false;
    }

    /**
     * Transforms the collection into an array of objects.
     * @return an array containing all the elements of the collection.
     */
    Object[] toArray(){

        throw new UnsupportedOperationException();
    }

    /**
     * Performs the Processor.process() on each element of the collection.
     * @param processor the processor to process each element.
     */
    void forEach(Processor processor){

    }

    /**
     * Adds all the elements of the given collection to this collection.
     * @param other the Collection to be added to this collection.
     */
    void addAll(Collection other){
        class LocalProcessor extends Processor{
            public void process(Object value){
                add(value);
            }
        }

        LocalProcessor localProcessor = new LocalProcessor();
        other.forEach(localProcessor);
    }

    /**
     * Removes all the elements from the collection.
     */
    void clear(){

    }
}
