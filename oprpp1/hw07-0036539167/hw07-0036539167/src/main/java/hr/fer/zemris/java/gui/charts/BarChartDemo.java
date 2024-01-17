package hr.fer.zemris.java.gui.charts;

import hr.fer.zemris.java.gui.calc.Calculator;
import hr.fer.zemris.java.gui.calc.model.CalcModelImpl;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.List;

public class BarChartDemo extends JFrame {
    private final BarChartComponent barChartComponent;
    private String filePath;

    public BarChartDemo(BarChartComponent barChartComponent, String filePath) {
        setTitle("BarChart");
        setSize(700, 700);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.barChartComponent = barChartComponent;
        this.filePath = filePath;
        initGUI();
    }

    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        JLabel pathLabel = new JLabel(filePath);
        pathLabel.setHorizontalAlignment(JLabel.CENTER);
        pathLabel.setVerticalAlignment(JLabel.CENTER);
        cp.add(pathLabel, BorderLayout.NORTH);

        cp.add(barChartComponent, BorderLayout.CENTER);
        cp.setVisible(true);
    }

    public static void main(String[] args) {

        String filePath = args[0];
        List<XYValue> values = new ArrayList<XYValue>();
        String xDescription = "";
        String yDescription = "";
        int minY = 0;
        int maxY = 0;
        int yDistance = 0;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            xDescription = reader.readLine();
            yDescription = reader.readLine();
            values = parseValues(reader.readLine());
            minY = Integer.parseInt(reader.readLine().trim());
            maxY = Integer.parseInt(reader.readLine().trim());
            yDistance = Integer.parseInt(reader.readLine().trim());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        BarChart model = new BarChart(values, xDescription, yDescription, minY, maxY, yDistance);

        SwingUtilities.invokeLater(()->{
            new BarChartDemo(new BarChartComponent(model), filePath).setVisible(true);
        });
    }

    private static List<XYValue> parseValues(String line){
        List<XYValue> values = new ArrayList<>();
        String[] pairs = line.split("\\s+");
        for(String pair : pairs){
            String[] pairValues = pair.split(",");
            int x = Integer.parseInt(pairValues[0]);
            int y = Integer.parseInt(pairValues[1]);
            values.add(new XYValue(x, y));
        }
        return values;
    }
}
