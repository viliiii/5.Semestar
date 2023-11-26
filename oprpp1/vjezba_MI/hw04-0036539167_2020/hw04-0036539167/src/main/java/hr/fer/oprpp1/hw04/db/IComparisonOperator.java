package hr.fer.oprpp1.hw04.db;

/**
 * Interface to be implemented by each ComparisonOperator to make
 * logical implementations of operators between two strings.
 * For example LESS, LESS_EQUAL, GREATER, GREATER_EQUAL, LIKE...
 */
public interface IComparisonOperator {
    /**
     * Returns true if value1 is in implemented relation to value2.
     * @param value1 first operand of comparison
     * @param value2 second operand of comparison
     * @return  true if value1 is in implemented relation to value2
     */
    public boolean satisfied(String value1, String value2);



}
