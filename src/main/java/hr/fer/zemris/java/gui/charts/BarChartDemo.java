package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BarChartDemo extends JFrame {

    private String path;
    private BarChart barChart;

    public BarChartDemo(Path path, BarChart barChart) {
        this.path = path.toAbsolutePath().toString();
        this.barChart = barChart;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initGUI(this.path);
        this.setSize(750, 300);
    }

    private void initGUI(String path) {
        System.out.println("size:" + this.getSize());

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        JLabel label = new JLabel(path);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        cp.add(label, BorderLayout.PAGE_START);

        BarChartComponent barChartComponent = new BarChartComponent(barChart);

        cp.add(barChartComponent, BorderLayout.CENTER);

    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Unexpected number of arguments: " + args.length);
            return;
        }
        
        Path path = Path.of(args[0]);
        BarChart chart = parseInput(path);
        SwingUtilities.invokeLater(() -> new BarChartDemo(path, chart).setVisible(true));
    }

    private static BarChart parseInput(Path path) throws IOException {
        Scanner sc = new Scanner(path);
        String xDes = sc.nextLine();
        String yDes = sc.nextLine();
        String listLine = sc.nextLine();
        String[] points = listLine.split("\\s");
        List<XYValue> list = new ArrayList<>();
        for (String s : points) {
            try {
                list.add(new XYValue(Integer.parseInt(s.split(",")[0]), Integer.parseInt(s.split(",")[1])));
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                System.out.println("Wrong x,y format: " + s);
                System.exit(0);
            }

        }
        int miny = sc.nextInt();
        int maxY = sc.nextInt();
        int step = sc.nextInt();

        return new BarChart(list, xDes, yDes, miny, maxY, step);
    }
}
