package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class PrimModelImpl implements PrimModel, ListModel<Integer> {

    private int current;
    private List<Integer> primes;
    private final List<ListDataListener> listeners = new ArrayList<>();

    public PrimModelImpl() {
        current = 1;
        primes = new ArrayList<>();
        primes.add(current);
    }

    @Override
    public void next() {
        int next = current + 1;

        while(true) {
            if(isPrime(next)) {
                current = next;
                primes.add(current);
                ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED,
                        primes.size()-1, primes.size()-1);
                for(var listener:listeners) {
                    listener.intervalAdded(event);
                }
                return;
            }
            next++;
        }
    }

    @Override
    public boolean isPrime(int number) {

        for(int i=2; i<=Math.sqrt(number); i++){
            if(number % i == 0) return false;
        }

        return true;
    }

    public int getCurrent() {
        return current;
    }

    /**
     * Returns the length of the list.
     *
     * @return the length of the list
     */
    @Override
    public int getSize() {
        return primes.size();
    }

    /**
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at <code>index</code>
     */
    @Override
    public Integer getElementAt(int index) {
        return primes.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListDataListener(ListDataListener listener) {
        listeners.remove(listener);
    }

}
