package hr.fer.zemris.java.gui.layouts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RCPositionTest {

    @Test
    public void parseTest() {

        assertEquals(2, RCPosition.parse("2, 6").getRow());
        assertEquals(6, RCPosition.parse("2, 6").getColumn());

        assertEquals(-2, RCPosition.parse("-2, 6").getRow());
        assertEquals(-6, RCPosition.parse("2, -6").getColumn());

        assertEquals(12, RCPosition.parse("12, 6").getRow());
        assertEquals(61231, RCPosition.parse("2, 61231").getColumn());


    }
}