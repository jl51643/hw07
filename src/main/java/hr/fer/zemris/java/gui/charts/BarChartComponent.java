package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Model of bar chart component
 */
public class BarChartComponent extends JComponent {

    /**
     * Bar chart that is being drown
     */
    private BarChart chart;

    /**
     * Multiple usage constant gap between elements of chart
     */
    private static final int GAP = 10;

    /**
     * Construction new Bar chart component for given chart
     *
     * @param chart chart that is drawn
     */
    public BarChartComponent(BarChart chart) {
        this.chart = chart;
        //setBorder(BorderFactory.createLineBorder(Color.RED, 20));
    }

    @Override
    public void paintComponent(Graphics g) {

        Insets insets = getInsets();

        int width = getWidth() - insets.left - insets.right;

        Graphics2D g2d = (Graphics2D) g;

        AffineTransform defaultAt = g2d.getTransform();
        AffineTransform at = new AffineTransform();
        at.rotate(-Math.PI / 2);
        g2d.setTransform(at);

        //drawing Y coordinate description
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setColor(Color.BLACK);

        int rotatedHeight = g2d.getClip().getBounds().x;

        //after -90° rotation y axis is spreading from up-left corner of screen towards up-right corner
        //and -x axis is spreading from up-left corner of screen towards down-left corner
        int yAxisDescriptionXCoordinate = insets.left + fm.getHeight() + GAP;
        int yAxisDescriptionYCoordinate = (rotatedHeight - fm.stringWidth(chart.getYDescription())) / 2 - getY();
        g2d.drawString(chart.getYDescription(), yAxisDescriptionYCoordinate, yAxisDescriptionXCoordinate);

        //restoring default options for x and y axes
        g2d.setTransform(defaultAt);

        //drawing X coordinate description
        int xAxisDescriptionXCoordinate = (width - fm.stringWidth(chart.getXDescription())) / 2;
        int xAxisDescriptionYCoordinate =   getHeight() - insets.bottom + getY() - GAP - fm.getHeight();
        g2d.drawString(chart.getXDescription(), xAxisDescriptionXCoordinate, xAxisDescriptionYCoordinate);

        //drawing y values
        int maxY = chart.getMaxY();
        int minY = chart.getMinY();
        int drawingMinY = minY;
        int step = chart.getStep();
        while ((maxY - drawingMinY) % step != 0)
            drawingMinY++;

        int yValuesXCoordinate = yAxisDescriptionXCoordinate + GAP + fm.getHeight(); //height because of direction of y description
        int xValuesXCoordinateStart = yValuesXCoordinate + GAP * 2;
        int xValuesXCoordinateEnd = width - xValuesXCoordinateStart;
        int yValuesYCoordinateStartValue = xAxisDescriptionYCoordinate - GAP - fm.getHeight() - GAP;
        int yValuesYCoordinateEndValue = getY() + GAP + insets.top;
        int yDrawingStep = (yValuesYCoordinateStartValue - yValuesYCoordinateEndValue) / ((maxY - drawingMinY) / step + 1);
        int counter = 0;
        for (int i = drawingMinY; i <= maxY; i += step) {
            int currentY = yValuesYCoordinateStartValue - fm.getAscent() / 2 - yDrawingStep * counter++;

            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(i), yValuesXCoordinate - fm.stringWidth(String.valueOf(i)), currentY + fm.getAscent() / 2);

            g2d.drawLine(yValuesXCoordinate + GAP, currentY, xValuesXCoordinateStart, currentY);

            g2d.setColor(Color.ORANGE);
            if (i == drawingMinY) {
                g2d.setColor(Color.BLACK);
                g2d.fillPolygon(new int[]{xValuesXCoordinateEnd + GAP, xValuesXCoordinateEnd + GAP * 2, xValuesXCoordinateEnd + GAP}, new int[]{yValuesYCoordinateStartValue - GAP,yValuesYCoordinateStartValue - GAP /2, yValuesYCoordinateStartValue}, 3);
            }
            g2d.drawLine(xValuesXCoordinateStart, currentY, xValuesXCoordinateEnd + GAP, currentY);
        }

        //draw x values
        List<Integer> xEs = chart.getList().stream().map(XYValue::getX).collect(Collectors.toList());

        int xValuesYCoordinate = xAxisDescriptionYCoordinate - GAP * 2 - fm.getHeight();
        int xDrawingStep = (xValuesXCoordinateEnd - xValuesXCoordinateStart) / xEs.size();
        int xAxisYCoordinate = yValuesYCoordinateStartValue - fm.getAscent() / 2;
        for (int i = 0; i <= xEs.size(); i++) {
            g2d.setStroke(new BasicStroke(1));
            int currentX = xValuesXCoordinateStart + xDrawingStep * i;

            g2d.setColor(Color.BLACK);
            g2d.drawLine(currentX, xValuesYCoordinate, currentX, xAxisYCoordinate);

            if (i != xEs.size())
                g2d.drawString(String.valueOf(xEs.get(i)), currentX + (xDrawingStep - fm.stringWidth(String.valueOf(xEs.get(i))))/2, xValuesYCoordinate + fm.getHeight());

            g2d.setColor(Color.ORANGE);
            if (i == 0) {
                g2d.setColor(Color.BLACK);
                g2d.fillPolygon(new int[]{xValuesXCoordinateStart - GAP / 2, xValuesXCoordinateStart, xValuesXCoordinateStart + GAP / 2}, new int[]{yValuesYCoordinateEndValue, yValuesYCoordinateEndValue - GAP, yValuesYCoordinateEndValue}, 3);
            }
            g2d.drawLine(currentX, xAxisYCoordinate , currentX, yValuesYCoordinateEndValue);

            if (i == xEs.size())
                continue;
            g2d.setColor(Color.ORANGE);
            int yValue = chart.getList().get(i).getY();
            int drawingYValue = (yValue - drawingMinY) * yDrawingStep / step;
            g2d.fillRect(currentX, xAxisYCoordinate - drawingYValue , xDrawingStep, drawingYValue);

            //drawing shadows
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(currentX + xDrawingStep - 1, xAxisYCoordinate - 2, currentX + xDrawingStep - 1, xAxisYCoordinate - drawingYValue + 1);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setStroke(new BasicStroke(6));
            g2d.drawLine(currentX + xDrawingStep + 3, xAxisYCoordinate - 3, currentX + xDrawingStep + 3, xAxisYCoordinate - drawingYValue + GAP + 3);
        }
    }
}
