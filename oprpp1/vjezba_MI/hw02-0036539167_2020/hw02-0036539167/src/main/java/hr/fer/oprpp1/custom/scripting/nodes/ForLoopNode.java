package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;

/**
 * A  node representing a single for-loop construct.
 */
public class ForLoopNode extends Node{
    ArrayIndexedCollection childrenNodes = null;    //if it's null, it hasn't been created yet.

    private final ElementVariable variable;
    private final Element startExpression;
    private final Element endExpression;
    private final Element stepExpression; //can be null.

    /**
     * Constructor with all the elements of the forLoopNode.
     * @param variable
     * @param startExpression
     * @param endExpression
     * @param stepExpression
     */
    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = stepExpression;
    }

    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression){
        this(variable, startExpression, endExpression, null);
    }

    public ElementVariable getVariable() {
        return variable;
    }

    public Element getStartExpression() {
        return startExpression;
    }

    public Element getEndExpression() {
        return endExpression;
    }

    public Element getStepExpression() {
        return stepExpression;
    }

    /**
     * Checks if the given Node is equal to this Node.
     * @param obj the object to be checked if equal
     * @return true if equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        ForLoopNode other = (ForLoopNode) obj;
        if(!startExpression.asText().equals(other.startExpression.asText())) return false;
        if(!variable.asText().equals(other.variable.asText())) return false;
        if(!stepExpression.asText().equals(other.stepExpression.asText())) return false;
        if(!endExpression.asText().equals(other.endExpression.asText())) return false;
        return true;
    }

    /** String representation of the node.
     * @return String representation of the node
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{$ FOR ");
        sb.append(variable.asText()).append(" ");
        sb.append(startExpression.asText()).append(" ");
        sb.append(endExpression.asText());
        if(stepExpression!= null){
            sb.append(" ");
            sb.append(stepExpression.asText());
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
