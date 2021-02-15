package hr.fer.zemris.java.gui.layouts;

import org.junit.jupiter.api.Test;

import javax.lang.model.type.ExecutableType;
import javax.swing.*;

import java.awt.*;
import java.lang.reflect.Executable;

import static org.junit.jupiter.api.Assertions.*;

public class CalcLayoutTest {

    @Test
    public void addLayoutComponentShouldThrowParseStringTest() {
        CalcLayout cl = new CalcLayout();

        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), "0, 3"));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), "6, 3"));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), "3, 0"));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), "3, 8"));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), "-3, 3"));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), "3,-3"));

        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), "1,2"));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), "1,3"));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), "1, 4"));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), "1, 5"));

        cl.addLayoutComponent(new JButton(), "3, 3");
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), "3, 3"));

    }

    @Test
    public void addLayoutComponentShouldThrowRCPositionTest() {

        CalcLayout cl = new CalcLayout();

        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), new RCPosition(0, 3)));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), new RCPosition(6, 3)));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), new RCPosition(3, 0)));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), new RCPosition(3, 8)));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), new RCPosition(-3, 3)));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), new RCPosition(3, -3)));

        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), new RCPosition(1, 2)));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), new RCPosition(1, 3)));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), new RCPosition(1, 4)));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), new RCPosition(1, 5)));

        cl.addLayoutComponent(new JButton(), new RCPosition(3, 3));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JButton(), new RCPosition(3, 3)));
    }

    @Test
    public void testWidthHeight() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
        p.add(l1, new RCPosition(2,2));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();

        assertEquals(152, dim.width);
        assertEquals(158, dim.height);
    }

    @Test
    public void testWidthHeight2() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
        p.add(l1, new RCPosition(1,1));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();

        assertEquals(152, dim.width);
        assertEquals(158, dim.height);
    }
}