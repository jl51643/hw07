package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;

public class RCPosition {

    /**
     * row number
     */
    private int row;

    /**
     * column number
     */
    private int column;

    /**
     * Constructing new RCPosition
     *
     * @param row
     * @param column
     */
    public RCPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * @return returns row number
     */
    public int getRow() {
        return row;
    }

    /**
     * @return returns column number
     */
    public int getColumn() {
        return column;
    }

    /**
     * Takes two real numbers in format "row, column" and returns new instance of RCPosition initialized
     * with given row and column number
     *
     * @param text row and column positions
     * @return returns new RCPosition initialized with given row and column number
     * @throws IllegalArgumentException if given arguments can not be interpreted as two real numbers
     */
    public static RCPosition parse (String text) {
        text = text.trim();
        char[] argument = text.toCharArray();
        String row = "", column = "";
        int i = 0;
        if (argument[i] == '-')
            row += argument[i++];
        while (i < argument.length && Character.isDigit(argument[i]))
            row += argument[i++];
        while (i < argument.length && !Character.isDigit(argument[i]) ) {
            if (argument[i] == '-') {
                column += argument[i++];
                break;
            }
            i++;
        }
        while (i < argument.length && Character.isDigit(argument[i]))
            column += argument[i++];

        if (row.equals("") || column.equals(""))
            throw new IllegalArgumentException("Can not parse argument: " + text);
        try {
            return new RCPosition(Integer.parseInt(row), Integer.parseInt(column));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Can not parse argument: " + text);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RCPosition position = (RCPosition) o;
        return getRow() == position.getRow() && getColumn() == position.getColumn();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn());
    }
}
