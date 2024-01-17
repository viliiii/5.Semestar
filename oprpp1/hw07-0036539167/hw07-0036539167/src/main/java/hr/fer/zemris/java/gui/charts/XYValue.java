package hr.fer.zemris.java.gui.charts;

public class XYValue {
    private final int x;
    private final int y;

    public XYValue(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int X() {
        return x;
    }

    public int Y() {
        return y;
    }
}
