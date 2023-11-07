package hr.fer.oprpp1.custom.collections;

/**
 * Interface to be implemented by some Tester class
 * and defined what should that class test.
 */
public interface Tester<T> {
    /**
     * Method for testing if some Object satisfies some conditions.
     * @param obj to be tested.
     * @return true if obj passes the test, false otherwise
     */
    boolean test(T obj);
}
