package primitives;

import java.util.Objects;

/**
 * Represents a point in the 3D space.
 * A point is defined by its coordinates (x, y, z).
 */
public class Point {
    final protected Double3 xyz; // The coordinates of the point
    final public static Point ZERO = new Point(Double3.ZERO); // The origin point (0, 0, 0)

    /**
     * Constructs a point with the given coordinates.
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @param z the z-coordinate of the point
     */
    public Point(double x, double y, double z) {
        this( new Double3(x, y, z));
    }

    /**
     * Constructs a point with the given Double3 object.
     *
     * @param xyz the Double3 object representing the coordinates of the point
     */
     Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Adds a vector to the point, returning a new point.
     *
     * @param v the vector to add
     * @return a new point resulting from the addition of the vector to this point
     */
    public Point add(Vector v) {
        return new Point(this.xyz.add(v.xyz));
    }

    /**
     * Subtracts a point from this point, returning a vector.
     *
     * @param p the point to subtract
     * @return the vector resulting from the subtraction of the given point from this point
     */
    public Vector subtract(Point p) {
        return new Vector(xyz.subtract(p.xyz));
    }

    /**
     * Calculates the squared distance between this point and another point.
     *
     * @param other the other point
     * @return the squared distance between this point and the other point
     */
    public double distanceSquared(Point other) {
        return (this.xyz.d1 - other.xyz.d1) * (this.xyz.d1 - other.xyz.d1)
                + (this.xyz.d2 - other.xyz.d2) * (this.xyz.d2 - other.xyz.d2)
                + (this.xyz.d3 - other.xyz.d3) * (this.xyz.d3 - other.xyz.d3);
    }

    /**
     * Calculates the distance between this point and another point.
     *
     * @param other the other point
     * @return the distance between this point and the other point
     */
    public double distance(Point other) {
        return Math.sqrt(distanceSquared(other));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point point)) return false;

        return xyz.equals(point.xyz);
    }

    @Override
    public int hashCode() {
        return xyz.hashCode();
    }

    @Override
    public String toString() {
        return "Point{" + xyz + '}';
    }

    public double getX() {
        return xyz.d1;
    }

    public double getY() {
       return xyz.d2;
    }
    public double getZ() {
        return xyz.d3;
    }
}
