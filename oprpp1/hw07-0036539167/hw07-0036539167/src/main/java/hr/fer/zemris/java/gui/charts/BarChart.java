package hr.fer.zemris.java.gui.charts;

import java.util.List;

public class BarChart {
    private final List<XYValue> xyValues;
    private final String xDescription;
    private final String yDescription;
    private final int minY;
    private final int maxY;
    private final int yDistance;

    public BarChart(List<XYValue> xyValues, String xDescription, String yDescription, int minY, int maxY, int yDistance){
        this.xyValues = xyValues;
        this.xDescription = xDescription;
        this.yDescription = yDescription;
        if(minY < 0) throw new IllegalArgumentException("Min Y must be positive.");
        this.minY = minY;
        if(!(maxY>minY)) throw new IllegalArgumentException("Max Y must be greater than Min Y.");
        this.maxY = maxY;

        this.yDistance = yDistance;

        for(var v:xyValues){
            if(v.Y() < minY) throw new IllegalArgumentException("All y-s must be greater than Min Y.");
        }

    }

    public List<XYValue> getXyValues() {
        return xyValues;
    }

    public String getxDescription() {
        return xDescription;
    }

    public String getyDescription() {
        return yDescription;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getyDistance() {
        return yDistance;
    }
}
