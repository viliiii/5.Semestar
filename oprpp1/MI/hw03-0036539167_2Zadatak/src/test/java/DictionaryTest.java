import static org.junit.jupiter.api.Assertions.*;


import hr.fer.oprpp1.custom.collections.Dictionary;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
public class DictionaryTest {

    @Test
    public void testAddGet(){
        Dictionary<Integer, String> dict = new Dictionary<>();

        dict.put(1, "Barbara");
        dict.put(2, "Vilim");

        assertEquals(dict.get(1), "Barbara");
        assertEquals(dict.get(2), "Vilim");

    }

    @Test
    public void testSizeAndRemove(){
        Dictionary<Integer, String> dict = new Dictionary<>();

        dict.put(1, "Barbara");
        dict.put(2, "Vilim");

        dict.remove(2);
        assertEquals(1, dict.size());
        dict.remove(1);
        assertEquals(0, dict.size());

    }


    @Test
    public void testException(){
        Dictionary<Integer, String> dict = new Dictionary<>();

        dict.put(1, "Barbara");
        dict.put(2, "Vilim");

        assertThrows(NullPointerException.class, () -> dict.put(null, "Jakov"));
        assertThrows(NullPointerException.class, () -> dict.remove(null));
        assertThrows(NullPointerException.class, () -> dict.get(null));

    }
}
