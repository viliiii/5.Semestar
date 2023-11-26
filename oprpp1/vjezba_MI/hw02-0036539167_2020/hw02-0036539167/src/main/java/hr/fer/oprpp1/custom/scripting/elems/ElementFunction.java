package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Class representing Function element in the code.
 */
public class ElementFunction extends Element{
    private final String name;

    /**
     * Default constructor.
     * @param name the name of the function
     */
    public ElementFunction(String name) {
        this.name = name;
    }

    /**
     * Returns the string representation of this element.
     * @return  the string representation of this element.
     */
    @Override
    public String asText() {
        return "@"+name;
    }
}
