package hr.fer.zemris.java.gui.layouts;
import java.awt.*;
import java.util.HashMap;

public class CalcLayout implements LayoutManager2{

    private int gap;

    //mora postojat mapa koja pamti koja komponenta je oddana na koju poziciju
    // , tipa hashmap<ime, rcposition>

    private HashMap<Component, RCPosition> positions;

    public CalcLayout() {
        gap = 0;
        positions = new HashMap<>();
    }

    public CalcLayout(int gap) {
        this.gap = gap;
        positions = new HashMap<>();
    }

    /*641 strana u knjizi je neka implementacija LayoutManager-a i ima još jedna ispod. Na temelju
    toga implementirat ove metode.
    */
    /*
    također u uputama zadatka ima za neke motede kako
    * */



    /**
     * Adds the specified component to the layout, using the specified
     * constraint object.
     *
     * @param comp        the component to be added
     * @param constraints where/how the component is added to the layout.
     */
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if(comp == null || constraints == null) throw new NullPointerException("Comp or constraints are required.");
        if(!(constraints instanceof String) && !(constraints instanceof RCPosition)){
            throw new IllegalArgumentException("Constraints must be String or RCPosition object.");
        }
        RCPosition constraint;
        if(constraints instanceof String){
            constraint = parse((String) constraints);
        }else {
            constraint = (RCPosition) constraints;
        }

        if(constraint.getRow() == 1 && (constraint.getColumn()>1 && constraint.getColumn()<6)) throw new CalcLayoutException("Invalid position.");
        if(constraint.getRow() < 1 || constraint.getRow() > 5) throw new CalcLayoutException("Invalid position.");
        if(constraint.getColumn() < 1 || constraint.getColumn() > 7) throw new CalcLayoutException("Invalid position.");

        RCPosition existing = positions.get(comp);
        if(positionOccupied(constraint)) throw new CalcLayoutException("Position already exists.");
        if(existing != null) throw new CalcLayoutException("Position already exists.");
        if(positions.containsValue(constraint)) throw new CalcLayoutException("Already in.");

