package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class StudentDB {


    /**
     * Formatter for List of students.
     * @param records List of StudentRecords.
     * @return List<String> made of students.
     */
    public static List<String> format(List<StudentRecord> records){

        if(records.isEmpty()){
            return new LinkedList<>();
        }

        int longestLastname = records.stream()
                .mapToInt(record -> record.getLastName().length())
                .max()
                .orElse(0);

        int longestFirstname = records.stream()
                .mapToInt(record -> record.getFirstName().length())
                .max()
                .orElse(0);

        LinkedList<String> rows = new LinkedList<>();

        StringBuilder sbFirstLast = new StringBuilder();
        sbFirstLast.append("+");
        sbFirstLast.append("=".repeat(12));
        sbFirstLast.append("+");
        sbFirstLast.append("=".repeat(longestLastname+2));
        sbFirstLast.append("+");
        sbFirstLast.append("=".repeat(longestFirstname+2));
        sbFirstLast.append("+");
        sbFirstLast.append("=".repeat(3));
        sbFirstLast.append("+");

        rows.add(sbFirstLast.toString());

        StringBuilder sbStudent = new StringBuilder();
        for(StudentRecord r: records){
            sbStudent.append("| ");
            sbStudent.append(r.getJmbag());
            sbStudent.append(" | ");

            sbStudent.append(r.getLastName());
            sbStudent.append(" ".repeat(1 + longestLastname-r.getLastName().length()));
            sbStudent.append("| ");

            sbStudent.append(r.getFirstName());
            sbStudent.append(" ".repeat(1 + longestFirstname-r.getFirstName().length()));
            sbStudent.append("| ");
            sbStudent.append(r.getFinalGrade());
            sbStudent.append(" |");


            rows.add(sbStudent.toString());
            sbStudent.setLength(0);
        }

        rows.add(sbFirstLast.toString());
        return rows;
    }


    /**
     * Actual query App. When Run, it allows user to make queries
     * until the user writes "exit" and presses enter or makes an invalid
     * query, then the exception is thrown.
     */
    public static void main(String[] args) {
        List<String> lines;
        try {
            lines = Files.readAllLines(
                    Paths.get("src/main/java/hr/fer/oprpp1/hw04/db/database.txt"),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StudentDatabase db = new StudentDatabase(lines);
        Scanner scanner = new Scanner(System.in);
        String query;
        System.out.print("> ");
        while (!Objects.equals(query = scanner.nextLine(), "exit")){
            QueryParser parser = new QueryParser(query);

            List<StudentRecord> selectedRecords = new LinkedList<>();
            if(parser.isDirectQuery()) {
                if(db.forJMBAG(parser.getQueriedJMBAG()) != null){
                        selectedRecords.add(db.forJMBAG(parser.getQueriedJMBAG()));
                }
            } else {
                selectedRecords = db.filter(new QueryFilter((LinkedList<ConditionalExpression>) parser.getQuery()));
            }

            List<String> output = format(selectedRecords);
            output.forEach(System.out::println);
            System.out.println("Records selected: " + (output.size()-2 == -2 ? 0: output.size()-2));
            System.out.print("> ");
        }

        System.out.println("Goodbye!");


    }
}
