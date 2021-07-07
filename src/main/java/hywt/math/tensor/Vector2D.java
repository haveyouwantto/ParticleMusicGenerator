package hywt.math.tensor;

import java.util.Objects;

public class Vector2D implements Cloneable {
    public double x;
    public double y;

    public Vector2D() {
        this(0, 0);
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2D fromPolar(double r, double o) {
        return new Vector2D(
                r * Math.cos(o),
                r * Math.sin(o)
        );
    }

    public Vector2D add(Vector2D v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2D add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2D subtract(Vector2D v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vector2D subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2D multiply(double n) {
        x *= n;
        y *= n;
        return this;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double product(Vector2D v) {
        return x * v.x + y * v.y;
    }

    public double angle(Vector2D v) {
        return Math.acos(product(v) / (length() * v.length()));
    }

//    public double getAngle() {
//        if (x == 0) {
//            if (y == 0) return Double.NaN;
//            return y > 0 ? Math.toRadians(90) : Math.toRadians(270);
//        }
//        return Math.atan(y / x);
//    }

    @Override
    public String toString() {
        return "Vector2D(" + x + ',' + y + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return Double.compare(vector2D.x, x) == 0 &&
                Double.compare(vector2D.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public Vector2D clone() {
        return new Vector2D(x, y);
    }

    public Vector3D to3D() {
        return new Vector3D(x, 0, y);
    }

}