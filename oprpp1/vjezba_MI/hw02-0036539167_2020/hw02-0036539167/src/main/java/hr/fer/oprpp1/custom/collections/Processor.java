package hr.fer.oprpp1.custom.collections;

/**
 * The Processor Interface with method process().
 * Each concrete Processor will be defined as a new
 * class which implements interface Processor and overrides
 * method process().
 */
public interface Processor {

    /**
     * Performs the processing on the given object.
     *
     * @param value the value to be processed.
     */
    void process(Object value);
}
