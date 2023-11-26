package hr.fer.oprpp1.hw04.db;

/**
 * Class responsible for obtaining a requested field
 * value from given StudentRecord.
 */
public interface IFieldValueGetter {
    /**
     * Returns String value of requested field which is implemented in
     * different IFieldValueGetters.
     * @param record the Student whose requested field is to be obtained.
     * @return the requested field value.
     */
    public String get(StudentRecord record);

}
