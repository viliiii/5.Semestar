import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static hr.fer.oprpp1.hw05.crypto.Util.hextobyte;
import static hr.fer.oprpp1.hw05.crypto.Util.bytetohex;

import static org.junit.jupiter.api.Assertions.*;
public class UtilTest {

    @Test
    public void hextobyteTest(){
        assertEquals("[1, -82, 34]", Arrays.toString(hextobyte("01aE22")));
        assertThrows(IllegalArgumentException.class, ()->hextobyte("yy"));
        assertThrows(IllegalArgumentException.class, ()->hextobyte("fff"));

    }

    @Test
    public void bytetohexTest(){
        assertEquals("01ae22", bytetohex(new byte[]{1, -82, 34}));
    }
}
