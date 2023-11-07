import hr.fer.oprpp1.hw04.db.StudentDatabase;
import hr.fer.oprpp1.hw04.db.StudentRecord;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class StudentDatabaseTest {

    @Test
    public void forJmbagTest(){
        List<String> lines;
        try {
            lines = Files.readAllLines(
                    Paths.get("src/test/java/database.txt"),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StudentDatabase sdb = new StudentDatabase(lines);

        assertEquals("Hibner", sdb.forJMBAG("0000000020").getLastName());
    }

    @Test
    public void filterTest(){
        List<String> lines;
        try {
            lines = Files.readAllLines(
                    Paths.get("src/test/java/database.txt"),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StudentDatabase sdb = new StudentDatabase(lines);

        List<StudentRecord> all = sdb.filter((s) -> true);
        assertEquals(all.size(), sdb.size());

        List<StudentRecord> none = sdb.filter((s) -> false);
        assertEquals(0, none.size());

    }
}
