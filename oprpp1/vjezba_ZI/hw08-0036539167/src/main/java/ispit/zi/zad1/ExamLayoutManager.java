package ispit.zi.zad1;

import java.awt.*;
import java.util.HashMap;

public class ExamLayoutManager implements LayoutManager2 {

    public static int AREA1 = 1;
    public static int AREA2 = 2;
    public static int AREA3 = 3;

    private int postotak;

    private final HashMap<Component, Integer> positions;


    public ExamLayoutManager(int postotak) {
        if(postotak < 10 || postotak > 90) throw new IllegalArgumentException("Postotak mora biti 10...90");
        this.postotak = postotak;

        positions = new HashMap<>();

    }

    public int getPostotak() {
        return postotak;
    }

    public void setPostotak(int postotak) {
        this.postotak = postotak;
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        int constraint = (Integer) constraints; //pozicija 1, 2 ili 3

       positions.put(comp, constraint);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return calcDimension(target, Component::getMaximumSize);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {

    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        positions.remove(comp);
    }

    private interface SizeGetter{
        Dimension getSize(Component comp);
    }

    private Dimension calcDimension(Container parent, SizeGetter getter){

        Dimension dim = new Dimension(0,0);

        for(int i=0; i<3; i++){
            Component comp = parent.getComponent(i);
            Dimension compDim = getter.getSize(comp);

            if(compDim != null){
                dim.width = Math.max(dim.width, compDim.width);
                dim.height = Math.max(dim.height, compDim.height);
            }
        }

        return dim;
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return calcDimension(parent, Component::getPreferredSize);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return calcDimension(parent, Component::getMinimumSize);
    }

    @Override
    public void layoutContainer(Container parent) {

        double h_ = parent.getHeight() * ((1.0*postotak)/100.0);
        double h = parent.getHeight() - h_;

        double w_ = parent.getWidth() * ((1.0*postotak)/100.0);
        double w = parent.getWidth() - w_;

        for (int i=0; i<3; i++) {
            Component comp = parent.getComponent(i);
            int position = positions.get(comp);

            if(position == 1){
                comp.setBounds(0, 0, (int)(w+w_), (int)h_);
            }else if(position == 2){
                comp.setBounds(0, (int)h_, (int)(w_), (int)(h));
            }else if(position == 3){
                comp.setBounds((int)w_, (int)h_, (int)(w), (int)(h) );
            }
        }
    }
}
