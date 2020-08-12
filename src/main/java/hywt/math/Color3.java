package hywt.math;

import java.util.Objects;

public class Color3 {
    public int r;
    public int g;
    public int b;

    public Color3(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color3(double rp, double gp, double bp) {
        Mapper m = new Mapper(0, 1, 0, 255);
        this.r = (int) m.map(rp);
        this.g = (int) m.map(gp);
        this.b = (int) m.map(bp);
    }

    public Color3(int intVal) {
        this.r = (intVal >> 16) & 0xff;
        this.g = (intVal >> 8) & 0xff;
        this.b = intVal & 0xff;
    }

    public int toInt() {
        return (r << 16) | (g << 8) | b;
    }

    public String toCommandColor() {
        return String.format("%f %f %f", this.r / 255d, this.g / 255d, this.b / 255d);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color3 color3 = (Color3) o;
        return r == color3.r &&
                g == color3.g &&
                b == color3.b;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, g, b);
    }

    @Override
    public String toString() {
        return "Color3{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
    }
}
