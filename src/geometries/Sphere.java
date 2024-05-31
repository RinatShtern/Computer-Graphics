package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Represents a sphere geometry in 3D space.
 * A sphere is defined by its radius and center point.
 */
public class Sphere implements Geometry {
    private final Point center; // The center point of the sphere
    private final double radius;

    /**
     * Constructs a sphere with the given radius and center point.
     *
     * @param center The center point of the sphere
     * @param radius The radius of the sphere
     */
    public Sphere(Point center, double radius) {
        this.radius = radius;
        this.center = center;
    }

    /**
     * Retrieves the normal vector to the sphere at a given point.
     *
     * @param p The point on the sphere
     * @return The normal vector to the sphere at the given point
     */
    @Override
    public Vector getNormal(Point p) {
        return p.subtract(center).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();
        Point center = this.center;
        double radius = this.radius;

        Vector u = center.subtract(p0);

        double tm = alignZero(v.dotProduct(u));
        double dSquared = alignZero(u.lengthSquared() - tm * tm);
        double radiusSquared = radius * radius;

        // if d >= radius, no intersections
        if (dSquared >= radiusSquared) {
            return null;
        }

        double th = Math.sqrt(radiusSquared - dSquared);
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        // if t1 <= 0 and t2 <= 0, no intersections
        if (t1 <= 0 && t2 <= 0) {
            return null;
        }

        // if t1 > 0 and t2 > 0, return two intersections
        if (t1 > 0 && t2 > 0) {
            Point p1 = p0.add(v.scale(t1));
            Point p2 = p0.add(v.scale(t2));
            return List.of(p1, p2);
        }

        // if only t1 > 0, return one intersection
        if (t1 > 0) {
            Point p1 = p0.add(v.scale(t1));
            return List.of(p1);
        }

        // if only t2 > 0, return one intersection
        Point p2 = p0.add(v.scale(t2));
        return List.of(p2);
    }

    @Override
    public List<Point> findIntsersections(Ray ray) {
        return List.of();
    }
}
