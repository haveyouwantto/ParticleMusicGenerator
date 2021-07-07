package hywt.mc.particle;

import hywt.math.Line2D;

public class LineInfo {
    private final int splits;
    private final Line2D line;

    public LineInfo(int splits, Line2D line) {
        this.splits = splits;
        this.line = line;
    }

    public int getSplits() {
        return splits;
    }

    public Line2D getLine(){
        return line;
    }
}
