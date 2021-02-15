package hr.fer.zemris.java.gui.prim;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimListModelTest {

    PrimListModel model;

    @BeforeEach
    public void setUp() {
        model = new PrimListModel();
    }

    @Test
    void nextTest() {
        assertEquals(1, model.getSize());
        assertEquals(1, model.getElementAt(0));

        model.next();
        assertEquals(2, model.getSize());
        assertEquals(2, model.getElementAt(1));

        model.next();
        assertEquals(3, model.getSize());
        assertEquals(3, model.getElementAt(2));

        model.next();
        assertEquals(4, model.getSize());
        assertEquals(5, model.getElementAt(3));

        for (int i = 0; i < 5; i++) {
            model.next();
        }

        assertEquals(9, model.getSize());
        assertEquals(19,model.getElementAt(model.getSize()-1));
    }

    @Test
    void getSize() {

        assertEquals(1, model.getSize());

        model.next();
        assertEquals(2, model.getSize());

        model.next();
        assertEquals(3, model.getSize());

        model.next();
        model.next();
        assertEquals(5, model.getSize());
    }

    @Test
    void getElementAt() {
        assertEquals(1, model.getElementAt(0));
        for (int i = 0; i < 10; i++) {
            model.next();
        }

        assertEquals(29, model.getElementAt(model.getSize()-1));
    }

}