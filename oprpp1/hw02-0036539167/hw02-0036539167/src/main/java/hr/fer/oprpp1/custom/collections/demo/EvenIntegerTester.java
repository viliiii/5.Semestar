package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.Tester;

/**
 * Implementation of Interface Tester used for testing if the
 * given integer is even or not.
 */
public class EvenIntegerTester implements Tester {
    /**
     * Tests if the given parameter is instance of integer and
     * if it's even.
     * @param obj integer to be tested if even
     * @return true if obj is even, false otherwise
     */
    public boolean test(Object obj) {
        if(!(obj instanceof Integer)) return false;
        Integer i = (Integer)obj;
        return i % 2 == 0;
    }
}

