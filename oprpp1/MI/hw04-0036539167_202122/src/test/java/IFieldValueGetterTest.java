import hr.fer.oprpp1.hw04.db.FieldValueGetters;
import hr.fer.oprpp1.hw04.db.StudentRecord;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class IFieldValueGetterTest {
    @Test
    public void test1(){
        StudentRecord sr = new StudentRecord("00365", "Pavić", "Pavle", "3");
        assertEquals(sr.getFirstName(), FieldValueGetters.FIRST_NAME.get(sr));
        assertEquals(sr.getLastName(), FieldValueGetters.LAST_NAME.get(sr));
        assertEquals(sr.getJmbag(), FieldValueGetters.JMBAG.get(sr));
    }
}
