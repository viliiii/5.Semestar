import hr.fer.oprpp1.hw04.db.ComparisonOperators;
import hr.fer.oprpp1.hw04.db.IComparisonOperator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComparisonOperatorsTest {
    @Test
    public void testLESS(){
        IComparisonOperator op = ComparisonOperators.LESS;

        assertTrue(op.satisfied("Ana", "Jasna"));
        assertFalse(op.satisfied("Jasna", "Ana"));
    }

    @Test
    public void testLESS_OR_EQUALS(){
        IComparisonOperator op = ComparisonOperators.LESS_OR_EQUALS;

        assertTrue(op.satisfied("Ana", "Jasna"));
        assertTrue(op.satisfied("Jasna", "Jasna"));
    }

    @Test
    public void testGREATER(){
        IComparisonOperator op = ComparisonOperators.GREATER;

        assertFalse(op.satisfied("Ana", "Jasna"));
        assertTrue(op.satisfied("Jasna", "Ana"));
    }

    @Test
    public void testGREATER_OR_EQUALS(){
        IComparisonOperator op = ComparisonOperators.GREATER_OR_EQUALS;

        assertFalse(op.satisfied("Ana", "Jasna"));
        assertTrue(op.satisfied("Jasna", "Jasna"));
    }

    @Test
    public void testEQUALS(){
        IComparisonOperator op = ComparisonOperators.EQUALS;

        assertFalse(op.satisfied("Ana", "Jasna"));
        assertTrue(op.satisfied("Jasna", "Jasna"));
    }

    @Test
    public void testNOT_EQUALS(){
        IComparisonOperator op = ComparisonOperators.NOT_EQUALS;

        assertTrue(op.satisfied("Ana", "Jasna"));
        assertFalse(op.satisfied("Jasna", "Jasna"));
    }

    @Test
    public void testLIKE(){
        IComparisonOperator op = ComparisonOperators.LIKE;

        assertFalse(op.satisfied("Zagreb", "Aba*"));
        assertFalse(op.satisfied("AAA", "AA*AA"));
        assertTrue(op.satisfied("AAAA", "AA*AA"));
        assertTrue(op.satisfied("AAMAA", "AA*AA"));
        assertThrows(IllegalArgumentException.class, ()-> op.satisfied("Ja", "**"));

    }

}
