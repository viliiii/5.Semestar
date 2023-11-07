package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Class representing the Variable element in the code.
 */
public class ElementVariable extends Element{
    private final String name;

    /**
     * Default constructor.
     * @param name of the variable
     */
    public ElementVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns the string representation of this element.
     * @return  the string representation of this element.
     */
    @Override
    public String asText() {
        return name;
    }
}
