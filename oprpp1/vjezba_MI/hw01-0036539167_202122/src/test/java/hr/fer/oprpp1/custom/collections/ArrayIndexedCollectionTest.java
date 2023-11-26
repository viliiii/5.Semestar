package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayIndexedCollectionTest {

    @Test
    public void testDefaultConstructor() {
        ArrayIndexedCollection arr = new ArrayIndexedCollection();

        assertEquals(0, arr.size());
    }

    @Test
    public void testCapacityConstructor() {
        ArrayIndexedCollection arr = new ArrayIndexedCollection(25);

        assertEquals(0, arr.size());
    }

    @Test
    public void testOtherCollectionConstructor() {
        ArrayIndexedCollection other = new ArrayIndexedCollection();
        other.add(1);
        other.add(9);

        ArrayIndexedCollection arr = new ArrayIndexedCollection(other, 2);

        assertEquals(1, arr.get(0));
        assertEquals(9, arr.get(1));
        assertEquals(2, arr.size());
    }

    @Test
    public void testDelegatedConstructor() {
        ArrayIndexedCollection other = new ArrayIndexedCollection();
        other.add(1);

        ArrayIndexedCollection arr = new ArrayIndexedCollection(other);

        assertEquals(1, arr.get(0));
        assertEquals(1, arr.size());
    }

    @Test
    public void testAdd() {
        ArrayIndexedCollection arr = new ArrayIndexedCollection(3);

        arr.add(1);
        arr.add(2);
        arr.add(3);

        assertEquals(1, arr.get(0));
        assertEquals(3, arr.get(2));
        assertEquals(3, arr.size());

        arr.add(4);
        assertEquals(4, arr.get(3));
        assertEquals(4, arr.size());

    }

    @Test
    public void testGet() {
        ArrayIndexedCollection arr = new ArrayIndexedCollection();
        arr.add(23);

        assertEquals(23, arr.get(0));

        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(2));
    }

    @Test
    public void testClear() {
        ArrayIndexedCollection arr = new ArrayIndexedCollection();
        arr.add(1);
        arr.add(2);
        arr.add(3);

        arr.clear();

        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(0));
    }

    @Test
    public void testInsert() {
        ArrayIndexedCollection arr = new ArrayIndexedCollection();
        arr.add(0);
        arr.add(2);
        arr.add(3);

        arr.insert(1, 1);


        assertEquals(1, arr.get(1));
        assertThrows(NullPointerException.class, () -> arr.insert(null, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.insert(2, -1));
    }

    @Test
    public void testIndexOf() {
        ArrayIndexedCollection arr = new ArrayIndexedCollection();
        arr.add(0);
        arr.add(2);
        arr.add(3);

        assertEquals(2, arr.indexOf(3));
        assertEquals(0, arr.indexOf(0));
        assertEquals(-1, arr.indexOf(null));
        assertEquals(-1, arr.indexOf(4));
    }

    @Test
    public void testRemove() {
        ArrayIndexedCollection arr = new ArrayIndexedCollection();
        arr.add(0);
        arr.add(2);
        arr.add(3);

        arr.remove(1);

        assertEquals(3, arr.get(1));
        assertEquals(0, arr.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.remove(2));

    }


}
