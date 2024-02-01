package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class BarChartComponent extends JComponent {
    BarChart barChart;

    public BarChartComponent(BarChart barChart) {
        super();    //!
        this.barChart = barChart;
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform starting = g2d.getTransform();

        AffineTransform at = new AffineTransform(starting);
        at.rotate(-Math.PI / 2);
        g2d.setTransform(at);

        FontMetrics fontMetrics = g2d.getFontMetrics();
        String text = barChart.getyDescription();
        int textWidth = fontMetrics.stringWidth(text);
        int textHeight = fontMetrics.getHeight();
        //Left vertical text
        int x = -(getHeight()/2 + textWidth/2);
        int y = 1 + textHeight;
        g2d.drawString(text, x, y);

        g2d.setTransform(starting);

        //Bottom text
        text = barChart.getxDescription();
        textWidth = fontMetrics.stringWidth(text);
        textHeight = fontMetrics.getHeight();
        x = getWidth()/2 - textWidth/2;
        y = getHeight() - textHeight;
        g2d.drawString(text, x, y);

        //odredi neki normalan x od kud to krece prema gore i onda ipsilon svakome
        //drugaciji za neku stalnu vrijednost i to postavljat u for-u.
        //pitaj chata kako s Graphics2D napravit vertikalnu liniju razlicitih brojeva koji
        //stoje uz y os grafa (histograma).

        //origin dot
        int xOrigin = textHeight + 1 + 50;
        int yOrigin = getHeight() - (xOrigin);

        //y os
        g2d.drawLine(xOrigin, yOrigin, xOrigin, 20);
        int yOsLength = Math.abs(yOrigin - 20);
        int[] xpoints = {xOrigin,xOrigin-6, xOrigin+6};
        int[] ypoints = {18,26,26};
        g2d.fillPolygon(xpoints, ypoints, 3);

        //x os
        g2d.drawLine(xOrigin, yOrigin, getWidth()-20, yOrigin);
        int xOsLength = Math.abs(xOrigin - (getWidth() - 20));
        xpoints = new int[]{getWidth() - 18, getWidth() - 26, getWidth() - 26};
        ypoints = new int[]{yOrigin, yOrigin - 6, yOrigin + 6};
        g2d.fillPolygon(xpoints, ypoints, 3);

        //y os numbers
        int numOfSteps = (barChart.getMaxY()- barChart.getMinY()) / barChart.getyDistance() + 1;
        if(((barChart.getMaxY()- barChart.getMinY()) % barChart.getyDistance()) != 0) {
            numOfSteps++;
        }
        int pixelGap = yOsLength / numOfSteps;


        for(int i = 0; i <numOfSteps; i++) {
            String number = String.valueOf(barChart.getMinY() + i * barChart.getyDistance());
            textWidth = fontMetrics.stringWidth(number);
            x = xOrigin - textWidth - 10;
            y = yOrigin - i*pixelGap + 3;

            g2d.drawString(number, x, y);
            g2d.drawLine(xOrigin-5,y-3, xOrigin, y-3);
        }

        //rectangles
        numOfSteps = barChart.getXyValues().size();
        int rectangleWidth = (xOsLength-15) / numOfSteps;
        int rectangleHeight;
        for(int i = 0; i < numOfSteps; i++){
           x = xOrigin + i*rectangleWidth;
           y = yOrigin;
           rectangleHeight = pixelGap * ((barChart.getXyValues().get(i).Y() - barChart.getMinY()) / barChart.getyDistance());
           g2d.drawRect(x, y-rectangleHeight, rectangleWidth, rectangleHeight);

           g2d.drawLine(xOrigin + i*rectangleWidth, yOrigin, xOrigin + i*rectangleWidth, yOrigin+5);
           g2d.drawLine(xOrigin + (1+i)*rectangleWidth, yOrigin, xOrigin + (1+i)*rectangleWidth, yOrigin+5);

           String number = String.valueOf(barChart.getXyValues().get(i).X());
           int width = fontMetrics.stringWidth(number);
           g2d.drawString(number, x+rectangleWidth/2 - width, yOrigin+10 + fontMetrics.getHeight());
        }


    }

    public void drawNew(BarChart bc){
        this.barChart = bc;
        repaint();
    }
}