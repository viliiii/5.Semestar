package hr.fer.oprpp1.hw04.db;

/**
 * Implementations of IFieldValueGetter interface, each for different field.
 */
public class FieldValueGetters {
    public static final IFieldValueGetter FIRST_NAME = (r) -> r.getFirstName();
    public static final IFieldValueGetter LAST_NAME = (r) -> r.getLastName();
    public static final IFieldValueGetter JMBAG = (r) -> r.getJmbag();


}
