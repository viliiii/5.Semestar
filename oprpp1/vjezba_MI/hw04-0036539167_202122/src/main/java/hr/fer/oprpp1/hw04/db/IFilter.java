package hr.fer.oprpp1.hw04.db;

/**
 * Interface to be implemented by Filters that check if
 * given StudentRecord will be accepted or not.
 */
public interface IFilter {
    /**
     * @param record the StudentRecord
     * @return  true if the record is accepted, false otherwise.
     */
    public boolean accepts(StudentRecord record);
}
