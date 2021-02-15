package hr.fer.zemris.java.gui.layouts;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Model of Calculator layout
 */
public class CalcLayout implements LayoutManager2 {

    /**
     * Constant number of rows in layout
     */
    private static final int ROWS = 5;

    /**
     * Constant number of columns in layout
     */
    private static final int COLUMNS = 7;

    /**
     * Map of components added to layout
     */
    private Map<RCPosition, Component> childrenComponents;

    /**
     * Gap between components
     */
    private final int gap;

    /**
     * Constructing new CalcLayout with given gap between components
     *
     * @param gap get between components added to layout
     */
    public CalcLayout(int gap) {
        this.gap = gap;
        this.childrenComponents = new HashMap<>();
    }

    /**
     * Constructing new CalcLayout with default gap.
     * Default gap is 0.
     */
    public CalcLayout() {
        this(0);
    }

    /**
     * Adds the specified component to the layout, using the specified constraint object.
     *
     * @param comp component to add to layout
     * @param constraints constraint on adding component
     * @throws NullPointerException if given constraint is null
     * @throws IllegalArgumentException if given constraint can not be applied to default number of rows and columns
     * @throws CalcLayoutException if given constraint do not matches requirements of CalcLayout of
     * if component with given constraint is already added to layout
     */
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        RCPosition position;
        if (constraints == null)
            throw new NullPointerException("Given constraints is null");
        if (constraints instanceof RCPosition)
            position = (RCPosition) constraints;
        else if (constraints instanceof String)
            position = RCPosition.parse((String) constraints);
        else
            throw new IllegalArgumentException("Unexpected constraints");
        if (position.getRow() < 1 || position.getRow() > 5)
            throw new CalcLayoutException("Position out of bounds");
        if (position.getColumn() < 1 || position.getColumn() > 7)
            throw new CalcLayoutException("Position out of bounds");
        if (position.getRow() == 1 && position.getColumn() != 1 && position.getColumn() != 6 && position.getColumn() != 7)
            throw new CalcLayoutException("Forbidden position");
        if (this.childrenComponents.containsKey(position))
            throw new CalcLayoutException("Component with given position already exists");

        this.childrenComponents.put(position, comp);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        int width = calculateComponentsDimensions(component -> {
            if (component.getMaximumSize() == null)
                return 0;
            return component.getMaximumSize().width;
        }) * COLUMNS + gap * (COLUMNS - 1);

        int height = calculateComponentsDimensions(component -> {
            if (component.getMaximumSize() == null)
                return 0;
            return component.getMaximumSize().height;
        }) * ROWS + gap * (ROWS - 1);

        return new Dimension(width, height);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        for (Map.Entry<RCPosition, Component> entry : this.childrenComponents.entrySet())
            if (entry.getValue().equals(comp))
                this.childrenComponents.remove(entry.getKey());
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        int width = calculateComponentsDimensions(component -> {
            if (component.getPreferredSize() == null)
                return 0;
            return component.getPreferredSize().width;
        }) * COLUMNS + gap * (COLUMNS - 1);

        int height = calculateComponentsDimensions(component -> {
            if (component.getPreferredSize() == null)
                return 0;
            return component.getPreferredSize().height;
        }) * ROWS + gap * (ROWS - 1);

        return new Dimension(width, height);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        int width = calculateComponentsDimensions(component -> {
            if (component.getMinimumSize() == null)
                return 0;
            return component.getMinimumSize().width;
        }) * COLUMNS + gap * (COLUMNS - 1);

        int height = calculateComponentsDimensions(component -> {
            if (component.getMinimumSize() == null)
                return 0;
            return component.getMinimumSize().height;
        }) * ROWS + gap * (ROWS - 1);
        return new Dimension(width, height);
    }

    @Override
    public void layoutContainer(Container parent) {

        Insets insets = parent.getInsets();
        float widthOfComponents =   1f * (parent.getWidth() - (insets.left + insets.right) - (COLUMNS - 1) * gap) / COLUMNS;
        float heightOfComponents = 1f * (parent.getHeight() - (insets.top + insets.bottom) - (ROWS - 1) * gap) / ROWS;

        for (Map.Entry<RCPosition, Component> entry : this.childrenComponents.entrySet()) {
            int column = entry.getKey().getColumn();
            int row = entry.getKey().getRow();

            int x = insets.left + (column - 1) * (gap + (int)widthOfComponents);
            int y = insets.top + (row - 1) * (gap + (int)heightOfComponents);

            if (column == 1 && row == 1) {
                entry.getValue().setBounds(x, y, (int) Math.floor(widthOfComponents * 5 + 4 * gap), (int) Math.floor(heightOfComponents));
            }
            else if (column % 2 == 0) {
                entry.getValue().setBounds(x, y, (int) Math.floor(widthOfComponents), (int) Math.floor(heightOfComponents));
            }
            else {
                entry.getValue().setBounds(x, y, (int) Math.ceil(widthOfComponents), (int) Math.ceil(heightOfComponents));
            }
        }

    }

    /**
     * Calculates maximum dimension of component depending on which function gets as argument
     *
     * @param dimension determines which dimension of components will be calculated
     * @return returns maximal value of searched dimension considering all components added to layout
     */
    private int calculateComponentsDimensions(Function<Component, Integer> dimension) {
        int maxDimension = 0;
        for (Map.Entry<RCPosition, Component> c : this.childrenComponents.entrySet()) {
            int size = dimension.apply(c.getValue());
            if (c.getKey().getRow() == 1 && c.getKey().getColumn() == 1)
                size = (size - (ROWS - 1) * gap) / ROWS;
            if (size > maxDimension)
                maxDimension = size;
        }
        return maxDimension;
    }

}
