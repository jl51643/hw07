package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.view.CalculatorView;

import javax.swing.*;
import java.awt.*;

/**
 * Calculator app
 */
public class Calculator {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new CalculatorView().setVisible(true);
        });
    }
}
