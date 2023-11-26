package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.Processor;

/**
 * A node representing an entire code document.
 */
public class DocumentNode extends Node{
    ArrayIndexedCollection childrenNodes = null;    //if it's null, it hasn't been created yet.

    /**
     * Checks if the given document node has equal tree structure as this one.
     * @param obj the document node to check
     * @return true if the given document node has equal tree structure as this one
     */
    @Override
    public boolean equals(Object obj) {
        DocumentNode other = (DocumentNode) obj;
        int i;
        if(childrenNodes != null && other.childrenNodes!=null){
            if(childrenNodes.size() != other.childrenNodes.size()) return false;
            for(i=0; i<childrenNodes.size();i++){
                if(!childrenNodes.get(i).equals(other.childrenNodes.get(i))) return false;
                if(((Node)childrenNodes.get(i)).numberOfChildren()>0){
                    return childrenNodes.get(i).equals(other.childrenNodes.get(i));
                }
            }
        }else if(childrenNodes == null && other.childrenNodes!=null){
            return false;
        } else if (childrenNodes != null && other.childrenNodes==null) {
            return false;
        }
        return true;
    }



    /** String representation of the node.
     * @return String representation of the node
     */
    @Override
    public String toString() {
        return this.asText();
    }

    /**
     * Adds given child to an internally managed collection of children.
     *
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
     *
     * @return a number of (direct) children
     */
    @Override
    public int numberOfChildren(){
        if(this.childrenNodes == null) return 0;
        return this.childrenNodes.size();
    }

    /**
     * Returns the child at the specified index.
     *
     * @param index specified index
     * @return Node at given index
     * @throws IndexOutOfBoundsException if the collection is empty or
     *                                   the index is out of bounds.
     */
    @Override
    public Node getChild(int index) {
        if(this.childrenNodes == null){
            throw new IndexOutOfBoundsException("There are no children.");
        }
        return (Node) this.childrenNodes.get(index);
    }

    /**
     * Recursive helper method for getting the String representation of
     * the node with its children.
     * @return String representation of the node.
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
