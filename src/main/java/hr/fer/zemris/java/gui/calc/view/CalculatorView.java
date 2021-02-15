package hr.fer.zemris.java.gui.calc.view;


import hr.fer.zemris.java.gui.calc.model.CalcModelImpl;
import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.function.*;
import static java.lang.Math.PI;


/**
 * Calculator View
 */
public class CalculatorView extends JFrame {

    /**
     * Queue of numbers
     */
    private LinkedList<Double> queue = new LinkedList<>();

    /**
     * Calculator model
     */
    private CalcModelImpl model = new CalcModelImpl();

    /**
     * Constructing new Calculator view
     */
    public CalculatorView() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initGUI();
        pack();
    }

    /**
     * Initializing graphic user interface
     */
    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new CalcLayout(3));

        cp.add(new Display(model), "1, 1");

        cp.add(evaluateButton(), "1, 6");

        cp.add(new OperationButton(model, "clr", CalcModel::clear), "1, 7");
        cp.add(new OperationButton(model,"reset", CalcModel::clearAll), "2, 7");
        cp.add(new OperationButton(model, "push", (model) -> queue.push(model.getValue())), "3, 7");
        cp.add(new OperationButton(model, "pop", (model) -> model.setValue(queue.pop())), "4, 7");
        cp.add(new OperationButton(model, "+/-", CalcModel::swapSign), "5, 4");
        cp.add(new OperationButton(model, ".", CalcModel::insertDecimalPoint), "5, 5");

        cp.add(binaryOperatorButton("/", (l, r) -> l / r), "2, 6");
        cp.add(binaryOperatorButton("*", (l, r) -> l * r), "3, 6");
        cp.add(binaryOperatorButton("-", (l, r) -> l - r), "4, 6");
        cp.add(binaryOperatorButton("+", (l, r) -> l + r), "5, 6");

        Inverse inv = new Inverse(cp, "inv");
        cp.add(inv, "5, 7");


        cp.add(new UnaryOperatorButton(model, "1/x", "1/x", inv.isSelected(), v -> 1 / v, v -> 1 / v), "2, 1");
        cp.add(new UnaryOperatorButton(model, "sin", "arcsin", inv.isSelected(), v -> Math.sin(UnaryOperatorButton.customAngle(v)), v -> Math.asin(UnaryOperatorButton.customAngle(v))), "2, 2");
        cp.add(new UnaryOperatorButton(model, "log" , "10^x", inv.isSelected(), Math::log10, v -> Math.pow(10, v)), "3, 1");
        cp.add(new UnaryOperatorButton(model, "cos", "arccos", inv.isSelected(), v -> Math.cos(UnaryOperatorButton.customAngle(v)), v -> Math.acos(UnaryOperatorButton.customAngle(v))), "3, 2");
        cp.add(new UnaryOperatorButton(model, "ln", "e^x", inv.isSelected(), Math::log, v -> Math.pow(Math.E, v)), "4, 1");
        cp.add(new UnaryOperatorButton(model, "tan", "arctan", inv.isSelected(), v -> Math.tan(UnaryOperatorButton.customAngle(v)), v -> Math.atan(UnaryOperatorButton.customAngle(v))), "4, 2");
        cp.add(new UnaryOperatorButton(model, "ctg", "arctg", inv.isSelected(), v -> 1 / Math.tan(UnaryOperatorButton.customAngle(v)), v -> 1 / Math.atan(UnaryOperatorButton.customAngle(v))), "5, 2");

        cp.add(new ChangeableBinaryOperatorButton(model, "x^n", "x^(1/n)", inv.isSelected(), Math::pow, (l, r) -> Math.pow(l, 1 / r)), "5, 1");

        int number = 0;
        cp.add(numberButton(number++), "5, 3");
        for (int i = 4; i > 1; i--)
            for (int j = 3; j < 6; j++)
                cp.add(numberButton(number++), String.format("%d, %d", i, j));
    }

    /**
     * Model of calculator display
     */
    private static class Display extends JLabel implements CalcValueListener {

        /**
         * CalcModel on which value changes is this label registered
         */
        CalcModel model;
        String value = "0";

        /**
         * Constructing new display
         *
         * @param model
         */
        public Display(CalcModel model) {
            super("",SwingConstants.RIGHT);
            this.model = model;
            model.addCalcValueListener(this);
            this.setBorder(BorderFactory.createLineBorder(Color.black));
        }


        @Override
        public void valueChanged(CalcModel model) {
            repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            Rectangle r = g.getClipBounds();
            g.setColor(Color.YELLOW);
            g.fillRect(r.x, r.y, r.width, r.height);

            g.setColor(Color.BLACK);
            g.setFont(getFont().deriveFont(30f));
            value = model.toString();
            FontMetrics fm = g.getFontMetrics();
            g.drawString(value, getWidth() - getInsets().right - fm.stringWidth(value), getHeight() / 2 + fm.getAscent() / 2);
        }


    }

    /**
     * Factory method returning models of calculator number buttons
     *
     * @param number number on button
     * @return returns new JButton object set up as calculator number button
     */
    private JButton numberButton (int number) {
        JButton button = new JButton(String.valueOf(number));
        button.setFont(button.getFont().deriveFont(30f));
        button.setOpaque(true);
        button.addActionListener(l -> model.insertDigit(number));
        return button;
    }

    /**
     * Factory method returning models of calculator binary operator buttons
     *
     * @param func title of button
     * @param operator operation that button is executing
     * @return returns new JButton object set up as calculator binary operator button
     */
    private JButton binaryOperatorButton(String func, DoubleBinaryOperator operator) {
        JButton button = new JButton(func);
        button.setOpaque(true);
        button.addActionListener(l -> {
            if (model.hasFrozenValue())
                throw new CalculatorInputException("Calculator have frozen value");
            if (model.getPendingBinaryOperation() != null) {
                double evaluatedValue = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
                model.setValue(evaluatedValue);
            }
            model.freezeValue(model.toString());
            model.setActiveOperand(model.getValue());
            model.setPendingBinaryOperation(operator);
            model.clear();
        });
        return button;
    }

    /**
     * Factory method returning models of calculator evaluate buttons
     *
     * @return returns new JButton object set up as calculator evaluate button
     */
    private JButton evaluateButton() {
        JButton button = new JButton("=");
        button.setOpaque(true);
        button.addActionListener(l -> {
            double evaluatedValue = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
            model.setValue(evaluatedValue);
            model.clearActiveOperand();
            model.setPendingBinaryOperation(null);
        });
        return button;
    }

    /**
     * Model of calculator utility functions button
     */
    private static class OperationButton extends JButton {

        /**
         * CalcModel which this operation is changing
         */
        CalcModel model;

        /**
         * Title of operation
         */
        String operation;

        /**
         * Function which this button is executing
         */
        Consumer<CalcModel> function;

        /**
         * Constructing new utility operation button
         *
         * @param model model which this operation is changing
         * @param operation title of button
         * @param function function which this button is executing
         */
        public OperationButton(CalcModel model, String operation, Consumer<CalcModel> function) {
            super(operation);
            this.model = model;
            this.operation = operation;
            this.function = function;
            this.addActionListener(l -> function.accept(model));
        }
    }


    /**
     * Model of unary operation calculator button
     */
    private static class UnaryOperatorButton extends JButton {

        /**
         * Model which this button is changing
         */
        CalcModel model;

        /**
         * Title of regular function
         */
        String operation;

        /**
         * Title of inverse function
         */
        String inverseOperation;

        /**
         * Regular function that is this button executing
         */
        Function<Double, Double> function;

        /**
         * Inverse function that is this button executing
         */
        Function<Double, Double> inverseFunction;

        /**
         * Boolean flag that determines which function is button executing
         */
        boolean inverse;

        /**
         * Constructing new unary operator button
         *
         * @param model model which this button is changing
         * @param operation title of regular function
         * @param inverseOperation title of inverse function
         * @param inverse flag that determines which function is button executing
         * @param function regular function that is this button executing
         * @param inverseFunction inverse function that is this button executing
         */
        public UnaryOperatorButton(CalcModel model, String operation, String inverseOperation, boolean inverse, Function<Double, Double> function, Function<Double, Double> inverseFunction) {
            super(operation);
            this.model = model;
            this.operation = operation;
            this.inverseOperation = inverseOperation;
            this.function = function;
            this.inverseFunction = inverseFunction;
            this.inverse = inverse;
            this.addActionListener(l -> {
                if (model.hasFrozenValue())
                    throw new CalculatorInputException("Calculator have frozen value");
                if (!this.inverse) {
                    this.setText(operation);
                    model.setValue(function.apply(model.getValue()));
                } else {
                    this.setText(inverseOperation);
                    model.setValue(inverseFunction.apply(model.getValue()));
                }
            });
            setValue(this.inverse/*inverse.isSelected()*/);
        }

        /**
         * Setting inverse flag on given value
         *
         * @param inverse flag that determines which function is button executing
         */
        public void setInverse(boolean inverse) {
            this.inverse = inverse;
        }

        /**
         * Setting title on button
         *
         * @param inverse flag that determines which title is set on button
         */
        private void setValue(boolean inverse) {
            if (inverse) {
                this.setText(inverseOperation);
            } else {
                this.setText(operation);
            }
        }

        /**
         * Transformers angle to interval [0, 2π>
         *
         * @param angle angle in radians
         * @return returns transformed angle
         */
        public static double customAngle(double angle) {
            if (angle < 0)
                angle += (2 * PI);
            return angle;
        }
    }

    /**
     * Model of JCheckbox switching values on changeable buttons on calculator
     */
    private static class Inverse extends JCheckBox {

        /**
         * Container whose components value is this checkbox changing
         */
        Container parent;

        /**
         * Constructing new Inverse checkbox
         *
         * @param parent container
         * @param text title of checkbox
         */
        public Inverse(Container parent, String text) {
            super(text);
            this.parent = parent;
            this.addActionListener(l -> {
                for (Component c : parent.getComponents()) {
                    if (c instanceof UnaryOperatorButton) {
                        ((UnaryOperatorButton) c).setInverse(this.isSelected());
                        ((UnaryOperatorButton) c).setValue(this.isSelected());
                    }
                    if (c instanceof ChangeableBinaryOperatorButton) {
                        ((ChangeableBinaryOperatorButton) c).setInverse(this.isSelected());
                        ((ChangeableBinaryOperatorButton) c).setValue(this.isSelected());
                    }
                }
            });
        }
    }

    /**
     * Model of calculator changeable binary operator button
     */
    private static class ChangeableBinaryOperatorButton extends JButton {

        /**
         * CalcModel which is this button changing
         */
        CalcModel model;

        /**
         * Regular button title
         */
        String operation;

        /**
         * Inverse button title
         */
        String inverseOperation;

        /**
         * Regular function that is this button executing
         */
        DoubleBinaryOperator function;

        /**
         * Inverse function that is this button executing
         */
        DoubleBinaryOperator inverseFunction;

        /**
         * Boolean flag determining which function is button executing
         */
        boolean inverse;

        /**
         * Constructing new changeable binary operator button
         *
         * @param model model which is this button changing
         * @param operation regular button title
         * @param inverseOperation inverse button title
         * @param inverse flag determining which function is button executing
         * @param function regular function that is this button executing
         * @param inverseFunction inverse function that is this button executing
         */
        public ChangeableBinaryOperatorButton(CalcModel model, String operation, String inverseOperation, boolean inverse, DoubleBinaryOperator function, DoubleBinaryOperator inverseFunction) {
            super(operation);
            this.model = model;
            this.operation = operation;
            this.inverseOperation = inverseOperation;
            this.function = function;
            this.inverseFunction = inverseFunction;
            this.inverse = inverse;
            this.addActionListener(l -> {
                if (model.hasFrozenValue())
                    throw new CalculatorInputException("Calculator have frozen value");
                if (model.getPendingBinaryOperation() != null) {
                    double evaluatedValue = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
                    model.setValue(evaluatedValue);
                }
                model.freezeValue(model.toString());
                model.setActiveOperand(model.getValue());

                if (!this.inverse) {
                    model.setPendingBinaryOperation(function);
                } else {
                    model.setPendingBinaryOperation(inverseFunction);
                }
                model.clear();
            });
            setValue(inverse);
        }

        /**
         * Setting inverse flag on given value
         *
         * @param inverse flag that determines which title is set on button
         */
        public void setInverse(boolean inverse) {
            this.inverse = inverse;
        }

        /**
         * Setting title of button
         *
         * @param inverse determines which title is set on button
         */
        private void setValue(boolean inverse) {
            if (inverse) {
                this.setText(inverseOperation);
            } else {
                this.setText(operation);
            }
        }
    }
}
