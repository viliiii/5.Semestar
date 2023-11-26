package hr.fer.oprpp1.hw04.db;

/**
 * Class with some implemented IComparisonOperator-s used to compare
 * two String objects. Implementations using lambda expressions are trivial.
 */
public class ComparisonOperators {
    public static final IComparisonOperator LESS = (s1, s2) -> s1.compareTo(s2) < 0;
    public static final IComparisonOperator LESS_OR_EQUALS = (s1, s2) -> s1.compareTo(s2) <= 0;
    public static final IComparisonOperator GREATER = (s1, s2) -> s1.compareTo(s2) > 0;
    public static final IComparisonOperator GREATER_OR_EQUALS = (s1, s2) -> s1.compareTo(s2) >= 0;
    public static final IComparisonOperator EQUALS = (s1, s2) -> s1.compareTo(s2) == 0;
    public static final IComparisonOperator NOT_EQUALS = (s1, s2) -> s1.compareTo(s2) != 0;

    /**
     * IComparisonOperator implementing LIKE.
     * Examples:
     * AAA LIKE A* = true.
     * Johnson LIKE Jo* = true.
     * Petrovic LIKE P*ic = true.
     * AAA LIKE AA*AA = false.
     */
    public static final IComparisonOperator LIKE = new IComparisonOperator() {
        @Override
        public boolean satisfied(String value1, String value2) {
            if(value1.compareTo(value2) == 0) return true;
            if(value2.indexOf("*") != value2.lastIndexOf("*")) throw new IllegalArgumentException("Too much wildcards **");
            if(value2.length() == 1 && value2.charAt(0) == '*') return true;

            if(value2.charAt(value2.length()-1) == '*'){
                return value1.startsWith(value2.substring(0, value2.length() - 1));
            }

            if(value2.startsWith("*")){
                return value1.endsWith(value2.substring(1));
            }
            //

            int indWild = value2.indexOf('*');
            return value1.substring(0, indWild).contains(value2.substring(0, indWild))
                    && value1.substring(indWild).contains(value2.substring(indWild + 1));
        }
    };
}
