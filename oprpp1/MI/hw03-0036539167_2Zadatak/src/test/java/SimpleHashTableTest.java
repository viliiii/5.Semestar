import static org.junit.jupiter.api.Assertions.*;


import hr.fer.oprpp1.custom.collections.SimpleHashtable;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleHashTableTest {
    @Test
    public void testPutandGet(){
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5);

        assertEquals(5, examMarks.get("Kristina"));
        assertEquals(5, examMarks.get("Ivana"));
        assertEquals(2, examMarks.get("Ante"));
    }

    @Test
    public void testSize(){
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5);

        assertEquals(4, examMarks.size());
    }

    /**
     * This test could possibly fail on different versions of Java
     * because of hashCode() method. This was tested on Java 17.
     */
    @Test
    public void testString(){
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5);

        assertEquals("[Ante=2, Ivana=5, Jasna=2, Kristina=5]", examMarks.toString());
    }

    @Test
    public void testNullPointerException(){
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5);

        assertThrows(NullPointerException.class, ()->examMarks.put(null, 2));
    }

    @Test
    public void testClear(){
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5);

        examMarks.clear();

        assertEquals(0, examMarks.size());
    }

    @Test
    public void testIterator(){
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);

        examMarks.put("Ivana", 5);

        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
        while(iter.hasNext()) {
            SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
            if(pair.getKey().equals("Ivana")) {
                iter.remove();
            }
        }

        assertEquals(3, examMarks.size());
        assertFalse(iter.hasNext());

        assertThrows(NoSuchElementException.class, ()->iter.next());
    }


    @Test
    public void testIteratorIllegalState(){
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);

        examMarks.put("Ivana", 5);

        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
        while(iter.hasNext()) {
            SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
            if(pair.getKey().equals("Ivana")) {
                iter.remove();
                assertThrows(IllegalStateException.class, iter::remove);
            }
        }
    }


    @Test
    public void testIteratorConcurrentMod(){
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);

        examMarks.put("Ivana", 5);

        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
        SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
        examMarks.remove("Ivana");
        assertThrows(ConcurrentModificationException.class,iter::next);
    }


}
