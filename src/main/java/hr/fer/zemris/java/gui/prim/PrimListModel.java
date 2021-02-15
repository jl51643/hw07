package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.*;

/**
 * Model of auto-generating list of prim numbers
 */
public class PrimListModel implements ListModel<Integer> {

    /**
     * List of generated prim number
     */
    List<Integer> primList = new ArrayList<>();

    /**
     * List of active listener on this model
     */
    List<ListDataListener> listeners = new ArrayList<>();

    /**
     * Construction new model containing first prim number
     */
    public PrimListModel() {
        primList.add(1);
    }

    /**
     * Generates next prim number
     */
    public void next() {
        int number = primList.get(primList.size()-1) + 1;
        boolean isPrime = false;
        while (!isPrime) {
            isPrime = true;
            for (int i = 2; i <= Math.sqrt(number); i++) {
                if (number % i == 0) {
                    isPrime = false;
                    number++;
                    break;
                }
            }
        }
        primList.add(number);
        for (ListDataListener l : listeners) {
            l.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, primList.size() - 1, primList.size() - 1));
        }
    }

    @Override
    public int getSize() {
        return primList.size();
    }

    @Override
    public Integer getElementAt(int index) {
        return primList.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }
}
