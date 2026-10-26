package model;

// A place in the warehouse (metres).
public record Point(double x, double y) {
    public double distanceTo(Point other) {
        return Math.hypot(x - other.x, y - other.y);
    }
}
