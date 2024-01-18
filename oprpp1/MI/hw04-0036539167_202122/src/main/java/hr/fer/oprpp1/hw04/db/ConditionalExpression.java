package hr.fer.oprpp1.hw04.db;

/**
 * Class representation of parsed db query into the:
 * FieldGetter, ComparisonOperator and StringLiteral.
 * (firstName           =                  "Marko")
 */
public class ConditionalExpression {
    private IFieldValueGetter FieldGetter;
    private IComparisonOperator ComparisonOperator;
    private String StringLiteral;

    /**
     * Constructor.
     * @param fieldGetter   FieldGetter
     * @param stringLiteral     StringLiteral
     * @param comparisonOperator        ComparisonOperator
     */
    public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral, IComparisonOperator comparisonOperator) {
        FieldGetter = fieldGetter;
        ComparisonOperator = comparisonOperator;
        StringLiteral = stringLiteral;
    }

    public IFieldValueGetter getFieldGetter() {
        return FieldGetter;
    }

    public IComparisonOperator getComparisonOperator() {
        return ComparisonOperator;
    }

    public String getStringLiteral() {
        return StringLiteral;
    }
}
