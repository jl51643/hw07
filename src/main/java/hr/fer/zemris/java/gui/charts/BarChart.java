package hr.fer.zemris.java.gui.charts;

import java.util.List;
import java.util.function.Consumer;

public class BarChart {

    private List<XYValue> list;
    private String xDescription;
    private String yDescription;
    private int minY;
    private int maxY;
    private int step;

    public BarChart(List<XYValue> list,String xDescription, String yDescription, int minY, int maxY, int step) {
        this.list = list;
        this.xDescription = xDescription;
        this.yDescription = yDescription;

        if (minY < 0)
            throw new IllegalArgumentException("Minimal value can not be negative");
        this.minY = minY;

        if (maxY <= minY)
            throw new IllegalArgumentException("Maximal value must be greater than minimal value");
        this.maxY = maxY;

        if (step < 0)
            throw new IllegalArgumentException("Minimal value can not be negative");
        this.step = step;

        checkMinValues(list, minY);
    }

    private static void checkMinValues(List<XYValue> list, int minY) {
        list.stream().map(XYValue::getY)
                .forEach(y -> {
                    if (y < minY)
                        throw new IllegalArgumentException("Graph value can not be less than min value");
                });
    }

    public List<XYValue> getList() {
        return list;
    }

    public String getXDescription() {
        return xDescription;
    }

    public String getYDescription() {
        return yDescription;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getStep() {
        return step;
    }
}
