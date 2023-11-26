package hr.fer.oprpp1.hw04.db;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a database of student records.
 * Each student record contains JMBAG, last name,
 * first name, and final grade. It saves StudentRecords
 * in private list which is filled using List of rows of the
 * .txt file given through the constructor.
 */
public class StudentDatabase {
    private List<StudentRecord> students;
    private HashMap<String, StudentRecord> index;

    /**
     * Constructs a StudentDatabase object from a
     * list of string rows representing student records in .txt file.
     * Fills the private List<StudentRecord> students and private HasMap
     * for fast retrieval using only jmbag.
     *
     * @param rows List of strings representing student records in the format:
     *            JMBAG LastName FirstName Grade
     */
    public StudentDatabase(List<String> rows) {
        students = new LinkedList<>();
        index = new HashMap<>();
        for(String row: rows){
            String[] split = row.split("\\s+");
            String jmbag = split[0];
            String lastName = (split.length == 5 ? split[1]+" "+split[2] : split[1]);
            String firstName = (split.length == 5 ? split[3] : split[2]);
            String finalGrade = (split.length == 5 ? split[4] : split[3]);
            if(Integer.parseInt(finalGrade) < 1 || Integer.parseInt(finalGrade) > 5){
                System.out.println("Invalid provided database file: some grade is invalid.");
                System.exit(-1);
            }
            StudentRecord student = new StudentRecord(jmbag, lastName, firstName, finalGrade);
            if(students.contains(student)){
                System.out.println("Invalid provided database file: there are duplicates.");
                System.exit(-1);
            }

            students.add(student);
        }


        for(StudentRecord s: students){
            index.put(s.getJmbag(), s);
        }
    }

    /**
     * Retrieves the student record for the given JMBAG in O(1).
     *
     * @param jmbag JMBAG of the student
     * @return StudentRecord object corresponding to the given JMBAG, or null if no such record exists
     */
    public StudentRecord forJMBAG(String jmbag){
        return index.get(jmbag);
    }

    /**
     * Filters student records using the specified IFilter filter and returns the filtered list of student records.
     *
     * @param filter IFilter to be applied on student records
     * @return a new List of student records that satisfy the given filter criteria
     */
    public List<StudentRecord> filter(IFilter filter){
        List<StudentRecord> tmp = new LinkedList<>();
        for(StudentRecord s: students){
            if(filter.accepts(s)) tmp.add(s);
        }
        return tmp;
    }

    /**
     * Returns the number of student records in the database.
     *
     * @return Number of student records in the database
     */
    public int size(){
        return students.size();
    }

    public List<StudentRecord> getStudentRecords(){
        return List.copyOf(students);
    }
}
