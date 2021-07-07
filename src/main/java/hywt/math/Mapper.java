package hywt.math;

public class Mapper {
    private final double sourceStart;
    private final double sourceEnd;
    private final double targetStart;
    private final double targetEnd;

    public Mapper(double sourceStart, double sourceEnd, double targetStart, double targetEnd) {
        this.sourceStart = sourceStart;
        this.sourceEnd = sourceEnd;
        this.targetStart = targetStart;
        this.targetEnd = targetEnd;
    }

    public static int clamp(int c) {
        return Math.max(0, Math.min(255, c));
    }

    public double map(double value) {
        return value / (sourceEnd - sourceStart) * (targetEnd - targetStart) + targetStart;
    }

    public double getSourceStart() {
        return sourceStart;
    }

    public double getSourceEnd() {
        return sourceEnd;
    }

    public double getTargetStart() {
        return targetStart;
    }

    public double getTargetEnd() {
        return targetEnd;
    }

    @Override
    public String toString() {
        return "Mapper{" +
            "sourceStart=" + sourceStart +
            ", sourceEnd=" + sourceEnd +
            ", targetStart=" + targetStart +
            ", targetEnd=" + targetEnd +
            '}';
    }
}
