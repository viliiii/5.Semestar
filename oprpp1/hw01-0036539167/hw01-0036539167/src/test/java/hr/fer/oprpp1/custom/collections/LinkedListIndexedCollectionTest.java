package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedListIndexedCollectionTest {

    @Test
    public void defaultConstructorTest() {
        LinkedListIndexedCollection list = new LinkedListIndexedCollection();
        assertEquals(0, list.size());

    }



    @Test
    public void collectionConstructorTest() {
        LinkedListIndexedCollection list = new LinkedListIndexedCollection();
        list.add(0);
        list.add(1);
        list.add(2);

        LinkedListIndexedCollection list2 = new LinkedListIndexedCollection(list);

        assertEquals(0, list2.get(0));
        assertEquals(1, list2.get(1));
        assertEquals(2, list2.get(2));
        assertEquals(3, list2.size());

    }

    @Test
    public void addTest() {
        LinkedListIndexedCollection list = new LinkedListIndexedCollection();
        assertThrows(NullPointerException.class, () -> list.add(null));
        list.add(0);
        list.add(1);
        list.add(2);

        assertEquals(0, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(2, list.get(2));
        assertEquals(3, list.size());
        assertThrows(NullPointerException.class, ()->list.add(null));
    }

    @Test
    public void getTest(){
        LinkedListIndexedCollection list = new LinkedListIndexedCollection();

        list.add(0);
        list.add(1);
        list.add(2);

        assertThrows(IndexOutOfBoundsException.class, ()-> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, ()-> list.get(3));
        assertEquals(2, list.get(2));
    }

    @Test
    public void clearTest(){
        LinkedListIndexedCollection list = new LinkedListIndexedCollection();

        list.add(0);
        list.add(1);
        list.add(2);

        list.clear();
        assertThrows(IndexOutOfBoundsException.class, ()-> list.get(1));
        assertEquals(0, list.size());


    }

    @Test
    public void insertTest(){
        LinkedListIndexedCollection list = new LinkedListIndexedCollection();

        list.add(0);
        list.add(1);
        list.add(3);

        list.insert(2, 2);
        assertEquals(2, list.get(2));

        list.insert(4, 4);
        assertEquals(4, list.get(4));

        list.insert(-1, 0);
        assertEquals(-1, list.get(0));


        assertThrows(IndexOutOfBoundsException.class, ()->list.insert(10, -1));
        assertThrows(IndexOutOfBoundsException.class, ()->list.insert(10, list.size() + 1));

    }

    @Test
    public void indexOfTest(){
        LinkedListIndexedCollection list = new LinkedListIndexedCollection();

        list.add(0);
        list.add(1);
        list.add(2);

        assertEquals(0, list.indexOf(0));
        assertEquals(1, list.indexOf(1));
        assertEquals(2, list.indexOf(2));
        assertEquals(-1, list.indexOf(50));

    }

    @Test
    public void removeTest(){
        LinkedListIndexedCollection list = new LinkedListIndexedCollection();

        list.add(0);
        list.add(1);
        list.add(2);

        list.remove(1);
        assertEquals(2, list.get(1));

        list.remove(0);
        assertEquals(2, list.get(0));

        list.remove(0);
        assertThrows(IndexOutOfBoundsException.class, ()-> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, ()->list.remove(-1));
    }




}