        positions.put(comp, constraint);

    }

    /**
     * Calculates the maximum size dimensions for the specified container,
     * given the components it contains.
     *
     * @param target the target container
     * @return the maximum size of the container
     * @see Component#getMaximumSize
     * @see LayoutManager
     */
    @Override
    public Dimension maximumLayoutSize(Container target) {
        return calcDimension(target, Component::getMaximumSize);
    }

    /**
     * Returns the alignment along the x axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param target the target container
     * @return the x-axis alignment preference
     */
    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    /**
     * Returns the alignment along the y axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param target the target container
     * @return the y-axis alignment preference
     */
    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    /**
     * Invalidates the layout, indicating that if the layout manager
     * has cached information it should be discarded.
     *
     * @param target the target container
     */
    @Override
    public void invalidateLayout(Container target) {

    }

    /**
     * If the layout manager uses a per-component string,
     * adds the component {@code comp} to the layout,
     * associating it
     * with the string specified by {@code name}.
     *
     * @param name the string to be associated with the component
     * @param comp the component to be added
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the specified component from the layout.
     *
     * @param comp the component to be removed
     */
    @Override
    public void removeLayoutComponent(Component comp) {
        positions.remove(comp);
    }

    /**
     * Calculates the preferred size dimensions for the specified
     * container, given the components it contains.
     *
     * @param parent the container to be laid out
     * @return the preferred dimension for the container
     * @see #minimumLayoutSize
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return calcDimension(parent, Component::getPreferredSize);
    }

    /**
     * Calculates the minimum size dimensions for the specified
     * container, given the components it contains.
     *
     * @param parent the component to be laid out
     * @return the minimum dimension for the container
     * @see #preferredLayoutSize
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return calcDimension(parent, Component::getMinimumSize);
    }

    /**
     * Lays out the specified container.
     *
     * @param parent the container to be laid out
     */
    @Override
    public void layoutContainer(Container parent) {
        Insets parentInsets = parent.getInsets();
        int n = parent.getComponentCount();

        double oneWidth = ((parent.getWidth() - 6*gap - parentInsets.left - parentInsets.right) * 1.0) / 7;
        double oneHeight = ((parent.getHeight() - 4*gap - parentInsets.top - parentInsets.bottom) * 1.0) / 5;

        for (int i = 0; i < n; i++) {
            Component comp = parent.getComponent(i);
            RCPosition pos = positions.get(comp);

            if(pos.getRow() == 1 && pos.getColumn() == 1){
                int w = (int) Math.floor(oneWidth);  //bio ceil
                int h = (int) Math.floor(oneHeight);    //
                comp.setBounds(parentInsets.left, parentInsets.top, w*5 + gap*4, h);
            }else{
                int w = 0;
                int h = 0;
                if(pos.getRow() % 2 != 0){
                    w = (int) Math.floor(oneWidth); //
                }else{
                    w = (int) Math.floor(oneWidth);
                }

                if(pos.getColumn() % 2 != 0){
                    h = (int) Math.floor(oneHeight);    //
                }else{
                    h = (int) Math.floor(oneHeight);
                }

                comp.setBounds(parentInsets.left + (pos.getColumn() - 1)*w + (pos.getColumn() - 1)*gap,
                        parentInsets.top + (pos.getRow() - 1)*h + (pos.getRow() - 1)*gap,
                        w, h);

            }


                /*if(i%2 != 0) {
                int w = (int) Math.ceil(oneWidth);
                int h = (int) Math.ceil(oneHeight);
                comp.setBounds(parentInsets.left + (pos.getColumn() - 1)*w + (pos.getColumn() - 1)*gap,
                        parentInsets.top + (pos.getRow() - 1)*h + (pos.getRow() - 1)*gap,
                        w, h);
            }else{
                int w = (int) Math.floor(oneWidth);
                int h = (int) Math.floor(oneHeight);
                comp.setBounds(parentInsets.left + (pos.getColumn() - 1)*w + (pos.getColumn() - 1)*gap,
                        parentInsets.top + (pos.getRow() - 1)*h + (pos.getRow() - 1)*gap,
                        w, h);
            }*/


        }
    }

    /**
     * Parses the given text into an {@link RCPosition}. The text should be in the format "row,column", where row and column are integers.
     *
     * @param text the text to parse
     * @return the parsed {@link RCPosition}
     * @throws IllegalArgumentException if the text is not in the expected format
     */
    public static RCPosition parse(String text){
        String cleaned = text.trim().replaceAll("\\s+", "");
        String[] split = cleaned.split(",");
        if(split.length != 2) throw new IllegalArgumentException("RCPosition must have exactly two numbers");
        return new RCPosition(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    private boolean positionOccupied(RCPosition pos){
        for(RCPosition p: positions.values()){
            if(p.getRow() == pos.getRow() && p.getColumn() == pos.getColumn()){
                return true;
            }
        }
        return false;
    }

    private interface SizeGetter{
        Dimension getSize(Component comp);
    }

    private Dimension calcDimension(Container parent, SizeGetter getter){
        int n = parent.getComponentCount();
        Dimension dim = new Dimension(0, 0);
        for(int i = 0; i < n; i++) {
            Component comp = parent.getComponent(i);
            Dimension compDim = getter.getSize(comp);

            if(compDim != null) {
                if(!(positions.get(comp).getRow() == 1 && positions.get(comp).getColumn() == 1)){
                    dim.width = Math.max(dim.width, compDim.width);
                }else{
                    dim.width = (int) Math.max(dim.width, (1.0*(compDim.width-4*gap))/5);
                }
                dim.height = Math.max(dim.height, compDim.height);

            }
        }

        dim.height += 4*gap + 4*dim.height; //bilo je 5*dim.height
        dim.width += 6*gap + 6*dim.width;   //bilo je 7*dim.width

        Insets parentInsets = parent.getInsets();
        dim.width += parentInsets.left + parentInsets.right;
        dim.height += parentInsets.top + parentInsets.bottom;

        return dim;
    }
}
