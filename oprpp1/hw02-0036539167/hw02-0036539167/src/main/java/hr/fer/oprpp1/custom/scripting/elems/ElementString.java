package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Class representing the String in the code.
 */
public class ElementString extends Element{
    private final String value;

    /**
     * Default constructor.
     * @param value the value of the String element.
     */
    public ElementString(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of this element.
     * @return  the string representation of this element.
     */
    @Override
    public String asText() {

        return "\""+value.replace("\"", "\\\"")+"\"";
    }
}
