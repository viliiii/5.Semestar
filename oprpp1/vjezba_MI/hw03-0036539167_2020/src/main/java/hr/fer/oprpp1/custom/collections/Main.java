package hr.fer.oprpp1.custom.collections;

import java.util.Iterator;

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

    //main za problem1 iz w03
    /*public static void main(String[] args) {
        List<String> col1 = new ArrayIndexedCollection<>();
        List<String> col2 = new LinkedListIndexedCollection<>();
        col1.add("Ivana");
        col2.add("Jasna");
        Collection<String> col3 = col1;
        Collection<String> col4 = col2;
        col1.get(0);
        col2.get(0);
        //col3.get(0); // neće se prevesti! Razumijete li zašto?
        //col4.get(0); // neće se prevesti! Razumijete li zašto?
        col1.forEach(System.out::println); // Ivana
        col2.forEach(System.out::println); // Jasna
        col3.forEach(System.out::println); // Ivana
        col4.forEach(System.out::println); // Jasna

        LinkedListIndexedCollection<String>  list = new LinkedListIndexedCollection<>(col1);
        list.forEach(System.out::println);

        ObjectStack<Integer> obS = new ObjectStack<>();
        obS.push(1);
        obS.push(2);
        System.out.println(obS.pop());
        System.out.println(obS.pop());


    }*/

    //main za problem3a iz hw03
    /*public static void main(String[] args) {
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
// fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana
// query collection:
        Integer kristinaGrade = examMarks.get("Kristina");
        System.out.println("Kristina's exam grade is: " + kristinaGrade); // writes: 5
// What is collection's size? Must be four!
        System.out.println("Number of stored pairs: " + examMarks.size()); // writes: 4

        for(SimpleHashtable.TableEntry<String,Integer> pair : examMarks) {
            System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
        }

        System.out.println("------------------------------------");

        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
        while(iter.hasNext()) {
            SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
            System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
            iter.remove();
        }
        System.out.printf("Veličina: %d%n", examMarks.size());

    }*/

    public static void main(String[] args) {
        Collection<Integer> c1 = new ArrayIndexedCollection<>();
        Collection<Integer> c2 = new ArrayIndexedCollection<>();
        /*c2.add("marko");
        c2.add("nikola");

        c1.addModified(c2, String::toUpperCase);



        c1.forEach(System.out::println);*/

        for (int i = 0; i <15; i++) {
            c1.add(i);
        }

        c1.transferInto(c2, (v)-> v>7);

        c1.forEach(System.out::println);
        System.out.println("********************************");
        c2.forEach(System.out::println);

    }
}
