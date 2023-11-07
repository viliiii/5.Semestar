package hr.fer.oprpp1.custom.collections;

import hr.fer.oprpp1.custom.collections.demo.EvenIntegerTester;

import java.util.Arrays;

public class Main {
/*    public static void main(String[] args) {
        ArrayIndexedCollection col = new ArrayIndexedCollection(2);
        col.add(Integer.valueOf(20));
        col.add("New York");
        col.add("San Francisco"); // here the internal array is reallocated to 4
        System.out.println(col.contains("New York")); // writes: true
        col.remove(1); // removes "New York"; shifts "San Francisco" to position 1
        System.out.println(col.get(1)); // writes: "San Francisco"
        System.out.println(col.size()); // writes: 2
        col.add("Los Angeles");
        LinkedListIndexedCollection col2 = new LinkedListIndexedCollection(col);
// This is local class representing a Processor which writes objects to System.out
        class P implements Processor {
            public void process(Object o) {
                System.out.println(o);
            }
        };
        System.out.println("col elements:");
        col.forEach(new P());
        System.out.println("col elements again:");
        System.out.println(Arrays.toString(col.toArray()));
        System.out.println("col2 elements:");
        col2.forEach(new P());
        System.out.println("col2 elements again:");
        System.out.println(Arrays.toString(col2.toArray()));
        System.out.println(col.contains(col2.get(1))); // true
        System.out.println(col2.contains(col.get(1))); // true
        col.remove(Integer.valueOf(20)); // removes 20 from collection (at position 0).

    }*/

    public static void main(String[] args) {
        List col1 = new ArrayIndexedCollection();
        List col2 = new LinkedListIndexedCollection();
        col1.add("Ivana");
        col2.add("Jasna");
        Collection col3 = col1;
        Collection col4 = col2;
        col1.get(0);
        col2.get(0);
        //col3.get(0); // neće se prevesti! Razumijete li zašto?
        //col4.get(0); // neće se prevesti! Razumijete li zašto?
        col1.forEach(System.out::println); // Ivana
        col2.forEach(System.out::println); // Jasna
        col3.forEach(System.out::println); // Ivana
        col4.forEach(System.out::println); // Jasna



    }
}
