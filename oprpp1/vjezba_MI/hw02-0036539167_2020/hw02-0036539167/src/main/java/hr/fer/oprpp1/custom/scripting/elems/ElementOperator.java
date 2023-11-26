package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Class representing the Operator element in the code.
 */
public class ElementOperator extends Element{
    private final String symbol;

    /**
     * Default constructor.
     * @param symbol the String representation of the Operator element.
     */
    public ElementOperator(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the string representation of this element.
     * @return  the string representation of this element.
     */
    @Override
    public String asText() {
        return symbol;
    }
}
