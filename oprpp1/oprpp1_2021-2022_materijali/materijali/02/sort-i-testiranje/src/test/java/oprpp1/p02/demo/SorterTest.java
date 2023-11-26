package oprpp1.p02.demo;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class SorterTest {

  @Test
  public void testNullArray() {
    assertThrows(NullPointerException.class, () -> Sorter.sortirajDvaElementa(null));
  }

  @Test
  public void testTooBigArray() {
    assertThrows(IllegalArgumentException.class, () -> Sorter.sortirajDvaElementa(new int[]{3, 5, 1}));
  }

  @Test
  public void testTooSmallArray() {
    assertThrows(IllegalArgumentException.class, () -> Sorter.sortirajDvaElementa(new int[] {1}));
  }

  @Test
  public void testAlreadySortedArray() {
    int[] input = {3, 7};
    int[] expected = {3, 7};
    // provjeri element po element jesu li result i expected jednaki
    // ako jesu, test prolazi, inače pada
    //		int[] result = Sorter.sortirajDvaElementa(input);
    //		for(int i = 0; i < input.length; i++) {
    //			if(result[i] != expected[i]) Assertions.fail();
    //		}
    assertArrayEquals(expected, Sorter.sortirajDvaElementa(input));
  }

  @Test
  public void testShouldChangeOrderInArray() {
    int[] input = {7, 3};
    int[] expected = {3, 7};
    assertArrayEquals(expected, Sorter.sortirajDvaElementa(input));
  }
}

