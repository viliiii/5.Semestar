package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Class representing Double elements in the
 * code.
 */
public class ElementConstantDouble extends Element{
    private final double value;

    /**
     * Default constructor.
     * @param value of the double element
     */
    public ElementConstantDouble(double value) {
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
