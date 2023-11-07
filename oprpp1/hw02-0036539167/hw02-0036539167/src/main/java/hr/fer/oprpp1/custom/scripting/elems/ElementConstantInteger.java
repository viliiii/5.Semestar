package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Class representing Integer elements in the
 * code.
 */
public class ElementConstantInteger extends Element{
    private final int value;

    /**
     * Default constructor.
     * @param value of the integer element
     */
    public ElementConstantInteger(int value) {
        this.value = value;
    }

    /**
     * Returns the string representation of this element.
     * @return  the string representation of this element.
     */
    @Override
    public String asText() {
        return String.valueOf(value);
    }
}
