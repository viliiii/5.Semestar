package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.scripting.elems.Element;

/**
 * A node representing a command which generates some
 * textual output dynamically.
 */
public class EchoNode extends Node{
    ArrayIndexedCollection childrenNodes = null;    //if it's null, it hasn't been created yet.

    private final Element[] elements;


    /**
     * Constructor for the EchoNode with the given elements.
     * @param elements elements to append into the node
     */
    public EchoNode(Element[] elements) {
        this.elements = elements;
    }

    /**
     * Default constructor.
     */
    public EchoNode() {
        this(null);
    }

    public Element[] getElements() {
        return elements;
    }

    /**
     * Checks if the given EchoNode is equal to this EchoNode.
     * @param obj the object to be checked if equal
     * @return true if equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        EchoNode other = (EchoNode) obj;
        for(int i=0; i<elements.length; i++){
            if(!elements[i].asText().equals(other.elements[i].asText())) return false;
        }

        return true;
    }

    /** String representation of the node.
     * @return String representation of the node
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{$= ");
        try {
            for (Element el:
                    elements) {
                sb.append(el.asText()).append(" ");
            }
        }catch (NullPointerException ignored){

        }
        sb.append(" $}");
        return sb.toString();
    }

    /**
     *  Adds given child to an internally managed collection of children.
     * @param child to be added
     */
    @Override
    public void addChildNode(Node child){
        if(this.childrenNodes == null){
            this.childrenNodes = new ArrayIndexedCollection();
        }
        this.childrenNodes.add(child);
    }

    /**
     * Returns a number of (direct) children.
     * @return a number of (direct) children
     */
    @Override
    public int numberOfChildren(){
        if(this.childrenNodes == null) return 0;
        return this.childrenNodes.size();
    }

    /**
     * Returns the child at the specified index.
     * @param index specified index
     * @return Node at given index
     * @throws IndexOutOfBoundsException if the collection is empty or
     * the index is out of bounds.
     */
    @Override
    public Node getChild(int index){
        if(this.childrenNodes == null){
            throw new IndexOutOfBoundsException("There are no children.");
        }
        return (Node) this.childrenNodes.get(index);
    }

    /** Helper String representation of the node.
     * @return String representation of the node
     */
    @Override
    public String asText(){
        StringBuilder sb = new StringBuilder();
        int i;
        for(i=0; i<this.childrenNodes.size();i++){
            sb.append(this.childrenNodes.get(i).toString());
            if(((Node)this.childrenNodes.get(i)).numberOfChildren()>0){

                sb.append(((Node)this.childrenNodes.get(i)).asText());
            }

            if(((Node)this.childrenNodes.get(i)).getClass() == ForLoopNode.class){
                sb.append("{$END$}");
            }
        }

        return sb.toString();
    }
}
