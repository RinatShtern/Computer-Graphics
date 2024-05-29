package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Represents a sphere geometry in the 3D space.
 * A sphere is defined by its radius and center point.
 */
public class Sphere extends RadialGeometry {
    private final Point center; // The center point of the sphere

    /**
     * Constructs a sphere with the given radius and center point.
     *
     * @param radius the radius of the sphere
     * @param center the center point of the sphere
     */
    public Sphere( Point center,double radius) {
        super(radius);
        this.center = center;
    }

    /**
     * Retrieves the normal vector to the sphere at a given point.
     *
     * @param p the point on the sphere
     * @return the normal vector to the sphere at the given point
     */
    @Override
    public Vector getNormal(Point p) {
        return p.subtract(center).normalize();
    }
    @Override
    public List<Point> findIntsersections(Ray ray) {
        return null;
    }

}
