package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Random;

public class Grafovi extends JFrame {

    private BarChart bc1;
    private BarChart bc2;
    private BarChart bc3;
    private BarChart bc4;

    BarChartComponent bcc1;
    BarChartComponent bcc2;
    BarChartComponent bcc3;
    BarChartComponent bcc4;

    public Grafovi(){
        setTitle("Grafovi");
        setSize(850, 850);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        bc1 = new BarChart(
                Arrays.asList(
                        new XYValue(1,8), new XYValue(2,20), new XYValue(3,22),
                        new XYValue(4,10), new XYValue(5,4)
                ),
                "Number of people in the car",
                "Frequency",
                0,
                22,
                2
        );
        bc2 = bc1;
        bc3 = bc1;
        bc4 = bc1;

        initGUI();
    }

    private BarChart randomGraph(int min, int max) {
        Random random = new Random();

        return new BarChart(
                Arrays.asList(
                        new XYValue(1,random.nextInt(min, max+1) + min), new XYValue(2,random.nextInt(min, max+1) + min), new XYValue(3,random.nextInt(min, max+1) + min),
                        new XYValue(4,random.nextInt(min, max+1) + min), new XYValue(5,random.nextInt(min, max+1) + min)
                ),
                "Number of people in the car",
                "Frequency",
                0,
                22,
                2
        );
    }

    private void initGUI(){
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());




        JPanel graphsPanel = new JPanel();
        graphsPanel.setLayout(new GridLayout(2,2));
        bcc1 = new BarChartComponent(bc1);
        bcc2 = new BarChartComponent(bc2);
        bcc3 = new BarChartComponent(bc3);
        bcc4 = new BarChartComponent(bc4);
        graphsPanel.add(bcc1);
        graphsPanel.add(bcc2);
        graphsPanel.add(bcc3);
        graphsPanel.add(bcc4);
        cp.add(graphsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4,1));
        JButton button1 = new JButton();
        button1.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bcc1.drawNew(randomGraph(0, 22));
            }
        });
        button1.setText("1");
        JButton button2 = new JButton("2");
        button2.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bcc2.drawNew(randomGraph(0, 22));
            }
        });
        button2.setText("2");
        JButton button3 = new JButton("3");
        button3.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bcc3.drawNew(randomGraph(0, 22));
            }
        });
        button3.setText("3");
        JButton button4 = new JButton("4");
        button4.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bcc4.drawNew(randomGraph(0, 22));
            }
        });
        button4.setText("4");

        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        buttonPanel.add(button4);
        cp.add(buttonPanel, BorderLayout.WEST);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Grafovi().setVisible(true);
        });
    }
}
