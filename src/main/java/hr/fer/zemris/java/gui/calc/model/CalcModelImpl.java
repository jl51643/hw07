package hr.fer.zemris.java.gui.calc.model;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.DoubleBinaryOperator;

/**
 * Implementation of calculator model
 */
public class CalcModelImpl implements CalcModel {

    /**
     * Boolean flag determining if model is open for changes
     */
    private boolean editable;

    /**
     * Boolean flag determining if current value is positive
     */
    private boolean positive;

    /**
     * String representation of current value
     */
    private String digitsString;

    /**
     * current value
     */
    private double digitsValue;

    /**
     * Current value frozen for view
     */
    private String frozenValue;

    /**
     * Active operand for next calculating operation
     */
    private OptionalDouble activeOperand = OptionalDouble.empty();

    /**
     * Next calculating operation
     */
    private DoubleBinaryOperator pendingOperator;

    /**
     * List of active listeners on this model
     */
    private List<CalcValueListener> listeners = new ArrayList<>();

    /**
     * Construction new model
     */
    public CalcModelImpl() {
        this.editable = true;
        this.positive = true;
        this.digitsString = "";
        this.digitsValue = 0;
        this.frozenValue = null;
    }

    @Override
    public void addCalcValueListener(CalcValueListener l) {
        listeners.add(l);
    }

    @Override
    public void removeCalcValueListener(CalcValueListener l) {
        listeners.remove(l);
    }

    @Override
    public double getValue() {
        return digitsValue;
    }

    @Override
    public void setValue(double value) {
        digitsValue = value;
        digitsString = String.valueOf(value);
        editable = false;
        for (CalcValueListener l : listeners)
            l.valueChanged(this);

    }

    @Override
    public boolean isEditable() {
        return this.editable;
    }

    @Override
    public void clear() {
        editable = true;
        digitsValue = 0;
        digitsString = "";
        positive = true;

        for (CalcValueListener l : listeners)
            l.valueChanged(this);
    }

    @Override
    public void clearAll() {
        clear();
        activeOperand = OptionalDouble.empty();
        pendingOperator = null;

        frozenValue = null;

        for (CalcValueListener l : listeners)
            l.valueChanged(this);
    }

    @Override
    public void swapSign() throws CalculatorInputException {
        if (!editable)
            throw new CalculatorInputException("Expression is not editable at this point");
        positive = !positive;
        if (positive)
            digitsString = digitsString.substring("-".length());
        else
            digitsString = "-" + digitsString;
        if (!digitsString.equals("") && !digitsString.equals("-"))
            digitsValue = Double.parseDouble(digitsString);
        frozenValue = null;

        for (CalcValueListener l : listeners)
            l.valueChanged(this);
    }

    @Override
    public void insertDecimalPoint() throws CalculatorInputException {
        if (digitsString.contains("."))
            throw new CalculatorInputException("Decimal point already in number");
        if (digitsString.equals("") ||digitsString.equals("-"))
            throw new CalculatorInputException("No numbers");
        digitsString += ".";
        frozenValue = null;

        for (CalcValueListener l : listeners)
            l.valueChanged(this);
    }

    @Override
    public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
        if(!isEditable())
            throw new CalculatorInputException("Not editable");
        if (digitsString.equals("0") && digit == 0)
            return;
        if (digitsString.equals("0") && digit != 0)
            digitsString = "";
        digitsString += digit;
        if (digitsString.length() >= 309)
            throw new CalculatorInputException("Too big number");
        try {
            digitsValue = Double.parseDouble(digitsString);
        } catch (NumberFormatException e) {
            throw new CalculatorInputException("Unexpected number format");
        }
        frozenValue = null;

        for (CalcValueListener l : listeners)
            l.valueChanged(this);
    }

    @Override
    public boolean isActiveOperandSet() {
        return activeOperand.isPresent();
    }

    @Override
    public double getActiveOperand() throws IllegalStateException {
        if (activeOperand.isEmpty())
            throw new IllegalStateException("No active operand");
        return activeOperand.getAsDouble();
    }

    @Override
    public void setActiveOperand(double activeOperand) {
        this.activeOperand = OptionalDouble.of(activeOperand);
        //this.clear();
    }

    @Override
    public void clearActiveOperand() {
        activeOperand = OptionalDouble.empty();
    }

    @Override
    public DoubleBinaryOperator getPendingBinaryOperation() {
        return pendingOperator;
    }

    @Override
    public void setPendingBinaryOperation(DoubleBinaryOperator op) {
        pendingOperator = op;
    }

    @Override
    public void freezeValue(String value) {
        frozenValue = value;
    }

    @Override
    public boolean hasFrozenValue() {
        return frozenValue != null;
    }

    @Override
    public String toString() {
        if (hasFrozenValue())
            return frozenValue;
        if (digitsString.equals("") || digitsString.equals("-"))
           if (positive)
               return "0";
           else
               return "-0";
        return digitsString;
    }
}
