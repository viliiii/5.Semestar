package hr.fer.zemris.java.gui.prim;

import javax.swing.event.ListDataListener;

public interface PrimModel {
    void next();

    boolean isPrime(int number);


    void addListDataListener(ListDataListener listener);


    void removeListDataListener(ListDataListener listener);
}
