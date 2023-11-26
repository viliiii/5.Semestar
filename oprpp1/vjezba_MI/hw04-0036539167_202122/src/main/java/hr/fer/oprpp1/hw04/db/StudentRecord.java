package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * Instances of this class represent records for each student.
 */
public class StudentRecord {
    private String jmbag;
    private String lastName;
    private String firstName;
    private String finalGrade;

    /**
     * Constructor with all the necessary parameters.
     * @param jmbag the jmbag
     * @param lastName the last name
     * @param firstName the first name
     * @param finalGrade the final grade
     */
    public StudentRecord(String jmbag, String lastName, String firstName, String finalGrade) {
        this.jmbag = jmbag;
        this.lastName = lastName;
        this.firstName = firstName;
        this.finalGrade = finalGrade;
    }

    /**
     * Returns true if the jmbag field is equal to the
     * other student's jmbag field.
     * @param o other student
     * @return true if the jmbag field is equal to the other student's
     * jmbag field.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentRecord that = (StudentRecord) o;
        return Objects.equals(jmbag, that.jmbag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jmbag);
    }

    public String getJmbag() {
        return jmbag;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFinalGrade() {
        return finalGrade;
    }
}
