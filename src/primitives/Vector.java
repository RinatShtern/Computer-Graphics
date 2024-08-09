package primitives;

import static primitives.Util.isZero;

/**
 * Represents a vector in the 3D space.
 * A vector is defined by its components (x, y, z) and can be used to represent direction and magnitude.
 */
public class Vector extends Point {

    public static final Vector Y = new Vector(0, 1, 0);
    public static final Vector X = new Vector(1, 0, 0);
    public static final Vector Z = new Vector(0, 0, 1);
    public static final Vector MINUSY = new Vector(0, -1, 0);

    /**
     * Constructs a vector with the given components.
     *
     * @param x the x-component of the vector
     * @param y the y-component of the vector
     * @param z the z-component of the vector
     * @throws IllegalArgumentException if the vector has zero length
     */
    public Vector(double x, double y, double z) {
        this(new Double3(x, y, z));
    }

    /**
     * Constructs a vector with the given Double3 object.
     *
     * @param xyz the Double3 object representing the components of the vector
     * @throws IllegalArgumentException if the vector has zero length
     */
    public Vector(Double3 xyz) throws IllegalArgumentException {
        super(xyz);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Error: Vector is zero!");
        }
    }

    /**
     * Adds another vector to this vector, returning a new vector.
     *
     * @param v the vector to add
     * @return a new vector resulting from the addition of the given vector to this vector
     */
    public Vector add(Vector v) {
        return new Vector(xyz.add(v.xyz));
    }

    /**
     * Scales this vector by a scalar, returning a new vector.
     *
     * @param scalar the scalar value
     * @return a new vector resulting from scaling this vector by the given scalar
     * @throws IllegalArgumentException if the scalar is zero
     */
    public Vector scale(double scalar) {
        if (isZero(scalar)) {
            throw new IllegalArgumentException("A vector cannot be scaled by " + scalar);
        }
        return new Vector(xyz.scale(scalar));
    }

    /**
     * Calculates the dot product of this vector and another vector.
     *
     * @param v the other vector
     * @return the dot product of this vector and the other vector
     */
    public double dotProduct(Vector v) {
        return xyz.d1 * v.xyz.d1 + xyz.d2 * v.xyz.d2 + xyz.d3 * v.xyz.d3;
    }

    /**
     * Calculates the cross product of this vector and another vector.
     *
     * @param v the other vector
     * @return the cross product of this vector and the other vector
     */
    public Vector crossProduct(Vector v) {
        return new Vector(
                xyz.d2 * v.xyz.d3 - xyz.d3 * v.xyz.d2,
                xyz.d3 * v.xyz.d1 - xyz.d1 * v.xyz.d3,
                xyz.d1 * v.xyz.d2 - xyz.d2 * v.xyz.d1
        );
    }

    /**
     * Calculates the squared length of this vector.
     *
     * @return the squared length of this vector
     */
    public double lengthSquared() {
        return xyz.d1 * xyz.d1 + xyz.d2 * xyz.d2 + xyz.d3 * xyz.d3;
    }

    /**
     * Calculates the length of this vector.
     *
     * @return the length of this vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Returns a new vector representing the normalized form of this vector.
     *
     * @return a new normalized vector
     */
    public Vector normalize() {
        return new Vector(xyz.reduce(length()));
    }

    /**
     * Checks if this vector is equal to another object.
     *
     * @param o the object to compare
     * @return true if the vectors are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector vector)) return false;

        return xyz.equals(vector.xyz);
    }

    /**
     * Calculates the hash code for the vector.
     *
     * @return the hash code for the vector
     */
    @Override
    public int hashCode() {
        return xyz.hashCode();
    }

    /**
     * Returns a string representation of the vector.
     *
     * @return a string representation of the vector
     */
    @Override
    public String toString() {
        return "Vector{" + xyz + '}';
    }

    public Vector makePerpendicularVector() {
        double a = getX(), b = getY(), c = getZ();
        return (a == b && b == c) ? new Vector(0, -a, a).normalize() : new Vector(b - c, c - a, a - b).normalize();
    }
}
