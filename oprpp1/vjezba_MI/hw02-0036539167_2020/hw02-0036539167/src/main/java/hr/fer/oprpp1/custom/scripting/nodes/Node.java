package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

/**
 * Base class for graph nodes (logical parts of the code).
 * Every node has its own collection of children that are inside
 * its logical part of the code.
 */
public abstract class Node {
    ArrayIndexedCollection childrenNodes = null;    //if it's null, it hasn't been created yet.

    /**
     *  Adds given child to an internally managed collection of children.
     * @param child to be added
     */
    public abstract void addChildNode(Node child);

    /**
     * Returns a number of (direct) children.
     * @return a number of (direct) children
     */
    public abstract int numberOfChildren();

    /**
     * Returns the child at the specified index.
     * @param index specified index
     * @return Node at given index
     * @throws IndexOutOfBoundsException if the collection is empty or
     * the index is out of bounds.
     */
    public abstract Node getChild(int index);

    /**
     * String representation of the node.
     * @return String representation of the node.
     */
    public abstract String asText();


}
