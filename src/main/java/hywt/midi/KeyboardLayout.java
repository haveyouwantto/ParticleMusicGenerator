package hywt.midi;

import hywt.math.Line2D;
import hywt.math.Point2D;

public class KeyboardLayout {
    private double timeRatio;
    private double keyRatio;
    private double xOffset;
    private double zOffset;

    public KeyboardLayout(double timeRatio, double keyRatio) {
        this.timeRatio = timeRatio;
        this.keyRatio = keyRatio;
    }

    public KeyboardLayout() {
        this.timeRatio = 1;
        this.keyRatio = 1;
    }

    public double getTimeRatio() {
        return timeRatio;
    }

    public KeyboardLayout setTimeRatio(double timeRatio) {
        this.timeRatio = timeRatio;
        return this;
    }

    public double getKeyRatio() {
        return keyRatio;
    }

    public KeyboardLayout setKeyRatio(double keyRatio) {
        this.keyRatio = keyRatio;
        return this;
    }

    public double getxOffset() {
        return xOffset;
    }

    public KeyboardLayout setxOffset(double xOffset) {
        this.xOffset = xOffset;
        return this;
    }

    public double getzOffset() {
        return zOffset;
    }

    public KeyboardLayout setzOffset(double zOffset) {
        this.zOffset = zOffset;
        return this;
    }

    public double getX(double n) {
        return n * timeRatio + xOffset;
    }

    public double getZ(double n) {
        return n * keyRatio + zOffset;
    }

    public Point2D getPoint(long tick, Note note) {
        return new Point2D(getX(tick), getZ(note.getPitch()));
    }

    public Line2D getLine(long startTick, long endTick, Note note1, Note note2) {
        return new Line2D(getPoint(startTick, note1), getPoint(endTick, note2));
    }
}
