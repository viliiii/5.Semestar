package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        Collection<String> prva = new ArrayIndexedCollection<>();
        Collection<String> druga = new ArrayIndexedCollection<>();
        prva.add("Ivo");
        prva.add("Ivka");
        prva.copyTransformedIntoIfAllowed(druga, String::length, n-> n.intValue() %2==0);

        prva.forEach(System.out::println);
        System.out.println("--------------------------------");
        druga.forEach(System.out::println);
    }

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
/*    public static void main(String[] args) {
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
}
